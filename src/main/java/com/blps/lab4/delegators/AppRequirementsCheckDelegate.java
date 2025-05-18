package com.blps.lab4.delegators;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.enums.AppStatus;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppRequirementsCheckDelegate implements JavaDelegate {

    private final AppRepository appRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long appId = (Long) execution.getVariable("appId");

        App app = appRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("App not found"));

        boolean meetsRequirements = app.getName() != null && !app.getName().isEmpty()
                && app.getVersion() > 0
                && app.isCorrectPermissions()
                && app.getStatus() == AppStatus.VALIDATED;

        if (!meetsRequirements) {
            throw new IllegalStateException("App doesn't meet minimum publication requirements");
        }

        execution.setVariable("meetsPublicationRequirements", meetsRequirements);
    }

    //PENDING STATUS
}