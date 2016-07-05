package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@JsonIgnoreProperties({"type", "expired", "_rev"})
@EqualsAndHashCode(callSuper = true)
public class Token extends CouchDBModel {
    public enum TokenMode {
        LEAD, MATE
    }
    @SuppressWarnings("unused")
    private final String Type = "Token";

    private Date expirationDate;

    private String teamId;
    private TokenMode mode;

    public boolean isExpired() {
        return expirationDate.before(new Date());
    }
}
