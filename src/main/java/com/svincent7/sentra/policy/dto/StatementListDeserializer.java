package com.svincent7.sentra.policy.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatementListDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(final JsonParser p, final DeserializationContext context) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        List<String> resources = new ArrayList<>();

        if (node.isTextual()) {
            // Handle single string like "*"
            resources.add(node.asText());
        } else if (node.isArray()) {
            // Handle array like ["act1", "act2"]
            for (JsonNode element : node) {
                if (element.isTextual()) {
                    resources.add(element.asText());
                }
            }
        }

        return resources;
    }
}
