package su.fedin;

import su.fedin.nn.NeuralNetwork;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
        UnaryOperator<Double> dsigmoid = y -> y * (1 - y);
        NeuralNetwork nn = new NeuralNetwork(0.01, sigmoid, dsigmoid, 3, 6, 7, 8);

        double[] res = nn.feedForward(new double[]{0.6, 0.5, 0.9});
        Arrays.stream(res).forEach(x-> System.out.print(x + " "));
        System.out.println();
    }
}