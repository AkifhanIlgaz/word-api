package com.zozak.word_api.service;

import com.zozak.word_api.dto.AuthRequest;
import com.zozak.word_api.dto.AuthResponse;
import com.zozak.word_api.dto.RefreshTokenRequest;
import com.zozak.word_api.entity.RefreshToken;
import com.zozak.word_api.entity.User;
import com.zozak.word_api.exception.BaseException;
import com.zozak.word_api.exception.ErrorMessage;
import com.zozak.word_api.exception.MessageType;
import com.zozak.word_api.repository.RefreshTokenRepository;
import com.zozak.word_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

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
        RefreshToken newRefreshToken = createRefreshToken(user, null);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    public AuthResponse login(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());

        try {
            authenticationProvider.authenticate(auth);
            User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
            String accessToken = jwtService.generateToken(user, TokenType.ACCESS);
            RefreshToken newRefreshToken = createRefreshToken(user, null);
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken.getToken())
                    .build();
        } catch (Exception e) {
            System.out.println("Kullancı adı veya sifre hatalı");
        }

        return null;
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken());
        if (refreshToken.isEmpty()) {
            throw new BaseException(new ErrorMessage(request.getRefreshToken(),MessageType.REFRESH_TOKEN_NOT_FOUND));
        }

        if (jwtService.isExpired(request.getRefreshToken())) {
            throw new BaseException(new ErrorMessage(request.getRefreshToken(),MessageType.EXPIRED_TOKEN));
        }

        User user = refreshToken.get().getUser();

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS);
        RefreshToken newRefreshToken = createRefreshToken(user, request.getRefreshToken());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    private RefreshToken createRefreshToken(User user, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreatedAt(new Date());
        refreshToken.setExpiresAt(new Date(System.currentTimeMillis() + jwtService.refreshTokenExpiration));
        refreshToken.setToken(jwtService.generateToken(user, TokenType.REFRESH));
        refreshToken.setUser(user);

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
}
