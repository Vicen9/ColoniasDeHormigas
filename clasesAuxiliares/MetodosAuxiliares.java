package clasesAuxiliares;

import IO.Configurador;
import IO.LectorDatos;

import java.util.*;


public class MetodosAuxiliares {

    public static ArrayList<Integer> greedy(LectorDatos datos, Random rand){

        PriorityQueue<Map.Entry<Integer, Double>> lista;
        Integer tam = datos.getDistancias()[0].length;
        Set<Integer> solSet = new HashSet<>(tam); // Utilizamos un HashSet para comprobar la pertenencia en O(1)
        Integer ini = rand.nextInt(0, tam);
        ArrayList<Integer> solSalida = new ArrayList<>(tam);

        for (int i = 0; i < tam; i++) {
            lista = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
            for (int j = 0; j < tam; j++) {
                if (ini != j && !solSet.contains(j)) {
                    lista.add(new AbstractMap.SimpleEntry<>(j, datos.getDistancias()[i][j]));
                }
            }
            Integer entrada = lista.peek().getKey();

            // Agregar a la solución y actualizar el conjunto de soluciones
            solSalida.add(entrada);
            solSet.add(entrada);

            // Actualizar el valor de 'ini' para la siguiente iteración
            ini = entrada;
        }

        return solSalida;

    }

    public static double calculaFitness(ArrayList<Integer> sol, double[][] mDistancias){
        double fitness = 0.0;
        for (int i = 0; i < sol.size()-1; i++) {
            fitness+= mDistancias[sol.get(i)][sol.get(i+1)];
        }
        return fitness;
    }

    public static double calculaFitness(Integer[] sol, double[][] mDistancias){
        double fitness = 0.0;
        for (int i = 0; i < sol.length-1; i++) {
            fitness+= mDistancias[sol[i]][sol[i+1]];
        }
        return fitness;
    }


    public static Double[][] inicializaMatrizFeromona(Configurador config, LectorDatos datos, Random rand, Double fInicial){
        ArrayList<Integer> vSolucion = greedy(datos,rand);
        Double fitness = calculaFitness(vSolucion, datos.getDistancias());

        Double feromonaInicial = (1/(fitness*config.getPoblacion().get(0)));
        fInicial = feromonaInicial;
        Double matrizFeromonas[][] = new Double[vSolucion.size()][vSolucion.size()];

        for(int i=0;i<matrizFeromonas.length;i++){
            for(int j=i;j< matrizFeromonas[0].length;j++){
                if(i == j){
                    matrizFeromonas[i][j] = Double.POSITIVE_INFINITY;
                }else{
                    matrizFeromonas[i][j] = matrizFeromonas[j][i] = feromonaInicial;
                }
            }
        }
        return matrizFeromonas;
    }

    public static Double[][] matrizHeuristica(LectorDatos datos){
        Double heuristica[][] = new Double[datos.getDistancias().length][datos.getDistancias().length];
        for(int i=0;i<datos.getDistancias().length;i++){
            for(int j=i;j< datos.getDistancias().length;j++){
                if(i == j){
                    heuristica[i][j] = Double.POSITIVE_INFINITY;
                }else{
                    heuristica[i][j] = heuristica[j][i] = 1/datos.getDistancias()[i][j];
                }
            }
        }
        return heuristica;
    }



}