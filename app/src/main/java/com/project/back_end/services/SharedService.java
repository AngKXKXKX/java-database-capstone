package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SharedService {

    private final TokenService tokenService;
    private final PatientService patientService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public SharedService(TokenService tokenService,
                    PatientService patientService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository) {
        this.tokenService = tokenService;
        this.patientService = patientService;
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

    public ResponseEntity<Map<String, Object>> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);
            String role = "admin";
    
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Admin not found"));
            }
    
            if (!admin.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid password"));
            }
    
            String token = tokenService.generateToken(username, role);
    
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "message", "Login successful"
            ));
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed"));
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

    public boolean validatePatient(String email) {
        Patient patient = patientRepository.findByEmail(email);
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

    public List<?> filterPatient(String token, String condition, String doctorName,Long patientID) {
        //String email = tokenService.extractEmail(token);
        if (condition != null && doctorName != null) {
            return patientService.filterByDoctorAndCondition(patientID, doctorName, condition);
        } else if (condition != null) {
            return patientService.filterByCondition(patientID, condition);
        } else if (doctorName != null) {
            return patientService.filterByDoctor(patientID, doctorName);
        } else {
            return patientService.getPatientAppointment(patientID);
        }
    }

}