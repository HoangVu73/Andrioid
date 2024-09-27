package com.example.quanlynhansu.crud;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditEmployeeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btnSaveEmployee, btnShowDepartments, btnShowPositions, btnChooseImage;
    private TextInputEditText etFirstName, etLastName, etHireDate;
    private ImageView ivImage;

    private String employeeId;
    private String departmentID;
    private String positionID;
    private String imageUrl; // To store image URL

    private Uri imageUri; // To store the image URI
    private List<Departments> departmentList = new ArrayList<>();
    private List<Positions> positionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        // Initialize UI components
        btnSaveEmployee = findViewById(R.id.btn_save_employee);
        btnShowDepartments = findViewById(R.id.btn_select_department);
        btnShowPositions = findViewById(R.id.btn_select_position);
        btnChooseImage = findViewById(R.id.btn_change_image);
        ivImage = findViewById(R.id.img_employee);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etHireDate = findViewById(R.id.et_hire_date);

        // Get employeeId from Intent
        Intent intent = getIntent();
        employeeId = intent.getStringExtra("employeeId");

        if (employeeId == null) {
            Toast.makeText(this, "Không tìm thấy ID nhân viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadEmployeeData(employeeId);

        // Save employee data on button click
        btnSaveEmployee.setOnClickListener(v -> saveEmployeeData());

        // Show departments dialog
        btnShowDepartments.setOnClickListener(v -> showDepartmentDialog());

        // Show positions dialog
        btnShowPositions.setOnClickListener(v -> showPositionDialog());

        // Choose image from gallery
        btnChooseImage.setOnClickListener(v -> openFileChooser());
    }

    private void loadEmployeeData(String employeeId) {
        DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference("Employees").child(employeeId);
        employeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employees employee = snapshot.getValue(Employees.class);
                if (employee != null) {
                    etFirstName.setText(employee.getFirstName());
                    etLastName.setText(employee.getLastName());
                    etHireDate.setText(employee.getHireDate());
                    departmentID = employee.getDepartmentID();
                    positionID = employee.getPositionID();
                    imageUrl = employee.getImageUrl();
                    // Load the image into ImageView (if using a library like Glide or Picasso)
                    // For example: Glide.with(EditEmployeeActivity.this).load(imageUrl).into(ivImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditEmployeeActivity.this, "Lỗi khi tải dữ liệu nhân viên", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveEmployeeData() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String hireDate = etHireDate.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || hireDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference("Employees").child(employeeId);

        if (imageUri != null) {
            // Upload the image if a new one has been selected
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("employee_images/" + employeeId + ".jpg");
            UploadTask uploadTask = storageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // URL of the uploaded image
                    imageUrl = uri.toString();
                    updateEmployee(employeeRef, firstName, lastName, hireDate);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(EditEmployeeActivity.this, "Lỗi khi tải hình ảnh lên", Toast.LENGTH_SHORT).show();
            });
        } else {
            // No new image, update the employee data without changing the image URL
            updateEmployee(employeeRef, firstName, lastName, hireDate);
        }
    }

    private void updateEmployee(DatabaseReference employeeRef, String firstName, String lastName, String hireDate) {
        Employees updatedEmployee = new Employees(employeeId, firstName, lastName, departmentID, positionID, hireDate, imageUrl);
        employeeRef.setValue(updatedEmployee).addOnSuccessListener(aVoid -> {
            Toast.makeText(EditEmployeeActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(EditEmployeeActivity.this, "Lỗi khi cập nhật thông tin", Toast.LENGTH_SHORT).show();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivImage.setImageBitmap(bitmap); // Display selected image
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi chọn hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDepartmentDialog() {
        DatabaseReference departmentRef = FirebaseDatabase.getInstance().getReference("Departments");
        departmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                departmentList.clear();
                for (DataSnapshot departmentSnapshot : snapshot.getChildren()) {
                    Departments department = departmentSnapshot.getValue(Departments.class);
                    if (department != null) {
                        departmentList.add(department);
                    }
                }

                CharSequence[] departmentNames = new CharSequence[departmentList.size()];
                for (int i = 0; i < departmentList.size(); i++) {
                    departmentNames[i] = departmentList.get(i).getDepartmentName();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(EditEmployeeActivity.this);
                builder.setTitle("Chọn Phòng Ban");
                builder.setItems(departmentNames, (dialog, which) -> {
                    departmentID = departmentList.get(which).getId();
                    Toast.makeText(EditEmployeeActivity.this, "Chọn: " + departmentList.get(which).getDepartmentName(), Toast.LENGTH_SHORT).show();
                });
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditEmployeeActivity.this, "Lỗi khi tải danh sách phòng ban", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPositionDialog() {
        DatabaseReference positionRef = FirebaseDatabase.getInstance().getReference("Positions");
        positionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                positionList.clear();
                for (DataSnapshot positionSnapshot : snapshot.getChildren()) {
                    Positions position = positionSnapshot.getValue(Positions.class);
                    if (position != null) {
                        positionList.add(position);
                    }
                }

                CharSequence[] positionNames = new CharSequence[positionList.size()];
                for (int i = 0; i < positionList.size(); i++) {
                    positionNames[i] = positionList.get(i).getName();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(EditEmployeeActivity.this);
                builder.setTitle("Chọn Chức Vụ");
                builder.setItems(positionNames, (dialog, which) -> {
                    positionID = positionList.get(which).getId();
                    Toast.makeText(EditEmployeeActivity.this, "Chọn: " + positionList.get(which).getName(), Toast.LENGTH_SHORT).show();
                });
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditEmployeeActivity.this, "Lỗi khi tải danh sách chức vụ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
