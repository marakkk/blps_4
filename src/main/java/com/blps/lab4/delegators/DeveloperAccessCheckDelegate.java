package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.Developer;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.blps.lab4.repo.googleplay.DeveloperRepository;
import com.blps.lab4.enums.DevAccount;
import lombok.RequiredArgsConstructor;

@Component("developerAccessCheckDelegate")
@RequiredArgsConstructor
public class DeveloperAccessCheckDelegate implements JavaDelegate {

    private final DeveloperRepository developerRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long developerId = (Long) execution.getVariable("developerId");

        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new IllegalStateException("Developer not found"));

        if (developer.getAccStatus() == DevAccount.UNPAID) register(developerId);

        if (!developer.isPaymentProfile()) {
            throw new IllegalStateException("Payment profile not set up");
        }

        execution.setVariable("hasSubmissionAccess", true);
    }

    public void register(Long developerId) {
        Developer dev = developerRepository.findById(developerId).orElseThrow();
        dev.setAccStatus(DevAccount.PAID);
        dev.setPaymentProfile(true);
        developerRepository.save(dev);
    }

}
