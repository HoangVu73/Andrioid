package com.example.quanlynhansu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlynhansu.Items.Departments;
import com.example.quanlynhansu.R;
import com.example.quanlynhansu.crud.EditDepartmentActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.ViewHolder> {

    private List<Departments> departmentsList;
    private Context context;
    private DatabaseReference databaseDepartments;
    private int selectedPosition = -1; // Vị trí được chọn để hiển thị nút

    public DepartmentsAdapter(List<Departments> departmentsList, Context context) {
        this.departmentsList = departmentsList;
        this.context = context;
        this.databaseDepartments = FirebaseDatabase.getInstance().getReference("Departments");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Departments department = departmentsList.get(position);
        holder.txtDepartmentName.setText(department.getDepartmentName());

        // Hiển thị hoặc ẩn nút sửa và xóa dựa trên vị trí được chọn
        boolean isSelected = position == selectedPosition;
        holder.btnEditDepartment.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.btnDeleteDepartment.setVisibility(isSelected ? View.VISIBLE : View.GONE);

        // Xử lý sự kiện nhấn vào item
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = isSelected ? -1 : position; // Thay đổi vị trí được chọn
            notifyDataSetChanged(); // Cập nhật giao diện
        });

        // Xử lý nút sửa
        holder.btnEditDepartment.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditDepartmentActivity.class);
            intent.putExtra("departmentId", department.getId()); // Gửi ID phòng ban
            intent.putExtra("departmentName", department.getDepartmentName());
            context.startActivity(intent);
        });

        // Xử lý nút xóa
        holder.btnDeleteDepartment.setOnClickListener(v -> {
            String departmentId = department.getId();
            databaseDepartments.child(departmentId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        departmentsList.remove(position); // Xóa khỏi danh sách
                        notifyItemRemoved(position); // Cập nhật giao diện
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return departmentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDepartmentName;
        Button btnEditDepartment, btnDeleteDepartment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDepartmentName = itemView.findViewById(R.id.txt_department_name);
            btnEditDepartment = itemView.findViewById(R.id.btn_edit_department);
            btnDeleteDepartment = itemView.findViewById(R.id.btn_delete_department);
        }
    }
}
