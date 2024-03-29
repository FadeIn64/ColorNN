package su.fedin.nn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.UnaryOperator;

public class NeuralNetwork {


    public static NeuralNetwork load(String name){
        Loader loader = Loader.getInstance();
        return loader.load(name);
    }

    String name;
    private final double learningRate;
    Layer[] layers;
//    private UnaryOperator<Double> activation;
//    private UnaryOperator<Double> derivative;
    Activation activation;

    @JsonCreator
    public NeuralNetwork(
            @JsonProperty("name") String name,
            @JsonProperty("learningRate") double learningRate,
            @JsonProperty("layers")Layer[] layers,
            @JsonProperty("activation") Activation activation) {
        this.name = name;
        this.learningRate = learningRate;
        this.layers = layers;
        this.activation = activation;
    }

    public NeuralNetwork(String name, double learningRate, Activation activation,  int... layerSize) {
        this.name = name;
        this.learningRate = learningRate;
        this.activation = activation;
        layers = new Layer[layerSize.length];
        for (int i = 0; i < layerSize.length; i++) {
            int nextSize = 0;
            if(i < layerSize.length - 1) nextSize = layerSize[i + 1];
            layers[i] = new Layer(layerSize[i], nextSize);
            layers[i].generateRandomValues();
        }
    }

    public double[] feedForward(double[] inputs) {
        System.arraycopy(inputs, 0, layers[0].neurons, 0, inputs.length);
        for (int i = 1; i < layers.length; i++)  {
            Layer currentLayer = layers[i - 1];
            Layer nextLayer = layers[i];
            for (int j = 0; j < nextLayer.size; j++) {
                nextLayer.neurons[j] = 0;
                for (int k = 0; k < currentLayer.size; k++) {
                    nextLayer.neurons[j] += currentLayer.neurons[k] * currentLayer.weights[k][j];
                }
                nextLayer.neurons[j] += nextLayer.biases[j];
                nextLayer.neurons[j] = activation.activation.apply(nextLayer.neurons[j]);
            }
        }
        return layers[layers.length - 1].neurons;
    }

    public void backpropagation(int epochs, int batchSize, double[][] inputsToTarget, double[][] targets){
        int samplesCount = inputsToTarget.length;
        Tracer tracer = Tracer.getTracer(this);
        for (int i = 1; i <= epochs; i++) {
            int right = 0;
            double errorSum = 0;
            for (int j = 0; j < batchSize; j++) {
                int sampleIndex = (int)(Math.random() * samplesCount);


                double[] outputs = this.feedForward(inputsToTarget[sampleIndex]);
                int maxOutValue = 0;
                double maxOutWeight = -1;
                int maxTargetValue = 0;
                double maxTargetWeight = -1;
                for (int k = 0; k < outputs.length; k++) {
                    if(outputs[k] > maxOutWeight) {
                        maxOutWeight = outputs[k];
                        maxOutValue = k;
                    }
                }
                for (int k = 0; k < targets[sampleIndex].length; k++) {
                    if(targets[sampleIndex][k] > maxTargetWeight) {
                        maxTargetWeight = targets[sampleIndex][k];
                        maxTargetValue = k;
                    }
                }
                if(maxTargetValue == maxOutValue && Math.abs(maxOutWeight - maxTargetWeight) < 1e-1) right++;
                for (int k = 0; k < targets[sampleIndex].length; k++) {
                    errorSum += (targets[sampleIndex][k] - outputs[k]) * (targets[sampleIndex][k] - outputs[k]);
                }
                this.backpropagation(targets[sampleIndex]);
            }
          if (i % 100 == 0)
                tracer.trace(i, right, errorSum);
          System.out.println("epoch:\t" + i + "\tcorrect:\t" + right + "\terror:\t" + errorSum);
        }
    }

    private void backpropagation(double[] targets) {
        double[] errors = new double[layers[layers.length - 1].size];
        for (int i = 0; i < layers[layers.length - 1].size; i++) {
            errors[i] = targets[i] - layers[layers.length - 1].neurons[i];
        }
        for (int k = layers.length - 2; k >= 0; k--) {
            Layer curLayer = layers[k];
            Layer nextLayer = layers[k + 1];

            double[] gradients = new double[nextLayer.size];
            for (int i = 0; i < nextLayer.size; i++) {
                gradients[i] = errors[i] * activation.derivative.apply(nextLayer.neurons[i]);
                gradients[i] *= learningRate;
            }

            double[][] deltas = new double[nextLayer.size][curLayer.size];
            for (int i = 0; i < nextLayer.size; i++) {
                for (int j = 0; j < curLayer.size; j++) {
                    deltas[i][j] = gradients[i] * curLayer.neurons[j];
                }
            }

            double[] errorsNext = new double[curLayer.size];
            for (int i = 0; i < curLayer.size; i++) {
                errorsNext[i] = 0;
                for (int j = 0; j < nextLayer.size; j++) {
                    errorsNext[i] += curLayer.weights[i][j] * errors[j];
                }
            }

            errors = new double[curLayer.size];
            System.arraycopy(errorsNext, 0, errors, 0, curLayer.size);
            double[][] weightsNew = new double[curLayer.weights.length][curLayer.weights[0].length];
            for (int i = 0; i < nextLayer.size; i++) {
                for (int j = 0; j < curLayer.size; j++) {
                    weightsNew[j][i] = curLayer.weights[j][i] + deltas[i][j];
                }
            }

            curLayer.weights = weightsNew;
            for (int i = 0; i < nextLayer.size; i++) {
                nextLayer.biases[i] += gradients[i];
            }
        }
    }

    //Наговнокодил из-за Майкла Джексона
    public void save(){
        Saver.saveNN(this);
    }

}
