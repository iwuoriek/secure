package com.example.secure.controller;

import com.example.secure.dto.FundsTransfer;
import com.example.secure.dto.PublicUser;
import com.example.secure.dto.UserCredentials;
import com.example.secure.dto.UserDto;
import com.example.secure.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
public class AppController {
    @Autowired
    private AppService service;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity registerUser(@RequestBody UserDto userDto) {
        String message = service.registerUser(userDto).get();
        return new ResponseEntity(message, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCredentials credentials) {
        String userToken = service.userLogin(credentials).get();
        return new ResponseEntity<>(userToken, HttpStatus.OK);
    }

    @GetMapping("/customer/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PublicUser>> getAllCustomers() {
        List<PublicUser> customerList = service.findAllCustomers();
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @PutMapping("/deposit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity makeADeposit(@RequestBody FundsTransfer funds) {
        service.makeDeposit(funds);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/transfer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity makeATransfer(@RequestBody FundsTransfer funds) {
        service.makeTransfer(funds);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity updateCustomer(@RequestBody UserDto customer) {
        new HttpHeaders().setBearerAuth("");
        service.updateCustomer(customer);
        return new ResponseEntity(HttpStatus.OK);
    }
}
