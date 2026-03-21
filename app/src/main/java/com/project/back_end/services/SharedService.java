package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedService {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public SharedService(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public boolean validateToken(String token, String role) {
        try {
            return tokenService.isTokenValid(token, role);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResponseEntity<String> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null) {
                return new ResponseEntity<>("Admin not found", HttpStatus.UNAUTHORIZED);
            }
            if (!admin.getPassword().equals(password)) {
                return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
            }
            String token = tokenService.generateToken(admin.getUsername(), "admin");
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Doctor> filterDoctor(String name, String specialty, String timePeriod) {
        List<Doctor> doctors = doctorRepository.findAll(); // fallback
        if (name != null && !name.isEmpty()) {
            doctors = doctorRepository.findByNameContainingIgnoreCase(name);
        }
        if (specialty != null && !specialty.isEmpty()) {
            doctors.removeIf(d -> !d.getSpecialty().equalsIgnoreCase(specialty));
        }
        if (timePeriod != null && !timePeriod.isEmpty()) {
            doctors.removeIf(d -> !d.isAvailableDuring(timePeriod));
        }
        return doctors;
    }

    public int validateAppointment(Long doctorId, String requestedTime) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) return -1;
        return doctor.getAvailableTimes().contains(requestedTime) ? 1 : 0;
    }

    public boolean validatePatient(String email, String phone) {
        Patient patient = patientRepository.findByEmailOrPhone(email, phone);
        return patient == null;
    }

    public ResponseEntity<String> validatePatientLogin(String email, String password) {
        try {
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return new ResponseEntity<>("Patient not found", HttpStatus.UNAUTHORIZED);
            }
            if (!patient.getPassword().equals(password)) {
                return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
            }
            String token = tokenService.generateToken(email, "patient");
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<?> filterPatient(String token, String condition, String doctorName, PatientService patientService) {
        String email = tokenService.extractEmail(token);
        if (condition != null && doctorName != null) {
            return patientService.filterByDoctorAndCondition(email, doctorName, condition);
        } else if (condition != null) {
            return patientService.filterByCondition(email, condition);
        } else if (doctorName != null) {
            return patientService.filterByDoctor(email, doctorName);
        } else {
            return patientService.getPatientAppointment(email);
        }
    }

}