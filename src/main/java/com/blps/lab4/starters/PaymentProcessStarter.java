package com.blps.lab4.starters;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessStarter {

    private final RuntimeService runtimeService;


    public void startPaymentProcess(Long appId, Long userId) {
        Map<String, Object> variables = Map.of(
                "appId", appId,
                "userId", userId

        );

        runtimeService.startProcessInstanceByKey("Process_0zrnfl0", variables);
    }



}
