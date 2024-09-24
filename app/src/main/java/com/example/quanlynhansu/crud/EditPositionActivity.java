package com.example.quanlynhansu.crud;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhansu.Items.Positions;
import com.example.quanlynhansu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPositionActivity extends AppCompatActivity {

    private EditText etPositionId, etPositionName;
    private Button btnSavePosition;
    private DatabaseReference databaseReference;
    private String positionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_positions);

        etPositionId = findViewById(R.id.et_position_id);
        etPositionName = findViewById(R.id.et_position_name);
        btnSavePosition = findViewById(R.id.btn_save_position);

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu từ Intent
        positionId = getIntent().getStringExtra("position_id");
        String positionName = getIntent().getStringExtra("position_name");

        // Thiết lập dữ liệu vào EditText
        etPositionId.setText(positionId);
        etPositionName.setText(positionName);

        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("Positions");

        // Lưu chức vụ
        btnSavePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePosition();
            }
        });
    }

    private void savePosition() {
        String id = etPositionId.getText().toString().trim();
        String name = etPositionName.getText().toString().trim();

        if (id.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Positions position = new Positions(id, name);
        databaseReference.child(positionId).setValue(position)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditPositionActivity.this, "Chức vụ đã được lưu", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại Activity trước
                    } else {
                        Toast.makeText(EditPositionActivity.this, "Lỗi khi lưu chức vụ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
