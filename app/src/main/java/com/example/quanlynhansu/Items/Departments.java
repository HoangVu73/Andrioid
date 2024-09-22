package com.example.quanlynhansu.Items;

import java.util.HashMap;
import java.util.Map;

public class Departments {
    private String id;
    private String departmentName;

    // Map để lưu trữ danh sách các phòng ban
    private static Map<String, String> departmentsMap = new HashMap<>();

    public Departments() {}

    public Departments(String id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
        departmentsMap.put(id, departmentName);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public static String getDepartmentName(String id) {
        return departmentsMap.get(id);
    }
}
