package com.example.quanlynhansu.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlynhansu.Adapter.EvaluationAdapter;
import com.example.quanlynhansu.Items.EmployeeEvaluations;
import com.example.quanlynhansu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EvaluateActivity extends AppCompatActivity {

    private TextView txtEmployeeName;
    private EditText edtEvaluation, edtScore;
    private Button btnAddEvaluation;
    private RecyclerView recyclerViewEvaluations;
    private EvaluationAdapter evaluationAdapter;
    private ArrayList<EmployeeEvaluations> evaluationsList;
    private DatabaseReference databaseReference;
    private String employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);

             // Ánh xạ các view
        txtEmployeeName = findViewById(R.id.txt_employee_name);
        edtEvaluation = findViewById(R.id.edt_evaluation);
        edtScore = findViewById(R.id.edt_score);
        btnAddEvaluation = findViewById(R.id.btn_add_evaluation);
        recyclerViewEvaluations = findViewById(R.id.recyclerViewEvaluations);

        // Nhận employeeId từ Intent
        employeeId = getIntent().getStringExtra("employeeId");

        // Cấu hình RecyclerView
        recyclerViewEvaluations.setLayoutManager(new LinearLayoutManager(this));
        evaluationsList = new ArrayList<>();
        evaluationAdapter = new EvaluationAdapter(this, evaluationsList);
        recyclerViewEvaluations.setAdapter(evaluationAdapter);

        // Thêm sự kiện cho nút Thêm Đánh Giá
        btnAddEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvaluation();
            }
        });

        // Hiển thị đánh giá từ Firebase
        fetchEvaluations();
    }

    private void addEvaluation() {
        String evaluationText = edtEvaluation.getText().toString();
        String scoreText = edtScore.getText().toString();

        if (evaluationText.isEmpty() || scoreText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đánh giá mới
        String evaluationId = FirebaseDatabase.getInstance().getReference().push().getKey();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        EmployeeEvaluations evaluation = new EmployeeEvaluations(evaluationId, employeeId, evaluationText, Integer.parseInt(scoreText), date);

        // Lưu đánh giá vào Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("EmployeeEvaluations").child(employeeId);
        databaseReference.child(evaluationId).setValue(evaluation);

        Toast.makeText(this, "Đánh giá đã được thêm", Toast.LENGTH_SHORT).show();
        edtEvaluation.setText("");
        edtScore.setText("");
    }

    private void fetchEvaluations() {
        databaseReference = FirebaseDatabase.getInstance().getReference("EmployeeEvaluations").child(employeeId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                evaluationsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeEvaluations evaluation = dataSnapshot.getValue(EmployeeEvaluations.class);
                    evaluationsList.add(evaluation);
                }
                evaluationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EvaluateActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
