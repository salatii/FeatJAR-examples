package de.featjar.examples.anlysis;

import de.featjar.base.cli.Commands;
import de.featjar.base.computation.ComputePresence;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.formula.analysis.bool.BooleanSolution;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentationOfCNFFormula;
import de.featjar.formula.analysis.sat4j.ComputeSolutionSAT4J;
import de.featjar.formula.analysis.value.ValueAssignment;
import de.featjar.formula.io.FormulaFormats;
import de.featjar.formula.structure.formula.IFormula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Scanner;

import static de.featjar.base.computation.Computations.*;

public class SimpleExample {
    protected static final ExtensionManager extensionManager = new ExtensionManager();

    public static void main(String[] args) {
        // load formula (cnf) of choosen featuremodel
        File file = new File(SimpleExample.class.getClassLoader().getResource("featuremodels/basic.xml").getPath());
        IFormula formula = Commands.loadFile(file.getPath(), extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
        String config = loadConfiguration(SimpleExample.class.getClassLoader().getResource("featuremodels/basic.txt").getPath());
        ValueAssignment assignment = parseConfig(config);
        // transform formula into boolean representation for further calculations
        var booleanRepresentation = async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new);
        // get boolean clause list from boolean representation
        var booleanClauseList = getKey(booleanRepresentation);
        // create HasSolution analyse with boolean clause list
        var analysis = new ComputeSolutionSAT4J(booleanClauseList);
        // set assignment for partial config
        var variableMap = getValue(booleanRepresentation);
        analysis.setAssumedAssignment(assignment.toBoolean(variableMap));
        // compute result of analyze
        ComputePresence<BooleanSolution> result = new ComputePresence<>(analysis);
        System.out.println(!result.computeResult().get());
    }

    public static String loadConfiguration(String filepath) {
        File file = new File(filepath);
        String result = "";
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine())
                result += sc.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        result = result.replaceAll(" ", "");
        return result;
    }

    static public ValueAssignment parseConfig(String config) {
        String[] configArr = config.split(",");
        LinkedHashMap<String, Object> variableValuePairs = new LinkedHashMap<>();
        Arrays.stream(configArr).forEach(entry -> {
            if(entry.charAt(0) == '-') {
                variableValuePairs.put(entry.substring(1), false);
            } else {
                variableValuePairs.put(entry, true);
            }
        });
        return new ValueAssignment(variableValuePairs);
    }
}
