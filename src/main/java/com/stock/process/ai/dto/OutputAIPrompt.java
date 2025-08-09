package com.stock.process.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OutputAIPrompt {

    private String text;

    public OutputAIPrompt() {}

    public String getText() {
        return text;
    }

    public OutputAIPrompt setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
