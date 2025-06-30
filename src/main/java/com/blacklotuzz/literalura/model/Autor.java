package com.blacklotuzz.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // un autor = muchos libros
    private List<Libro> libros= new ArrayList<>();

    public Autor(){}

    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }

    @Override
    public String toString() {

        String librosStr = (libros == null || libros.isEmpty())
                ? "No tiene libros registrados"
                :"[" + libros.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(", ")) + "]";

        return "Autor: " + (nombre == null ? "Desconocido" : nombre) +
                "\nFecha de Nacimiento: " + (fechaNacimiento  == null ? "-" : fechaNacimiento) +
                "\nFecha de Fallecimiento: " + (fechaFallecimiento == null ? "-" : fechaFallecimiento) +
                "\nLibros: " + librosStr +
                "\n";
    }
}
