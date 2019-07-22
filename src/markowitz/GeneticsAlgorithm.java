package markowitz;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GeneticsAlgorithm {
    
    private int CurrentGeneration;//Generacion actual
    private int MaxGenerations; //cantidad de generaciones

    Stadistics[] EstadisticasPoblacion;//Poblacion inicial con valores de 1 para cada uno, de manera que no 
    //halla ningun valor para la combinacion, por ello la dist_est, media, varianza, covarianza son altas.
    Nodo[] Generation;
    
    ArrayList<Fitness> Elite;//Mi mejor poblacion o la elite

    Fitness[] Hijos;//mi lista de hijos usada para la mutacion.

    public GeneticsAlgorithm(int max) {
        
        MaxGenerations = max;
        CurrentGeneration = 0;
        
        Files file = new Files();//Inicia el proceso leyendo el archivo.

        EstadisticasPoblacion = new Stadistics[10];//Contiene la poblacion y los porcentajes

        Generation = new Nodo[10]; //Contiene lo que deverdad nos interesa las covarianzas,y el rendimiento esperado fijo
        Hijos = new Fitness[300]; //Con esta hago las 300 poblaciones 
        Elite = new ArrayList<>();//la elite de hijos ordenada de la mejor cartera a la peor.

        for (int i = 0; i < 10; i++) {
            EstadisticasPoblacion[i] = new Stadistics(file.Line[i]);//Leemos y generamos las estadisitcas.
        }
        
        LoadStadistics(1);//Aca leemos las estadisticas 
        PrintInfo();//imprimimos en pantalla
        RunSimulation();//aca inicia la simulacion de verdad.
    }
    
    public void RunSimulation() {
        
        GeneratePopulation();//leemos la primera generacion de individuos
        PoblacionElite();//generamos nuestra elite inicial

        while (CurrentGeneration < MaxGenerations) {
            System.out.println("");
            System.out.println("Generacion::" + CurrentGeneration);
            System.out.println("");
            GeneraCruza();
            PoblacionElite();
            CurrentGeneration++;
        }
        RadioSharpe();
        Files file = new Files();
        file.WriteFile(Elite);
    }
    
    public void GeneraCruza() {
        //Fitness Hijos[] = new Fitness[300];
        System.out.println("");
        System.out.println("genera Inicia cruza");
        for (int i = 0, j = 1; j < Hijos.length; i += 2, j += 2) {
            int a = 999, b = 999;
            while (a == b) {
                a = (int) Math.floor(Math.random() * Hijos.length);
                b = (int) Math.floor(Math.random() * Hijos.length);
            }
            
            for (int k = 0, l = 5; k < 5; k++, l++) { //aca intercambiamos las parejas
                double alfa = Hijos[a].Porcentajes[k];
                Hijos[a].Porcentajes[k] = Hijos[b].Porcentajes[k];
                Hijos[b].Porcentajes[k] = alfa;
                
            }
            //aca le damos algo de aleatoriedad o mutacion

            for (int k = 0; k < Hijos[a].Porcentajes.length; k++) {
                Hijos[a].Porcentajes[k] = Hijos[a].Porcentajes[k] / 0.05;
                Hijos[b].Porcentajes[k] = Hijos[b].Porcentajes[k] / 0.05;;
                
            }
            
            double acum = 0, acum2 = 0;
            for (int k = 0; k < Hijos[a].Porcentajes.length; k++) {
                acum += Hijos[a].Porcentajes[k];
                acum2 += Hijos[b].Porcentajes[k];
                
            }
            
            for (int k = 0; k < Hijos[a].Porcentajes.length; k++) {
                Hijos[a].Porcentajes[k] = Hijos[a].Porcentajes[k] / acum;
                Hijos[b].Porcentajes[k] = Hijos[b].Porcentajes[k] / acum2;
            }
        }
        CalculeData();
        for (int i = 0; i < Hijos.length; i++) {
            Hijos[i].PrintInfo();
        }
        
    }
    
    private void GeneratePopulation() {//Generando poblacion Random inicial.

        double[] number = new double[10];
        System.out.println("");
        System.out.println("");
        System.out.println("Carga Inicial de procentajes ");
        for (int i = 0; i < Hijos.length; i++) {
            
            double acum = 0;
            for (int j = 0; j < 10; j++) {
                double valorDado = Math.floor(Math.random() * 100);
                number[j] = valorDado;
                acum += valorDado;
            }
            for (int j = 0; j < 10; j++) {
                number[j] = number[j] / acum * 100;
                number[j] = number[j] / 100;
            }
            GenerateGeneration(number, i);
        }
        for (int i = 0; i < Hijos.length; i++) {
            
            Hijos[i].PrintInfo();
            
        }
        
    }
    
    private void GenerateGeneration(double[] number, int pos) {
        Hijos[pos] = new Fitness();
        for (int i = 0; i < 10; i++) {
            Hijos[pos].Porcentajes[i] = number[i];//cargo los porcentajes de variacion
            Hijos[pos].Rendimiento += number[i] * this.Generation[i].Rend_Esperado; //Hallo el rendimiento esperado
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i != j) {
                    Hijos[pos].Covarianza += Hijos[pos].Porcentajes[i] * Hijos[pos].Porcentajes[j] * this.Generation[i].covarianza[j];
                }
                if (i == j) {
                    Hijos[pos].Covarianza += Math.pow(Hijos[pos].Porcentajes[i], 2) * Generation[i].Varianza;
                }
                
            }
        }
        Hijos[pos].MaxCoef = Hijos[pos].Rendimiento / Hijos[pos].Covarianza;
    }
    
    public void CalculeData() {
        
        for (int pos = 0; pos < Hijos.length; pos++) {
            Hijos[pos].InitComponents();
            for (int i = 0; i < 10; i++) {
                Hijos[pos].Rendimiento += Hijos[pos].Porcentajes[i] * this.Generation[i].Rend_Esperado; //Hallo el rendimiento esperado
            }
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (i != j) {
                        Hijos[pos].Covarianza += Hijos[pos].Porcentajes[i] * Hijos[pos].Porcentajes[j] * this.Generation[i].covarianza[j];
                    }
                    if (i == j) {
                        Hijos[pos].Covarianza += Math.pow(Hijos[pos].Porcentajes[i], 2) * Generation[i].Varianza;
                    }
                }
            }
            Hijos[pos].MaxCoef = Hijos[pos].Rendimiento / Hijos[pos].Covarianza;
        }
    }
    
    private void LoadStadistics(double PM) {
        for (int i = 0; i < 10; i++) {
            Generation[i] = new Nodo(PM);
            Generation[i].Rend_Esperado = EstadisticasPoblacion[i].Media(PM);
            Generation[i].Varianza = EstadisticasPoblacion[i].Varianza(PM, Generation[i].Rend_Esperado);
            Generation[i].Dis_Est = Math.sqrt(Generation[i].Varianza);
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i != j) {
                    Generation[i].covarianza[j] = EstadisticasPoblacion[i].Covarianza(EstadisticasPoblacion[j].listRendimientos, PM, Generation[i].Rend_Esperado, Generation[j].Rend_Esperado);
                }
                if (i == j) {
                    Generation[i].covarianza[j] = Generation[i].Varianza;
                }
            }
        }
    }
    
    public void PrintInfo() {
        for (int i = 0; i < 10; i++) {
            EstadisticasPoblacion[i].PrintInfo();
            Generation[i].PrintInfo();
        }
    }
    
    public void PoblacionElite() {//Funcion que toma los individuos mas optimos.

        ArrayList<Integer> number = new ArrayList<>();

        //La poblacion que dan mayor coeficiente, es decir los mas optimos.
        if (Elite.size() != Hijos.length) {
            for (int i = 0; i < Hijos.length; i++) {
                double mayor = -9999;
                int aux = -1;
                for (int j = 0; j < Hijos.length; j++) {
                    if (mayor < Hijos[j].MaxCoef && !number.contains(j)) {
                        mayor = Hijos[j].MaxCoef;
                        aux = j;
                    }
                }
                //Primera vez que no hay poblacion elite, para la segunda vez ya con una poblacion se empieza
                //reemplazar el valor
                if (mayor > -1 && !number.contains(aux) && Elite.size() < Hijos.length) {
                    Fitness obj = new Fitness();
                    obj.MaxCoef = Hijos[aux].MaxCoef;
                    obj.Rendimiento = Hijos[aux].Rendimiento;
                    obj.Covarianza = Hijos[aux].Covarianza;
                    for (int j = 0; j < 10; j++) {
                        obj.Porcentajes[j] = Hijos[aux].Porcentajes[j];
                    }
                    Elite.add(obj);
                    number.add(aux);
                }
            }
        } else {
            
            for (int i = 0; i < Hijos.length; i++) {
                for (int j = 0; j < Elite.size(); j++) {
                    if (Hijos[i].MaxCoef > Elite.get(j).MaxCoef && Elite.get(j).Igualdad(Hijos[i])) {
                        
                        Fitness obj = new Fitness();
                        obj.MaxCoef = Hijos[i].MaxCoef;
                        obj.Rendimiento = Hijos[i].Rendimiento;
                        obj.Covarianza = Hijos[i].Covarianza;
                        for (int l = 0; l < 10; l++) {
                            obj.Porcentajes[l] = Hijos[i].Porcentajes[l];
                        }
                        Elite.add(j, obj);
                        Elite.remove(Elite.size() - 1);
                        break;
                    }
                }
            }
        }

        //La poblacion que dan mayor coeficiente, es decir los mas optimos.
        System.out.println("");
        System.out.println("Poblacion elite");
        for (int i = 0; i < Elite.size(); i++) {
            Elite.get(i).PrintInfo();
        }
    }
    
    public void RadioSharpe() {
        System.out.println("");
        System.out.println("Porcentaje Ratio Sharpe");
        System.out.println("");
        Fitness obj = new Fitness();
        
        obj.MaxCoef = Elite.get(0).MaxCoef;
        obj.Rendimiento = Elite.get(0).Rendimiento;
        obj.Covarianza = Elite.get(0).Covarianza;
        for (int j = 0; j < 10; j++) {
            obj.Porcentajes[j] = Elite.get(0).Porcentajes[j] / Elite.get(0).Covarianza;
        }
        obj.PrintInfo();
    }
    
}
