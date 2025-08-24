package com.svincent7.sentra.policy.dto;

import lombok.Data;

import java.util.List;

@Data
public class Statement {
    private String sid;
    private String effect;
    private List<String> actions;
    private List<String> resources;
    private Condition conditions;
}
