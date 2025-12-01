package com.husain.bajajfinserv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class SubmissionRequest {

    private String finalQuery;

    public SubmissionRequest() {
    }

    public SubmissionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }
}
