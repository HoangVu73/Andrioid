package com.example.quanlynhansu.Items;

import java.util.HashMap;
import java.util.Map;

public class Positions {
    private String id;
    private String positionsName;

    // Map để lưu trữ danh sách các chức vụ
    private static Map<String, String> positionsMap = new HashMap<>();

    public Positions() {}

    public Positions(String id, String name) {
        this.id = id;
        this.positionsName = name;
        positionsMap.put(id, name);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return positionsName;
    }

    public void setName(String name) {
        this.positionsName = name;
    }

    public static String getPositionName(String id) {
        return positionsMap.get(id);
    }
}
