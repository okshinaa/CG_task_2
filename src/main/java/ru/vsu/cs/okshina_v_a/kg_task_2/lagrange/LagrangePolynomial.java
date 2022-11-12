package ru.vsu.cs.okshina_v_a.kg_task_2.lagrange;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class LagrangePolynomial {
    public int count = 0;
    public ArrayList<Point> points = new ArrayList<Point>();

    //проверяем, есть ли точка с такой х-координатой в массиве points
    public boolean checkPoint(double xn, double yn) {
        for(var i=0; i < points.size(); i++) {
            if(points.get(i).x==xn && points.get(i).y == yn) {
                return false;
            }
        }
        return true;
    }

    //упорядочиваем точки по х-коорд
    public void normArr() {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = 0; j < points.size() - 1 - i; j++) {
                if (points.get(j).x > points.get(j + 1).x) {
                    double tempX = points.get(j).x;
                    double tempY = points.get(j).y;

                    points.get(j).x = points.get(j + 1).x;
                    points.get(j).y = points.get(j + 1).y;
                    points.get(j + 1).x = tempX;
                    points.get(j + 1).y = tempY;
                }
            }
        }
    }

    //подстановка x0 в многочлен Лагранжа
    public double doLagrange(double x0) {
        double y0 = 0;//значение многочлена в точке y0
        double step;
        for (int i = 0; i < points.size(); i++) {
            step = points.get(i).y;
            //считаем множитель при i-ом значении
            for (int j = 0; j < points.size(); j++) {
                if (i!=j) {
                    step = step * (x0 - points.get(j).x) / (points.get(i).x - points.get(j).x);
                }
            }
            y0 += step;
        }
        return y0;
    }

    //отрисовка графика
    public void drawGraphic(GraphicsContext g) {
        if (points.size() == 0) {
            return;
        }
        double sty;
        g.beginPath();
        g.setFill(Color.BLACK);
        g.moveTo(300 + 75 * points.get(0).x, 300 - 75 * points.get(0).y);
        for (int i = 0; i < points.size() - 1; i++) {
            for (double stx = points.get(i).x; stx <= points.get(i + 1).x; stx += 0.01) {
                sty = doLagrange(stx);
                g.lineTo(300 + 75 * stx, 300 - 75 * sty);
            }
        }
        g.stroke();
        g.closePath();
    }
}