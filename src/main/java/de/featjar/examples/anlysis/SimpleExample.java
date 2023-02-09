package de.featjar.examples.anlysis;

import de.featjar.base.cli.CommandLineInterface;
import de.featjar.base.data.Computation;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentation;
import de.featjar.formula.analysis.sat4j.AnalyzeCoreDeadVariablesSAT4J;
import de.featjar.formula.analysis.sat4j.AnalyzeHasSolutionSAT4J;
import de.featjar.formula.analysis.value.ComputeValueRepresentation;
import de.featjar.formula.analysis.value.ValueAssignment;
import de.featjar.formula.io.FormulaFormats;
import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;

import java.io.File;

import static de.featjar.base.data.Computations.*;

public class SimpleExample {
    protected static final ExtensionManager extensionManager = new ExtensionManager();

    public static void main(String[] args) {
        // load formula (cnf) of choosen featuremodel
        File file = new File(SimpleExample.class.getClassLoader().getResource("featuremodels/car.xml").getPath());
        Formula formula = CommandLineInterface.loadFile(file.getPath(), extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();

        // transform formula into boolean representation for further calculations
        var booleanRepresentation =
                async(formula)
                        .map(ComputeNNFFormula::new)
                        .map(ComputeCNFFormula::new)
                        .map(ComputeBooleanRepresentation.OfFormula::new);
        // get boolean clause list from boolean representation
        var booleanClauseList = getKey(booleanRepresentation);

        // create HasSolution analyse with boolean clause list
        var hasSolutionAnalyze = new AnalyzeHasSolutionSAT4J().setInput(booleanClauseList);
        // compute result of analyze
        System.out.println(hasSolutionAnalyze.compute().get().get());
        // to check if feature model is void -> negate the result of hasSolutionAnalyze
        System.out.println(!hasSolutionAnalyze.compute().get().get());

        // get variableMap from booleanRepresentation for CoreDeadAnalyze
        var variableMap = getValue(booleanRepresentation);
        // create CoreDeadAnalyze
        var result = new AnalyzeCoreDeadVariablesSAT4J().setInput(booleanClauseList);
        //  parse result
        Computation<ValueAssignment> assignmentComputation = async(result, variableMap).map(ComputeValueRepresentation.OfAssignment::new);
        // compute result and print
        System.out.println(assignmentComputation.compute().get().get().print());
    }
}
