package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties({"type", "_rev", "valid"})
public class Mate {
    @SuppressWarnings("unused")
    private final String Type = "Mate";

    @JsonProperty("id")
    private String _id;
    private String _rev;

    private String name;
    private String surname;

    private String birthday;
    private String phone;
    private String email;
    private String vkId;

    private String startTime;
    private String workLength;
    private String breakLength;

    private String unitId;

    public boolean isValid() {
        return name != null && surname != null;
    }
}
