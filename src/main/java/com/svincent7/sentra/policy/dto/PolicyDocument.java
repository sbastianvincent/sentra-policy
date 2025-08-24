package com.svincent7.sentra.policy.dto;

import lombok.Data;

import java.util.List;

@Data
public class PolicyDocument {
    private String version;
    private List<Statement> statements;
}
