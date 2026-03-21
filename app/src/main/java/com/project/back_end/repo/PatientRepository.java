package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByEmail(String email);

    // 2. Find a patient by email or phone number
    Patient findByEmailOrPhone(String email, String phone);
}