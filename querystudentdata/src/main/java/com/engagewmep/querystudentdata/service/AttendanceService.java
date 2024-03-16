package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    // Add methods for attendance-related business logic
}
