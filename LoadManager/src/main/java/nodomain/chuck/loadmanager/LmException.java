package nodomain.chuck.loadmanager;

import static java.lang.String.format;

/**
 * @author Chuck Harrison <cfharr@gmail.com>
 * derived from SteVe project 
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.08.2014
 */
public class LmException extends RuntimeException {

    private static final long serialVersionUID = 0xdeadbeefL;

    public LmException(String message) {
        super(message);
    }

    public LmException(String message, Throwable cause) {
        super(message, cause);
    }

    // -------------------------------------------------------------------------
    // No String/variable interpolation in Java. Use format instead.
    // -------------------------------------------------------------------------

    public LmException(String template, Object arg1) {
        this(format(template, arg1));
    }

    public LmException(String template, Object arg1, Throwable cause) {
        this(format(template, arg1), cause);
    }

    public LmException(String template, Object arg1, Object arg2) {
        this(format(template, arg1, arg2));
    }

    public LmException(String template, Object arg1, Object arg2, Throwable cause) {
        this(format(template, arg1, arg2), cause);
    }
}
