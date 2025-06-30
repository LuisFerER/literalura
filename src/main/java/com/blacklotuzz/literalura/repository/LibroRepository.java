package com.blacklotuzz.literalura.repository;

import com.blacklotuzz.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Muchos libros suelen tener diferentes ediciones, por lo que sus títulos se asemejan bastante;
    // para guardar la variedad pero sin repeticiones, se agrega el "Equals".
    Optional<Libro> findByTituloEqualsIgnoreCase(String nombre);

    @Query("SELECT l FROM Libro l JOIN l.idiomas i WHERE i = :idioma")
    List<Libro> librosPorIdioma(String idioma);

}
