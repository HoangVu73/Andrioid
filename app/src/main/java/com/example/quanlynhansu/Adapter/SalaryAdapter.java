package com.example.quanlynhansu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhansu.Items.SalaryItems;
import com.example.quanlynhansu.R;

import java.util.List;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.SalaryViewHolder> {

    private List<SalaryItems> salaryList;

    public SalaryAdapter(List<SalaryItems> salaryList) {
        this.salaryList = salaryList;
    }

    @NonNull
    @Override
    public SalaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_salary
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salary, parent, false);
        return new SalaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryViewHolder holder, int position) {
        // Lấy dữ liệu từ salaryList và gán vào các TextView
        SalaryItems salaryItem = salaryList.get(position);
        holder.tvID.setText(salaryItem.getId());
        holder.tvName.setText(salaryItem.getName());
        holder.tvSalary.setText(salaryItem.getSalary()); // Đảm bảo getSalary() trả về String
    }

    @Override
    public int getItemCount() {
        return salaryList.size();
    }

    // Phương thức để cập nhật dữ liệu
    public void updateData(List<SalaryItems> newSalaryList) {
        this.salaryList.clear(); // Xóa dữ liệu cũ
        this.salaryList.addAll(newSalaryList); // Thêm dữ liệu mới
        notifyDataSetChanged();
    }

    // ViewHolder class cho các thành phần của item_salary
    public static class SalaryViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvName, tvSalary;

        public SalaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvSalary = itemView.findViewById(R.id.tvSalary);
        }
    }
}
