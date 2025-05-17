package com.blps.lab4.repo.googleplay;

import com.blps.lab4.entities.googleplay.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {}
