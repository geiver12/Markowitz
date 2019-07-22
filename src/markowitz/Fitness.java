package markowitz;

public class Fitness {

    double Rendimiento, Covarianza, MaxCoef;
    double Porcentajes[];

    public Fitness() {
        Porcentajes = new double[10];
        InitComponents();

    }

    public void InitComponents() {
        Rendimiento = 0;
        Covarianza = 0;
        MaxCoef = 0;
    }

    public boolean Igualdad(Fitness obj) {
        if (obj.MaxCoef != MaxCoef || obj.Covarianza != Covarianza || Rendimiento != obj.Rendimiento) {
            return true;
        }
        for (int i = 0; i < 10; i++) {
            int a = (int) Math.round(obj.Porcentajes[i] * 1000000000);
            int b = (int) Math.round(Porcentajes[i] * 1000000000);
            if (a != b) {
                return true;
            }
        }
        return false;
    }

    public void PrintInfo() {
        System.out.println("");
        System.out.print("MaxCoef:");
        System.out.printf("%.2f", MaxCoef, "        ");
        System.out.print("Rend::");
        System.out.printf("%.2f", Rendimiento, "        ");
        System.out.print("      Covarianza::");
        System.out.printf("%.2f", Covarianza, "         ");
        System.out.print("Porcentajes::");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%.2f", Porcentajes[i], " ");
            System.out.print("      ");
        }
    }

}
