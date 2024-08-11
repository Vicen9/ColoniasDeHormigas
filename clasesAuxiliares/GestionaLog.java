package clasesAuxiliares;



import IO.Configurador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GestionaLog {
    private StringBuilder log;

    private StringBuilder logCostes;
    private File carpeta;

    public GestionaLog(Configurador config) {
        log = new StringBuilder();
        carpeta = new File(config.getRutaLog());
        logCostes = new StringBuilder();
    }



    public void registraLog(String texto){
        log.append(texto);
    }

    public void escribeFichero(String ruta){
        FileWriter fichero = null;
        PrintWriter pw = null;

        try {

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);

            pw.print(log.toString());


        }catch (IOException ex){
        }
        finally {
            try{
                if (null != fichero){
                    fichero.close();
                }
            }catch (IOException e2){
            }
        }
    }

}
