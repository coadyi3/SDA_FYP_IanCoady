package dcu.ie.sdaiancoadyfinalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class DeviceViewAdapter extends RecyclerView.Adapter<DeviceViewAdapter.ViewHolder> {
    private Context dNewContext;
    private ArrayList<Integer> deviceID;
    private ArrayList<String> deviceModel;
    private ArrayList<String> environmentType;

    DeviceViewAdapter(Context dNewContext, ArrayList<Integer> id, ArrayList<String> model, ArrayList<String> environment){
        this.dNewContext = dNewContext;
        this.deviceID = id;
        this.deviceModel = model;
        this.environmentType = environment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_device_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.deviceSN             .setText(deviceID.get(position).toString());
        holder.deviceModel          .setText(deviceModel.get(position));
        holder.deviceEnvironment    .setText(environmentType.get(position));

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editDevice = new Intent(dNewContext, EditDevice.class);
                editDevice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                editDevice.putExtra("Serial", deviceID.get(position));
                dNewContext.startActivity(editDevice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceID.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView deviceSN;
        TextView deviceModel;
        TextView deviceEnvironment;
        Button editBtn;
        RelativeLayout parentItemLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentItemLayout    = itemView.findViewById(R.id.listItemLayout);
            deviceSN            = itemView.findViewById(R.id.deviceSN);
            deviceModel         = itemView.findViewById(R.id.deviceModel);
            deviceEnvironment   = itemView.findViewById(R.id.environment);
            editBtn             = itemView.findViewById(R.id.edit_button);

        }
    }
}
