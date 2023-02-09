package de.featjar.examples.anlysis.ComplexExample;

import de.featjar.base.data.Computation;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentation;
import de.featjar.formula.analysis.sat4j.AnalyzeCoreDeadVariablesSAT4J;
import de.featjar.formula.analysis.value.ComputeValueRepresentation;
import de.featjar.formula.analysis.value.ValueAssignment;
import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;

import static de.featjar.base.data.Computations.*;
import static de.featjar.base.data.Computations.async;

public class CoreDeadAnalysis {
    public String coreFeatures(Formula formula) {
        // get formula as boolean representation
        var booleanRepresentation =
                async(formula)
                        .map(ComputeNNFFormula::new)
                        .map(ComputeCNFFormula::new)
                        .map(ComputeBooleanRepresentation.OfFormula::new);
        // get boolean Clause list from boolean representation
        var booleanClauseList = getKey(booleanRepresentation);
        // get variable map
        var variableMap = getValue(booleanRepresentation);
        // create analysis
        var result = new AnalyzeCoreDeadVariablesSAT4J().setInput(booleanClauseList);
        //  parse result
        Computation<ValueAssignment> assignmentComputation = async(result, variableMap).map(ComputeValueRepresentation.OfAssignment::new);
        return assignmentComputation.compute().get().get().print();
    }
}
