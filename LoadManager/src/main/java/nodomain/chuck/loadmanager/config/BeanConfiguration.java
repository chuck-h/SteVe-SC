package nodomain.chuck.loadmanager.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mysql.cj.conf.PropertyDefinitions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import nodomain.chuck.loadmanager.LmConfiguration;
/*
import de.rwth.idsg.steve.service.DummyReleaseCheckService;
import de.rwth.idsg.steve.service.GithubReleaseCheckService;
import de.rwth.idsg.steve.service.ReleaseCheckService;
import de.rwth.idsg.steve.utils.InternetChecker;
*/

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.PreDestroy;
import javax.validation.Validator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static nodomain.chuck.loadmanager.LmConfiguration.CONFIG;

/**
 * Configuration and beans of Spring Framework.
 *
 * @author Chuck Harrison <cfharr@gmail.com>
 * derived from SteVe project 
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 15.08.2014
 */
@Slf4j
@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan("nodomain.chuck.loadmanager")
public class BeanConfiguration implements WebMvcConfigurer {

    private HikariDataSource dataSource;
    private ScheduledThreadPoolExecutor executor;

    /**
     * https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
     */
    private void initDataSource() {
        LmConfiguration.DB dbConfig = CONFIG.getDb();

        HikariConfig hc = new HikariConfig();

        // set standard params
        hc.setJdbcUrl("jdbc:mysql://" + dbConfig.getIp() + ":" + dbConfig.getPort() + "/" + dbConfig.getSchema());
        hc.setUsername(dbConfig.getUserName());
        hc.setPassword(dbConfig.getPassword());

        // set non-standard params
        hc.addDataSourceProperty(PropertyDefinitions.PNAME_cachePrepStmts, true);
        hc.addDataSourceProperty(PropertyDefinitions.PNAME_useServerPrepStmts, true);
        hc.addDataSourceProperty(PropertyDefinitions.PNAME_prepStmtCacheSize, 250);
        hc.addDataSourceProperty(PropertyDefinitions.PNAME_prepStmtCacheSqlLimit, 2048);
        hc.addDataSourceProperty(PropertyDefinitions.PNAME_characterEncoding, "utf8");
        hc.addDataSourceProperty(PropertyDefinitions.PNAME_serverTimezone, CONFIG.getTimeZoneId());
        hc.addDataSourceProperty(PropertyDefinitions.PNAME_useSSL, true);

        dataSource = new HikariDataSource(hc);
    }

    /**
     * Can we re-use DSLContext as a Spring bean (singleton)? Yes, the Spring tutorial of
     * Jooq also does it that way, but only if we do not change anything about the
     * config after the init (which we don't do anyways) and if the ConnectionProvider
     * does not store any shared state (we use DataSourceConnectionProvider of Jooq, so no problem).
     *
     * Some sources and discussion:
     * - http://www.jooq.org/doc/3.6/manual/getting-started/tutorials/jooq-with-spring/
     * - http://jooq-user.narkive.com/2fvuLodn/dslcontext-and-threads
     * - https://groups.google.com/forum/#!topic/jooq-user/VK7KQcjj3Co
     * - http://stackoverflow.com/questions/32848865/jooq-dslcontext-correct-autowiring-with-spring
     */
    @Bean
    public DSLContext dslContext() {
        initDataSource();

        Settings settings = new Settings()
                // Normally, the records are "attached" to the Configuration that created (i.e. fetch/insert) them.
                // This means that they hold an internal reference to the same database connection that was used.
                // The idea behind this is to make CRUD easier for potential subsequent store/refresh/delete
                // operations. We do not use or need that.
                .withAttachRecords(false)
                // To log or not to log the sql queries, that is the question
                .withExecuteLogging(CONFIG.getDb().isSqlLogging());

        // Configuration for JOOQ
        org.jooq.Configuration conf = new DefaultConfiguration()
                .set(SQLDialect.MYSQL)
                .set(new DataSourceConnectionProvider(dataSource))
                .set(settings);

        return DSL.using(conf);
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Lm-Executor-%d")
                                                                .build();

        executor = new ScheduledThreadPoolExecutor(5, threadFactory);
        return executor;
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * There might be instances deployed in a local/closed network with no internet connection. In such situations,
     * it is unnecessary to try to access Github every time, even though the request will time out and result
     * report will be correct (that there is no new version). With DummyReleaseCheckService we bypass the intermediate
     * steps and return a "no new version" report immediately.
     */
/**********
    @Bean
    public ReleaseCheckService releaseCheckService() {
        if (InternetChecker.isInternetAvailable()) {
            return new GithubReleaseCheckService();
        } else {
            return new DummyReleaseCheckService();
        }
    }
********/
    @PreDestroy
    public void shutDown() {
        if (dataSource != null) {
            dataSource.close();
        }

        if (executor != null) {
            gracefulShutDown(executor);
        }
    }

    private void gracefulShutDown(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            log.error("Termination interrupted", e);

        } finally {
            if (!executor.isTerminated()) {
                log.warn("Killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

    // -------------------------------------------------------------------------
    // Web config
    // -------------------------------------------------------------------------

    /**
     * Resolver for JSP views/templates. Controller classes process the requests
     * and forward to JSP files for rendering.
     */
    @Bean
    public InternalResourceViewResolver urlBasedViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    // springBus definition moved here from config/OcppConfiguration.java 
    @Bean(name = Bus.DEFAULT_BUS_ID, destroyMethod = "shutdown")
    public SpringBus springBus() {
        return new SpringBus();
    }

    /**
     * Resource path for static content of the Web interface.
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/manager/signin").setViewName("signin");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

}
