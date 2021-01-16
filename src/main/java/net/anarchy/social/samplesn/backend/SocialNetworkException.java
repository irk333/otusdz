package net.anarchy.social.samplesn.backend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SocialNetworkException extends ResponseStatusException {

    public SocialNetworkException(HttpStatus status) {
        super(status);
    }

    public SocialNetworkException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public SocialNetworkException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public SocialNetworkException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
