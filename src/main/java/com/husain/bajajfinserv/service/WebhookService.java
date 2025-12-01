package com.husain.bajajfinserv.service;

import com.husain.bajajfinserv.dto.WebhookRequest;
import com.husain.bajajfinserv.dto.WebhookResponse;
import com.husain.bajajfinserv.dto.SubmissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${student.name}")
    private String studentName;

    @Value("${student.regNo}")
    private String studentRegNo;

    @Value("${student.email}")
    private String studentEmail;

    @Value("${api.webhook.generate}")
    private String webhookGenerateUrl;

    @Value("${api.webhook.test}")
    private String webhookTestUrl;

    // SQL Query for Question 2
    private final String SQL_QUERY = "SELECT d.DEPARTMENT_NAME, " +
            "ROUND(AVG(YEAR(CURDATE()) - YEAR(e.DOB)), 2) AS AVERAGE_AGE, " +
            "STRING_AGG(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME), ', ' ORDER BY e.EMP_ID LIMIT 10) AS EMPLOYEE_LIST " +
            "FROM DEPARTMENT d " +
            "LEFT JOIN EMPLOYEE e ON d.DEPARTMENT_ID = e.DEPARTMENT " +
            "INNER JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
            "WHERE p.AMOUNT > 70000 " +
            "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME " +
            "ORDER BY d.DEPARTMENT_ID DESC";

    public void executeWebhookFlow() {
        try {
            // Step 1: Generate Webhook
            logger.info("Generating webhook...");
            WebhookResponse webhookResponse = generateWebhook();

            if (webhookResponse == null || webhookResponse.getWebhook() == null) {
                logger.error("Failed to get webhook URL");
                return;
            }

            String webhook = webhookResponse.getWebhook();
            String accessToken = webhookResponse.getAccessToken();

            logger.info("Webhook generated: " + webhook);
            logger.info("Access Token received");

            // Step 2: Submit SQL Query
            logger.info("Submitting SQL query...");
            submitAnswer(webhook, accessToken, SQL_QUERY);

            logger.info("Query submitted successfully!");

        } catch (Exception e) {
            logger.error("Error: " + e.getMessage(), e);
        }
    }

    private WebhookResponse generateWebhook() {
        try {
            WebhookRequest request = new WebhookRequest(
                    studentName,
                    studentRegNo,
                    studentEmail
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity entity = new HttpEntity<>(request, headers);

            ResponseEntity response = restTemplate.postForEntity(
                    webhookGenerateUrl,
                    entity,
                    WebhookResponse.class
            );

            return (WebhookResponse) response.getBody();
        } catch (Exception e) {
            logger.error("Error generating webhook: " + e.getMessage(), e);
            return null;
        }
    }

    private void submitAnswer(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            SubmissionRequest request = new SubmissionRequest(sqlQuery);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            HttpEntity entity = new HttpEntity<>(request, headers);

            ResponseEntity response = restTemplate.postForEntity(
                    webhookUrl,
                    entity,
                    String.class
            );

            logger.info("Submission response: " + response.getStatusCode());
            logger.info("Response body: " + response.getBody());

        } catch (Exception e) {
            logger.error("Error submitting answer: " + e.getMessage(), e);
        }
    }

}