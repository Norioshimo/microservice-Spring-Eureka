package com.msvc.jwt.service;

import com.msvc.jwt.dto.AuthUserDto;
import com.msvc.jwt.dto.NewUserDto;
import com.msvc.jwt.dto.RequestDto;
import com.msvc.jwt.dto.TokenDto;
import com.msvc.jwt.entity.AuthUser;
import com.msvc.jwt.repository.AuthUserRepository;
import com.msvc.jwt.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public AuthUser save(NewUserDto dto) {
        Optional<AuthUser> usu = authUserRepository.findByUserName(dto.getUserName());

        if (usu.isPresent()) {
            return null;
        }

        String password = passwordEncoder.encode(dto.getPassword());
        AuthUser authUser = AuthUser.builder()
                .userName(dto.getUserName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();

        return authUserRepository.save(authUser);
    }

    public TokenDto login(AuthUserDto dto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());

        /*System.out.println("Imprimir usuarios");
        authUserRepository.findAll().forEach(item->{
            System.out.println(item);
        });*/


        if (!user.isPresent()) {
            System.out.println("Usuario no encontrado");
            return null;
        }
        if (passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
            System.out.println("Usuario y clave Valido");
            return new TokenDto(jwtProvider.createToken(user.get()));
        }

        System.out.println("Clave del usuario invalido.");
        return null;
    }

    public TokenDto validate(String token, RequestDto requestDto) {
        if (!jwtProvider.validate(token,requestDto)) {
            return null;
        }

        String userName = jwtProvider.getUserNameFronToken(token);
        if (!authUserRepository.findByUserName(userName).isPresent()) {
            return null;
        }
        return new TokenDto(token);
    }

}
