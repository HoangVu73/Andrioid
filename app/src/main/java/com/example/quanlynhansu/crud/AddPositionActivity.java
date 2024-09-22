package com.example.quanlynhansu.crud;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhansu.Items.Positions;
import com.example.quanlynhansu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPositionActivity extends AppCompatActivity {

    private EditText etPositionId, etPositionName;
    private Button btnSave;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_positions); // Đặt layout cho activity này

        etPositionId = findViewById(R.id.et_position_id);
        etPositionName = findViewById(R.id.et_position_name);
        btnSave = findViewById(R.id.btn_save_position);
        btnBack = findViewById(R.id.ic_back);

        btnBack.setOnClickListener(v -> finish()); // Quay lại activity trước

        btnSave.setOnClickListener(v -> savePosition()); // Gọi phương thức lưu chức vụ
    }

    private void savePosition() {
        String positionId = etPositionId.getText().toString().trim();
        String positionName = etPositionName.getText().toString().trim();

        if (TextUtils.isEmpty(positionId)) {
            etPositionId.setError("Vui lòng nhập ID chức vụ");
            etPositionId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(positionName)) {
            etPositionName.setError("Vui lòng nhập tên chức vụ");
            etPositionName.requestFocus();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Positions");
        Positions position = new Positions(positionId, positionName);

        databaseReference.child(positionId).setValue(position)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddPositionActivity.this, "Thêm chức vụ thành công", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại activity trước
                    } else {
                        Toast.makeText(AddPositionActivity.this, "Lỗi khi thêm chức vụ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
