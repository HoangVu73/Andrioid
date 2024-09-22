package com.example.quanlynhansu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlynhansu.Items.Employees;
import com.example.quanlynhansu.R;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    private List<Employees> employees;
    private OnEmployeeClickListener listener;
    private int selectedPosition = -1; // Vị trí được chọn

    public EmployeeAdapter(List<Employees> employees, OnEmployeeClickListener listener) {
        this.employees = employees;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employees employee = employees.get(position);

        // Thiết lập hình ảnh cho nhân viên
        Glide.with(holder.itemView.getContext())
                .load(employee.getImageUrl())
                .placeholder(R.drawable.ic_default_avatar)
                .into(holder.imgEmployee);

        // Thiết lập giá trị cho các trường thông tin
        holder.txtEmployeeIdValue.setText(employee.getId());
        holder.txtEmployeeNameValue.setText(employee.getFirstName() + " " + employee.getLastName());
        holder.txtEmployeePositionValue.setText(employee.getPositionID());
        holder.txtDepartmentValue.setText(employee.getDepartmentID());
        holder.txtHireDateValue.setText(employee.getHireDate());

        // Hiện hoặc ẩn nút dựa trên vị trí được chọn
        boolean isSelected = position == selectedPosition;
        holder.btnEdit.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.btnDelete.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.btnEvaluate.setVisibility(isSelected ? View.VISIBLE : View.GONE);

        // Thiết lập sự kiện click cho mỗi mục
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = isSelected ? -1 : position;
            notifyItemChanged(selectedPosition == -1 ? position : selectedPosition);
            notifyItemChanged(position);
        });

        // Thiết lập sự kiện click cho nút Sửa, Xóa và Đánh Giá
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(position));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(position));
        holder.btnEvaluate.setOnClickListener(v -> listener.onEvaluateClick(position));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgEmployee;
        TextView txtEmployeeIdValue;
        TextView txtEmployeeNameValue;
        TextView txtEmployeePositionValue;
        TextView txtDepartmentValue;
        TextView txtHireDateValue;
        Button btnEdit, btnDelete, btnEvaluate; // Thêm btnEvaluate

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEmployee = itemView.findViewById(R.id.img_employee);
            txtEmployeeIdValue = itemView.findViewById(R.id.txt_employee_id_value);
            txtEmployeeNameValue = itemView.findViewById(R.id.txt_employee_name_value);
            txtEmployeePositionValue = itemView.findViewById(R.id.txt_employee_position_value);
            txtDepartmentValue = itemView.findViewById(R.id.txt_department_value);
            txtHireDateValue = itemView.findViewById(R.id.txt_hire_date_value);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEvaluate = itemView.findViewById(R.id.btn_evaluate); // Khởi tạo btnEvaluate
        }
    }

    public interface OnEmployeeClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onEvaluateClick(int position); // Thêm phương thức cho nút Đánh Giá
    }
}
