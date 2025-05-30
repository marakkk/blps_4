package com.blps.lab4.entities.googleplay;

import com.blps.lab4.enums.DevAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@Entity
@Table(name = "developer")
@NoArgsConstructor
@AllArgsConstructor
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    private String email;
    private boolean paymentProfile;

    @Enumerated(EnumType.STRING)
    private DevAccount accStatus;

    private double earnings;

    @OneToMany(mappedBy = "developer", fetch = FetchType.EAGER)
    private List<App> apps;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Developer developer = (Developer) o;
        return Objects.equals(id, developer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}