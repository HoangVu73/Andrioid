package com.example.quanlynhansu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhansu.R;
import com.example.quanlynhansu.Adapter.PositionAdapter;
import com.example.quanlynhansu.Items.Positions;
import com.example.quanlynhansu.crud.AddPositionActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PositionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PositionAdapter positionAdapter;
    private List<Positions> positionList;
    private DatabaseReference databaseReference;
    private Button btnAddPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positions);

        recyclerView = findViewById(R.id.recyclerViewPositions);
        btnAddPosition = findViewById(R.id.btn_add_position);
        positionList = new ArrayList<>();
        positionAdapter = new PositionAdapter(positionList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(positionAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Positions");

        // Lấy danh sách chức vụ từ Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                positionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Positions position = snapshot.getValue(Positions.class);
                    if (position != null) {
                        positionList.add(position);
                    }
                }
                positionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        ImageView btnBack = findViewById(R.id.ic_back);
        btnBack.setOnClickListener(v -> finish());

        // Mở Activity thêm chức vụ
        btnAddPosition.setOnClickListener(v -> {
            Intent intent = new Intent(PositionActivity.this, AddPositionActivity.class);
            startActivity(intent);
        });
    }
}
