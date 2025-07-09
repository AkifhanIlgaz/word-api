package com.zozak.word_api.service;

import com.zozak.word_api.dto.AuthRequest;
import com.zozak.word_api.dto.AuthResponse;
import com.zozak.word_api.entity.User;
import com.zozak.word_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  AuthenticationProvider authenticationProvider;

    public AuthResponse register(AuthRequest request) {
        User user = User.builder().
                email(request.getEmail()).
                passwordHash(passwordEncoder.encode(request.getPassword())).
                build();

        User savedUser= userRepository.save(user);
        String accessToken = jwtService.generateToken(savedUser, TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(savedUser, TokenType.REFRESH);
//        TODO: Store refresh token on db
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse login(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());

        try {
            authenticationProvider.authenticate(auth);
            User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
            String accessToken = jwtService.generateToken(user, TokenType.ACCESS);
            String refreshToken = jwtService.generateToken(user, TokenType.REFRESH);
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            System.out.println("Kullancı adı veya sifre hatalı");
        }

        return null;
    }
}
