package com.msvc.hotel.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/staffs")
public class StaffController {

    @GetMapping
    public ResponseEntity<List<String>>lsitarStaffs(){
        List<String>staffs = Arrays.asList("Staff 1","Staff 2","Staff 3","Staff 4");

        return new ResponseEntity<>(staffs, HttpStatus.OK);
    }
}
