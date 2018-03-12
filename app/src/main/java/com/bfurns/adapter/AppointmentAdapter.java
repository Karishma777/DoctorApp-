package com.bfurns.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bfurns.R;
import com.bfurns.activity.PatientProfileActivity;
import com.bfurns.application.AppClass;
import com.bfurns.model.AppointementModel;
import com.bfurns.model.DoctorStaffModel;
import com.bfurns.utility.MyPreferences;
import com.bfurns.utility.URLListner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Explicate1 on 12/9/2017.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolerModule>{


    private ArrayList<AppointementModel> arrayList;
    private Context context;
    public static boolean isSignedIn = false;


    public AppointmentAdapter(ArrayList<AppointementModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public AppointmentAdapter.MyViewHolerModule onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appintement_list_item, parent, false);

        return new AppointmentAdapter.MyViewHolerModule(layoutView,context,arrayList);
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.MyViewHolerModule holder, int position) {

        AppointementModel item=arrayList.get(position);

        holder.name.setText(item.getPatient_name());
        holder.time.setText(item.getTime());
        holder.category.setText(item.getApp_type());
        holder.date.setText(item.getDate());
        holder.contact.setText(item.getContact());
        String mode=item.getMode_of_appointement();
        if (mode.equals("0")){
            holder.mode.setText("App");

        }else if(mode.equals("1")){
            holder.mode.setText("Walkin");
        }else {
            holder.mode.setText("Telephonic");
        }


        String image=item.getImage();


        if(!image.equalsIgnoreCase("")){

            Picasso.with(context)
                    .load(URLListner.PATIENT_BASEURLL+image)
                    .into(holder.imageView);
        }else{

            Picasso.with(context).load(R.drawable.placeholder_user).resize(40,40).into(holder.imageView);
        }






    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static final class MyViewHolerModule extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imageView;
        TextView name,category,mode,time,date,contact;
        private ArrayList<AppointementModel> arrayList;
        private Context context;
        String id,user_id,bus_id,sub_id,doctor_id,app_status,app_date,app_type;

        public MyViewHolerModule(View itemView,Context context,ArrayList<AppointementModel> arrayList) {
            super(itemView);
            this.arrayList = arrayList;
            this.context = context;
            imageView = (CircleImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            category = (TextView) itemView.findViewById(R.id.category);
            mode = (TextView) itemView.findViewById(R.id.mode);
            date = (TextView) itemView.findViewById(R.id.Date);
            time = (TextView) itemView.findViewById(R.id.time);
            contact = (TextView) itemView.findViewById(R.id.contact);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();

            AppointementModel businessImage=this.arrayList.get(position);
            id=businessImage.getId();
            user_id=businessImage.getUser_id();
            bus_id=businessImage.getBus_id();
            sub_id=businessImage.getSub_user_id();
            doctor_id=businessImage.getDoct_id();
            app_status=businessImage.getApp_staus();
            app_date=businessImage.getDate();
            app_type=businessImage.getApp_type();

            AppClass.setAppointmentId(id);
            AppClass.setClinic_id(bus_id);
            AppClass.setSubID(sub_id);
            AppClass.SetUUserId(user_id);
            AppClass.setAppStatus(app_status);

            Intent intent=new Intent(this.context,PatientProfileActivity.class);
             intent.putExtra(MyPreferences.DATE,app_date);

            this.context.startActivity(intent);
        }
    }
}
