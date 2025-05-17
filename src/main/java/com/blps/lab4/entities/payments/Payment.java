package com.blps.lab4.entities.payments;

import com.blps.lab4.enums.MonetizationType;
import com.blps.lab4.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;

    @Enumerated(EnumType.STRING)
    private MonetizationType monetizationType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Long developerId;
    private Long appId;

    private Long userId;
}
