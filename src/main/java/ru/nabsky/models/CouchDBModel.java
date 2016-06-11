package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class CouchDBModel {
    @JsonProperty("id")
    private String _id;
    private String _rev;
}
