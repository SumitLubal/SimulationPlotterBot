package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField text_y,text_x;
    @FXML
    Pane vbox;
    @FXML
    Label label;
    @FXML
    Button button_draw;


    //custom data
    double originX = 0, originY = 0;
    double armLength = 100;
    double maxHeight, maxWidth;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        getRectangle();
        System.out.println("That was easy, wasn't it?" + vbox.getHeight());
    }

    Rectangle getRectangle() {
        Rectangle r = new Rectangle();
        r.setX(0);
        r.setY(0);
        originY = maxHeight = maxWidth = (armLength * Math.sqrt(2));

        r.setWidth(maxWidth);
        r.setHeight(maxHeight);
        r.setOpacity(10);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.BLACK);
        return r;
    }
    List<Point> fillRandomPoints(){
        ArrayList<Point> layer = new ArrayList<>();
        //start with 20, 20
        int x = 20,y=20;
        //add or remove randomly with difference of one.. make sure not go below 0 and above maxWidth
        for(int i=0;i<200;i++){
            if(getRandomBoolean() && x>0 && y>0 ) {
                layer.add(new Point(--x,--y));
            }else if(x<maxWidth && y<maxHeight){
                layer.add(new Point(++x,++y));
            }
        }
        return layer;
    }
    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
        //I tried another approaches here, still the same result
    }

    Line getLineWithAngle(double startX, double startY, double Angle, boolean isFirstArm) {

        Line line = new Line();/*
        double endX = startX + armLength * Math.sin(Math.toRadians(Angle));
        double endY = startY + armLength * Math.cos(Math.toRadians(Angle));
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        return line;*/
        if(isFirstArm) {
            line.setStartX(startX);
            line.setStartY(startY);
            line.setEndX((armLength * Math.cos(Math.toRadians(Angle))) + startX);
            line.setEndY(startY - (armLength * Math.sin(Math.toRadians(Angle))));
        }else{
            line.setStartX(startX);
            line.setStartY(startY);
            line.setEndX((armLength * Math.cos(Math.toRadians(Angle))) + startX);
            line.setEndY(startY - (armLength * Math.sin(Math.toRadians(Angle))));
        }
        return line;
    }

    public static double calcRotationAngleInDegrees(Point centerPt, Point targetPt) {
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

        theta += Math.PI / 2.0;
        double angle = Math.toDegrees(theta);
        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public void drawButtonClicked(ActionEvent event) {
        System.out.println("That was easy, wasn't it?" + vbox.getHeight());

        vbox.getChildren().add(getRectangle());
        if(text_x.getText().equals("") || text_y.getText().equals("")){
            label.setText("Something");
            return;
        }
        int x = Integer.parseInt(text_x.getText());
        int y = Integer.parseInt(text_y.getText());

        Point drawOnToPoint = new Point(x, y);
        //getIntersection point
        Point origine = new Point(originX, originY);
        Point intersectionPoint = getInterSectionPoint(drawOnToPoint);
        System.out.println("Intersection " + intersectionPoint.getX()+" : "+intersectionPoint.getY());

        //getAngle for first line
        double angle1 = calcRotationAngleInDegrees(origine, intersectionPoint);
        double angle2 = calcRotationAngleInDegrees(intersectionPoint, drawOnToPoint);
        System.out.println("Anlges " + angle1+" : "+angle2);
        //draw both arms with intersection point
        Line firstArm = getLineWithAngle(originX, originY, 90 -  angle1, true);
        Line secondArm = getLineWithAngle(intersectionPoint.getX(), intersectionPoint.getY(), 90 - angle2, false);

        vbox.getChildren().add(firstArm);
        vbox.getChildren().add(secondArm);
        //draw both circle to see if they match the calculation
        javafx.scene.shape.Circle c1 = new javafx.scene.shape.Circle(origine.getX(), origine.getY(), armLength);
        javafx.scene.shape.Circle c2 = new javafx.scene.shape.Circle(drawOnToPoint.getX(), drawOnToPoint.getY(), armLength);
        c1.setFill(Color.TRANSPARENT);
        c1.setStroke(Color.BLACK);
        c2.setFill(Color.TRANSPARENT);
        c2.setStroke(Color.BLACK);
        vbox.getChildren().add(c1);
        vbox.getChildren().add(c2);
    }

    Point getInterSectionPoint(Point destinationPoint) {
        Circle c1 = new Circle(new Vector2(originX, originY), armLength);
        Circle c2 = new Circle(new Vector2(destinationPoint.getX(), destinationPoint.getY()), armLength);
        CircleCircleIntersection utilForIntersection = new CircleCircleIntersection(c1, c2);
        Vector2[] points = utilForIntersection.getIntersectionPoints();
        return new Point(points[0].x, points[0].y);
    }
}
