package markowitz;

public class Nodo {

    double Rend_Esperado, Varianza, Dis_Est, PorcentajeMutacion;
    double covarianza[];

    public Nodo(double porce) {
        PorcentajeMutacion = porce;
        this.Rend_Esperado = this.Varianza = this.Dis_Est = 0;
        covarianza = new double[10];
        for (int i = 0; i < 10; i++) {
            covarianza[i] = 0;
        }
    }

    public void PrintInfo() {
        System.out.printf("La media o rendiemiento esperado es  %.6f", Rend_Esperado);
        System.out.println("    Varianza=" + Varianza);
        System.out.printf("El riesgo de la accion en la criptomoneda es =  %.2f", Dis_Est, "%");
        System.out.println("");
        System.out.println("Sus 10 Covarianzas");
        for (int i = 0; i < 10; i++) {
            System.out.print(covarianza[i] + "   ");
        }
    }

}
