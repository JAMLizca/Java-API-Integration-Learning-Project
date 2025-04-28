package com.aluracursos.screenmacth.calculos;

public class FiltroRecomendacion {

    public void filtra(Clasificable clasificacion){
        if (clasificacion.getClasificacion() >= 4){
            System.out.println("Muy bien evaluado en el momento");
        } else if (clasificacion.getClasificacion() >= 2){
            System.out.println("popular en el momento");
        } else {
            System.out.println("Colocálo en tu lista para verlo después");
        }
    }
}
