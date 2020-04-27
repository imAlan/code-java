package performance.lambdas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CapturingLambdaTest {


    /**
     * Non Capturing Lambdas perform better than Capturing Lambdas
     * <p>
     * Non capturing will reuse the lambdas while capturing will create a new one each time
     * <p>
     * http://tutorials.jenkov.com/java/lambda-expressions.html#local-variable-capture
     * https://blog.codefx.org/java/instances-non-capturing-lambdas/#Evaluation-of-Lambda-Expressions
     * https://www.logicbig.com/tutorials/core-java-tutorial/java-8-enhancements/java-capturing-lambda.html
     */
    @Test
    public void testNonCapturingLambdaIsMorePerformant() {
        Assertions.assertTrue(testPerfNonCapturingLambda() < testPerfCapturingLambda());
    }


    public static long testPerfCapturingLambda() {
        long total = 0;
        for (int i = 0; i < 5; i++) {
            long startTime = System.nanoTime();

            capturingLambda();

            long stopTime = System.nanoTime();
            total += stopTime - startTime;

        }

        System.out.println(total / 5);
        return total / 5;
    }

    public static long testPerfNonCapturingLambda() {
        long total = 0;

        for (int i = 0; i < 5; i++) {
            long startTime = System.nanoTime();
            nonCapturingLambda();
            long stopTime = System.nanoTime();
            total += stopTime - startTime;
        }
        System.out.println(total / 5);
        return total / 5;
    }

    private static void capturingLambda() {
        Executor executor = new Executor();
        Number bb = new Number();
        Event event = new Event();
        Set<Integer> lamdbaHashcode = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            bb.setLong(i);
            Consumer<Event> consumer = e -> e.set(bb.getLong());
            lamdbaHashcode.add(consumer.hashCode());
            executor.execute(() ->
                    consumer.accept(event));
        }

        assertEquals(1000, lamdbaHashcode.size(), "Capturing Lamdbas will be recreated");
    }

    private static void nonCapturingLambda() {
        Executor executor = new Executor();
        Number bb = new Number();
        Event event = new Event();
        Set<Integer> lamdbaHashcode = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            bb.setLong(i);
            BiConsumer<Event, Number> consumer = (e, bbb) -> e.set(bbb.getLong());
            lamdbaHashcode.add(consumer.hashCode());
            executor.execute(() -> consumer.accept(event, bb));
        }

        assertEquals(1, lamdbaHashcode.size(), "Capturing lambda will be reused");
    }

    private volatile static int counter = 0;

    private static class Number {
        private long value;

        public Number() {
            counter++;
        }

        public void setLong(long value) {
            this.value = value;
        }

        public long getLong() {
            return value;
        }
    }

    private static class Event {
        private long value;

        public void set(long value) {
            this.value = value;
        }
    }

    private static class Executor {

        public void execute(Runnable run) {
            run.run();
        }

    }

}
