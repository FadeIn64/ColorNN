package su.fedin;

import su.fedin.nn.NeuralNetwork;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
        UnaryOperator<Double> dsigmoid = y -> y * (1 - y);
        NeuralNetwork nn = new NeuralNetwork(0.1, sigmoid, dsigmoid, 2, 4, 2);

//        double[] res = nn.feedForward(new double[]{0.6, 0.5, 0.9});
//        Arrays.stream(res).forEach(x-> System.out.print(x + " "));
//        System.out.println();

        nn.backpropagation(5000, 20,
                new double[][]{
                        {0, 1},
                        {1, 0},
                        {1, 1},
                        {0, 0},
                        {0.5, 1},
                        {1, 0.5},
                        {0, 0.5},
                        {0.5, 0}
                },
                new double[][]{
                        {1, 0},
                        {0, 1},
                        {0, 0},
                        {1, 1},
                        {0.5, 0},
                        {0, 0.5},
                        {1, 0.5},
                        {0.5, 1}
                });

        double[] res = nn.feedForward(new double[]{0.3, 1});
        Arrays.stream(res).forEach(x-> System.out.print(x + " "));
        System.out.println();
    }
}