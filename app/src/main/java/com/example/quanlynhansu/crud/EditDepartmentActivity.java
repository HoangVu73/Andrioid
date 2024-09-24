package com.example.quanlynhansu.crud;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlynhansu.Items.Departments;
import com.example.quanlynhansu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditDepartmentActivity extends AppCompatActivity {

    private EditText editDepartmentId, editDepartmentName;
    private Button btnSave;
    private DatabaseReference databaseDepartments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_department); // Sử dụng layout tương ứng

        editDepartmentId = findViewById(R.id.edit_department_id);
        editDepartmentName = findViewById(R.id.edit_department_name);
        btnSave = findViewById(R.id.btn_save);

        String departmentId = getIntent().getStringExtra("departmentId");
        String departmentName = getIntent().getStringExtra("departmentName");

        editDepartmentId.setText(departmentId);
        editDepartmentName.setText(departmentName);

        databaseDepartments = FirebaseDatabase.getInstance().getReference("Departments");

        btnSave.setOnClickListener(v -> updateDepartment(departmentId));

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void updateDepartment(String departmentId) {
        String name = editDepartmentName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập tên phòng ban", Toast.LENGTH_SHORT).show();
            return;
        }

        Departments updatedDepartment = new Departments(departmentId, name);
        databaseDepartments.child(departmentId).setValue(updatedDepartment)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditDepartmentActivity.this, "Cập nhật phòng ban thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng activity sau khi cập nhật
                })
                .addOnFailureListener(e -> Toast.makeText(EditDepartmentActivity.this, "Cập nhật phòng ban thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
