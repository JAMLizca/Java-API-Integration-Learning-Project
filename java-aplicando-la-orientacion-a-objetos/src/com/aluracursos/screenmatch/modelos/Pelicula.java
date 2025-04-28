package com.aluracursos.screenmatch.modelos;

import com.aluracursos.screenmacth.calculos.Clasificable;

public class Pelicula extends Titulo implements Clasificable {
    private String director;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public int getClasificacion() {
        return (int) (calculaMedia() / 2);
    }
}
