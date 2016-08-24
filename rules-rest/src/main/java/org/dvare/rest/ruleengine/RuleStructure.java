package org.dvare.rest.ruleengine;


class RuleStructure implements Comparable<RuleStructure> {
    String ruleId;
    Integer priority;
    String rule;
    Object model;


    @Override
    public int compareTo(RuleStructure other) {
        return this.priority.compareTo(other.priority);
    }
}
