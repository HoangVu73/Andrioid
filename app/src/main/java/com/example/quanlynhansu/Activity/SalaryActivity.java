package com.example.quanlynhansu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhansu.Adapter.SalaryAdapter;
import com.example.quanlynhansu.Items.SalaryItems;
import com.example.quanlynhansu.R;
import com.example.quanlynhansu.crud.AddSalaryActivity;
import com.example.quanlynhansu.crud.EditSalaryActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SalaryActivity extends AppCompatActivity implements SalaryAdapter.OnSalaryItemClickListener {

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
        salaryAdapter = new SalaryAdapter(salaryList, this);
        recyclerViewSalaries.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSalaries.setAdapter(salaryAdapter);

        // Button thêm lương
        Button btnAddSalary = findViewById(R.id.btn_add_salary);
        btnAddSalary.setOnClickListener(v -> {
            Intent intent = new Intent(SalaryActivity.this, AddSalaryActivity.class);
            startActivity(intent);
        });

        // Nút quay lại
        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu lương từ Firebase
        loadSalaryData();
    }

    // Hàm lấy dữ liệu lương từ Firebase
    private void loadSalaryData() {
        DatabaseReference salaryRef = FirebaseDatabase.getInstance().getReference("Salaries");

        salaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                salaryList.clear(); // Xóa danh sách trước khi thêm mới

                long itemCount = dataSnapshot.getChildrenCount(); // Đếm số lượng mục
                Log.d("SalaryData", "Số mục lương tải về: " + itemCount); // Ghi số lượng mục tải về

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SalaryItems salaryItem = snapshot.getValue(SalaryItems.class);
                    if (salaryItem != null) {
                        salaryList.add(salaryItem);
                        Log.d("SalaryData", "ID: " + salaryItem.getId());
                    }
                }
                salaryAdapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SalaryData", "Lỗi khi lấy dữ liệu lương: " + databaseError.getMessage());
            }
        });
    }

    // Hàm xử lý sự kiện sửa lương
    @Override
    public void onEdit(SalaryItems salaryItem) {
        Intent intent = new Intent(this, EditSalaryActivity.class);
        intent.putExtra("salaryItem", salaryItem);
        startActivity(intent);
    }

    // Hàm xử lý sự kiện xóa lương
    @Override
    public void onDelete(SalaryItems salaryItem) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa lương")
                .setMessage("Bạn có chắc chắn muốn xóa mục lương này không?")
                .setPositiveButton("Có", (dialog, which) -> deleteSalary(salaryItem))
                .setNegativeButton("Không", null)
                .show();
    }

    // Hàm xóa lương
    private void deleteSalary(SalaryItems salaryItem) {
        DatabaseReference salaryRef = FirebaseDatabase.getInstance().getReference("Salaries").child(salaryItem.getId());
        salaryRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loadSalaryData(); // Cập nhật lại danh sách sau khi xóa
                Toast.makeText(SalaryActivity.this, "Đã xóa mục lương: " + salaryItem.getEmployeeId(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SalaryActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
