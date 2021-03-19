/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject_draws;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
 *
 * @author Artur
 */
public class TModel {
    
    public TModel()
    {
        p0 = new Point();
    }
    
    
    /**
     * Oblicza wartośc wybranej funkcji dla podanych parametrów
     * @param nr - numer fukcji
     *       0: f(x) = Ax + B
     *       1: f(x) = Ax^2 + Bx + C
     *       2: f(x) = A*sin(Bx + C) + D
     *       3: f(x) = A/(Bx + C) + D
     * @param x - arumentem
     * @param A - parametr funkcji
     * @param B - parametr funkcji
     * @param C - parametr funkcji
     * @param D - parametr funkcji
     * @return wartość funcji
     */
    private double f(int nr, double x, double A, double B, double C, double D)
    {
        double y = 0.0;
        switch(nr)
        {
            case 0: y = A * x + B; break;
            case 1: y = A*x*x + B*x + C; break;
            default: y = x;
        }
        
        return y;
    }
    
    /**
     * Tablicuję wybraną funkcję
     * @param nr
     * @param a
     * @param b
     * @param A
     * @param B
     * @param C
     * @param D 
     */
    private void tabulate (int nr, double a, double b, double A, double B, double C, double D)
    {
       int n = image.getWidth(null) - 2 * ramka;
       double delta = (b - a)/ (n - 1);
       Fx = new double[n];
       
       double min, max;
       min = max = Fx[0] = f(nr, a, A, B, C, D);
       for (int i = 1; i < n; ++i)
       {
           Fx[i] = f(nr, a + i * delta, A, B, C, D);
           if (Fx[i] < min)
               min = Fx[i];
           if (Fx[i] > max)
               max = Fx[i];
           // dla i = 0
           // Fx[0] = f(a + 0 * delta) = f(a) 
           // dla i = n - 1
           // Fx[n] = f(a + (n - 1) * delta) = f(a + (n - 1) * (b - a)/(n-1)) = f(a + b - a) = f(b)
       }
       
       int h = image.getHeight(null) - 2 * ramka;   // wysokośc obszaru rysowania wykresu
       // skalowanie wartości funkcji
       for (int i = 0; i < n; ++i)
       {
           if (Math.abs(max - min) > 1.0e-8 )
           {
               Fx[i] = h * (Fx[i] - min)/ (max - min);
           }
           else 
           { // funkcja stała
               if (max > 0)
                   Fx[i] = h;
               else
                  if (min < 0)
                      Fx[i] = -h;
                  else
                      Fx[i] = 0.0;
           }
       }

       // ustalenie położenia osi Y
       if (a > 0)
           p0.x = ramka/2;
       else
       {
           if (b < 0)
               p0.x = image.getWidth(null) - ramka/2;
           else
               p0.x = ramka + (int) (-a * n / (b - a) + 0.5);
       }

       // ustalenie położenia osi X
       if (max < 0)
           p0.y = h +ramka/2;
       else
       {
           if (min > 0)
               p0.y =  ramka/2;
           else
               if (Math.abs(max - min) < 1.0e-8)
                   p0.y = ramka + h/2;
               else
                   p0.y = image.getHeight(null) - ramka - (int) (-min * h / (max - min) + 0.5);
       }
       
    }
    
    /**
     * 
     * @param nr
     * @param a
     * @param b
     * @param A
     * @param B
     * @param C
     * @param D
     * @param image - obiekt, na którym zostanie narysowany wykres
     * @return obiekt z narysowanym wykresem
     */
    public Image drawGraph(int nr, double a, double b, double A, double B, double C, double D, Color color, Image image)
    {
        this.image = image;
        int h = image.getHeight(null) - ramka;   // wysokośc obszaru rysowania wykresu
        
        tabulate (nr, a, b, A, B, C, D);   // tablicujemy funkcję
        
        Graphics g = image.getGraphics();  // pobieramy kontekst graficzny
        g.setColor(Color.BLACK);           // ustalmy kolor osi układu współrzędnych 
        drawCoordinate(p0);                // rysujemy układ współrzędnych
        g.setColor(color);
        for (int i = 1; i < Fx.length; ++i)
        {
            g.drawLine(ramka + i - 1, h - (int)(Fx[i - 1] + 0.5), ramka + i, h - (int)(Fx[i] + 0.5));
        }
        
        
        return image;
    }
    
    
    /**
     * Rysuje układ współrzędnych, którego środek przechodzi przez punkt p    
     * @param p - punkt, przez który przechodz początek układu współrzędnych
     */
    public void drawCoordinate(Point p)
    {
       int h = image.getHeight(null);
       int w = image.getWidth(null);
       
       
       Graphics g = image.getGraphics();
      /// g.setColor(color);
       g.drawLine(ramka/2, h - p.y, w - ramka/2, h - p.y);       // oś pozioma 
       g.drawLine( (int)(w - 3.0*ramka /4.0 + 0.5), h - p.y - 2, w - ramka/2, h - p.y);       // górna część grota
       g.drawLine( (int)(w - 3.0*ramka /4.0 + 0.5), h - p.y + 2, w - ramka/2, h - p.y);       // dolna część grota
       
       g.drawLine(p.x, h - ramka/2, p.x, h - h + ramka/2);       // oś pozioma 
       g.drawLine(p.x - 2, h - h + (int) (3.0 *ramka/4.0), p.x, h - h + ramka/2);       // lewa część grota
       g.drawLine(p.x + 2, h - h + (int) (3.0 *ramka/4.0), p.x, h - h + ramka/2);       // prawa część grota
       g.dispose();
    }
    
    private Point p0;       // położenie początku układu współrzędnych
    private double Fx[];    // tablica z wartościami funcji
    private int ramka = 20; 
    private Image image;
    //private Color color;    // kolor wykresu
}
