package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties({"type", "_rev"})
public class Time {

    @SuppressWarnings("unused")
    private final String Type = "Time";

    @JsonProperty("id")
    private String _id;
    private String _rev;

    private Long start;
    private Long end;

    private TimeMode mode;

    private String mateId;
    private String dayId;

    private String photoStart;
    private String photoEnd;
}
