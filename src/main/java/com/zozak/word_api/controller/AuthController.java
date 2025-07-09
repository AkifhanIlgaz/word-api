package com.zozak.word_api.controller;

import com.zozak.word_api.dto.AuthRequest;
import com.zozak.word_api.dto.AuthResponse;
import com.zozak.word_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public RootEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        AuthResponse resp = authenticationService.register(request);

        return RootEntity.ok(resp);
    }

    @PostMapping("/login")
    public RootEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse resp = authenticationService.login(request);

        return RootEntity.ok(resp);
    }

}
