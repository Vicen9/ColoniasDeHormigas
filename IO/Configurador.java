package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configurador {

    ArrayList<String> archivos;
    String rutaLog;
    ArrayList<Integer> poblacion;
    ArrayList<Long> semillas;
    ArrayList<Integer> condicionParada;
    ArrayList<Integer> alpha;
    ArrayList<Integer> beta;
    Double reglaTransicion;
    Double actuaFeromona;
    Double actuaLocalFeromona;

    public Configurador(String ruta) {
        archivos = new ArrayList<>();
        rutaLog = new String();
        poblacion = new ArrayList<>();
        semillas = new ArrayList<>();
        condicionParada = new ArrayList<>();
        alpha = new ArrayList<>();
        beta = new ArrayList<>();

        String linea;
        FileReader f = null;

        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);

            while ( (linea = b.readLine()) != null ){
                String[] split = linea.split("=");
                switch (split[0]){
                    case "Archivos":

                        String[] v = split[1].split(" ");

                        for (int i = 0; i < v.length; i++) {
                            archivos.add(v[i]);}
                    break;

                    case "Poblacion":

                        String[] s = split[1].split(" ");

                        for (int i = 0; i < s.length; i++) {
                            poblacion.add(Integer.parseInt(s[i]));
                        }
                    break;
                    case "Semillas":

                        String[] se = split[1].split(" ");

                        for (int i = 0; i < se.length; i++) {
                            semillas.add(Long.parseLong(se[i]));
                        }
                        break;


                    case "RutaLogs":

                            try{
                                rutaLog = split[1];
                            }catch (ArrayIndexOutOfBoundsException ex){
                                rutaLog = "."+ File.separator;
                            }
                        break;

                    case "CondicionParada":

                        String[] parada = split[1].split(" ");

                        for (int i = 0; i < parada.length; i++) {
                            condicionParada.add(Integer.parseInt(parada[i]));
                        }

                        break;

                    case "Alpha":

                        String[] paradaAlpha = split[1].split(" ");

                        for (int i = 0; i < paradaAlpha.length; i++) {
                            alpha.add(Integer.parseInt(paradaAlpha[i]));
                        }

                        break;

                    case "Beta":
                        String[] paradaBeta = split[1].split(" ");

                        for (int i = 0; i < paradaBeta.length; i++) {
                            beta.add(Integer.parseInt(paradaBeta[i]));
                        }
                        break;
                    case "ReglaTransicion":
                        reglaTransicion = Double.parseDouble(split[1]);
                        break;
                    case "ActuaFeromona":
                        actuaFeromona = Double.parseDouble(split[1]);
                        break;
                    case "ActuaLocalFeromona":
                        actuaLocalFeromona = Double.parseDouble(split[1]);
                        break;


                }

            }


        }catch (IOException ex){
            System.err.println("Error a leer el archivo configurador: "+ex);
        }


    }


    public ArrayList<String> getArchivos() {
        return archivos;
    }

    public String getRutaLog() {
        return rutaLog;
    }

    public ArrayList<Integer> getPoblacion() {
        return poblacion;
    }

    public ArrayList<Long> getSemillas() {
        return semillas;
    }

    public ArrayList<Integer> getCondicionParada() {
        return condicionParada;
    }

    public ArrayList<Integer> getAlpha() {
        return alpha;
    }

    public ArrayList<Integer> getBeta() {
        return beta;
    }

    public Double getReglaTransicion() {
        return reglaTransicion;
    }

    public Double getActuaFeromona() {
        return actuaFeromona;
    }

    public Double getActuaLocalFeromona() {
        return actuaLocalFeromona;
    }
}

