package ru.vsu.cs.okshina_v_a.kg_task_2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import ru.vsu.cs.okshina_v_a.kg_task_2.lagrange.LagrangePolynomial;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private GraphicsContext g;
    private final ScreenConverter screenConverter = new ScreenConverter(-1, 1, 2, 2, WIDTH, HEIGHT);

    private ScreenPoint startPoint = null;
    private int currentScroll = 0;
    private double delta = 0;
    private double scale = 1;
    private BigDecimal axesCenterX = new BigDecimal("0");
    private BigDecimal axesCenterY = new BigDecimal("0");
    LagrangePolynomial lp = new LagrangePolynomial();
    public ArrayList<RealPoint> pointsNew = new ArrayList<RealPoint>();

    @FXML
    private Canvas drawingCanvas = new Canvas(HEIGHT, WIDTH);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        g = drawingCanvas.getGraphicsContext2D();
        repaint();
    }

    private void repaint() {
        paint(g);
    }

    protected void paint(GraphicsContext graphicsContext) {
        screenConverter.setScreenHeight(HEIGHT);
        screenConverter.setScreenWidth(WIDTH);
        graphicsContext.clearRect(0, 0, WIDTH, HEIGHT);

        drawDashes(graphicsContext);
        drawAxes(graphicsContext);

        for (RealPoint rp : lp.points) {
            ScreenPoint sp = screenConverter.realPointToScreen(rp);
            int tx = sp.getX();
            int ty = sp.getY();
            g.setFill(Color.BLACK);
            //g.moveTo(tx,ty);
            g.fillOval(tx, ty, 5, 5);
        }
        drawGraphic(graphicsContext);
    }

    public void onScroll(ScrollEvent e) {
        int clicks = -(int) e.getTextDeltaY() / 2;
        currentScroll += clicks;
        scale = getScale();
        screenConverter.changeScale(scale);
        screenConverter.moveCorner(new RealPoint(0,0));

        repaint();
    }

    public void onMouseDragged(MouseEvent e) {
        ScreenPoint newPoint = new ScreenPoint((int) e.getX(), (int) e.getY());
        RealPoint p1 = screenConverter.screenPointToReal(newPoint);
        RealPoint p2 = screenConverter.screenPointToReal(startPoint);

        RealPoint deltaPoint = p2.minus(p1);
        screenConverter.moveCorner(deltaPoint);
        delta = (int) (Math.sqrt(Math.pow(screenConverter.getCenterX(), 2) + Math.pow(screenConverter.getCenterY(), 2)) * 15);

        startPoint = newPoint;
        repaint();
    }

    public void onMousePressed(MouseEvent e) {
        startPoint = new ScreenPoint((int) e.getX(), (int) e.getY());

        double tx = e.getX();//получение координат
        double ty = e.getY();

        /*if(lp.count == 0) {
            drawDashes(g);
            drawAxes(g);
        }*/

        RealPoint p = screenConverter.screenPointToReal(startPoint);
        if(lp.checkPoint(p.getX(), p.getY())) {
            lp.points.add(p);
            //lp.count++;
            //рисуем новую точку:

        }
        repaint();
    }

    public void computeFunction(ActionEvent actionEvent) {
        //lp.normArr();
        //drawGraphic(g);
        repaint();
    }

    public void deleteFunction(ActionEvent actionEvent) {
        lp.points.clear();
        pointsNew.clear();
        //lp.count = 0;
        screenConverter.setScreenHeight(HEIGHT);
        screenConverter.setScreenWidth(WIDTH);
        g.clearRect(0, 0, WIDTH, HEIGHT);

        drawDashes(g);
        drawAxes(g);
    }

    private void drawDashes(GraphicsContext graphicsContext) {
        for (BigDecimal i = new BigDecimal(0); i.compareTo(BigDecimal.valueOf(screenConverter.getRealWidth() / 2 + delta)) <= 0; i = i.add(BigDecimal.valueOf(screenConverter.getRealWidth() / 20))) {
            RealPoint upX = new RealPoint(i.doubleValue(), axesCenterY.doubleValue() + 0.02);
            RealPoint downX = new RealPoint(i.doubleValue(),axesCenterY.doubleValue()- 0.02);
            drawLine(graphicsContext, new Line(upX, downX));

            RealPoint textXpoint = new RealPoint(i.doubleValue(), axesCenterY.doubleValue() - 0.04);
            var screenPointX = screenConverter.realPointToScreen(textXpoint);
            graphicsContext.fillText("" + i.subtract(axesCenterX), screenPointX.getX(), screenPointX.getY());


            RealPoint upY = new RealPoint(axesCenterX.doubleValue() + 0.02, i.doubleValue());
            RealPoint downY = new RealPoint(axesCenterX.doubleValue() - 0.02, i.doubleValue());
            drawLine(graphicsContext, new Line(upY, downY));

            //System.out.println(i + " " + axesCenterX);
            if (i.doubleValue() != axesCenterX.doubleValue()) {
                RealPoint textYpoint = new RealPoint(axesCenterX.doubleValue() + 0.04, i.doubleValue());
                var screenPointY = screenConverter.realPointToScreen(textYpoint);
                graphicsContext.fillText("" + i.subtract(axesCenterY), screenPointY.getX(), screenPointY.getY());
            }
        }

        for (BigDecimal i = BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 20);
             i.compareTo(BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 2 - delta)) >= 0;
             i = i.add(BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 20))) {


            RealPoint upX = new RealPoint(i.doubleValue(),  axesCenterY.doubleValue() + 0.02);
            RealPoint downX = new RealPoint(i.doubleValue(),  axesCenterY.doubleValue() - 0.02);
            drawLine(graphicsContext, new Line(upX, downX));

            RealPoint textXpoint = new RealPoint(i.doubleValue(),  axesCenterY.doubleValue() - 0.04);
            var screenPointX = screenConverter.realPointToScreen(textXpoint);
            graphicsContext.fillText("" + i.subtract(axesCenterX), screenPointX.getX(), screenPointX.getY());


            RealPoint upY = new RealPoint(axesCenterX.doubleValue() + 0.02, i.doubleValue());
            RealPoint downY = new RealPoint(axesCenterX.doubleValue() - 0.02, i.doubleValue());
            drawLine(graphicsContext, new Line(upY, downY));

            if (i.doubleValue() != axesCenterY.doubleValue()) {
                RealPoint textYpoint = new RealPoint(axesCenterX.doubleValue() + 0.04, i.doubleValue());
                var screenPointY = screenConverter.realPointToScreen(textYpoint);
                graphicsContext.fillText("" + (i.subtract(axesCenterY)), screenPointY.getX(), screenPointY.getY());
            }
        }
    }

    private double getScale() {
        int[] scales = new int[]{1,2,5};
        int clicks = Math.abs(currentScroll);

        double newScale = scales[clicks % 3];
        double delta = 0.1d;

        if(currentScroll == 0) {
            return 1;
        }

        if(currentScroll > 0) {
            delta = 10;
        }
        clicks = clicks / 3;
        while(clicks > 0) {
            newScale *= delta;
            clicks--;
        }

        return newScale;
    }

    private void drawLine(GraphicsContext g, Line line) {
        ScreenPoint p1 = screenConverter.realPointToScreen(line.getStartPoint());
        ScreenPoint p2 = screenConverter.realPointToScreen(line.getEndPoint());

        g.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }


    private void drawAxes(GraphicsContext graphicsContext) {
        Line oX = new Line(new RealPoint(-1 - delta, 0), new RealPoint(1 + delta, 0));
        Line oY = new Line(new RealPoint(0, -1 - delta), new RealPoint(0, 1 + delta));
        drawLine(graphicsContext, oX);
        drawLine(graphicsContext, oY);
    }
    private void drawGraphic(GraphicsContext g) {
        if (lp.points.size() == 0) {
            return;
        }

        g.setFill(Color.BLACK);
        double stepX = screenConverter.getRealWidth() / WIDTH;
        RealPoint prev = new RealPoint(screenConverter.getCenterX(), lp.doLagrange(screenConverter.getCenterX()));
        for (int i = 1; i <= WIDTH; i++) {
            double x = screenConverter.getCenterX() + i * stepX;
            double y = lp.doLagrange(x);
            RealPoint cur = new RealPoint(x, y);
            drawLine(g, new Line(prev, cur));
            prev = cur;
        }

        /*double sty;

        for (int i = 0; i < lp.points.size() - 1; i++) {
            for (double stx = lp.points.get(i).x; stx <= lp.points.get(i + 1).x; stx += 0.01) {
                sty = lp.doLagrange(stx);
                pointsNew.add(new RealPoint(stx, sty));
            }
        }

        g.setFill(Color.BLACK);


        for (int k = 0; k < pointsNew.size() - 1; k++) {
            for (double ktx = pointsNew.get(k).x; ktx <= pointsNew.get(k + 1).x; ktx += 0.01) {
                for (double kty = pointsNew.get(k).y; kty <= pointsNew.get(k + 1).y; kty += 0.01) {
                    Line line = new Line(pointsNew.get(k), pointsNew.get(k+1));
                    drawLine(g, line);
                }
            }
        }*/
    }
}