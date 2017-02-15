package com.wiley.autotest.cuanto.api;

import java.util.Arrays;
import java.util.List;

public class AnalysisState {

    public static final AnalysisState Unanalyzed = new AnalysisState("Unanalyzed");
    public static final AnalysisState Bug = new AnalysisState("Bug");
    public static final AnalysisState Environment = new AnalysisState("Environment");
    public static final AnalysisState Harness = new AnalysisState("Harness");
    public static final AnalysisState NoRepro = new AnalysisState("No Repro");
    public static final AnalysisState Other = new AnalysisState("Other");
    public static final AnalysisState TestBug = new AnalysisState("Test Bug");
    public static final AnalysisState Investigate = new AnalysisState("Investigate");
    private final String analysisState;

    AnalysisState(String stateName) {
        this.analysisState = stateName;
    }

    public String toString() {
        return this.analysisState;
    }

    /**
     * Creates an AnalysisState for this state name. This probably isn't what you want -- you should favor the static
     * AnalysisState members on this class or use the valueOf() method. This method is here for the rare case when a
     * Cuanto server has non-default AnalysisStates that aren't named the same as the static members on this class. If
     * you specify an AnalysisState that doesn't exist, you will get an error when you attempt to create a TestOutcome
     * with that AnalysisState. Consider yourself warned.
     *
     * @param stateName The custom analysis state name -- should match an AnalysisState on the server.
     * @return An AnalysisState corresponding to the specified name.
     */
    public static AnalysisState forState(String stateName) {
        return new AnalysisState(stateName);
    }

    /**
     * Creates an AnalysisState for this state name. This only works for the static values defined on this AnalysisState
     * class, not custom AnalysisStates -- see forState() if you wish to use custom AnalysisStates.
     *
     * @param stateName The analysis state name to use.
     * @return The AnalysisState corresponding to the stateName.
     * @throws IllegalArgumentException If the AnalysisState is not one of the defaults available.
     */
    public static AnalysisState valueOf(String stateName) throws IllegalArgumentException {
        for (AnalysisState state : getAnalysisStateList()) {
            if (state.toString().equalsIgnoreCase(stateName)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unrecognized AnalysisState: " + stateName);
    }

    /**
     * Gets a List of the default AnalysisStates available with Cuanto.
     *
     * @return A List of the known defaults for AnalysisStates.
     */
    public static List<AnalysisState> getAnalysisStateList() {
        return Arrays.asList(Unanalyzed, Bug, Environment, Harness, NoRepro, Other, TestBug, Investigate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnalysisState that = (AnalysisState) o;
        return !(analysisState != null ? !analysisState.equals(that.analysisState) : that.analysisState != null);
    }

    @Override
    public int hashCode() {
        return analysisState != null ? analysisState.hashCode() : 0;
    }
}
