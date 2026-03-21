package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.SharedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")  // Base path configurable via application properties
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
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Admin admin) {
        try {
            Map<String, Object> response = service.validateAdmin(admin.getUsername(), admin.getPassword());
            // If validation returned an error message, set proper status
            if (response.containsKey("error")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            return ResponseEntity.ok(response); // 200 OK with token and message
        } catch (Exception e) {
            // Generic error handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred during login."));
        }
    }
}