package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"gt", ">"}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType})
public class Greater extends EqualityOperationExpression {
    public Greater() {
        super("gt", ">");
    }

    public com.dvare.expression.operation.validation.Greater copy() {
        return new com.dvare.expression.operation.validation.Greater();
    }

}