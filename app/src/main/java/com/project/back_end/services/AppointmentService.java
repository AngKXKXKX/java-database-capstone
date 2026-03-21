package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final TokenService tokenService; 
    private final SharedService service; 

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository,
                              TokenService tokenService,
                              SharedService service) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
        this.service = service;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1; // success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // failure
        }
    }

    @Transactional
    public String updateAppointment(Long appointmentId, Appointment updatedAppointment, Long patientId) {
        Appointment existing = appointmentRepository.findById(appointmentId).orElse(null);
        if (existing == null) return "Appointment not found";
        if (!existing.getPatientId().equals(patientId)) return "Unauthorized update";

        boolean doctorAvailable = true; 
        if (!doctorAvailable) return "Doctor not available at requested time";

        existing.setAppointmentTime(updatedAppointment.getAppointmentTime());
        existing.setStatus(updatedAppointment.getStatus());
        appointmentRepository.save(existing);
        return "Appointment updated successfully";
    }

    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {
        Appointment existing = appointmentRepository.findById(appointmentId).orElse(null);
        if (existing == null) return "Appointment not found";
        if (!existing.getPatientId().equals(patientId)) return "Unauthorized cancellation";

        appointmentRepository.delete(existing);
        return "Appointment canceled successfully";
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointments(Long doctorId, LocalDateTime start, LocalDateTime end, String patientName) {
        if (patientName == null || patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        } else {
            return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, patientName, start, end
            );
        }
    }

    @Transactional
    public void changeStatus(Long appointmentId, int status) {
        appointmentRepository.updateStatus(status, appointmentId);
    }
}