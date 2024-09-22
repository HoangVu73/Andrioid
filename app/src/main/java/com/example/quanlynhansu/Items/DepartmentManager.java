package com.example.quanlynhansu.Items;

import java.util.HashMap;
import java.util.Map;

public class DepartmentManager {
    private static Map<String, Departments> departmentsMap = new HashMap<>();

    // Thêm phòng ban vào map
    public static void addDepartment(Departments department) {
        departmentsMap.put(department.getId(), department);
    }

    public static String getDepartmentName(String id) {
        Departments department = departmentsMap.get(id);
        return department != null ? department.getDepartmentName() : "Unknown";
    }
}
