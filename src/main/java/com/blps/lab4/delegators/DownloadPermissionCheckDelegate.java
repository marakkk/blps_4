package com.blps.lab4.delegators;

import com.blps.lab4.enums.PaymentStatus;
import com.blps.lab4.repo.payments.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("downloadPermissionCheckDelegate")
public class DownloadPermissionCheckDelegate implements JavaDelegate {

    private final PaymentRepository paymentRepository;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long userId = (Long) execution.getVariable("userId");
        Long appId = (Long) execution.getVariable("appId");

        boolean hasAccess = paymentRepository.existsByAppIdAndUserIdAndStatus(
                appId, userId, PaymentStatus.SUCCESS);

        if (!hasAccess) {
            throw new RuntimeException("User doesn't have download permissions");
        }
    }
}