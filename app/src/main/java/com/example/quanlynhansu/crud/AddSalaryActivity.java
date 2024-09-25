package com.example.quanlynhansu.crud;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhansu.Items.SalaryItems;
import com.example.quanlynhansu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddSalaryActivity extends AppCompatActivity {

    private Spinner spinnerEmployee;
    private EditText edtBasicSalary, edtAllowance, edtTax, edtInsurance;
    private Button btnAddSalary;

    private DatabaseReference salaryDatabase;
    private DatabaseReference employeeDatabase; // Database reference for employees
    private List<String> employeeNames; // List to store employee names
    private List<String> employeeIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salary);

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());

        spinnerEmployee = findViewById(R.id.spinnerEmployee);
        edtBasicSalary = findViewById(R.id.edtBasicSalary);
        edtAllowance = findViewById(R.id.edtAllowance);
        edtTax = findViewById(R.id.edtTax);
        edtInsurance = findViewById(R.id.edtInsurance);
        btnAddSalary = findViewById(R.id.btnAddSalary);

        // Firebase Database reference
        salaryDatabase = FirebaseDatabase.getInstance().getReference("Salaries");
        employeeDatabase = FirebaseDatabase.getInstance().getReference("Employees"); // Change to your employees node

        // Initialize employee names list
        employeeNames = new ArrayList<>();
        employeeIDs = new ArrayList<>();

        // Load employee data into spinner
        loadEmployeeData();
        loadSalaryData();


        btnAddSalary.setOnClickListener(v -> addSalary());
    }

    private void loadEmployeeData() {
        employeeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeNames.clear();
                employeeIDs.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.child("id").getValue(String.class);
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);

                    if (firstName != null && lastName != null) {
                        String fullName = firstName + " " + lastName;
                        employeeNames.add(fullName);
                        employeeIDs.add(id);
                    }
                }

                Log.d("EmployeeData", "Number employee: " + employeeNames.size());
                if (employeeNames.isEmpty()) {
                    Log.d("EmployeeData", "No employees found in the database.");
                }

                // Set up the spinner adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddSalaryActivity.this,
                        android.R.layout.simple_spinner_item, employeeNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEmployee.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EmployeeData", "Database error: " + databaseError.getMessage());
                Toast.makeText(AddSalaryActivity.this, "Failed to load employees.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addSalary() {
        int selectedPosition = spinnerEmployee.getSelectedItemPosition();
        String employeeId = employeeIDs.get(selectedPosition);

        String employeeName = spinnerEmployee.getSelectedItem().toString();
        String basicSalaryStr = edtBasicSalary.getText().toString().trim();
        String allowanceStr = edtAllowance.getText().toString().trim();
        String taxStr = edtTax.getText().toString().trim();
        String insuranceStr = edtInsurance.getText().toString().trim();

        if (TextUtils.isEmpty(basicSalaryStr) || TextUtils.isEmpty(allowanceStr) ||
                TextUtils.isEmpty(taxStr) || TextUtils.isEmpty(insuranceStr)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double basicSalary = Double.parseDouble(basicSalaryStr);
        double allowance = Double.parseDouble(allowanceStr);
        double tax = Double.parseDouble(taxStr);
        double insurance = Double.parseDouble(insuranceStr);

        // Tính lương thực nhận
        double netSalary = basicSalary + allowance - tax - insurance;

        // Tạo đối tượng SalaryItem và lưu vào Firebase
        String salaryId = salaryDatabase.push().getKey();
        // Tạo đối tượng SalaryItem và lưu vào Firebase
        SalaryItems salaryItem = new SalaryItems(salaryId, employeeId, employeeName, basicSalary, allowance, tax, insurance, String.valueOf(netSalary));



        if (salaryId != null) {
            salaryDatabase.child(salaryId).setValue(salaryItem).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddSalaryActivity.this, "Thêm lương thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình trước đó
                } else {
                    Toast.makeText(AddSalaryActivity.this, "Thêm lương thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadSalaryData() {
        // Giả sử bạn có một DatabaseReference cho salaries
        DatabaseReference salaryDatabase = FirebaseDatabase.getInstance().getReference("Salaries");

        salaryDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SalaryItems> salaryList = new ArrayList<>(); // Tạo danh sách lương

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SalaryItems salaryItem = snapshot.getValue(SalaryItems.class);
                    if (salaryItem != null) {
                        salaryList.add(salaryItem); // Thêm vào danh sách
                    }
                }

                // Cập nhật giao diện với dữ liệu mới
                // Nếu bạn sử dụng RecyclerView, hãy cập nhật adapter
                // salaryAdapter.updateData(salaryList);
                // Nếu bạn sử dụng ListView, bạn có thể thông báo rằng dữ liệu đã thay đổi
                // salaryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddSalaryActivity.this, "Failed to load salaries.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
