package com.example.quanlynhansu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhansu.Adapter.SalaryAdapter;
import com.example.quanlynhansu.Items.SalaryItems;
import com.example.quanlynhansu.R;

import com.example.quanlynhansu.crud.AddEmployeeActivity;
import com.example.quanlynhansu.crud.AddSalaryActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SalaryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSalaries;
    private List<SalaryItems> salaryList;
    private SalaryAdapter salaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);

        // Ánh xạ RecyclerView
        recyclerViewSalaries = findViewById(R.id.recyclerViewSalaries);

        // Khởi tạo danh sách và adapter
        salaryList = new ArrayList<>();
        salaryAdapter = new SalaryAdapter(salaryList);
        recyclerViewSalaries.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSalaries.setAdapter(salaryAdapter);

        Button btnAddSalary = findViewById(R.id.btn_add_salary);
        btnAddSalary.setOnClickListener(v -> {
            Intent intent = new Intent(SalaryActivity.this, AddSalaryActivity.class);
            startActivity(intent);
        });

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu lương từ Firebase
        loadSalaryData();
    }

    // Hàm lấy dữ liệu lương từ Firebase
    private void loadSalaryData() {
        DatabaseReference salaryRef = FirebaseDatabase.getInstance().getReference("salaries");

        salaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                salaryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SalaryItems salaryItem = snapshot.getValue(SalaryItems.class);
                    if (salaryItem != null) {
                        salaryList.add(salaryItem);
                    }
                }
                salaryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi khi lấy dữ liệu thất bại
            }
        });
    }
}
