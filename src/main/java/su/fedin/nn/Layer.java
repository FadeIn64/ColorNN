package su.fedin.nn;

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

    void generateRandomValues(){
        for (int i = 0; i < size; i++){
            neurons[i] = 0;
            biases[i] = Math.random() * 2 - 1;
            for (int j = 0; j < weights[0].length; j++)
                weights[i][j] = Math.random() * 2 - 1;
        }
    }
}
