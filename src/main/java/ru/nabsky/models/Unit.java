package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties({"type", "_rev", "valid"})
public class Unit {
    @SuppressWarnings("unused")
    private final String Type = "Unit";

    @JsonProperty("id")
    private String _id;
    private String _rev;

    private String name;

    private String startTime;
    private String workLength;
    private String breakLength;

    public boolean isValid() {
        return name != null;
    }
}
