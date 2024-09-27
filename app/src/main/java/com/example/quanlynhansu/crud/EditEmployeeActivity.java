package com.example.quanlynhansu.crud;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhansu.Items.Departments;
import com.example.quanlynhansu.Items.Employees;
import com.example.quanlynhansu.Items.Positions;
import com.example.quanlynhansu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditEmployeeActivity extends AppCompatActivity {

    private Button btnSaveEmployee;
    private Button btnShowDepartments;
    private Button btnShowPositions;
    private TextInputEditText etFirstName, etLastName, etHireDate;

    private String employeeId;
    private String departmentID;
    private String positionID;

    private List<Departments> departmentList = new ArrayList<>();
    private List<Positions> positionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee); // Đảm bảo rằng layout là activity_edit_employee

        // Ánh xạ các thành phần trong layout
        btnSaveEmployee = findViewById(R.id.btn_save_employee);
        btnShowDepartments = findViewById(R.id.btn_select_department);
        btnShowPositions = findViewById(R.id.btn_select_position);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etHireDate = findViewById(R.id.et_hire_date); // Cập nhật thành phần hire date

        // Nhận thông tin nhân viên từ Intent
        Intent intent = getIntent();
        employeeId = intent.getStringExtra("employeeId");

        if (employeeId == null) {
            Toast.makeText(this, "Không tìm thấy ID nhân viên", Toast.LENGTH_SHORT).show();
            finish(); // Thoát Activity nếu không có employeeId
            return;
        }

        loadEmployeeData(employeeId);

        // Xử lý lưu thông tin nhân viên
        btnSaveEmployee.setOnClickListener(v -> saveEmployeeData());

        // Xử lý hiện danh sách phòng ban
        btnShowDepartments.setOnClickListener(v -> loadDepartments());

        // Xử lý hiện danh sách chức vụ
        btnShowPositions.setOnClickListener(v -> loadPositions());
    }

    private void loadEmployeeData(String employeeId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(employeeId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Employees employee = snapshot.getValue(Employees.class);
                    if (employee != null) {
                        // Thiết lập thông tin nhân viên
                        etFirstName.setText(employee.getFirstName());
                        etLastName.setText(employee.getLastName());
                        etHireDate.setText(employee.getHireDate()); // Cập nhật hire date
                        departmentID = employee.getDepartmentID();
                        positionID = employee.getPositionID();
                    }
                } else {
                    Toast.makeText(EditEmployeeActivity.this, "Không tìm thấy thông tin nhân viên", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditEmployeeActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDepartments() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Departments");
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                departmentList.clear();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Departments department = snapshot.getValue(Departments.class);
                    if (department != null) {
                        department.setId(snapshot.getKey());
                        departmentList.add(department);
                    }
                }
                // Hiển thị danh sách phòng ban
                displayDepartmentList();
            } else {
                Toast.makeText(this, "Lỗi khi tải danh sách phòng ban", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDepartmentList() {
        String[] departmentNames = new String[departmentList.size()];
        for (int i = 0; i < departmentList.size(); i++) {
            departmentNames[i] = departmentList.get(i).getDepartmentName(); // Giả sử có phương thức getDepartmentName() trong lớp Departments
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn phòng ban")
                .setItems(departmentNames, (dialog, which) -> {
                    departmentID = departmentList.get(which).getId();
                    Toast.makeText(this, "Đã chọn phòng ban: " + departmentNames[which], Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void loadPositions() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Positions");
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                positionList.clear();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Positions position = snapshot.getValue(Positions.class);
                    if (position != null) {
                        position.setId(snapshot.getKey());
                        positionList.add(position);
                    }
                }
                // Hiển thị danh sách chức vụ
                displayPositionList();
            } else {
                Toast.makeText(this, "Lỗi khi tải danh sách chức vụ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayPositionList() {
        String[] positionNames = new String[positionList.size()];
        for (int i = 0; i < positionList.size(); i++) {
            positionNames[i] = positionList.get(i).getName(); // Giả sử có phương thức getName() trong lớp Positions
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn chức vụ")
                .setItems(positionNames, (dialog, which) -> {
                    positionID = positionList.get(which).getId();
                    Toast.makeText(this, "Đã chọn chức vụ: " + positionNames[which], Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void saveEmployeeData() {
        // Tạo đối tượng nhân viên mới với thông tin đã được cập nhật
        Employees updatedEmployee = new Employees();
        updatedEmployee.setId(employeeId);
        updatedEmployee.setFirstName(etFirstName.getText().toString().trim());
        updatedEmployee.setLastName(etLastName.getText().toString().trim());
        updatedEmployee.setDepartmentID(departmentID); // Sử dụng ID phòng ban đã chọn
        updatedEmployee.setPositionID(positionID); // Sử dụng ID chức vụ đã chọn
        updatedEmployee.setHireDate(etHireDate.getText().toString().trim()); // Lưu ngày tuyển dụng

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(employeeId);
        databaseReference.setValue(updatedEmployee).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditEmployeeActivity.this, "Cập nhật thông tin nhân viên thành công", Toast.LENGTH_SHORT).show();
                finish(); // Kết thúc Activity
            } else {
                Toast.makeText(EditEmployeeActivity.this, "Lỗi khi cập nhật thông tin nhân viên", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
