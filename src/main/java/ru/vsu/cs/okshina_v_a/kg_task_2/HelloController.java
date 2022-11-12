package ru.vsu.cs.okshina_v_a.kg_task_2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import ru.vsu.cs.okshina_v_a.kg_task_2.lagrange.LagrangePolynomial;
import ru.vsu.cs.okshina_v_a.kg_task_2.lagrange.Point;
import ru.vsu.cs.okshina_v_a.kg_task_2.lagrange.LagrangePolynomial;
import ru.vsu.cs.okshina_v_a.kg_task_2.lagrange.Point;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;


public class HelloController implements Initializable {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private final ScreenConverter screenConverter = new ScreenConverter(-1, 1, 2, 2, WIDTH, HEIGHT);

    private ScreenPoint startPoint = null;
    LagrangePolynomial lp = new LagrangePolynomial();

    @FXML
    private Canvas drawingCanvas = new Canvas(HEIGHT, WIDTH);

    private int currentScroll = 0;

    private double delta = 0;

    private double scale = 1;

    private GraphicsContext g;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = drawingCanvas.getGraphicsContext2D();
        repaint();
    }

    @FXML
    private void onScroll(ScrollEvent e) {
        int clicks = -(int) e.getTextDeltaY() / 2;
        currentScroll += clicks;
        scale = getScale();
        screenConverter.changeScale(scale);
        screenConverter.moveCorner(new RealPoint(0,0));

        repaint();
    }

    private void repaint() {
        paint(g);
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        if (lp.count == 0) {
            drawDashes(g);
            drawAxes(g);
        }

        startPoint = new ScreenPoint((int) e.getX(), (int) e.getY());

        double tx = e.getX();
        double ty = e.getY();
        if (lp.checkPoint(tx/75-4,4-ty/75)) {
            lp.points.add( new Point(tx / 75 - 4, 4 - ty / 75));
            lp.count++;
            //рисуем новую точку:
            g.beginPath();
            g.moveTo(tx,ty);
            g.strokeOval(tx, ty,3,3);
            g.fill();
            g.closePath();
        }
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        ScreenPoint newPoint = new ScreenPoint((int) e.getX(), (int) e.getY());
        RealPoint p1 = screenConverter.screenPointToReal(newPoint);
        RealPoint p2 = screenConverter.screenPointToReal(startPoint);

        RealPoint deltaPoint = p2.minus(p1);
        screenConverter.moveCorner(deltaPoint);
        delta = (int) (Math.sqrt(Math.pow(screenConverter.getCenterX(), 2) + Math.pow(screenConverter.getCenterY(), 2)) * 15);

        startPoint = newPoint;
        repaint();
    }

    protected void paint(GraphicsContext g) {
        screenConverter.setScreenHeight(HEIGHT);
        screenConverter.setScreenWidth(WIDTH);
        g.clearRect(0, 0, WIDTH, HEIGHT);

        scale = getScale();

        drawDashes(g);

        drawAxes(g);
        lp.normArr();
        lp.drawGraphic(g);
        lp.count = 0;
    }

    public void computeFunction(ActionEvent actionEvent) {
        repaint();
    }

    public void deleteFunction(ActionEvent actionEvent) {
        lp.points.clear();
        lp.count = 0;
        screenConverter.setScreenHeight(HEIGHT);
        screenConverter.setScreenWidth(WIDTH);
        g.clearRect(0, 0, WIDTH, HEIGHT);

        scale = getScale();

        drawDashes(g);
        drawAxes(g);

    }



    private void drawDashes(GraphicsContext graphicsContext) {
        for (BigDecimal i = new BigDecimal(0); i.compareTo(BigDecimal.valueOf(screenConverter.getRealWidth() / 2 + delta)) <= 0; i = i.add(BigDecimal.valueOf(screenConverter.getRealWidth() / 20))) {
            RealPoint upX = new RealPoint(i.doubleValue(), 0 + 0.02);
            RealPoint downX = new RealPoint(i.doubleValue(), 0 - 0.02);
            drawLine(graphicsContext, new Line(upX, downX));

            RealPoint textXpoint = new RealPoint(i.doubleValue(), 0 - 0.04);
            var screenPointX = screenConverter.realPointToScreen(textXpoint);
            graphicsContext.fillText("" + i, screenPointX.getX(), screenPointX.getY());


            RealPoint upY = new RealPoint(0 + 0.02, i.doubleValue());
            RealPoint downY = new RealPoint(0 - 0.02, i.doubleValue());
            drawLine(graphicsContext, new Line(upY, downY));

            if (i.doubleValue() != 0) {
                RealPoint textYpoint = new RealPoint(0 + 0.04, i.doubleValue());
                var screenPointY = screenConverter.realPointToScreen(textYpoint);
                graphicsContext.fillText("" + i, screenPointY.getX(), screenPointY.getY());
            }
        }

        for (BigDecimal i = BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 20); i.compareTo(BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 2 - delta)) >= 0; i = i.add(BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 20))) {
            RealPoint upX = new RealPoint(i.doubleValue(), 0 + 0.02);
            RealPoint downX = new RealPoint(i.doubleValue(), 0 - 0.02);
            drawLine(graphicsContext, new Line(upX, downX));

            RealPoint textXpoint = new RealPoint(i.doubleValue(), 0 - 0.04);
            var screenPointX = screenConverter.realPointToScreen(textXpoint);
            graphicsContext.fillText("" + i, screenPointX.getX(), screenPointX.getY());


            RealPoint upY = new RealPoint(0 + 0.02, i.doubleValue());
            RealPoint downY = new RealPoint(0 - 0.02, i.doubleValue());
            drawLine(graphicsContext, new Line(upY, downY));

            if (i.doubleValue() != 0) {
                RealPoint textYpoint = new RealPoint(0 + 0.04, i.doubleValue());
                var screenPointY = screenConverter.realPointToScreen(textYpoint);
                graphicsContext.fillText("" + i, screenPointY.getX(), screenPointY.getY());
            }
        }
    }

    private void drawAxes(GraphicsContext graphicsContext) {
        Line oX = new Line(new RealPoint(-1 - delta, 0), new RealPoint(1 + delta, 0));
        Line oY = new Line(new RealPoint(0, -1 - delta), new RealPoint(0, 1 + delta));
        drawLine(graphicsContext, oX);
        drawLine(graphicsContext, oY);
    }

    private void drawLine(GraphicsContext g, Line line) {
        ScreenPoint p1 = screenConverter.realPointToScreen(line.getStartPoint());
        ScreenPoint p2 = screenConverter.realPointToScreen(line.getEndPoint());

        g.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
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
}