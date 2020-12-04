package edu.nju.se.teamnamecannotbeempty.backend.config.parameter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "peer-review-msg")
public class PeerReviewMsg extends GlobalMsg {
    private String invalidTerm;
    private String nullParam;

    public String getInvalidTerm() {
        return invalidTerm;
    }

    public void setInvalidTerm(String invalidTerm) {
        this.invalidTerm = invalidTerm;
    }

    public String getNullParam() {
        return nullParam;
    }

    public void setNullParam(String nullParam) {
        this.nullParam = nullParam;
    }
}
