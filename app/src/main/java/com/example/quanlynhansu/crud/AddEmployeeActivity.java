package com.example.quanlynhansu.crud;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhansu.Items.Departments;
import com.example.quanlynhansu.Items.Employees;
import com.example.quanlynhansu.Items.Positions;
import com.example.quanlynhansu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEmployeeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText etFirstName, etLastName;
    private Button btnSelectDepartment, btnSelectPosition, btnChooseImage, btnSaveEmployee, btnHireDate;
    private ImageView ivImage;
    private List<Departments> departmentList = new ArrayList<>();
    private List<Positions> positionList = new ArrayList<>();
    private String[] departments, positions;
    private Uri imageUri;
    private int selectedDepartmentIndex = -1;
    private int selectedPositionIndex = -1;
    private String hireDate;
    private TextInputEditText etEmployeeCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());

        initViews();
        loadDepartments();
        loadPositions();

        setOnClickListeners();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmployeeCode = findViewById(R.id.et_employee_code);
        btnSelectDepartment = findViewById(R.id.btn_select_department);
        btnSelectPosition = findViewById(R.id.btn_select_position);
        btnChooseImage = findViewById(R.id.btn_choose_image);
        ivImage = findViewById(R.id.iv_image);
        btnSaveEmployee = findViewById(R.id.btn_save_employee);
        btnHireDate = findViewById(R.id.btn_hire_date);
    }

    private void setOnClickListeners() {
        btnSelectDepartment.setOnClickListener(v -> showSelectionDialog(departments, "Chọn Phòng Ban", selected -> {
            selectedDepartmentIndex = selected;
            btnSelectDepartment.setText(departmentList.get(selected).getDepartmentName());
        }));

        btnSelectPosition.setOnClickListener(v -> showSelectionDialog(positions, "Chọn Chức Vụ", selected -> {
            selectedPositionIndex = selected;
            btnSelectPosition.setText(positionList.get(selected).getName());
        }));

        btnChooseImage.setOnClickListener(v -> openFileChooser());

        btnHireDate.setOnClickListener(v -> showDatePickerDialog());

        btnSaveEmployee.setOnClickListener(v -> saveEmployee());
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
                departments = departmentList.stream().map(Departments::getDepartmentName).toArray(String[]::new);
            } else {
                Toast.makeText(this, "Lỗi khi tải danh sách phòng ban", Toast.LENGTH_SHORT).show();
            }
        });
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
                positions = positionList.stream().map(Positions::getName).toArray(String[]::new);
            } else {
                Toast.makeText(this, "Lỗi khi tải danh sách chức vụ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSelectionDialog(String[] items, String title, SelectionCallback callback) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(items, (dialog, which) -> callback.onItemSelected(which))
                .show();
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            hireDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            btnHireDate.setText(hireDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveEmployee() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String employeeCode = etEmployeeCode.getText().toString(); // Lấy mã nhân viên

        if (selectedDepartmentIndex == -1 || selectedPositionIndex == -1 || firstName.isEmpty() || lastName.isEmpty() || hireDate == null || imageUri == null || employeeCode.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String departmentId = departmentList.get(selectedDepartmentIndex).getId();
        String positionId = positionList.get(selectedPositionIndex).getId();

        // Tạo đối tượng Employees với employeeCode làm ID
        Employees employee = new Employees(employeeCode, firstName, lastName, departmentId, positionId, hireDate, imageUri.toString());

        // Kiểm tra xem employeeCode có tồn tại trong Firebase hay không
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employees");
        databaseReference.child(employeeCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(AddEmployeeActivity.this, "Mã nhân viên đã tồn tại. Vui lòng sử dụng mã khác.", Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu mã không tồn tại, lưu nhân viên
                    databaseReference.child(employeeCode).setValue(employee)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AddEmployeeActivity.this, "Nhân viên đã được lưu", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(AddEmployeeActivity.this, "Lỗi khi lưu nhân viên", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddEmployeeActivity.this, "Lỗi khi kiểm tra mã nhân viên", Toast.LENGTH_SHORT).show();
            }
        });
    }


    interface SelectionCallback {
        void onItemSelected(int selectedId);
    }
}
