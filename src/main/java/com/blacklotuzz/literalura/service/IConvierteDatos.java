package com.blacklotuzz.literalura.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

}
