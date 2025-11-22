package com.msvc.jwt.security;

import com.msvc.jwt.dto.RequestDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@ConfigurationProperties(prefix = "admin-paths")
public class RouteValidator {
    private List<RequestDto> paths;


    public List<RequestDto> getPaths() {
        return paths;
    }

    public void setPaths(List<RequestDto> paths) {
        this.paths = paths;
    }

    public boolean isAdmin(RequestDto requestDto) {
        System.out.println("Validar path");
        boolean permisoPath = paths.stream().anyMatch(p -> {
            System.out.println(p.getUri() + "=" + requestDto.getUri() + " | " + p.getMethod() + "=" + requestDto.getMethod());
            return Pattern.matches(p.getUri(), requestDto.getUri()) && p.getMethod().equals(requestDto.getMethod());
        });
        System.out.println("Tiene permiso path");
        return permisoPath;
    }
}
