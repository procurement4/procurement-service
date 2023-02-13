package com.procurement.apps.model;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String category;
    private int stock;
    private UUID user_id;
}
