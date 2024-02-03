package su.fedin.nn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Saver {

    static String DEFAULT_SAVE_PATH = "./saves";
    static Map<String, Saver> pool = new HashMap<>();

    static void saveNN(NeuralNetwork nn){
        Saver saver =  pool.get(nn.name);
        if (saver == null){
            saver = new Saver(nn.name);
            pool.put(nn.name, saver);
        }
        saver.save(nn);
    }

    private String path;
    private String name;
    private File saveDir;
    private final ObjectMapper mapper = new ObjectMapper();


    Saver(String path, String name) {
        this.path = path;
        this.name = name;

        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    Saver(String name) {
        this(DEFAULT_SAVE_PATH, name);
    }

    void save(NeuralNetwork nn){
        if (!Objects.equals(nn.name, name))
            return;

        File save = new File(path);
        if (!save.exists())
            save.mkdir();

        saveDir = new File(path+"/"+name);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        File mainPart = new File(saveDir.getAbsolutePath()+"/"+name+".json");

        if (!mainPart.exists()) {
            try {
                mainPart.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            FileOutputStream mainOut = new FileOutputStream(mainPart, false);
            String mainJson = mapper.writeValueAsString(nn);
            mainOut.write(mainJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        save(nn.layers);

    }

    private void save(Layer[] layers){
        File layersDir = new File(saveDir+"/layers");
        if (!layersDir.exists())
            layersDir.mkdir();
        for (int i = 0; i< layers.length; i++){
            File save = new File(layersDir.getAbsolutePath() +"/"+i);
            if (!save.exists()) {
                try {
                    save.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try(FileOutputStream out = new FileOutputStream(save)) {
                String json = mapper.writeValueAsString(layers[i]);
                out.write(json.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
