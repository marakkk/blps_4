package com.blps.lab4.delegators;

import com.blps.lab4.services.DeveloperService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.blps.lab4.repo.payments.PaymentRepository;
import com.blps.lab4.entities.payments.Payment;
import com.blps.lab4.enums.PaymentStatus;
import com.blps.lab4.enums.MonetizationType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentProcessingDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessingDelegate.class);
    private final PaymentRepository paymentRepository;
    private static final double PUBLISHING_FEE = 25.0;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long developerId = (Long) execution.getVariable("developerId");
        Long appId = (Long) execution.getVariable("appId");
        String monetizationTypeStr = (String) execution.getVariable("monetizationType");
        MonetizationType monetizationType = MonetizationType.valueOf(monetizationTypeStr);

        Payment payment = Payment.builder()
                .amount(PUBLISHING_FEE)
                .monetizationType(monetizationType)
                .developerId(developerId)
                .appId(appId)
                .build();

        if (payment.getAmount() > 0) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            throw new IllegalStateException("Payment failed");
        }

        paymentRepository.save(payment);
        logger.info("Developer {} has successfully paid the fee: {}", developerId, payment.getAmount());

        execution.setVariable("paymentStatus", payment.getStatus().name());
    }
}