package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private String customer_ref;
    private BigInteger invoice_no;
    private String invoice_type;
    private String settlement_method;
    private String station;
    private String date;
    private String billing_month;
    private String billing_category;
    private int settlement_period;
    private List<Item> items;
}
