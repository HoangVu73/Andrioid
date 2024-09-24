package com.example.quanlynhansu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlynhansu.Items.Departments;
import com.example.quanlynhansu.R;
import com.example.quanlynhansu.Adapter.DepartmentsAdapter;
import com.example.quanlynhansu.crud.AddDepartmentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DepartmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DepartmentsAdapter departmentsAdapter;
    private List<Departments> departmentsList;
    private DatabaseReference databaseDepartments;
    private Button btnAddDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        recyclerView = findViewById(R.id.recyclerViewDepartments);
        btnAddDepartment = findViewById(R.id.btn_add_department);
        departmentsList = new ArrayList<>();
        databaseDepartments = FirebaseDatabase.getInstance().getReference("Departments");

        // Cấu hình RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        departmentsAdapter = new DepartmentsAdapter(departmentsList, this);
        recyclerView.setAdapter(departmentsAdapter);

        // Lấy danh sách phòng ban từ Firebase
        databaseDepartments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                departmentsList.clear(); // Xóa danh sách cũ
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Departments department = snapshot.getValue(Departments.class);
                    department.setId(snapshot.getKey()); // Lấy ID từ Firebase
                    departmentsList.add(department);
                }
                departmentsAdapter.notifyDataSetChanged(); // Cập nhật giao diện
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DepartmentsActivity.this, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện nhấn nút thêm phòng ban
        btnAddDepartment.setOnClickListener(v -> {
            Intent intent = new Intent(DepartmentsActivity.this, AddDepartmentActivity.class);
            startActivity(intent);
        });
    }
}
