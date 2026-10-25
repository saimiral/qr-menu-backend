package com.saimiral.qr_menu_backend.service;

import com.saimiral.qr_menu_backend.dto.LoginRequestDTO;
import com.saimiral.qr_menu_backend.dto.LoginResponseDTO;
import com.saimiral.qr_menu_backend.entity.StoreUser;
import com.saimiral.qr_menu_backend.exception.BadRequestException;
import com.saimiral.qr_menu_backend.repository.StoreUserRepository;
import com.saimiral.qr_menu_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StoreUserRepository storeUserRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        StoreUser user = storeUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponseDTO(
                token,
                user.getRole().name(),
                user.getStore().getSlug()
        );
    }
}