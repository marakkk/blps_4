package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.entities.googleplay.AppUser;
import com.blps.lab4.entities.payments.Payment;
import com.blps.lab4.enums.MonetizationType;
import com.blps.lab4.enums.PaymentStatus;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.repo.googleplay.UserRepository;
import com.blps.lab4.repo.payments.PaymentRepository;
import com.blps.lab4.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@RequiredArgsConstructor
@Component("paymentProcessDelegate")
public class PaymentProcessDelegate implements JavaDelegate {

    private final PaymentService paymentService;



    public void execute(DelegateExecution execution) throws Exception {
        Long userId = (Long) execution.getVariable("userId");
        Long appId = (Long) execution.getVariable("appId");

        try {
            Payment payment = paymentService.payForApp(userId, appId);
            execution.setVariable("paymentId", payment.getId());
            execution.setVariable("paymentSuccessful", true);
        } catch (ResponseStatusException e) {
            execution.setVariable("errorMessage", e.getReason());
            execution.setVariable("paymentSuccessful", false);
            throw new BpmnError("PAYMENT_FAILED", "Payment failed: " + e.getMessage());
        }
    }

}


