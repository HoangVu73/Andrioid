package com.example.quanlynhansu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlynhansu.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Tìm layout Quản Lý Nhân Sự
        LinearLayout layoutEmployeeManagement = findViewById(R.id.layout_employee_management);

        // Tìm layout Quản Lý Chức Vụ
        LinearLayout layoutPositionManagement = findViewById(R.id.layout_position);

        // Tìm layout Quản Lý Phòng Ban
        LinearLayout layoutDepartmentManagement = findViewById(R.id.layout_department);

        // Tìm layout Quản Lý Tiền lương
        LinearLayout layoutSalaryManagement = findViewById(R.id.layout_salary);

        // Tìm nút đăng xuất
        LinearLayout layoutLogout = findViewById(R.id.layout_logout);

        // Sự kiện khi nhấn vào layout "Quản Lý Nhân Sự"
        layoutEmployeeManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EmployeesActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện khi nhấn vào layout "Quản Lý Chức Vụ"
        layoutPositionManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PositionActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện khi nhấn vào layout "Quản Lý Phòng Ban"
        layoutDepartmentManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DepartmentsActivity.class);
                startActivity(intent);
            }
        });

        layoutSalaryManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SalaryActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện khi nhấn vào layout "Đăng Xuất"
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
