package de.featjar.examples.anlysis;

import de.featjar.base.cli.Commands;
import de.featjar.base.computation.ComputePresence;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.formula.analysis.bool.BooleanSolution;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentationOfCNFFormula;
import de.featjar.formula.analysis.sat4j.ComputeSolutionSAT4J;
import de.featjar.formula.io.FormulaFormats;
import de.featjar.formula.structure.formula.IFormula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;

import java.io.File;

import static de.featjar.base.computation.Computations.*;

public class SimpleExample {
    protected static final ExtensionManager extensionManager = new ExtensionManager();

    public static void main(String[] args) {
        //TODO Value Assignment for partial config

        // load formula (cnf) of choosen featuremodel
        File file = new File(SimpleExample.class.getClassLoader().getResource("featuremodels/basic.xml").getPath());
        IFormula formula = Commands.loadFile(file.getPath(), extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();

        // transform formula into boolean representation for further calculations
        var booleanRepresentation = async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new);
        // get boolean clause list from boolean representation
        var booleanClauseList = getKey(booleanRepresentation);

        // create HasSolution analyse with boolean clause list
        var hasSolutionAnalyze = new ComputeSolutionSAT4J(booleanClauseList);
        // compute result of analyze
        ComputePresence<BooleanSolution> result = new ComputePresence<>(hasSolutionAnalyze);
        System.out.println(!result.computeResult().get());
    }
}
