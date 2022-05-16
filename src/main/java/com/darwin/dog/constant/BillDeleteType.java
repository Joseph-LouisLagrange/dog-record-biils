package com.darwin.dog.constant;

public interface BillDeleteType {
    int NO_DELETE = 0,
            BILL_DELETE = 1,
            LEDGER_DELETE = 1<<1,
            ACCOUNT_DELETE = 1<<2;
}
