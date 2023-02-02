package de.featjar.examples.anlysis;

import de.featjar.base.cli.CommandLineInterface;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.formula.io.FormulaFormats;
import de.featjar.formula.structure.formula.Formula;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Analysis {
    protected static final ExtensionManager extensionManager = new ExtensionManager();
    private static Set<String> MODEL_NAMES = new HashSet<>();

    private static Formula loadModel(String filepath) {
        return CommandLineInterface.loadFile(filepath, extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
    }

    private static String getPathFromResource(String resource) {
        resource = "featuremodels/" + resource;
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
            analyze = scanner.nextLine().toLowerCase().trim();
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

    public static Set<String> listFiles(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        File file = new File(Analysis.class.getClassLoader().getResource("featuremodels/").getFile());
        MODEL_NAMES = listFiles(file.getAbsolutePath());
        Scanner scanner = new Scanner(System.in);
        boolean stop = false;

        HashMap<Integer, String> featureModelMap = new HashMap<>();
        int i = 1;
        for (String entry : MODEL_NAMES) {
            featureModelMap.put(i, entry);
            i++;
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
