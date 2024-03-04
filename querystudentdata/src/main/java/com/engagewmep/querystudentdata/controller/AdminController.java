package com.engagewmep.querystudentdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.engagewmep.querystudentdata.service.UserService;

@RestController
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }
}
