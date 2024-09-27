package com.example.quanlynhansu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlynhansu.Items.EmployeeEvaluations;
import com.example.quanlynhansu.R;
import java.util.ArrayList;
import android.widget.TextView;

public class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<EmployeeEvaluations> evaluationsList;

    public EvaluationAdapter(Context context, ArrayList<EmployeeEvaluations> evaluationsList) {
        this.context = context;
        this.evaluationsList = evaluationsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evaluation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmployeeEvaluations evaluation = evaluationsList.get(position);
        holder.txtEvaluation.setText(evaluation.getEvaluationText());
        holder.txtScore.setText(String.valueOf(evaluation.getScore()));
        holder.txtDate.setText(evaluation.getDate());
    }

    @Override
    public int getItemCount() {
        return evaluationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEvaluation, txtScore, txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEvaluation = itemView.findViewById(R.id.txt_evaluation);
            txtScore = itemView.findViewById(R.id.txt_score);
            txtDate = itemView.findViewById(R.id.txt_date);
        }
    }
}
