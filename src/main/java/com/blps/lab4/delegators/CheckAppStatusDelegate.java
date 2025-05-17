package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.enums.AppStatus;
import com.blps.lab4.repo.googleplay.AppRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CheckAppStatusDelegate implements JavaDelegate {

    private final AppRepository appRepository;

    @Override
    public void execute(DelegateExecution execution) {
        Long appId = (Long) execution.getVariable("appId");
        App app = appRepository.findById(appId).orElseThrow(() -> new RuntimeException("App not found"));
        if (app.getStatus() != AppStatus.UNDER_REVIEW) {
            throw new IllegalStateException("App must be in UNDER_REVIEW status");
        }
        execution.setVariable("app", app);
    }
}
