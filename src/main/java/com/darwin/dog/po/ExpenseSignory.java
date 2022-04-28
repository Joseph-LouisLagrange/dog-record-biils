package com.darwin.dog.po;

import com.darwin.dog.constant.BillType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("0")
public class ExpenseSignory extends Signory {
}
