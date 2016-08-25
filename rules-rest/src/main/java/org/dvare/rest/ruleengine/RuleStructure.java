package org.dvare.rest.ruleengine;


import org.dvare.binding.data.DataRow;
import org.dvare.binding.model.TypeBinding;

public class RuleStructure implements Comparable<RuleStructure> {
    public String ruleId;
    public String name;
    public Integer priority;
    public String rule;
    public TypeBinding typeBinding;
    public DataRow dataRow;


    @Override
    public int compareTo(RuleStructure other) {
        return this.priority.compareTo(other.priority);
    }

}
