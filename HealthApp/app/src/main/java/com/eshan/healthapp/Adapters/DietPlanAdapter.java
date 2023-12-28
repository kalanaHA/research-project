package com.eshan.healthapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eshan.healthapp.Models.DietPlan;
import com.eshan.healthapp.R;
import com.eshan.healthapp.databinding.DietPlanItemRowBinding;

import java.util.List;

public class DietPlanAdapter extends RecyclerView.Adapter<DietPlanAdapter.DietPlanViewHolder> {

    private Context context;
    private List<DietPlan>dietPlanList;

    public DietPlanAdapter(Context context, List<DietPlan> dietPlanList) {
        this.context = context;
        this.dietPlanList = dietPlanList;
    }

    @NonNull
    @Override
    public DietPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DietPlanViewHolder(LayoutInflater.from(context).inflate(R.layout.diet_plan_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DietPlanViewHolder holder, int position) {

//        DietPlan dietPlan = dietPlanList.get(position);
//
//        String dietPlanNo = "Plan " + (position + 1);
//        String created = "Created : "+dietPlan.getCreated_at();
//        String updated = "Updated : "+dietPlan.getUpdated_at();
//
//        holder.binding.createdDate.setText(created);
//        holder.binding.dietPlanNo.setText(dietPlanNo);
//        holder.binding.updatedDate.setText(updated);
//        holder.binding.dietPlanText.setText(dietPlan.getDiet_plan());

    }

    @Override
    public int getItemCount() {
        return dietPlanList.size();
    }

    public class DietPlanViewHolder extends RecyclerView.ViewHolder {
        public DietPlanItemRowBinding binding;

        public DietPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DietPlanItemRowBinding.bind(itemView);
        }
    }

}
