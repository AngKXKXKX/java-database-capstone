package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.project.back_end.services.SharedService; 

@Controller
public class DashboardController {

    // 2. Autowire the shared service that handles token validation
    @Autowired
    private SharedService sharedService;

    // 3. Admin dashboard route
    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable("token") String token) {
        ModelAndView mv = new ModelAndView();
        boolean valid = sharedService.validateToken(token, "admin"); // Replace with actual method

        if (valid) {
            // Token is valid → forward to admin dashboard view
            mv.setViewName("admin/adminDashboard");
        } else {
            // Invalid token → redirect to home/login
            mv.setViewName("redirect:/");
        }
        return mv;
    }

    // 4. Doctor dashboard route
    @GetMapping("/doctorDashboard/{token}")
    public ModelAndView doctorDashboard(@PathVariable("token") String token) {
        ModelAndView mv = new ModelAndView();
        boolean valid = sharedService.validateToken(token, "doctor"); // Replace with actual method

        if (valid) {
            // Token is valid → forward to doctor dashboard view
            mv.setViewName("doctor/doctorDashboard");
        } else {
            // Invalid token → redirect to home/login
            mv.setViewName("redirect:/");
        }
        return mv;
    }
}