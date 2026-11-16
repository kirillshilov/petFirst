package org.example.petFirst.controller;

import lombok.RequiredArgsConstructor;
import org.example.petFirst.model.LoginDTO;
import org.example.petFirst.model.RefreshTokenDTO;
import org.example.petFirst.model.Token;
import org.example.petFirst.model.Tokens;
import org.example.petFirst.service.interfaces.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping ("login")
    public Tokens login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }
    @PostMapping("refresh")
    public Token refresh (@RequestBody RefreshTokenDTO refreshToken){
        return authService.refresh(refreshToken);
    }
}
