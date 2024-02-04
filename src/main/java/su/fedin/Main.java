package su.fedin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.fedin.nn.Activation;
import su.fedin.nn.NeuralNetwork;
import su.fedin.nn.train.TrainInput;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        NeuralNetwork nn = new NeuralNetwork("Test",0.001, Activation.SIGMOID, 3, 30, 30, 15);

//        double[] res = nn.feedForward(new double[]{0.6, 0.5, 0.9});
//        Arrays.stream(res).forEach(x-> System.out.print(x + " "));
//        System.out.println();

//        testNN(nn);

        String[] output = new String[]{"Черный", "Белый", "Серый", "Красный", "Бордовый", "Розовый", "Оранжевый",
                "Желтый", "Зеленый", "Темно-зеленый", "Салатовый", "Синий", "Фиолетовый", "Голубой", "Темно-Синий"
        };


        TrainInput trainInput = new TrainInput("color");
        trainInput.loadData();
        trainInput.normalizeData();

        nn.backpropagation(5000, 15, trainInput.getInput(), trainInput.getTarget());

        double[] res = nn.feedForward(new double[]{(double) 18 /255, (double) 103 /255, (double) 95 /255});
        int maxInd = 0;
        for (int i = 1; i < res.length; i++)
            maxInd = (res[maxInd] < res[i])? i: maxInd;
        Arrays.stream(res).forEach(x-> System.out.print(x + " "));
        System.out.println();
        System.out.println(output[maxInd]);

        nn.save();


    }

    private static void jacksonTest(NeuralNetwork nn) {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json;
        try {
            json = om.writeValueAsString(nn);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(json);
    }

    private static void testNN(NeuralNetwork nn) {
        nn.backpropagation(50000, 2,
                new double[][]{
                        {0, 1},
                        {1, 0},
                        {1, 1},
                        {0, 0},

                },
                new double[][]{
                        {1, 0},
                        {0, 1},
                        {0, 0},
                        {1, 1},
                });

        double[] res = nn.feedForward(new double[]{0.5, 1});
        Arrays.stream(res).forEach(x-> System.out.print(x + " "));
        System.out.println();
    }
}