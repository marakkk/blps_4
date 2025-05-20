package com.blps.lab4.starters;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppSubmissionProcessStarter {

    private final RuntimeService runtimeService;

    public void startSubmissionProcess(Long appId, Long developerId, boolean wantsToMonetize, boolean wantsToCharge) {
        Map<String, Object> variables = Map.of(
                "appId", appId,
                "developerId", developerId,
                "wantsToMonetize", wantsToMonetize,
                "wantsToCharge", wantsToCharge
        );

        runtimeService.startProcessInstanceByKey("Process_0nuhigb", variables);
    }






    //starter modentiyacija
}
