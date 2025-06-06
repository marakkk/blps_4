package com.blps.lab4.services;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.enums.AppStatus;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.resourceAdapter.*;
import jakarta.resource.ResourceException;
import jakarta.resource.cci.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class GooglePlayService {

    private final AppRepository appRepository;
    private final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(GooglePlayService.class);

    public Map<String, String> autoReviewApp(App app) {

        if (app.getStatus() != AppStatus.PENDING) {
            throw new IllegalStateException("App must be submitted for review before auto-review can start.");
        }

        Map<String, String> response = new HashMap<>();
        int totalSeverity = 0;

        Map<String, Integer> technicalIssues = checkTechnicalIssues(app);
        for (Integer severity : technicalIssues.values()) {
            totalSeverity += severity;
        }

        if (totalSeverity > 15) {
            return rejectApp(app, "App has critical issues: " + technicalIssues.keySet());
        }

        Map<String, Integer> policyIssues = checkPolicyCompliance(app);
        for (Integer severity : policyIssues.values()) {
            totalSeverity += severity;
        }

        if (totalSeverity > 10) {
            return rejectApp(app, "App violates Google Play policies: " + policyIssues.keySet());
        }

        double manualReviewChance = Math.min(0.3 + (totalSeverity * 0.05), 0.9);

        if (random.nextDouble() < manualReviewChance) {
            app.setStatus(AppStatus.UNDER_REVIEW);

            response.put("message", "App requires manual review.");
            logger.info("App requires manual review.");
        } else {
            app.setStatus(AppStatus.APPROVED);
            response.put("message", "App approved automatically.");
            logger.info("App approved automatically.");
        }

        appRepository.save(app);
        return response;

    }

    public void publishApp(App app) {
        if (app.getStatus() != AppStatus.APPROVED) {
            throw new IllegalStateException("App must be approved before publishing.");
        }
        app.setStatus(AppStatus.PUBLISHED);
        appRepository.save(app);
    }

    private Map<String, String> rejectApp(App app, String reason) {
        app.setStatus(AppStatus.REJECTED);
        appRepository.save(app);
        return Map.of("reason", reason);
    }

    private Map<String, Integer> checkTechnicalIssues(App app) {
        Map<String, Integer> issues = new HashMap<>();
        if (!app.isCorrectPermissions()) {
            issues.put("Permissions are incorrect or excessive.", random.nextInt(5) + 3);
        }
        if (!app.isCorrectMetadata()) {
            issues.put("App metadata is incorrect or incomplete.", random.nextInt(4) + 2);
        }
        return issues;
    }

    private Map<String, Integer> checkPolicyCompliance(App app) {
        Map<String, Integer> issues = new HashMap<>();
        if (app.isViolatesGooglePlayPolicies()) {
            issues.put("App violates Google Play policies.", random.nextInt(6) + 5);
        }
        if (app.isChildrenBadPolicy()) {
            issues.put("App does not comply with children's content policies.", random.nextInt(6) + 5);
        }
        return issues;
    }
}
