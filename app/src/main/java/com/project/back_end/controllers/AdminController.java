package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.SharedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")  
public class AdminController {

    private final SharedService service;

    public AdminController(SharedService service) {
        this.service = service;
    }

    /**
     * Admin login endpoint
     * POST /{api.path}admin/login
     * 
     * @param admin Admin object containing username and password from request body
     * @return ResponseEntity<Map<String, Object>> with login status or JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody Admin admin) {
        try {
            return service.validateAdmin(admin.getUsername(), admin.getPassword());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred during login.");
        }
    }
}