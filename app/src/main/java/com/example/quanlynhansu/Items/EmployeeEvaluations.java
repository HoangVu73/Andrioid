package com.example.quanlynhansu.Items;

public class EmployeeEvaluations {

    private String evaluationId;
    private String employeeId;
    private String evaluationText;
    private int score;
    private String date;

    public EmployeeEvaluations() {
        // Firebase yêu cầu constructor mặc định
    }

    public EmployeeEvaluations(String evaluationId, String employeeId, String evaluationText, int score, String date) {
        this.evaluationId = evaluationId;
        this.employeeId = employeeId;
        this.evaluationText = evaluationText;
        this.score = score;
        this.date = date;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEvaluationText() {
        return evaluationText;
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }
}
