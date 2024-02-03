package su.fedin.nn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Tracer {
    private static final String HEADER = "EPOCH\tRIGHTS\tERROR\n";
    private static final String LINE_FORMAT= "%d\t%d\t%f\n";
    private String path;
    private String name;
    private File traceFile;

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
            FileOutputStream out = new FileOutputStream(traceFile, false);
            out.write(HEADER.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Tracer(String name) {
        this("./trace", name);
    }

    void trace(int epoch, int right, double error){
        String line = String.format(LINE_FORMAT, epoch, right, error);
        try {
            FileOutputStream out = new FileOutputStream(traceFile, true);
            out.write(line.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
