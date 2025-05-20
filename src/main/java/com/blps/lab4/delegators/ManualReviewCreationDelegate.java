package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.enums.AppStatus;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.blps.lab4.services.GooglePlayService;
import com.blps.lab4.repo.googleplay.AppRepository;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ManualReviewCreationDelegate implements JavaDelegate {

    private final GooglePlayService googlePlayService;
    private final AppRepository appRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long appId = (Long) execution.getVariable("appId");
        App app = appRepository.findById(appId)
                .orElseThrow(() -> new IllegalStateException("App not found"));

        if (app.getStatus() != AppStatus.UNDER_REVIEW) {
            throw new IllegalStateException("App must be in UNDER_REVIEW status for manual review.");
        }

        execution.setVariable("manualReviewRequested", true);
        execution.setVariable("reviewStatus", "PENDING");

    }
}