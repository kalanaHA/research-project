package com.eshan.healthapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eshan.healthapp.Models.RiskStatus;
import com.eshan.healthapp.Models.User;
import com.eshan.healthapp.R;
import com.eshan.healthapp.databinding.HealthStatusContainerBinding;

import java.util.List;

public class HealthStatusAdapter extends RecyclerView.Adapter<HealthStatusAdapter.HealthStatusViewHolder> {

    private Context context;
    private List<RiskStatus>riskStatusList;
    private User user;

    public HealthStatusAdapter(Context context, List<RiskStatus> riskStatusList, User user) {
        this.context = context;
        this.riskStatusList = riskStatusList;
        this.user = user;
    }

    @NonNull
    @Override
    public HealthStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HealthStatusViewHolder(LayoutInflater.from(context).inflate(R.layout.health_status_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HealthStatusViewHolder holder, int position) {

        RiskStatus riskStatus = riskStatusList.get(position);

        String name = "Name : "+user.getName();
        String bloodGroup = "Blood Group : "+user.getBloodType();
        String issue = "Known Issue : "+user.getHealthStatus();
        String status = "Risk Status : "+riskStatus.getRisk_level();

        holder.binding.bloodGroupText.setText(bloodGroup);
        holder.binding.nameText.setText(name);
        holder.binding.issueText.setText(issue);
        holder.binding.riskStatusText.setText(status);
        holder.binding.dateText.setText(riskStatus.getUpdated_at());

    }

    @Override
    public int getItemCount() {
        return riskStatusList.size();
    }

    public static class HealthStatusViewHolder extends RecyclerView.ViewHolder {

        public HealthStatusContainerBinding binding;

        public HealthStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HealthStatusContainerBinding.bind(itemView);
        }
    }

}
