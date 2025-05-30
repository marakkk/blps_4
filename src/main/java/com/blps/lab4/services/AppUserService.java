package com.blps.lab4.services;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.blps.lab4.dto.AppDto;
import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.entities.googleplay.AppUser;
import com.blps.lab4.entities.payments.Payment;
import com.blps.lab4.enums.PaymentStatus;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.repo.googleplay.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppUserService {

    private final UserRepository userRepository;
    private final AppRepository appRepository;
    private final PaymentService paymentService;
    private final UserTransactionManager userTransaction;

    public List<AppDto> viewAppCatalog() {
        return appRepository.findAll().stream()
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
                .collect(Collectors.toList());
    }

    public String downloadApp(Long userId, Long appId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        App app = appRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("App not found"));

        if (app.isNotFree()) {
            boolean hasSuccessfulPayment = paymentService.hasSuccessfulPayment(userId, appId);
            if (!hasSuccessfulPayment) {
                throw new IllegalArgumentException("Payment required before downloading this app");
            }
        }


        app.setDownloads(app.getDownloads() + 1);
        appRepository.save(app);

        return "User " + user.getUsername() + " successfully downloaded " + app.getName() +
                ". Total downloads: " + app.getDownloads();
    }

    public String useApp(Long userId, Long appId) {
        try {
            userTransaction.begin();

            AppUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            App app = appRepository.findById(appId)
                    .orElseThrow(() -> new IllegalArgumentException("App not found"));

            String result = "User " + user.getUsername() + " started using " + app.getName() + ".";

            if (app.isInAppPurchases()) {
                result += "\nIn-app purchases detected.";
                Payment inAppPayment = paymentService.payForInAppPurchase(userId, appId);
                if (inAppPayment.getStatus() == PaymentStatus.SUCCESS) {
                    result += "\nUser purchased in-app content and continues using the app.";
                } else {
                    result += "\nUser could not purchase in-app content.";
                }
            }

            userTransaction.commit();
            return result;
        } catch (Exception e) {
            if (userTransaction != null) {
                try {
                    userTransaction.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    public String completePaidAppDownload(Long userId, Long appId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        App app = appRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("App not found"));

        if (!paymentService.hasSuccessfulPayment(userId, appId)) {
            throw new IllegalStateException("Payment verification failed");
        }

        app.setDownloads(app.getDownloads() + 1);
        appRepository.save(app);

        return "Download completed. Total downloads: " + app.getDownloads();
    }
}
