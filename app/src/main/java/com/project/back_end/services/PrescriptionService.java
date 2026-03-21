package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public Map<String, Object> savePrescription(Prescription prescription) {
        Map<String, Object> response = new HashMap<>();
    
        List<Prescription> existing = prescriptionRepository.findByAppointmentId(prescription.getAppointmentId());
        if (!existing.isEmpty()) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "Prescription already exists for this appointment.");
            return response; // just the Map
        }
    
        prescriptionRepository.save(prescription);
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "Prescription saved successfully.");
        return response;
    }
   
    public Map<String, Object> getPrescription(Long appointmentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
            if (prescriptions.isEmpty()) {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "No prescriptions found for the given appointment.");
                return response; // just the Map
            }

            response.put("status", HttpStatus.OK.value());
            response.put("prescription", prescriptions);
            return response; // just the Map
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "An error occurred while fetching the prescription.");
            return response; // just the Map
        }
    }
}