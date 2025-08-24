package com.svincent7.sentra.policy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class Statement {
    @JsonProperty("Sid")
    private String sid;

    @JsonProperty("Effect")
    private String effect;

    @JsonProperty("Action")
    @JsonDeserialize(using = StatementListDeserializer.class)
    private List<String> action;

    @JsonProperty("Resource")
    @JsonDeserialize(using = StatementListDeserializer.class)
    private List<String> resource;

    @JsonProperty("Condition")
    private Condition conditions;
}
