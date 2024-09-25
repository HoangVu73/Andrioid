package com.example.quanlynhansu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhansu.Adapter.EmployeeAdapter;
import com.example.quanlynhansu.Items.Employees;
import com.example.quanlynhansu.R;
import com.example.quanlynhansu.crud.AddEmployeeActivity;
import com.example.quanlynhansu.crud.EditEmployeeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class EmployeesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter adapter;
    private List<Employees> employeeList;
    private List<String> employeeKeys;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        // Khởi tạo RecyclerView
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));

        employeeList = new ArrayList<>();
        employeeKeys = new ArrayList<>();

        // Khởi tạo adapter và thiết lập sự kiện click
        adapter = new EmployeeAdapter(employeeList, new EmployeeAdapter.OnEmployeeClickListener() {
            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(EmployeesActivity.this, EditEmployeeActivity.class);
                intent.putExtra("employeeId", employeeKeys.get(position)); // Sử dụng "employeeId"
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                showDeleteConfirmation(position);
            }

            @Override
            public void onEvaluateClick(int position) {
                // Xử lý sự kiện đánh giá ở đây
                Intent intent = new Intent(EmployeesActivity.this, EvaluateActivity.class);
                intent.putExtra("employeeId", employeeKeys.get(position)); // Sử dụng "employeeId"
                startActivity(intent);
            }
        });
        recyclerViewEmployees.setAdapter(adapter);

        // Kết nối đến Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Employees");

        // Lắng nghe sự thay đổi dữ liệu từ Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList.clear();
                employeeKeys.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employees employee = snapshot.getValue(Employees.class);
                    if (employee != null) {
                        employeeList.add(employee);
                        employeeKeys.add(snapshot.getKey());
                        Log.d("EmployeesActivity", "Employee ID: " + snapshot.getKey()); // In ra ID nhân viên
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("EmployeesActivity", "Database error: " + databaseError.getMessage());
            }
        });

        // Gán sự kiện cho nút "Thêm nhân viên"
        Button btnAddEmployee = findViewById(R.id.btn_add_employee);
        btnAddEmployee.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeesActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
        });

        // Gán sự kiện cho nút "Quay lại"
        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());

        // Thiết lập tiêu đề
        TextView txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText("Quản Lý Nhân Viên");
    }

    private void showDeleteConfirmation(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa Nhân Viên")
                .setMessage("Bạn có chắc chắn muốn xóa nhân viên này không?")
                .setPositiveButton("Có", (dialog, which) -> deleteEmployee(employeeKeys.get(position)))
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteEmployee(String employeeKey) {
        databaseReference.child(employeeKey).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EmployeesActivity.this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EmployeesActivity.this, "Không thể xóa nhân viên!", Toast.LENGTH_SHORT).show();
                });
    }
}
