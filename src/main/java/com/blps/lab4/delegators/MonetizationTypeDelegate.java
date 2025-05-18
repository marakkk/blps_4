package com.blps.lab4.delegators;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.blps.lab4.enums.MonetizationType;

@Component
public class MonetizationTypeDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Boolean wantsToMonetize = (Boolean) execution.getVariable("wantsToMonetize");
        Boolean wantsToCharge = (Boolean) execution.getVariable("wantsToCharge");

        MonetizationType monetizationType;

        if (!wantsToMonetize && !wantsToCharge) {
            monetizationType = MonetizationType.FREE;
        } else if (wantsToCharge) {
            monetizationType = MonetizationType.FOR_MONEY;
        } else {
            monetizationType = MonetizationType.IN_APP_PURCHASES;
        }

        execution.setVariable("monetizationType", monetizationType.name());
    }
}