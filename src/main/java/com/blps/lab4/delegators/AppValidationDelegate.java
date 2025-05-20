package com.blps.lab4.delegators;

import com.blps.lab4.enums.AppStatus;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.entities.googleplay.App;
import lombok.RequiredArgsConstructor;

@Component("appValidationDelegate")
@RequiredArgsConstructor
public class AppValidationDelegate implements JavaDelegate {

    private final AppRepository appRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long appId = (Long) execution.getVariable("appId");

        App app = appRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("App not found"));

        if (app.getName() == null || app.getName().isEmpty()) {
            throw new IllegalStateException("App name is required");
        }

        if (app.getVersion() <= 0) {
            throw new IllegalStateException("Invalid app version");
        }

        if (!app.isCorrectPermissions()) {
            throw new IllegalStateException("App permissions are not correct");
        }

        app.setStatus(AppStatus.VALIDATED);
        appRepository.save(app);

        execution.setVariable("isAppValid", true);
    }

}