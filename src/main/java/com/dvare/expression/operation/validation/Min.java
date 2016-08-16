package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"Min", "min"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Min extends ArithmeticOperationExpression {
    public Min() {
        super("Min", "min");
    }

    public com.dvare.expression.operation.validation.Min copy() {
        return new com.dvare.expression.operation.validation.Min();
    }

}