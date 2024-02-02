package su.fedin.nn.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TrainInput {
    private String name;
    private String pathToFolder = "./dataset";

    private double[][] input;
    private double[][] target;

    public TrainInput(String name, String path) {
        this.name = name;
        this.pathToFolder = path;
    }

    public TrainInput(String name) {
        this.name = name;
    }

    public void normalizeData(){
        normalizeData(input);
        normalizeData(target);
    }

    private void normalizeData(double[][] data){
        for (int j = 0; j < data[0].length; j++){

            double max = data[0][j];
            for (int i = 1; i < data.length; i++)
                if(max < data[i][j])
                    max = data[i][j];

            for (int i = 1; i < data.length; i++)
                data[i][j] = data[i][j] / max;
        }
    }

    public void loadData(){
        File dir = new File(pathToFolder);
        if (!dir.isDirectory())
            throw new IllegalStateException("Path is not folder");

        File[] files = dir.listFiles(pathname -> {
            String fname = pathname.getName();
            return (name + ".input").equals(fname) || (name + ".target").equals(fname);
        });

        assert files != null;
        if (!(files.length == 2))
            throw new IllegalStateException("Folder not have correct data set");

        if((name + ".input").equals(files[0].getName())){
            input = loadData(files[0]);
            target = loadData(files[1]);
        }
        else{
            input = loadData(files[1]);
            target = loadData(files[0]);
        }
    }

    private double[][] loadData(File file){
        double[][] res;
        try {
            Scanner sc = new Scanner(file);
            String line = sc.nextLine();
           int dataCount = Integer.parseInt(line.split("\t")[0]);
            int dataSize = Integer.parseInt(line.split("\t")[1]);

            res = new double[dataCount][dataSize];

            for (int i = 0; i < res.length; i++){
                if (!sc.hasNext())
                    throw new IllegalStateException(file.getName() + " have uncorrected data format");

                String[] val = sc.nextLine().split("\t");
                if(val.length < dataSize)
                    throw new IllegalStateException(file.getName() + " have uncorrected data format");

                for (int j = 0; j < dataSize; j++)
                    res[i][j] = Double.parseDouble(val[j]);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
