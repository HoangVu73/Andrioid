package com.example.quanlynhansu.Items;

public class SalaryItems {
    private String id;
    private String name;
    private double basicSalary;
    private double allowance;
    private double tax;
    private double insurance;
    private String salary; // Lương thực tính, dưới dạng chuỗi đã định dạng

    // Constructor
    public SalaryItems(String id, String name, double basicSalary, double allowance, double tax, double insurance, String salary) {
        this.id = id;
        this.name = name;
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.tax = tax;
        this.insurance = insurance;
        this.salary = salary;
    }

    // Getters and Setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getInsurance() {
        return insurance;
    }

    public void setInsurance(double insurance) {
        this.insurance = insurance;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}

