package com.examples.appendixa;

import java.awt.*;

public interface Turtle {
    void flashLEDs();
    void turn(int angle);
    void forward(int distance);
    String queryPen();
    Color queryColor();
    void penDown();
    void penUp();
    void stop();
}

