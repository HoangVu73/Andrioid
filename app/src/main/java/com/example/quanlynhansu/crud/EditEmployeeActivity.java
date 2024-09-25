package com.example.quanlynhansu.crud;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.quanlynhansu.Items.Employees;
import com.example.quanlynhansu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditEmployeeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private ImageView ivImage;
    private TextInputEditText etFirstName;
    private TextInputEditText etLastName;
    private Button btnChooseImage;
    private Button btnSaveEmployee;

    private String employeeId;  // Biến này vẫn giữ nguyên
    private String departmentID;
    private String positionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        // Ánh xạ các thành phần trong layout
        ivImage = findViewById(R.id.iv_image);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        btnChooseImage = findViewById(R.id.btn_choose_image);
        btnSaveEmployee = findViewById(R.id.btn_save_employee);



        // Nhận thông tin nhân viên từ Intent
        Intent intent = getIntent();
        employeeId = intent.getStringExtra("employeeId");

        if (employeeId == null) {
            Toast.makeText(this, "Không tìm thấy ID nhân viên", Toast.LENGTH_SHORT).show();
            finish(); // Thoát Activity nếu không có employeeId
            return;
        }

        departmentID = intent.getStringExtra("departmentId");
        positionID = intent.getStringExtra("positionId");

        loadEmployeeData(employeeId);

        // Xử lý chọn hình ảnh
        btnChooseImage.setOnClickListener(v -> openImageChooser());

        // Xử lý lưu thông tin nhân viên
        btnSaveEmployee.setOnClickListener(v -> saveEmployeeData());

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());
    }

    // Mở bộ chọn hình ảnh
    private void openImageChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); // Đảm bảo sử dụng ACTION_OPEN_DOCUMENT
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser(); // Mở bộ chọn hình ảnh nếu quyền được cấp
            } else {
                Toast.makeText(this, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadEmployeeData(String employeeId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(employeeId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Employees employee = snapshot.getValue(Employees.class);
                    if (employee != null) {
                        etFirstName.setText(employee.getFirstName());
                        etLastName.setText(employee.getLastName());
                        departmentID = employee.getDepartmentID();
                        positionID = employee.getPositionID();

                        String imageUriString = employee.getImageUrl();
                        if (imageUriString != null) {
                            imageUri = Uri.parse(imageUriString);
                            // Thay đổi cách thiết lập URI cho ImageView
                            ivImage.setImageURI(imageUri);
                        } else {
                            ivImage.setImageDrawable(null); // Đặt lại nếu không có URI
                        }

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

    private void saveEmployeeData() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin nhân viên
        Employees updatedEmployee = new Employees(
                employeeId,
                firstName,
                lastName,
                departmentID,
                positionID,
                "",
                imageUri != null ? imageUri.toString() : null
        );

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(employeeId);
        databaseReference.setValue(updatedEmployee).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditEmployeeActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại Activity trước
            } else {
                Toast.makeText(EditEmployeeActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivImage.setImageURI(imageUri);
        }
    }
}
