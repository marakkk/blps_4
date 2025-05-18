package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.services.GooglePlayService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component("autoReviewDelegate")
public class AutoReviewDelegate implements JavaDelegate {

    private final AppRepository appRepository;
    private final GooglePlayService googlePlayService;

    @Override
    public void execute(DelegateExecution execution) {
        Long appId = (Long) execution.getVariable("appId");
        App app = appRepository.findById(appId).orElseThrow();

        Map<String, String> reviewResult = googlePlayService.autoReviewApp(app);

        boolean requiresManualReview = reviewResult.getOrDefault("message", "").equals("App requires manual review.");
        execution.setVariable("requiresManualReview", requiresManualReview);
    }
}
