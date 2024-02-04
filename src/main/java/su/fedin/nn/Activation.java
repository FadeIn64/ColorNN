package su.fedin.nn;

import java.util.function.UnaryOperator;

public enum Activation {
    SIGMOID(x -> 1 / (1 + Math.exp(-x)), y -> y * (1 - y));

    public final UnaryOperator<Double> activation;
    public final UnaryOperator<Double> derivative;

    Activation(UnaryOperator<Double> activation, UnaryOperator<Double> derivative){
        this.activation = activation;
        this.derivative = derivative;
    }

}
