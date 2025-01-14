package com.examples.appendixa;

import java.awt.*;

public class TurtleDriver {
    private Turtle turtle1;
    private Turtle turtle2;

    public TurtleDriver(Turtle turtle1, Turtle turtle2) {
        this.turtle1 = turtle1;
        this.turtle2 = turtle2;
    }

    public TurtleDriver(Turtle turtle1) {
        this.turtle1 = turtle1;
        this.turtle2 = null;
    }

    public void goNext(int angle) {
        turtle1.turn(angle);
        turtle1.forward(25);
        turtle1.stop();
    }

    public void goDrawing(int angle) {
        turtle1.queryColor();
        turtle1.forward(10);
        turtle1.turn(angle);
        turtle1.forward(15);
    }

    public void penDown() {
        turtle1.queryPen();
    }

    public void movePenDown() {
        turtle1.penDown();

        turtle1.forward(15);
        turtle1.turn(90);
        turtle1.forward(10);
    }

    public boolean hasMoved() {
        return true;
    }
}
