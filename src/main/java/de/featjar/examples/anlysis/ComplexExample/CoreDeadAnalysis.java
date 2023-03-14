package de.featjar.examples.anlysis.ComplexExample;

import de.featjar.formula.analysis.bool.ComputeBooleanRepresentationOfCNFFormula;
import de.featjar.formula.analysis.sat4j.ComputeCoreDeadVariablesSAT4J;
import de.featjar.formula.analysis.value.ComputeValueRepresentationOfAssignment;
import de.featjar.formula.analysis.value.ValueAssignment;
import de.featjar.formula.structure.formula.IFormula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static de.featjar.base.computation.Computations.*;

public class CoreDeadAnalysis {
    public Set<String> coreFeatures(IFormula formula) {
        var booleanRepresentation = async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var variableMap = getValue(booleanRepresentation);
        var analysis = new ComputeCoreDeadVariablesSAT4J(booleanClauseList);

        //  parse result
        ComputeValueRepresentationOfAssignment result = new ComputeValueRepresentationOfAssignment(analysis, variableMap);
        String core = result.computeResult().get().print();
        String[] coreArr = core.split(", ");
        Set<String> resultCore = new HashSet<>();
        Arrays.stream(coreArr).forEach(feature -> {
            if(!(feature.charAt(0)=='-')) {
                resultCore.add(feature);
            }
        });
        return resultCore;
    }
}
