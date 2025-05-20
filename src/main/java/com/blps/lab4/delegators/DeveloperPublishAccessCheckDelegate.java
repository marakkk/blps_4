package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.entities.googleplay.Developer;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.blps.lab4.repo.googleplay.DeveloperRepository;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.enums.AppStatus;
import lombok.RequiredArgsConstructor;

@Component("developerPublishAccessCheckDelegate")
@RequiredArgsConstructor
public class DeveloperPublishAccessCheckDelegate implements JavaDelegate {

    private final DeveloperRepository developerRepository;
    private final AppRepository appRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long developerId = (Long) execution.getVariable("developerId");
        Long appId = (Long) execution.getVariable("appId");

        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new IllegalStateException("Developer not found"));

        App app = appRepository.findById(appId)
                .orElseThrow(() -> new IllegalStateException("App not found"));

        if (!app.getDeveloper().equals(developer)) {
            throw new IllegalStateException("Developer doesn't own this app");
        }

        if (app.getStatus() != AppStatus.PENDING) {
            throw new IllegalStateException("App must be in PENDING status");
        }

        execution.setVariable("hasPublishAccess", true);
    }
}