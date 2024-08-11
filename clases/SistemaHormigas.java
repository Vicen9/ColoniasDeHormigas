package clases;

import IO.Configurador;
import IO.LectorDatos;
import clasesAuxiliares.GestionaLog;
import clasesAuxiliares.MetodosAuxiliares;
import clasesAuxiliares.Tiempos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SistemaHormigas {

    public static void sistemaHormigas(Configurador config, Random rand, Integer alpha, Integer beta,LectorDatos datos, Tiempos tiempos, GestionaLog log) {
        tiempos.acaba();
        Hormiga[] colonia = new Hormiga[config.getPoblacion().get(0)];
        Double fInicial=0.0;
        Double mFeromonas[][] = MetodosAuxiliares.inicializaMatrizFeromona(config,datos,rand,fInicial);
        Double mHeuristica[][] = MetodosAuxiliares.matrizHeuristica(datos);
        Hormiga theBestOfTheWord = new Hormiga(Double.MAX_VALUE);
        Hormiga theBestOfTheMoment = null;

        for (int i = 0; i < config.getCondicionParada().get(0) && tiempos.getTotal() <=config.getCondicionParada().get(1); i++) {
            tiempos.acaba();


            //Creamos las hormigas con la primera posicion aleatoria
            inicializaColonia(colonia,config,rand,datos);
            //Construir camino de las hormigas
            construccionDeCamino(colonia,config,datos,rand,alpha,beta,mFeromonas,mHeuristica,fInicial);
            //Calcular la mejor hormiga
            theBestOfTheMoment = theBestAnt(colonia,datos);

            //Si mejoramos la mejor del mundo, se sustituye, le quita el titulo y la mata, las hormigas son rocky
            if (theBestOfTheMoment.getFitness() < theBestOfTheWord.getFitness()){
                theBestOfTheWord = theBestOfTheMoment;
                log.registraLog("\n");
                log.registraLog("Actualización del mejor en Iteración: "+i+" con fitness: "+theBestOfTheWord.getFitness()+"\n");
                log.registraLog("\n");
            }

            //Demonio(Diablo chiquito)
            demonio(theBestOfTheMoment,mFeromonas,config);


            if (i%1000 == 0){
                log.registraLog("Iteración: "+i+" Fitness: "+theBestOfTheMoment.getFitness()+"\n");
            }
        }

        log.registraLog("Mejor Encontrado: "+theBestOfTheWord.getFitness()+"\n");

    }

    //Valor inicial de la colonia
    private static void inicializaColonia(Hormiga[] colonia,Configurador config, Random rand, LectorDatos datos){
        for (int j = 0; j < config.getPoblacion().get(0); j++) {
            colonia[j] = new Hormiga(rand, datos);
        }
    }

    private static void construccionDeCamino(Hormiga[] colonia, Configurador config, LectorDatos datos, Random rand, Integer alpha, Integer beta, Double[][] mFeromonas, Double[][] mHeuristica, Double fInicial){
        for (int i = 1; i < datos.getnCiudades(); i++) {
            for (int j = 0; j < config.getPoblacion().get(0); j++) {
                Integer elegido;
                if (config.getReglaTransicion() <= rand.nextFloat(0,1)){
                    //arg max
                    elegido = calculaArgumentoMax(datos,colonia[j],config,i-1,alpha,beta,mFeromonas,mHeuristica);
                } else {
                    //p'k
                    elegido = calculaPprimaK(datos,colonia[j],config,i-1,alpha,beta,mFeromonas,mHeuristica,rand);
                }
                colonia[j].getCamino()[i] = elegido;
                colonia[j].getVisitados()[elegido] = Boolean.TRUE;

                //Actualizacion local de la feromona
                actualizacionLocalFeromona(config,datos,colonia[j],mFeromonas,i-1,i,fInicial);
            }
        }
    }

    private static void actualizacionLocalFeromona(Configurador config, LectorDatos datos,Hormiga hormiga, Double[][] mFeromonas, Integer anterior, Integer actual, Double fInicial){
        mFeromonas[hormiga.getCamino()[anterior]][hormiga.getCamino()[actual]]= mFeromonas[hormiga.getCamino()[actual]][hormiga.getCamino()[anterior]] = (1-config.getActuaLocalFeromona()) * (mFeromonas[hormiga.getCamino()[anterior]][hormiga.getCamino()[actual]]) + (config.getActuaLocalFeromona()*fInicial);
    }

    private static Double[] calculaTauEta(LectorDatos datos,Hormiga hormiga, Configurador config, Integer anterior, Integer alpha, Integer beta, Double[][] mFeromonas, Double[][] mHeuristica){
        Double argCandidatos[] = new Double[datos.getnCiudades()];
        for (int i = 0; i < datos.getnCiudades(); i++) {
            if (!hormiga.getVisitados()[i]){
                argCandidatos[i] = (Math.pow(mFeromonas[hormiga.getCamino()[anterior]][i],alpha)*Math.pow(mHeuristica[hormiga.getCamino()[anterior]][i],beta));
            }
        }
        return argCandidatos;
    }

    private static Integer calculaArgumentoMax(LectorDatos datos,Hormiga hormiga, Configurador config, Integer anterior, Integer alpha, Integer beta, Double[][] mFeromonas, Double[][] mHeuristica){
        Double argCandidatos[] = calculaTauEta(datos,hormiga,config,anterior,alpha,beta,mFeromonas,mHeuristica);
        Integer pos = 151924;
        Double max = Double.MIN_VALUE;
        for (int i = 0; i < argCandidatos.length; i++) {
            if ((!hormiga.getVisitados()[i]) && (argCandidatos[i] > max)){
                max = argCandidatos[i];
                pos=i;
            }
        }

        return pos;
    }

    private static Integer calculaPprimaK(LectorDatos datos,Hormiga hormiga, Configurador config, Integer anterior, Integer alpha, Integer beta, Double[][] mFeromonas, Double[][] mHeuristica, Random rand){
        Double argCandidatos[] = calculaTauEta(datos,hormiga,config,anterior,alpha,beta,mFeromonas,mHeuristica);
        Double quesitos[] = new Double[argCandidatos.length];
        Double total = 0.0;
        for (int i = 0; i < argCandidatos.length; i++) {
            if (!hormiga.getVisitados()[i]){
                total += argCandidatos[i];
            }
        }

        for (int i = 0; i < argCandidatos.length; i++) {
            if (!hormiga.getVisitados()[i]){
                quesitos[i] = argCandidatos[i]/total;
                if(total==0){
                    quesitos[i] = 1.0;
                }
            }
        }

        //Tiramos la ruleta
        Double uniforme = rand.nextDouble(0,1);
        Double contador = 0.0;
        for (int i = 0; i < argCandidatos.length; i++) {
            if (!hormiga.getVisitados()[i]){
                contador += quesitos[i];
                if (uniforme <= contador){
                    return  i;
                }
            }
        }
        return -1;
    }


    private static Hormiga theBestAnt(Hormiga[] colonia,LectorDatos datos){
        Hormiga theBest = null;
        Double theBestFitness = Double.MAX_VALUE;

        for (int i = 0; i < colonia.length; i++) {
            colonia[i].calculaFitness(datos);
            if (colonia[i].getFitness() < theBestFitness){
                theBestFitness = colonia[i].getFitness();
                theBest = colonia[i];
            }
        }

        return theBest;
    }

    private static void demonio(Hormiga hormigaBest, Double[][] mFeromonas, Configurador config){
        double delta = 1/ hormigaBest.getFitness();

        //Sumamos el arco del mejor
        for (int i = 0; i < hormigaBest.getCamino().length-1; i++) {
            mFeromonas[hormigaBest.getCamino()[i]][hormigaBest.getCamino()[i+1]] += (config.getActuaFeromona()*delta);
            mFeromonas[hormigaBest.getCamino()[i+1]][hormigaBest.getCamino()[i]] = mFeromonas[hormigaBest.getCamino()[i]][hormigaBest.getCamino()[i+1]];
        }
        //Evaporación
        for (int i = 0; i <mFeromonas.length ; i++) {
            for (int j = 0; j < mFeromonas.length; j++) {
                if (i != j){
                    mFeromonas[i][j]=((1-config.getActuaFeromona())*mFeromonas[i][j]);
                }
            }
        }

    }


}
