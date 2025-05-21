package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.repo.googleplay.UserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("paymentAccessCheckDelegate")
public class PaymentAccessCheckDelegate implements JavaDelegate {

    private final UserRepository userRepository;
    private final AppRepository appRepository;

    public PaymentAccessCheckDelegate(UserRepository userRepository, AppRepository appRepository) {
        this.userRepository = userRepository;
        this.appRepository = appRepository;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long userId = (Long) execution.getVariable("userId");
        Long appId = (Long) execution.getVariable("appId");

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        App app = appRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("App not found"));

        if (!app.isNotFree()) {
            throw new RuntimeException("This app is free. No payment required.");
        }

        execution.setVariable("appPrice", app.getAppPrice());
    }
}