package com.blps.lab4.repo.googleplay;

import com.blps.lab4.entities.googleplay.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {}
