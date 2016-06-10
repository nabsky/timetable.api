package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties({"type", "_rev"})
public class Token {
    @SuppressWarnings("unused")
    private final String Type = "Token";

    @JsonProperty("id")
    private String _id;
    private String _rev;

    private Date expirationDate;

    private String teamId;
    private boolean leadMode;
}
