package de.featjar.examples.anlysis;

import de.featjar.base.cli.CommandLineInterface;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.formula.io.FormulaFormats;
import de.featjar.formula.structure.formula.Formula;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Analysis {
    protected static final ExtensionManager extensionManager = new ExtensionManager();
    private static final List<String> MODEL_NAMES = Arrays.asList( //
            "basic.xml",
            "simple.xml",
            "car.xml",
            "test.xml"
    );

    private static Formula loadModel(String filepath) {
        return CommandLineInterface.loadFile(filepath, extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
    }

    private static String getPathFromResource(String resource) {
        File file = new File(Analysis.class.getClassLoader().getResource(resource).getPath());
        if (file == null) {
            try {
                throw new FileNotFoundException(resource);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        } else {
            return file.toPath().toString();
        }
    }

    private static String selectAnalyse(Formula formula) {
        System.out.println("Choose one of the following analysis you want to perform:");
        System.out.println("coredead, isvoid");
        String analyze = "";
        while (true) {
            Scanner scanner = new Scanner(System.in);
            analyze = scanner.nextLine();
            switch (analyze) {
                case "coredead":
                    return new CoreDeadAnalysis().coreFeatures(formula);
                case "isvoid":
                    return String.valueOf(new IsVoidAnalysis().isVoid(formula));
                default:
                    System.out.println("enter a correct analyse");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean stop = false;

        HashMap<Integer, String> featureModelMap = new HashMap<>();
        for(int i = 0; i < MODEL_NAMES.size(); i++) {
            featureModelMap.put(i+1, MODEL_NAMES.get(i));
        }

        while(!stop) {
            System.out.println("Choose which feature model you want to analyse:");
            featureModelMap.entrySet().stream().forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
            System.out.println(-1 + ": to exit the dialog");
            System.out.println("Enter one of the corresponding numbers:");

            int chosen = scanner.nextInt();
            if(featureModelMap.containsKey(chosen)) {
                String path = featureModelMap.get(chosen);
                Formula formula = loadModel(getPathFromResource(path));
                System.out.println("Formula of " + featureModelMap.get(chosen) + ": " + formula.printParseable());
                System.out.println();

                System.out.println(selectAnalyse(formula));
                return;
            } else {
                if(chosen == -1) {
                    System.out.println("exit");
                    stop = true;
                } else {
                    System.out.println("you entered a wrong number");
                }
            }
        }
    }
}
