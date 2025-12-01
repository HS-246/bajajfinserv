package com.husain.bajajfinserv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor      // ← Jackson needs this to create empty object
@AllArgsConstructor     // ← Still needed for creating objects manually
@Getter
public class WebhookResponse {
    @JsonProperty("webhook")        // ← Maps API response field
    private String webhook;

    @JsonProperty("accessToken")    // ← Maps API response field
    private String accessToken;
}