package com.blacklotuzz.literalura.principal;

import com.blacklotuzz.literalura.model.*;
import com.blacklotuzz.literalura.repository.AutorRepository;
import com.blacklotuzz.literalura.repository.LibroRepository;
import com.blacklotuzz.literalura.service.ConsumoAPI;
import com.blacklotuzz.literalura.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;
    private List<Autor> autores;
    private List<Libro> libros;

    public Principal(LibroRepository libroRepositorio, AutorRepository autorRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Elija la opción a través de su número:
                    1 - Buscar libro por titulo 
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma            
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresAnio();
                    break;
                case 5:
                    listarLibrosIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private Datos obtenerDatos(String nombreLibro){
        var json = consumoAPI.obtenerDatos(URL + nombreLibro.replace(" ", "%20"));
        //System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        return datos;
    }

    private List<Autor> guardarAutor(Optional<DatosLibro> libroEncontrado) {

        var autorEncontrado = new ArrayList<Autor>();

        if (libroEncontrado.get().autores() != null && !libroEncontrado.get().autores().isEmpty()){

            DatosAutor autor = libroEncontrado.get().autores().get(0);

            // Verifica si ya existe en la base de datos o hay que agregarlo.
            Optional<Autor> existeAutor = autorRepositorio.findByNombre(autor.nombre());

            if (existeAutor.isPresent()){
                autorEncontrado.add(existeAutor.get());
            }
            else{
                Autor nuevoAutor = new Autor(autor);
                autorRepositorio.save(nuevoAutor);
                autorEncontrado.add(nuevoAutor);
            }
        }
        return autorEncontrado;
    }

    private void guardarLibro(Optional<DatosLibro> libroEncontrado, List<Autor> autores) {
        if (libroEncontrado.isPresent() && autores != null && !autores.isEmpty()){

            Optional<Libro> existeLibro = libroRepositorio.findByTituloEqualsIgnoreCase(libroEncontrado.get().titulo());
            if (existeLibro.isPresent()){
                System.out.println("\nNo se puede registrar el mismo libro mas de una vez.\n");
            }
            else {
                Libro libroNuevo = new Libro(libroEncontrado.get());
                libroNuevo.setAutor(autores.get(0));
                libroRepositorio.save(libroNuevo);

                System.out.println("\nLibro y autores guardados correctamente.\n");
            }
        }
    }

    private void buscarLibroWeb() {
        System.out.println("Ingrese el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();

        Datos datos = obtenerDatos(nombreLibro);

        if (datos == null || datos.results().isEmpty()) {
            System.out.println("\nLibro no encontrado.\n");
            return;
        }

        List<DatosLibro> datosLibro = datos.results();

        Optional<DatosLibro> libroEncontrado = datosLibro.stream()
                .findFirst();

        if (libroEncontrado.isPresent()) {
            autores = guardarAutor(libroEncontrado);
            guardarLibro(libroEncontrado, autores);
        }

    }

    private void listarLibrosRegistrados() {
        libros = libroRepositorio.findAll();

        if (libros.isEmpty()){
            System.out.println("\n Libros sin registrar en la base de datos.\n");

        }
        else {
            libros.forEach(System.out::println);
        }

    }

    private void listarAutoresRegistrados() {
        autores = autorRepositorio.findAll();

        if (autores.isEmpty()){
            System.out.println("\n Autores sin registrar en la base de datos.\n");
        }
        else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresAnio() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar");
        int anio = teclado.nextInt();
        teclado.nextLine();

        autores = autorRepositorio.autoresEnDeterminadoAnio(anio);

        if (autores.isEmpty() || autores == null) {
            System.out.println("\nNo se encontraron autores.\n");
            return;
        }

        autores.forEach(System.out::println);
    }

    private void listarLibrosIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - Español
                en - Inglés
                fr - Francés
                pt - portugués
                """);
        var idioma = teclado.nextLine();

        libros = libroRepositorio.librosPorIdioma(idioma);

        if (libros.isEmpty() || libros == null){
            System.out.println("\nNo se encontraron libros con ese idioma.\n");
            return;
        }
        libros.forEach(System.out::println);
    }
}
