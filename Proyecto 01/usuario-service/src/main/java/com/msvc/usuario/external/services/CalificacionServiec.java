package com.msvc.usuario.external.services;

import com.msvc.usuario.entities.Calificacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Se debe implementar los servicios....
@FeignClient(name = "calificacion-service")
public interface CalificacionServiec {

    @PostMapping
    public ResponseEntity<Calificacion>guardarCalificacion(Calificacion calificacion);

    @PutMapping("/calificaciones/{calificacionId}")
    public ResponseEntity<Calificacion>actualizarCalificacion(@PathVariable String calificacionId,Calificacion calificacion);

    @DeleteMapping("/calificaciones/{calificacionId}")
    public void eliminarCalificacion(@PathVariable String calificacionId);

}
