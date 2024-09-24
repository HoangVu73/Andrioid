package com.example.quanlynhansu.crud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhansu.Items.Employees;
import com.example.quanlynhansu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditEmployeeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private ImageView ivImage;
    private TextInputEditText etFirstName;
    private TextInputEditText etLastName;
    private Button btnChooseImage;
    private Button btnSaveEmployee;

    private String employeeId;
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
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        departmentID = intent.getStringExtra("departmentId");
        positionID = intent.getStringExtra("positionId");
        String hireDate = intent.getStringExtra("hireDate");
        String imageUriString = intent.getStringExtra("imageUri");

        // Hiển thị thông tin nhân viên
        if (firstName != null) {
            etFirstName.setText(firstName);
        }
        if (lastName != null) {
            etLastName.setText(lastName);
        }

        // Thiết lập imageUri nếu có
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            ivImage.setImageURI(imageUri); // Cập nhật hình ảnh
        }

        // Xử lý chọn hình ảnh
        btnChooseImage.setOnClickListener(v -> openImageChooser());

        // Xử lý lưu thông tin nhân viên
        btnSaveEmployee.setOnClickListener(v -> saveEmployeeData());

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());
    }

    // Mở bộ chọn hình ảnh
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), PICK_IMAGE_REQUEST);
    }

    // Nhận kết quả từ Intent chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivImage.setImageURI(imageUri); // Cập nhật hình ảnh trong ImageView
        }
    }

    // Lưu thông tin nhân viên vào Firebase
    private void saveEmployeeData() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String hireDate = ""; // Có thể lấy từ DatePicker hoặc TextInput nếu cần

        Employees updatedEmployee = new Employees(employeeId, firstName, lastName, departmentID, positionID, hireDate, imageUri != null ? imageUri.toString() : null);

        // Cập nhật thông tin vào Firebase
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
}
