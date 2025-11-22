package com.msvc.usuario.service.impl;

import com.msvc.usuario.entities.Calificacion;
import com.msvc.usuario.entities.Hotel;
import com.msvc.usuario.entities.Usuario;
import com.msvc.usuario.exceptions.ResourceNotFoundException;
import com.msvc.usuario.external.services.HotelService;
import com.msvc.usuario.repository.UsuarioRepository;
import com.msvc.usuario.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HotelService hotelService;

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        String randomUsuarioId = UUID.randomUUID().toString();
        usuario.setUsuarioId(randomUsuarioId);

        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encotrado con el Id : " + usuarioId));

        // Consumir usando RestTemplate
        String url = "http://calificacion-service/calificaciones/usuarios/" + usuario.getUsuarioId();
        log.info("URL: " + url);
        ResponseEntity<List<Calificacion>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Calificacion>>() {}
        );

        List<Calificacion> calificacionesDelUsuario = response.getBody();

        log.info("Cantidad  de calificaciones: "+calificacionesDelUsuario.size());



        log.info("Buscar hoteles");
        //Consumir usando FeignClient
        calificacionesDelUsuario = calificacionesDelUsuario.stream().map(calificacion -> {
            /*
            String urlHotel = "http://hotel-service/hoteles/" + calificacion.getHotelId();
            log.info("URL: " + urlHotel);
            ResponseEntity<Hotel> resHotel = restTemplate.exchange(
                    urlHotel,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Hotel>() {}  // <-- aquí
            );

            Hotel hotel = resHotel.getBody();*/

            Hotel hotel = hotelService.getHotel(calificacion.getHotelId() );

            calificacion.setHotel(hotel);
            return calificacion;
        }).collect(Collectors.toList());




        log.info("{}", calificacionesDelUsuario);

        usuario.setCalificacionList(calificacionesDelUsuario);

        return usuario;
    }
}
