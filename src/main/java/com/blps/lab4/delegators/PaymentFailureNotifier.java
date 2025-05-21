package com.blps.lab4.delegators;

import com.blps.lab4.services.MailNotificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component("paymentFailureNotifier")
public class PaymentFailureNotifier implements JavaDelegate {

    private final MailNotificationService mailNotificationService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentFailureNotifier.class);


    @Autowired
    public PaymentFailureNotifier(MailNotificationService mailNotificationService) {
        this.mailNotificationService = mailNotificationService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String error = (String) execution.getVariable("errorMessage");
        String userEmail = (String) execution.getVariable("userEmail");

        if (userEmail != null && !userEmail.isEmpty()) {
            String subject = "Payment Process Unsuccessful";
            String body = "Dear user,\n\n" +
                    "We regret to inform you that your payment process was unsuccessful.\n" +
                    "Reason: " + error + "\n\n" +
                    "Please try again or contact support if the problem persists.\n\n" +
                    "Best regards,\nYour Payment Team";

            mailNotificationService.notifyUser(userEmail, subject, body);
        }

        logger.info("Payment failed for user. Error: {}", error);    }
}