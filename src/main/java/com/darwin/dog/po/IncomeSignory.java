package com.darwin.dog.po;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class IncomeSignory extends Signory {
}
