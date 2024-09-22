package com.example.quanlynhansu.Items;

import java.util.HashMap;
import java.util.Map;

public class PositionManager {
    private static Map<String, Positions> positionsMap = new HashMap<>();

    // Thêm chức vụ vào map
    public static void addPosition(Positions position) {
        positionsMap.put(position.getId(), position);
    }

    public static String getPositionName(String id) {
        Positions position = positionsMap.get(id);
        return position != null ? position.getName() : "Unknown";
    }
}
