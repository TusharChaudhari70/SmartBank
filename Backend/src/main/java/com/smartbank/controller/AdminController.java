package com.smartbank.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbank.entity.Account;
import com.smartbank.repository.AccountRepository;
import com.smartbank.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getBankStats() {
        long totalUsers = userRepository.count();
        
        List<Account> allAccounts = accountRepository.findAll();
        BigDecimal totalLiquidity = BigDecimal.ZERO;
        
        for (Account acc : allAccounts) {
            if (acc.getBalance() != null) {
                totalLiquidity = totalLiquidity.add(acc.getBalance());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalUsers", totalUsers);
        response.put("totalLiquidity", totalLiquidity);
        
        return ResponseEntity.ok(response);
    }
}