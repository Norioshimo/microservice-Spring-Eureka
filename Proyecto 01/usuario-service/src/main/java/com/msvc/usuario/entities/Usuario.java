package com.msvc.usuario.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    @Column(name = "id")
    private String usuarioId;

    @Column(name = "nombre",length = 20)
    private String nombre;
    @Column(name = "email")
    private String email;
    @Column(name = "informacion")
    private String informacion;

    @Transient
    private List<Calificacion> calificacionList = new ArrayList<>();


}
