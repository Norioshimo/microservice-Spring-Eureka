package com.msvc.jwt.controller;

import com.msvc.jwt.dto.AuthUserDto;
import com.msvc.jwt.dto.NewUserDto;
import com.msvc.jwt.dto.RequestDto;
import com.msvc.jwt.dto.TokenDto;
import com.msvc.jwt.entity.AuthUser;
import com.msvc.jwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto>login(@RequestBody AuthUserDto authUserDto){
        System.out.println("Usuario: "+authUserDto);
        TokenDto tokenDto = authService.login(authUserDto);

        if(tokenDto==null){
            System.out.println("logueo invalido");
            return ResponseEntity.badRequest().build();
        }

        System.out.println("logueado con exito");

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto>validate(@RequestParam String token, @RequestBody RequestDto requestDto){
        TokenDto tokenDto = authService.validate(token,requestDto);
        if(tokenDto==null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthUser>create(@RequestBody NewUserDto newUserDto){
        AuthUser authUser = authService.save(newUserDto);
        if(authUser==null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(authUser);
    }


}
