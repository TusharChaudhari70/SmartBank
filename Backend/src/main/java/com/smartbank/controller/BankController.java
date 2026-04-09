package com.smartbank.controller;

import com.smartbank.entity.Transaction;
import com.smartbank.service.BankingService;
import com.smartbank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private BankingService bankingService;
    
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/welcome")
    public String welcome() {
        return "<h1>Welcome to Smart Bank Portal!</h1><p>Backend is working perfectly.</p>";
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestParam String from, 
            @RequestParam String to, 
            @RequestParam BigDecimal amount) {
        
        String result = bankingService.transferMoney(from.trim(), to.trim(), amount);
        
        if (result.startsWith("Error")) {
            return ResponseEntity.status(400).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history/{accountNumber}")
    public List<Transaction> getHistory(@PathVariable String accountNumber) {
        return bankingService.getTransactionHistory(accountNumber.trim());
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<Object> getBalance(@PathVariable String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber.trim())
                .map(account -> ResponseEntity.ok((Object) account.getBalance()))
                .orElse(ResponseEntity.status(404).body("Account " + accountNumber + " not found in DB"));
    }
}