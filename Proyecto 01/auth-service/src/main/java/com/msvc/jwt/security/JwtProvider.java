package com.msvc.jwt.security;

import com.msvc.jwt.dto.RequestDto;
import com.msvc.jwt.entity.AuthUser;
import com.netflix.discovery.converters.Auto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    //@Value("${jwt.secret}")
    //private String secret;

    private SecretKey secretKey;

    @Autowired
    private RouteValidator routeValidator;

    @PostConstruct
    protected void init() {
        // Convertir la clave base64 en SecretKey de al menos 256 bits
        //secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // Si quieres, puedes usar Keys.secretKeyFor(SignatureAlgorithm.HS256) para generar automáticamente una clave segura
    }

    public String createToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", authUser.getId());
        claims.put("role",authUser.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authUser.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token, RequestDto requestDto) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

        } catch (Exception e) {
            return false;
        }

        System.out.println("requestDto: "+requestDto);
        if(!isAdmin(token) && routeValidator.isAdmin(requestDto)){
            return  false;
        }else{
            return true;
        }
    }

    public String getUserNameFronToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return "Bad Token";
        }
    }

    private boolean isAdmin(String token){
        System.out.println("Validar si es el admin");
        boolean es=Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .get("role").equals("admin");
        System.out.println("Es admin? "+es);
        return es;
    }
}
