package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public List<LocalTime> getDoctorAvailability(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) return new ArrayList<>();

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        // Convert stored String times to LocalTime
        List<LocalTime> availableTimes = doctor.getAvailableTimes().stream()
                .map(LocalTime::parse)
                .collect(Collectors.toList());

        for (Appointment a : appointments) {
            availableTimes.remove(a.getAppointmentTime().toLocalTime());
        }

        return availableTimes;
    }

    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1; // conflict
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        if (doctorRepository.findById(doctor.getId()).isEmpty()) {
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        System.out.println("DEBUG: doctor list from DB = " + doctors);
        return doctors;
    }

    public int deleteDoctor(Long doctorId) {
        try {
            if (doctorRepository.findById(doctorId).isEmpty()) return -1;
            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Map<String, Object> validateDoctor(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(email);
        String role="doctor";
    
        if (doctor == null) {
            response.put("error", "Doctor not found");
            return response;
        }
    
        if (!doctor.getPassword().equals(password)) {
            response.put("error", "Invalid password");
            return response;
        }
    
        // Successful login, generate token
        String token = tokenService.generateToken(email, role != null ? role : "doctor");
        response.put("token", token);
        response.put("message", "Login successful");
        return response;
    }

    @Transactional(readOnly = true)
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike("%" + name + "%");
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByNameSpecialtyAndTime(String name, String specialty, String timePeriod) {
        List<Doctor> filtered = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterDoctorByTime(filtered, timePeriod);
    }

    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String timePeriod) {
        return doctors.stream().filter(d -> d.getAvailableTimes().stream().anyMatch(t -> {
            if ("AM".equalsIgnoreCase(timePeriod)) return  LocalTime.parse(t).getHour() < 12;
            else if ("PM".equalsIgnoreCase(timePeriod)) return  LocalTime.parse(t).getHour() >= 12;
            return true;
        })).collect(Collectors.toList());
    }

    public List<Doctor> filterDoctorByNameAndTime(String name, String timePeriod) {
        List<Doctor> filtered = doctorRepository.findByNameLike("%" + name + "%");
        return filterDoctorByTime(filtered, timePeriod);
    }

    public List<Doctor> filterDoctorByNameAndSpecialty(String name, String specialty) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    public List<Doctor> filterDoctorByTimeAndSpecialty(String specialty, String timePeriod) {
        List<Doctor> filtered = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterDoctorByTime(filtered, timePeriod);
    }

    public List<Doctor> filterDoctorBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    public List<Doctor> filterDoctorsByTime(String timePeriod) {
        List<Doctor> allDoctors = doctorRepository.findAll();
        return filterDoctorByTime(allDoctors, timePeriod);
    }
}