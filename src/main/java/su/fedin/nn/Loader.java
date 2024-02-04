package su.fedin.nn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class Loader {

    private static Loader loader;

    public static Loader getInstance(){
        if (loader == null)
            loader = new Loader();
        return loader;
    }

    private ObjectMapper mapper = new ObjectMapper();

    Loader() {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        mapper.enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION);
    }

    public NeuralNetwork load(String savePath, String name){
        File saves = new File(savePath);
        if (!saves.isDirectory() || !saves.exists())
            throw new IllegalStateException("Save path not exist");

        File nnFolder = new File(saves.getAbsolutePath()+"/"+name);
        if (!nnFolder.isDirectory() || !nnFolder.exists())
            throw new IllegalStateException("Save folder don't have NN");

        File mainPart = new File(nnFolder.getAbsolutePath()+"/"+name+".json");
        if (!mainPart.exists())
            throw new IllegalStateException("Save folder don't have NN");

        String json;
        NeuralNetwork nn;
        try {
            FileInputStream inputStream = new FileInputStream(mainPart);
            byte[] bytes = inputStream.readAllBytes();
            char[] chars = new char[bytes.length];
            for (int i = 0; i < bytes.length; i++)
                chars[i] = (char) bytes[i];
            json = String.valueOf(chars);
            System.out.println(json);
            nn = mapper.readValue(json, NeuralNetwork.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File layers = new File(nnFolder.getAbsolutePath()+"/layers");

        return nn;


    }
    public NeuralNetwork load(String name){
        return load(Saver.DEFAULT_SAVE_PATH, name);
    }
}
