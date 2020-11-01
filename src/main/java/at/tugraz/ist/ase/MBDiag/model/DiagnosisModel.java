package at.tugraz.ist.ase.MBDiag.model;

import org.apache.commons.collections4.SetUtils;
import org.chocosolver.solver.constraints.Constraint;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

//import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.COUNTER_UNION_OPERATOR;
//import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.incrementCounter;

/**
 * Contains the knowledge base for constraint problems.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public abstract class DiagnosisModel {

    private String inputFile;
    private String name;

    /**
     * The set of constraints which we assume to be always correct (background knowledge)
     */
    private Set<Constraint> correctConstraints = new LinkedHashSet<>();

    /**
     * The set of constraints which could be faulty = KB (knowledge base).
     */
    private Set<Constraint> possiblyFaultyConstraints = new LinkedHashSet<>();

    /**
     * Creates an empty diagnosis model.
     */
    public DiagnosisModel(String name, String inputFile) {
        this.name = name;
        this.inputFile = inputFile;
    }

    public String getName() {
        return name;
    }

    public String getInputFile() {
        return inputFile;
    }

    /**
     * Getter for the correct constraints (or background knowledge).
     *
     * @return
     */
    public Set<Constraint> getCorrectConstraints() {
        return correctConstraints;
    }

    /**
     * Sets the correct constraints (or background knowledge).
     *
     * @param correctConstraints
     */
    public void setCorrectConstraints(Collection<Constraint> correctConstraints) {
        this.correctConstraints = new LinkedHashSet<>(correctConstraints);
    }

    /**
     * Getter for the possibly faulty constraints (or knowledge base).
     *
     * @return
     */
    public Set<Constraint> getPossiblyFaultyConstraints() {
        return possiblyFaultyConstraints;
    }

    /**
     * Setter for the possibly faulty constraints (or knowledge base).
     *
     * @param possiblyFaultyConstraints
     */
    public void setPossiblyFaultyConstraints(Collection<Constraint> possiblyFaultyConstraints) {
        this.possiblyFaultyConstraints = new LinkedHashSet<>(possiblyFaultyConstraints);
    }

    /**
     * Getter for all constraints.
     *
     * @return
     */
    public Set<Constraint> getAllConstraints() {
//        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(correctConstraints, possiblyFaultyConstraints);
    }

    /**
     * Call this method to load the constraints for the model.
     * Override this method to add all variables and constraints.
     */
    public abstract void initialize();

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DiagnosisModel{");
        sb.append('\n').append(" Name=").append(this.name);
        sb.append('\n').append(',').append(correctConstraints.size()).append(" correctConstraints=").append(correctConstraints);
        sb.append('\n').append(',').append(possiblyFaultyConstraints.size()).append(" possiblyFaultyConstraints=").append(possiblyFaultyConstraints);
        sb.append('}');
        return sb.toString();
    }
}
