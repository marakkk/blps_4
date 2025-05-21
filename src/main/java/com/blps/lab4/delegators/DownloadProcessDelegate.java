package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.enums.PaymentStatus;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.repo.payments.PaymentRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("downloadProcessDelegate")
public class DownloadProcessDelegate implements JavaDelegate {

    private final AppRepository appRepository;
    private final PaymentRepository paymentRepository;

    public DownloadProcessDelegate(AppRepository appRepository, PaymentRepository paymentRepository) {
        this.appRepository = appRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public void execute(DelegateExecution execution) throws Exception {
        Long userId = (Long) execution.getVariable("userId");
        Long appId = (Long) execution.getVariable("appId");

        if (!paymentRepository.existsByAppIdAndUserIdAndStatus(appId, userId, PaymentStatus.SUCCESS)) {
            throw new RuntimeException("Payment verification failed");
        }

        App app = appRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("App not found"));

        app.setDownloads(app.getDownloads() + 1);
        appRepository.save(app);

        execution.setVariable("downloadCompleted", true);
    }
}