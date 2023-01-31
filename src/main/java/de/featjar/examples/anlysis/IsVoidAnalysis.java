package de.featjar.examples.anlysis;

import de.featjar.base.cli.CommandLineInterface;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.formula.analysis.bool.BooleanAssignment;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentation;
import de.featjar.formula.analysis.sat4j.AnalyzeHasSolutionSAT4J;
import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;

import static de.featjar.base.data.Computations.async;
import static de.featjar.base.data.Computations.getKey;

public class IsVoidAnalysis {
    public boolean isVoid(Formula formula) {
        var booleanRepresentation =
                async(formula)
                        .map(ComputeNNFFormula::new)
                        .map(ComputeCNFFormula::new)
                        .map(ComputeBooleanRepresentation.OfFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var result = new AnalyzeHasSolutionSAT4J().setInput(booleanClauseList);
        return !result.compute().get().get();
    }
}
