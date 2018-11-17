package nodomain.chuck.loadmanager;

import de.rwth.idsg.steve.utils.PropertiesFileLoader;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Chuck Harrison <cfharr@gmail.com>
 * derived from SteVe project
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 19.08.2014
 */
@Getter
public enum LmConfiguration {
    CONFIG;

    // Root mapping for Spring
    private final String springMapping = "/";
    // Web frontend
    private final String springManagerMapping = "/manager/*";
    // Mapping for CXF SOAP services
    private final String cxfMapping = "/services/*";
    // Dummy service path
    private final String routerEndpointPath = "/CentralSystemService";
    // Time zone for the application and database connections
    private final String timeZoneId = "UTC";  // or ZoneId.systemDefault().getId();

    // -------------------------------------------------------------------------
    // main.properties
    // -------------------------------------------------------------------------

    private final String contextPath;
    private final String loadmanagerVersion;
    private final String gitDescribe;
    private final ApplicationProfile profile;
    private final Auth auth;
    private final DB db;
    private final Jetty jetty;

    LmConfiguration() {
        PropertiesFileLoader p = new PropertiesFileLoader("main.properties");

        contextPath = sanitizeContextPath(p.getOptionalString("context.path"));
        loadmanagerVersion = p.getString("loadmanager.version");
        gitDescribe = useFallbackIfNotSet(p.getOptionalString("git.describe"), null);
        profile = ApplicationProfile.fromName(p.getString("profile"));

        jetty = Jetty.builder()
                     .serverHost(p.getString("lm.host"))
                     .gzipEnabled(p.getBoolean("server.gzip.enabled"))
                     .httpEnabled(p.getBoolean("http.enabled"))
                     .httpPort(p.getInt("http.port"))
                     .httpsEnabled(p.getBoolean("https.enabled"))
                     .httpsPort(p.getInt("https.port"))
                     .keyStorePath(p.getOptionalString("keystore.path"))
                     .keyStorePassword(p.getOptionalString("keystore.password"))
                     .build();

        db = DB.builder()
               .ip(p.getString("db.ip"))
               .port(p.getInt("db.port"))
               .schema(p.getString("db.schema"))
               .userName(p.getString("db.user"))
               .password(p.getString("db.password"))
               .sqlLogging(p.getBoolean("db.sql.logging"))
               .build();

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        auth = Auth.builder()
                   .passwordEncoder(encoder)
                   .userName(p.getString("auth.user"))
                   .encodedPassword(encoder.encode(p.getString("auth.password")))
                   .build();

        validate();
    }

    public String getLmCompositeVersion() {
        if (gitDescribe == null) {
            return loadmanagerVersion;
        } else {
            return loadmanagerVersion + "-g" + gitDescribe;
        }
    }

    private static String useFallbackIfNotSet(String value, String fallback) {
        if (value == null) {
            // if the property is optional, value will be null
            return fallback;
        } else if (value.startsWith("${")) {
            // property value variables start with "${" (if maven is not used, the value will not be set)
            return fallback;
        } else {
            return value;
        }
    }

    private String sanitizeContextPath(String s) {
        if (s == null || "/".equals(s)) {
            return "";

        } else if (s.startsWith("/")) {
            return s;

        } else {
            return "/" + s;
        }
    }

    private void validate() {
        if (!(jetty.httpEnabled || jetty.httpsEnabled)) {
            throw new IllegalArgumentException(
                    "HTTP and HTTPS are both disabled. Well, how do you want to access the server, then?");
        }
    }

    // -------------------------------------------------------------------------
    // Class declarations
    // -------------------------------------------------------------------------

    // Jetty configuration
    @Builder @Getter
    public static class Jetty {
        private final String serverHost;
        private final boolean gzipEnabled;

        // HTTP
        private final boolean httpEnabled;
        private final int httpPort;

        // HTTPS
        private final boolean httpsEnabled;
        private final int httpsPort;
        private final String keyStorePath;
        private final String keyStorePassword;
    }

    // Database configuration
    @Builder @Getter
    public static class DB {
        private final String ip;
        private final int port;
        private final String schema;
        private final String userName;
        private final String password;
        private final boolean sqlLogging;
    }

    // Credentials for Web interface access
    @Builder @Getter
    public static class Auth {
        private final PasswordEncoder passwordEncoder;
        private final String userName;
        private final String encodedPassword;
    }

}
