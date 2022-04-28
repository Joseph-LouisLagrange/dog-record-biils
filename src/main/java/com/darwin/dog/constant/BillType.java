package com.darwin.dog.constant;

import com.darwin.dog.po.Bill;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BillType {
    EXPENSE(0),INCOME(1);
    @JsonValue
    private int code;
    BillType(int code){
        this.code = code;
    }
    @JsonCreator
    public static BillType valueOf(int code){
        for (BillType value : BillType.values()) {
            if(value.code == code){
                return value;
            }
        }
        return null;
    }
}
