package com.examples.appendixa;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.awt.Color.BLACK;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;

@RunWith(JMock.class)
public class TurtleDriverTest {
    private final Mockery context = new JUnit4Mockery();
    private final Turtle turtle = context.mock(Turtle.class);

    @Test
    public void goesAMinimumDistance() {
        final Turtle turtle2 = context.mock(Turtle.class, "turtle2");
        final TurtleDriver driver = new TurtleDriver(turtle, turtle2); // configuração

        context.checking(new Expectations() {{ // expectativas
            ignoring(turtle2); // turtle2 pode ser instruída a fazer qualquer coisa. Este teste a ignora.

            allowing(turtle).flashLEDs();  // A tartaruga pode ser instruída qualquer número de vezes,
                                            // incluindo nenhuma, a piscar os seus LEDs.

            oneOf(turtle).turn(45); // A tartaruga deve ser instruída exatamente uma vez a virar 45 graus.

            oneOf(turtle).forward(with(greaterThan(20))); // A tartaruga deve ser instruída exatamente uma vez a avançar
            // com uma distância maior que 20 unidades.

            atLeast(1).of(turtle).stop(); // A tartaruga deve ser instruída ao menos uma vez a parar.
        }});

        driver.goNext(45); // chamada do código
        assertTrue("driver has moved", driver.hasMoved()); // mais verificações
    }

    @Test
    public void actionsExpectationsTest() {
        final TurtleDriver driver = new TurtleDriver(turtle);
        String PEN_DOWN = "Down";
        context.checking(new Expectations() {{
            oneOf(turtle).queryPen();  will(returnValue(PEN_DOWN)); // A tartaruga pode ser instruída consultar o seu estado qualquer número de vezes,
            // incluindo nenhuma, e deve retornar a sua direção.
        }});
        driver.penDown();
    }

    @Test
    public void sequenceExpectationsTest() {
        final TurtleDriver driver = new TurtleDriver(turtle);
        context.checking(new Expectations() {{
            final Sequence drawing = context.sequence("drawing");
            allowing(turtle).queryColor(); will(returnValue(BLACK));
            atLeast(1).of(turtle).forward(10); inSequence(drawing);
            oneOf(turtle).turn(45); inSequence(drawing);
            oneOf(turtle).forward(15); inSequence(drawing);
        }});

        driver.goDrawing(45);
        assertTrue("driver has moved", driver.hasMoved());
    }

    @Test
    public void stateExpectationsTest() {
        final TurtleDriver driver = new TurtleDriver(turtle);
        context.checking(new Expectations() {{
            final States pen = context.states("pen").startsAs("up");
            allowing(turtle).queryColor(); will(returnValue(BLACK));

            allowing(turtle).penDown(); then(pen.is("down"));
            allowing(turtle).penUp(); then(pen.is("up"));

            atLeast(1).of(turtle).forward(15); when(pen.is("down"));
            oneOf(turtle).turn(90); when(pen.is("down"));
            oneOf(turtle).forward(10); when(pen.is("down"));
        }});

        driver.movePenDown();
        assertTrue("turtle has moved", driver.hasMoved());
    }
}
