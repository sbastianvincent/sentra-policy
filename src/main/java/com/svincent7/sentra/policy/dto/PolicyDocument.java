package com.svincent7.sentra.policy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PolicyDocument {
    @JsonProperty("Version")
    private String version;

    @JsonProperty("Statement")
    private List<Statement> statement;
}
