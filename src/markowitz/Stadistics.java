package markowitz;

import java.util.ArrayList;

public class Stadistics {

    ArrayList<Double> listPrecio, listRendimientos;

    String name;

    public Stadistics(String line) {
        listPrecio = new ArrayList<>();
        listRendimientos = new ArrayList<>();
        String[] convert = line.split("-");
        this.name = convert[0];

        for (int i = 1; i < convert.length; i++) {
            try {
                listPrecio.add(Double.parseDouble(convert[i]));
            } catch (Exception e) {
            }

        }
        RendimientosPorEscenario();
    }

    public void PrintInfo() {
        System.out.println("");
        System.out.println("Criptomoneda::" + name);
        System.out.println("");
        System.out.println("Precios");
        System.out.println(listPrecio + "");
        System.out.println("Rendimientos de los precios ");
        System.out.println(listRendimientos + "");

    }

    public void RendimientosPorEscenario() {
        //Obtengo los rendimientos por escenarios. 
        for (int i = 0; i < listPrecio.size() - 1; i++) {
            double a = listPrecio.get(i + 1) / listPrecio.get(i) - 1;
            listRendimientos.add(a);
        }
    }

    public double Media(double PorcentajeMutacion) {//Obtengo el rendimiento esperador Erp
        double Rend_Esperado = 0;
        for (int i = 0; i < listRendimientos.size(); i++) {
            Rend_Esperado += PorcentajeMutacion * listRendimientos.get(i);
        }
        Rend_Esperado = Rend_Esperado / 60;
        return Rend_Esperado;
    }

    public double Varianza(double PorcentajeMutacion, double Rend_Esperado) {        //Obtengo la varianza
        double varianza = 0;
        for (int i = 0; i < listRendimientos.size(); i++) {
            varianza += PorcentajeMutacion * Math.pow((listRendimientos.get(i) - Rend_Esperado), 2);
        }
        varianza = varianza / 60;
        return varianza;
    }

    public double Covarianza(ArrayList<Double> Porcentaje, double PorcentajeMutacion, double Rend_Esperado1, double Erp) {
        double covarianza = 0;

        for (int i = 0; i < this.listRendimientos.size(); i++) {
            covarianza += PorcentajeMutacion * (this.listRendimientos.get(i) - Rend_Esperado1) * (Porcentaje.get(i) - Erp);
        }
        covarianza = covarianza / 60;
        return covarianza;

    }
}
