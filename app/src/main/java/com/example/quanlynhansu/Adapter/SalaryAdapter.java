package com.example.quanlynhansu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhansu.Items.SalaryItems;
import com.example.quanlynhansu.R;

import java.text.DecimalFormat;
import java.util.List;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.SalaryViewHolder> {

    private List<SalaryItems> salaryList;
    private OnSalaryItemClickListener listener;

    public interface OnSalaryItemClickListener {
        void onEdit(SalaryItems salaryItem);
        void onDelete(SalaryItems salaryItem);
    }

    public SalaryAdapter(List<SalaryItems> salaryList, OnSalaryItemClickListener listener) {
        this.salaryList = salaryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SalaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salary, parent, false);
        return new SalaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryViewHolder holder, int position) {
        SalaryItems salaryItem = salaryList.get(position);
        holder.tvID.setText(salaryItem.getEmployeeId());
        holder.tvName.setText(salaryItem.getName());

        // Định dạng số lương
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedSalary = decimalFormat.format(Double.parseDouble(salaryItem.getSalary()));
        holder.tvSalary.setText(formattedSalary);

        // Thiết lập sự kiện nhấp chuột để hiển thị PopupMenu
        holder.itemView.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.itemView);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_edit) {
                    listener.onEdit(salaryItem); // Gọi phương thức sửa
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    listener.onDelete(salaryItem); // Gọi phương thức xóa
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }


    @Override
    public int getItemCount() {
        return salaryList.size();
    }

    static class SalaryViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvName, tvSalary;

        public SalaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvSalary = itemView.findViewById(R.id.tvSalary);
        }
    }
}
