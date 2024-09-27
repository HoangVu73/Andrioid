    package com.example.quanlynhansu.Items;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class Employees {
        private String id;
        private String firstName;
        private String lastName;
        private String departmentID; // ID phòng ban
        private String positionID; // ID chức vụ
        private String hireDate;
        private String imageUrl;

        // Danh sách đánh giá của nhân viên
        private List<EmployeeEvaluations> evaluations;

        // Map để lưu trữ danh sách nhân viên
        private static Map<String, Employees> employeesMap = new HashMap<>();

        // Constructor mặc định
        public Employees() {
            this.evaluations = new ArrayList<>(); // Khởi tạo danh sách đánh giá
        }

        // Constructor với tham số
        public Employees(String id, String firstName, String lastName, String departmentID, String positionID, String hireDate, String imageUrl) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.departmentID = departmentID;
            this.positionID = positionID;
            this.hireDate = hireDate;
            this.imageUrl = imageUrl;
            this.evaluations = new ArrayList<>(); // Khởi tạo danh sách đánh giá
            employeesMap.put(id, this);
        }

        // Các phương thức getter và setter
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getDepartmentID() {
            return departmentID;
        }

        public void setDepartmentID(String departmentID) {
            this.departmentID = departmentID;
        }

        public String getPositionID() {
            return positionID;
        }

        public void setPositionID(String positionID) {
            this.positionID = positionID;
        }

        public String getHireDate() {
            return hireDate;
        }

        public void setHireDate(String hireDate) {
            this.hireDate = hireDate;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        // Phương thức để lấy tên phòng ban và chức vụ
        public String getDepartmentName() {
            return DepartmentManager.getDepartmentName(departmentID);
        }

        public String getPositionName() {
            return PositionManager.getPositionName(positionID);
        }

        // Quản lý danh sách đánh giá
        public List<EmployeeEvaluations> getEvaluations() {
            return evaluations;
        }

        public void addEvaluation(EmployeeEvaluations evaluation) {
            evaluations.add(evaluation);
        }

        public void removeEvaluation(EmployeeEvaluations evaluation) {
            evaluations.remove(evaluation);
        }
    }
