package com.example.quanlynhansu.Items;

public class EmployeeEvaluations {
    private String employeeID;
    private String evaluationDate;
    private int rating;
    private String comments;

    public EmployeeEvaluations() { }

    public EmployeeEvaluations(String employeeID, String evaluationDate, int rating, String comments) {
        this.employeeID = employeeID;
        this.evaluationDate = evaluationDate;
        this.rating = rating;
        this.comments = comments;
    }

    // Getters and setters
    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(String evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEmployeeName() {
        return EmployeesManager.getEmployeeName(employeeID);
    }
}
