package com.stock.process.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import jakarta.persistence.*;

/**
 * @author Nabeel Ahmed
 * Class use to define predeinfe function
 */
@Entity
@Table(name = "ask_question")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AskQuestion {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "question", nullable = false, unique = true)
    private String question;

    @JoinColumn(name = "category", nullable = false)
    private String category;

    @JoinColumn(name = "is_active", nullable = false)
    private boolean isActive;

    // values: "system" or "user"
    @Column(name = "source_type", nullable = false)
    private String sourceType;

    public AskQuestion() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
