package com.blps.lab4.delegators;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("grantAccessDelegate")
public class GrantAccessDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long userId = (Long) execution.getVariable("userId");
        Long appId = (Long) execution.getVariable("appId");

        execution.setVariable("accessGranted", true);
    }
}