package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"Add", "add", "+"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Add extends ArithmeticOperationExpression {
    public Add() {
        super("Add", "add", "+");
    }

    public com.dvare.expression.operation.validation.Add copy() {
        return new com.dvare.expression.operation.validation.Add();
    }

}