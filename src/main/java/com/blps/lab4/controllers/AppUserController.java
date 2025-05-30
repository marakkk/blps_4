package com.blps.lab4.controllers;

import com.blps.lab4.dto.AppDto;
import com.blps.lab4.services.AppUserService;
import com.blps.lab4.services.PaymentService;
import com.blps.lab4.starters.PaymentProcessStarter;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-actions")
public class AppUserController {

    private final AppUserService appUserService;
    private final PaymentProcessStarter paymentProcessStarter;

    @PreAuthorize("hasRole('USER') and hasAuthority('APP_CATALOG')")
    @GetMapping("/catalog")
    public ResponseEntity<List<AppDto>> viewAppCatalog() {
        List<AppDto> catalog = appUserService.viewAppCatalog();
        return ResponseEntity.ok(catalog);
    }

    @PreAuthorize("hasRole('USER') and hasAuthority('APP_DOWNLOAD')")
    @PostMapping("/{userId}/download/{appId}")
    public ResponseEntity<String> downloadApp(@PathVariable Long userId, @PathVariable Long appId) {
        String result = appUserService.downloadApp(userId, appId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER') and hasAuthority('APP_USE')")
    @PostMapping("/{userId}/use/{appId}")
    public ResponseEntity<String> useApp(@PathVariable Long userId, @PathVariable Long appId) {
        String result = appUserService.useApp(userId, appId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER') and hasAuthority('APP_PURCHASE')")
    @PostMapping("/{userId}/purchase/{appId}")
    public ResponseEntity<String> initiatePaidAppPurchase(@PathVariable("userId") Long userId, @PathVariable("appId") Long appId) {
        paymentProcessStarter.startPaymentProcess(appId, userId);
        return ResponseEntity.accepted().body("Purchase process started");
    }


}