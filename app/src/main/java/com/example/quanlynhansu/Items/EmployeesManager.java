package com.example.quanlynhansu.Items;

import java.util.HashMap;
import java.util.Map;

public class EmployeesManager {
    private static Map<String, Employees> employeesMap = new HashMap<>();

    // Thêm nhân viên vào danh sách
    public static void addEmployee(Employees employee) {
        employeesMap.put(employee.getId(), employee);
    }

    // Xóa nhân viên theo ID
    public static void removeEmployee(String id) {
        employeesMap.remove(id);
    }

    // Lấy nhân viên theo ID
    public static Employees getEmployee(String id) {
        return employeesMap.get(id);
    }

    // Lấy tất cả nhân viên
    public static Map<String, Employees> getAllEmployees() {
        return new HashMap<>(employeesMap);
    }

    // Lấy tên nhân viên theo ID
    public static String getEmployeeName(String employeeID) {
        Employees employee = getEmployee(employeeID);
        if (employee != null) {
            return employee.getFirstName() + " " + employee.getLastName();
        }
        return "Nhân viên không tồn tại";
    }
}
