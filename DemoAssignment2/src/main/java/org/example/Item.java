package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String customer_name;
    private String customer_details;
    private String vendor_name;
    private String vendor_details;
}
