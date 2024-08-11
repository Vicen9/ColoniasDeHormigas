package clases;

import IO.Configurador;
import IO.LectorDatos;
import clasesAuxiliares.MetodosAuxiliares;
import com.sun.jdi.IntegerType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Hormiga {

    private Integer camino[];

    private Boolean visitados[];
    private Double fitness;

    public Hormiga(Random rand, LectorDatos datos) {
        this.camino = new  Integer[datos.getnCiudades()];
        Integer ciudadAleatorio = rand.nextInt(0, datos.getnCiudades());
        this.camino[0] = ciudadAleatorio;
        this.visitados = new Boolean[datos.getnCiudades()];
        for (int i=0;i<visitados.length;i++){
            this.visitados[i] = Boolean.FALSE;
        }
        this.visitados[ciudadAleatorio] = Boolean.TRUE;
    }

    public Hormiga(Double _fitness){
        this.fitness = _fitness;
    }

    public void calculaFitness(LectorDatos datos){
        this.fitness = MetodosAuxiliares.calculaFitness(camino,datos.getDistancias());
    }


    public Integer[] getCamino() {
        return camino;
    }


    public Double getFitness() {
        return fitness;
    }

    public Boolean[] getVisitados() {
        return visitados;
    }
}
