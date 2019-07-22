package markowitz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Files {

    String Line[];

    public Files() {
        java.io.File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String userInput = "";
        String[] arrSplit;
        Line = new String[10];

        try {

            archivo = new java.io.File("precios.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                Line[i++] = line;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    void WriteFile(ArrayList<Fitness> Elite) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("salida.txt");
            pw = new PrintWriter(fichero);
            pw.println("Rendimientos");
            for (int i = 0; i < Elite.size(); i++) {
                int k = (int) Math.round(Elite.get(i).Rendimiento * 100);
                pw.println("" + k);
            }
            pw.println("Covarianza");
            for (int i = 0; i < Elite.size(); i++) {
                int k = (int) Math.round(Elite.get(i).Covarianza * 100);
                pw.println("" + k);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
