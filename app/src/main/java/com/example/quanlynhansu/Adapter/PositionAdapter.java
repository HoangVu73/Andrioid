package com.example.quanlynhansu.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhansu.Items.Positions;
import com.example.quanlynhansu.R;
import com.example.quanlynhansu.crud.EditPositionActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.PositionViewHolder> {
    private List<Positions> positionList;

    public PositionAdapter(List<Positions> positionList) {
        this.positionList = positionList;
    }

    @NonNull
    @Override
    public PositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_position, parent, false);
        return new PositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionViewHolder holder, int position) {
        Positions pos = positionList.get(position);
        holder.txtPositionName.setText(pos.getName());

        // Ẩn các nút "Sửa" và "Xóa" ban đầu
        holder.btnEditPosition.setVisibility(View.GONE);
        holder.btnDeletePosition.setVisibility(View.GONE);

        // Hiển thị nút "Sửa" và "Xóa" khi nhấn vào một item
        holder.itemView.setOnClickListener(v -> {
            if (holder.btnEditPosition.getVisibility() == View.GONE) {
                holder.btnEditPosition.setVisibility(View.VISIBLE);
                holder.btnDeletePosition.setVisibility(View.VISIBLE);
            } else {
                holder.btnEditPosition.setVisibility(View.GONE);
                holder.btnDeletePosition.setVisibility(View.GONE);
            }
        });

        // Xử lý sự kiện khi bấm nút "Sửa"
        holder.btnEditPosition.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditPositionActivity.class);
            intent.putExtra("position_id", pos.getId());
            intent.putExtra("position_name", pos.getName());
            v.getContext().startActivity(intent);
        });

        // Xử lý sự kiện khi bấm nút "Xóa"
        holder.btnDeletePosition.setOnClickListener(v -> {
            deleteFromFirebase(pos.getId(), holder.getAdapterPosition(), v);
        });
    }

    @Override
    public int getItemCount() {
        return positionList.size();
    }

    public static class PositionViewHolder extends RecyclerView.ViewHolder {
        TextView txtPositionName;
        Button btnEditPosition, btnDeletePosition;

        public PositionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPositionName = itemView.findViewById(R.id.txt_position_name);
            btnEditPosition = itemView.findViewById(R.id.btn_edit_position);
            btnDeletePosition = itemView.findViewById(R.id.btn_delete_position);
        }
    }

    // Xóa khỏi Firebase và RecyclerView
    private void deleteFromFirebase(String positionId, int position, View view) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Positions").child(positionId);

        // Xóa dữ liệu từ Firebase
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xóa item khỏi RecyclerView
                positionList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(view.getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
