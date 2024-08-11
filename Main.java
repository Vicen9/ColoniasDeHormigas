import IO.Configurador;
import IO.LectorDatos;
import clases.SistemaHormigas;
import clasesAuxiliares.GestionaLog;
import clasesAuxiliares.Tiempos;

import java.io.File;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Configurador config = new Configurador(args[0]);
        LectorDatos lectorDatos = new LectorDatos(config.getArchivos().get(0));
        Tiempos tiempos = new Tiempos();
        //FOR SEMILLA
        for (int i = 0; i < config.getSemillas().size(); i++) {
            //FOR Valores Transición
            for (int j = 0; j < config.getAlpha().size(); j++) {
                Random rand = new Random(config.getSemillas().get(i));
                GestionaLog log = new GestionaLog(config);

                //Llamar a hormigas
                tiempos.comienza();
                SistemaHormigas.sistemaHormigas(config, rand, config.getAlpha().get(j), config.getBeta().get(j), lectorDatos, tiempos, log);
                tiempos.acaba();
                log.registraLog("Tiempo: "+tiempos.getTotal()+"\n");
                log.escribeFichero(config.getRutaLog()+ File.separator+ config.getSemillas().get(i)+"_"+config.getArchivos().get(0)+"_Alpha"+config.getAlpha().get(j)+"_Beta"+config.getBeta().get(j)+".txt");
            }
        }

    }

}
