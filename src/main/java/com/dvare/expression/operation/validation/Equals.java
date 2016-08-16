package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;

@Operation(type = OperationType.VALIDATION, symbols = {"eq", "="})
public class Equals extends EqualityOperationExpression {
    public Equals() {
        super("eq", "=");
    }

    public com.dvare.expression.operation.validation.Equals copy() {
        return new com.dvare.expression.operation.validation.Equals();
    }

}