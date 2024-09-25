package com.example.quanlynhansu.crud;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhansu.Items.SalaryItems;
import com.example.quanlynhansu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSalaryActivity extends AppCompatActivity {

    private EditText etBasicSalary, etAllowance, etTax, etInsurance, etSalary;
    private Button btnSave;
    private SalaryItems salaryItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_salary);

        etBasicSalary = findViewById(R.id.etBasicSalary);
        etAllowance = findViewById(R.id.etAllowance);
        etTax = findViewById(R.id.etTax);
        etInsurance = findViewById(R.id.etInsurance);
        etSalary = findViewById(R.id.etSalary);
        btnSave = findViewById(R.id.btnSave);

        // Lấy dữ liệu từ Intent
        salaryItem = (SalaryItems) getIntent().getSerializableExtra("salaryItem");

        // Hiển thị thông tin hiện tại
        if (salaryItem != null) {
            etBasicSalary.setText(String.valueOf(salaryItem.getBasicSalary()));
            etAllowance.setText(String.valueOf(salaryItem.getAllowance()));
            etTax.setText(String.valueOf(salaryItem.getTax()));
            etInsurance.setText(String.valueOf(salaryItem.getInsurance()));
            etSalary.setText(salaryItem.getSalary());
        }

        btnSave.setOnClickListener(v -> updateSalary());
    }

    private void updateSalary() {
        // Lấy dữ liệu từ các trường nhập
        double basicSalary = Double.parseDouble(etBasicSalary.getText().toString());
        double allowance = Double.parseDouble(etAllowance.getText().toString());
        double tax = Double.parseDouble(etTax.getText().toString());
        double insurance = Double.parseDouble(etInsurance.getText().toString());
        String newSalary = etSalary.getText().toString();

        // Cập nhật dữ liệu vào Firebase
        DatabaseReference salaryRef = FirebaseDatabase.getInstance().getReference("Salaries").child(salaryItem.getId());
        salaryRef.child("basicSalary").setValue(basicSalary);
        salaryRef.child("allowance").setValue(allowance);
        salaryRef.child("tax").setValue(tax);
        salaryRef.child("insurance").setValue(insurance);
        salaryRef.child("salary").setValue(newSalary).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại Activity trước
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
