package com.eshan.healthapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eshan.healthapp.R;
import com.eshan.healthapp.ResponseModels.ExercisePlanResponse;
import com.eshan.healthapp.databinding.DietPlanItemRowBinding;

import java.util.List;

public class ExercisePlanAdapter extends RecyclerView.Adapter<ExercisePlanAdapter.ExercisePlanViewHolder> {

    private final Context context;
    private final List<ExercisePlanResponse>exercisePlanList;

    public ExercisePlanAdapter(Context context, List<ExercisePlanResponse> exercisePlanList) {
        this.context = context;
        this.exercisePlanList = exercisePlanList;
    }

    @NonNull
    @Override
    public ExercisePlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExercisePlanViewHolder(LayoutInflater.from(context).inflate(R.layout.diet_plan_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisePlanViewHolder holder, int position) {

        ExercisePlanResponse planResponse = exercisePlanList.get(position);
        holder.binding.planTitle.setText(planResponse.getExercise_plan().getPlan());
        holder.binding.planText.setText(planResponse.getExercise_plan().getExercises());
        holder.binding.dateText.setText(planResponse.getCreated_at());

    }

    @Override
    public int getItemCount() {
        return exercisePlanList.size();
    }

    public static class ExercisePlanViewHolder extends RecyclerView.ViewHolder {

        public DietPlanItemRowBinding binding;

        public ExercisePlanViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DietPlanItemRowBinding.bind(itemView);
        }
    }

}
