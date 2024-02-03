package su.fedin.nn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Tracer {
    private static final String HEADER = "EPOCH\tRIGHTS\tERROR\n";
    private static final String LINE_FORMAT= "%d\t%d\t%f\n";

    private static Map<String, Tracer> pool = new HashMap<>();

    static Tracer getTracer(NeuralNetwork nn){
        Tracer tracer = pool.get(nn.name);
        if(tracer == null){
            tracer = new Tracer(nn.name);
            pool.put(nn.name, tracer);
        }
        return tracer;
    }

    private String path;
    private String name;
    private File traceFile;
    private FileOutputStream out;

    Tracer(String path, String name) {
        this.path = path;
        this.name = name;

        traceFile = new File(path + "/" + name + ".trace");

        File traceDir = new File(path);
        if (!traceDir.exists())
            traceDir.mkdir();

        if (!traceFile.exists()) {
            try {
                traceFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            out = new FileOutputStream(traceFile, false);
            out.write(HEADER.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    Tracer(String name) {
        this("./trace", name);
    }

    void trace(int epoch, int right, double error){
        String line = String.format(LINE_FORMAT, epoch, right, error);
        try {
            out = new FileOutputStream(traceFile, true);
            out.write(line.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
