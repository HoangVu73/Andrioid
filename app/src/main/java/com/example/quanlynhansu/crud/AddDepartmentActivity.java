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

public class AddDepartmentActivity extends AppCompatActivity {

    private EditText edtDepartmentId, edtDepartmentName;
    private Button btnSaveDepartment;
    private DatabaseReference databaseDepartments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department); // Sử dụng layout tương ứng

        edtDepartmentId = findViewById(R.id.edt_department_id);
        edtDepartmentName = findViewById(R.id.edt_department_name);
        btnSaveDepartment = findViewById(R.id.btn_save_department);

        databaseDepartments = FirebaseDatabase.getInstance().getReference("Departments");

        btnSaveDepartment.setOnClickListener(v -> addDepartment());

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void addDepartment() {
        String id = edtDepartmentId.getText().toString().trim();
        String name = edtDepartmentName.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập ID và tên phòng ban", Toast.LENGTH_SHORT).show();
            return;
        }

        Departments department = new Departments(id, name);
        databaseDepartments.child(id).setValue(department)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddDepartmentActivity.this, "Thêm phòng ban thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng activity sau khi thêm
                })
                .addOnFailureListener(e -> Toast.makeText(AddDepartmentActivity.this, "Thêm phòng ban thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
