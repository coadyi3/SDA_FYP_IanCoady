package dcu.ie.sdaiancoadyfinalproject;

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
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Some of the code for this class was adapted from the SDA 2020 course project FireNoSQLDatabase located @
 * loop.dcu.ie, with the permission of the code authors.
 * @author Chris Coughlan 2020, Ian Coady 2020.
 *
 * This class is the recycler view adapter for the device list, it sets the text view values and
 * also dictates how big the list will be based on the size of the database.
 */
public class DeviceViewAdapter extends RecyclerView.Adapter<DeviceViewAdapter.ViewHolder> {
    private Context             dNewContext;
    private ArrayList<Integer>  deviceID;
    private ArrayList<String>   deviceModel;
    private ArrayList<String>   environmentType;


    /**
     * Constructor for the custom device class. Takes the following parameters:
     * @param dNewContext The context of the custom class itself. Used to set intent values later on.
     * @param id    The unique ID or serial number in our case of the device.
     * @param model The model name of the devices.
     * @param environment The software environment the device is located in.
     */
    DeviceViewAdapter(Context dNewContext, ArrayList<Integer> id, ArrayList<String> model, ArrayList<String> environment){
        this.dNewContext        = dNewContext;
        this.deviceID           = id;
        this.deviceModel        = model;
        this.environmentType    = environment;
    }

    /**
     * OnCreateViewHolder sets the GUI file of the individual recycler view list item.
     * It calls the view file and inflates it inside of the parent GUI file.
     *
     * @param parent    This is the parent GUI file.
     * @param viewType  Integer value associated with the type of view being inflated.
     * @return          Returns the view to the parent for inflation inside the activity.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_device_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * onBindViewHolder acts almost like onCreate in a standard android activity. Th GUI elements
     * are called and assigned to variables and listeners and test values are all set in here.
     *
     * @param holder    Inner clas where the recycler view list item GUI file elements resides.
     * @param position  The respective position of the list item i.e. 1st list item, 2nd list item etc.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.deviceSN             .setText(String.valueOf(deviceID.get(position)));
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

    /**
     * Required getItemCount method sets the size of the recycler view. I.e. how many list items will ti contain.
     * @return Int value of the DeviceID ArrayList.
     */
    @Override
    public int getItemCount() {
        if(deviceID.size() == 0){
            Toast.makeText(dNewContext, "Click the add button to add a Device!", Toast.LENGTH_SHORT).show();
        }
        return deviceID.size();
    }


    /**
     * Inner class to call the Recycler View view holder.
     * Sets the GUI elements to variables that are called from onBindViewHolder.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView        deviceSN;
        TextView        deviceModel;
        TextView        deviceEnvironment;
        Button          editBtn;
        RelativeLayout  parentItemLayout;

        /**
         * GUI elements are set as variables here.
         * @param itemView view xml file passed to viewholder
         */
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
