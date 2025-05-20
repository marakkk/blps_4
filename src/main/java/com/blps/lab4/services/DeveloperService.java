package com.blps.lab4.services;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.blps.lab4.dto.AppDto;
import com.blps.lab4.dto.DeveloperDto;
import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.entities.googleplay.Developer;
import com.blps.lab4.entities.payments.Payment;
import com.blps.lab4.enums.AppStatus;
import com.blps.lab4.enums.DevAccount;
import com.blps.lab4.enums.MonetizationType;
import com.blps.lab4.enums.PaymentStatus;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.repo.googleplay.DeveloperRepository;
import com.blps.lab4.repo.payments.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeveloperService {

    private final DeveloperRepository repository;
    private final AppRepository appRepository;
    private final GooglePlayService googlePlayService;
    private final UserTransactionManager userTransaction;

    public Map<String, String> publishApp(Long developerId, Long appId, boolean approvedByModerator, String moderatorComment) {
        try {
            userTransaction.begin();

            App app = appRepository.findById(appId).orElseThrow();

            Developer developer = repository.findById(developerId)
                    .orElseThrow(() -> new IllegalArgumentException("Developer not found with ID: " + app.getDeveloper().getId()));

            if (!app.getDeveloper().equals(developer)) throw new IllegalStateException("Unauthorized");
            if (app.getStatus() != AppStatus.PENDING) throw new IllegalStateException("App must be pending");

            if (approvedByModerator){
                googlePlayService.publishApp(app);

                userTransaction.commit();
                return Map.of("message", "Published successfully");
            }

            userTransaction.rollback();
            return Map.of("message", "App wasn't published because moderator didn't approve it");


        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    public DeveloperDto getDeveloperInfo(Long developerId) {
        Developer developer = repository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));
        return DeveloperDto.builder()
                .id(developer.getId())
                .name(developer.getUsername())
                .email(developer.getEmail())
                .paymentProfile(developer.isPaymentProfile())
                .accStatus(developer.getAccStatus())
                .earnings(developer.getEarnings())
                .apps(developer.getApps().stream()
                        .map(app -> AppDto.builder()
                                .id(app.getId())
                                .name(app.getName())
                                .version(app.getVersion())
                                .status(app.getStatus())
                                .downloads(app.getDownloads())
                                .revenue(app.getRevenue())
                                .inAppPurchases(app.isInAppPurchases())
                                .isNotFree(app.isNotFree())
                                .appPrice(app.getAppPrice())
                                .monetizationType(app.getMonetizationType())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
