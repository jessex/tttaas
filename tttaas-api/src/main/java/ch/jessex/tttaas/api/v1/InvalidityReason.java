package ch.jessex.tttaas.api.v1;

import org.apache.commons.lang.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The reason why some move is invalid.
 *
 * @author jessex
 * @since 0.0.1
 */
public class InvalidityReason {
    private final String reason;

    public InvalidityReason(String reason) {
        checkArgument(!StringUtils.isBlank(reason), "reason cannot be blank, empty, or null");
        this.reason = reason;
    }

    public InvalidityReason(String reason, Object... args) {
        checkArgument(!StringUtils.isBlank(reason), "reason cannot be blank empty or null");
        this.reason = String.format(reason, args);
    }

    public String getReason() {
        return reason;
    }
}
