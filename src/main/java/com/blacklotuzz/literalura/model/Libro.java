package com.blacklotuzz.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true, columnDefinition = "TEXT") // Evita nombres repetidos + Acepta longitudes largas de caracteres -> SQL: ALTER TABLE books ALTER COLUMN title TYPE TEXT;
    private String titulo;
    @ManyToOne
    private Autor autor;
    @ElementCollection(fetch = FetchType.EAGER) // Mapear la lista de idiomas dentro de postgres, para que se puedan hacer consultas
    private List<String> idiomas;
    private Integer totalDescargas;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idiomas = datosLibro.idiomas();
        this.totalDescargas = datosLibro.totalDescargas();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Integer getTotalDescargas() {
        return totalDescargas;
    }

    public void setTotalDescargas(Integer totalDescargas) {
        this.totalDescargas = totalDescargas;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        return
                "-----LIBRO-----" +
                "\nTitulo: " + titulo +
                "\nAutor: " + autor.getNombre() +
                "\nIdioma: " + idiomas +
                "\nNumero de Descargas: " + totalDescargas +
                "\n---------------\n";
    }
}
