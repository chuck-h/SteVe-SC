package nodomain.chuck.loadmanager;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static nodomain.chuck.loadmanager.LmConfiguration.CONFIG;

/**
 * We might also have used {@link Profile} for registering beans depending on profile,
 * but it only accepts String as value (which is not type safe) and we use enums
 * in {@link ApplicationProfile}. The newer {@link Condition} and {@link Conditional} APIs
 * are more flexible by being programmatic anyway.
 *
 * Typing the String value of the enum is not an option, because it might cause problems
 * in future when we change or refactor something. This is how technical debt starts
 * and then everything falls apart.
 *
 *
 * @author Chuck Harrison <cfharr@gmail.com>
 * derived from SteVe project 
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.12.2015
 */
public class LmProdCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return CONFIG.getProfile().isProd();
    }
}
