package dev.ritesh.Banking_v2.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.ritesh.Banking_v2.entities.AuthRequest;
import dev.ritesh.Banking_v2.entities.UserInfo;
import dev.ritesh.Banking_v2.services.JwtService;
import dev.ritesh.Banking_v2.services.UserService;

@RestController
public class UserController {
    
    @Autowired
    private UserService accountService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String createUser(@RequestBody UserInfo account) {
        return accountService.addUser(account);
    }

    @GetMapping("/user/profile")
    public Optional<UserInfo> getUser(Principal principal) {
        String queryUsername = principal.getName();
        return accountService.getUser(queryUsername);
    }

    @PostMapping("/user/deposit")
    public String deposit(Principal principal, @RequestParam double amount) {
        String username = principal.getName();
        return accountService.deposit(username, amount);
    }

    @PostMapping("/user/withdraw")
    public String withdraw(Principal principal, @RequestParam double amount) {
        String username = principal.getName();
        return accountService.withdraw(username, amount);
    }

    @PostMapping("/user/transfer")
    public String transfer(Principal principal, @RequestParam String toAccountname, @RequestParam double amount) {
        String fromAccountname = principal.getName();
        return accountService.transfer(fromAccountname, toAccountname, amount);
    }

    @GetMapping("/user/delete")
    public String deelteUser(Principal principal) {
        String username = principal.getName();
        return accountService.deleteUser(username);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }


}
