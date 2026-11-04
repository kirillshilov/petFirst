package org.example.petFirst.service.interfaces;

import org.example.petFirst.model.LoginDTO;
import org.example.petFirst.model.RefreshTokenDTO;
import org.example.petFirst.model.Token;
import org.example.petFirst.model.Tokens;
import org.springframework.stereotype.Service;


public interface AuthService {
    Tokens login(LoginDTO loginDTO);
    Token refresh(RefreshTokenDTO refreshToken);
}
