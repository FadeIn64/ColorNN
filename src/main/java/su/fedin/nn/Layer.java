package su.fedin.nn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Layer {
    public int size;
    public double[] neurons;
    public double[] biases;
    public double[][] weights;

    public Layer(int size, int nextSize) {
        this.size = size;
        neurons = new double[size];
        biases = new double[size];
        weights = new double[size][nextSize];
    }

    @JsonCreator
    public Layer(
            @JsonProperty("size")int size,
            @JsonProperty("neurons") double[] neurons,
            @JsonProperty("biases") double[] biases,
            @JsonProperty("weights") double[][] weights) {
        this.size = size;
        this.neurons = neurons;
        this.biases = biases;
        this.weights = weights;
    }

    void generateRandomValues(){
        for (int i = 0; i < size; i++){
            neurons[i] = 0;
            biases[i] = Math.random() * 2 - 1;
            for (int j = 0; j < weights[0].length; j++)
                weights[i][j] = Math.random() * 2 - 1;
        }
    }
}
