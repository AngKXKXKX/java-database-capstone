package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.SharedService;
import com.project.back_end.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SharedService service;
    private final TokenService tokenService;

    public AppointmentController(AppointmentService appointmentService, SharedService service, TokenService tokenService) {
        this.appointmentService = appointmentService;
        this.service = service;
        this.tokenService=tokenService;
    }

    @GetMapping("/appointments/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String token,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }
    
        Doctor doctor = tokenService.getDoctorFromToken(token);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Doctor not found"));
        }
    
        try {
            LocalDateTime rangeStart = (start != null) ? start : LocalDateTime.MIN;
            LocalDateTime rangeEnd = (end != null) ? end : LocalDateTime.MAX;
    
            List<Appointment> appointments = appointmentService.getAppointments(
                    doctor.getId(), rangeStart, rangeEnd, patientName
            );
    
            return ResponseEntity.ok(Map.of(
                    "count", appointments.size(),
                    "appointments", appointments
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch appointments", "details", e.getMessage()));
        }
    }
 
    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment
    ) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        try {
            int result = appointmentService.bookAppointment(appointment);
            if (result == 1) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Appointment booked successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Doctor not available or invalid appointment"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to book appointment"));
        }
    }

    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable String token,
            @PathVariable Long appointmentId,
            @RequestBody Appointment updatedAppointment,
            @RequestBody Long patientId
    ) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }
        try {
            String result = appointmentService.updateAppointment(appointmentId,updatedAppointment,patientId);
            if ("success".equalsIgnoreCase(result)) {
                return ResponseEntity.ok(Map.of("message", "Appointment updated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", result));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update appointment"));
        }
    }

    @DeleteMapping("/cancel/{token}/{appointmentId}/{patientId}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable String token,
            @PathVariable Long appointmentId,
            @PathVariable Long patientId
    ) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        try {
            boolean canceled = appointmentService.cancelAppointment(appointmentId,patientId);
            if (canceled) {
                return ResponseEntity.ok(Map.of("message", "Appointment canceled successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Appointment could not be canceled"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to cancel appointment"));
        }
    }
}