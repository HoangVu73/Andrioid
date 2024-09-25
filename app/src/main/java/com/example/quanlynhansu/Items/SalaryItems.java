package com.example.quanlynhansu.Items;

import android.os.Parcel;
import android.os.Parcelable;

public class SalaryItems implements Parcelable {
    private String id;
    private String employeeId;
    private String name;
    private double basicSalary;
    private double allowance;
    private double tax;
    private double insurance;
    private String salary; // Lương thực tính, dưới dạng chuỗi đã định dạng

    // Constructor mặc định
    public SalaryItems() {
    }

    // Constructor với tham số
    public SalaryItems(String id, String employeeId, String name, double basicSalary, double allowance, double tax, double insurance, String salary) {
        this.id = id;
        this.employeeId = employeeId;
        this.name = name;
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.tax = tax;
        this.insurance = insurance;
        this.salary = salary;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    // Implement Parcelable
    protected SalaryItems(Parcel in) {
        id = in.readString();
        employeeId = in.readString();
        name = in.readString();
        basicSalary = in.readDouble();
        allowance = in.readDouble();
        tax = in.readDouble();
        insurance = in.readDouble();
        salary = in.readString();
    }

    public static final Creator<SalaryItems> CREATOR = new Creator<SalaryItems>() {
        @Override
        public SalaryItems createFromParcel(Parcel in) {
            return new SalaryItems(in);
        }

        @Override
        public SalaryItems[] newArray(int size) {
            return new SalaryItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(employeeId);
        dest.writeString(name);
        dest.writeDouble(basicSalary);
        dest.writeDouble(allowance);
        dest.writeDouble(tax);
        dest.writeDouble(insurance);
        dest.writeString(salary);
    }
}
