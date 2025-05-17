package com.blps.lab4.delegators;

import com.blps.lab4.services.PayoutService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PayoutDelegate implements JavaDelegate {

    private final PayoutService payoutService;

    @Override
    public void execute(DelegateExecution execution) {
        payoutService.processDeveloperPayouts();
    }
}
