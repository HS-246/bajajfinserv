package com.husain.bajajfinserv.listener;

import com.husain.bajajfinserv.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StartupListener {

    private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);

    @Autowired
    private WebhookService webhookService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application started successfully!");
        logger.info("starting webhook flow...\n");
        webhookService.executeWebhookFlow();
    }

}
