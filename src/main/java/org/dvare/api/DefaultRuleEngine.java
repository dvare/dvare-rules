/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/

package org.dvare.api;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface DefaultRuleEngine {

    boolean triggerConditions(List<ListenerStructure> beforeMethods, List<ListenerStructure> conditions,
                              List<ListenerStructure> afterMethods, Object rule, Facts facts)
            throws IllegalAccessException, InvocationTargetException;


    void triggerBeforeListeners(List<ListenerStructure> beforeMethods, Object rule)
            throws IllegalAccessException, InvocationTargetException;

    void triggerAfterListeners(List<ListenerStructure> afterMethods, Object rule)
            throws IllegalAccessException, InvocationTargetException;

    InstancesBinding triggerAggregation(ListenerStructure methodStructure, Object rule, Facts facts)
            throws IllegalAccessException, InvocationTargetException, InterpretException;

    void triggerListeners(RuleStructure rule, Boolean result)
            throws IllegalAccessException, InvocationTargetException;
}
