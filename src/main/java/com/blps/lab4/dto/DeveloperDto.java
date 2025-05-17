package com.blps.lab4.dto;

import com.blps.lab4.enums.DevAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDto {
    private Long id;
    private String name;
    private String email;
    private boolean paymentProfile;
    private DevAccount accStatus;
    private double earnings;
    private List<AppDto> apps;
}
