package de.featjar.examples.anlysis.ComplexExample;

import de.featjar.base.computation.Computations;
import de.featjar.base.computation.ComputePresence;
import de.featjar.formula.analysis.bool.BooleanSolution;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentationOfCNFFormula;
import de.featjar.formula.analysis.sat4j.ComputeSolutionSAT4J;
import de.featjar.formula.structure.formula.IFormula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;

public class IsVoidAnalysis {
    public boolean isVoid(IFormula formula) {
        boolean isvoid = !Computations.async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new)
                .map(Computations::getKey)
                .map(ComputeSolutionSAT4J::new)
                .map(ComputePresence<BooleanSolution>::new)
                .get()
                .get();
        return isvoid;
    }
}
