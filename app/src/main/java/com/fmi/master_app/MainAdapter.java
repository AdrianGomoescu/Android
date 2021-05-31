package com.fmi.master_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    //Initialize variable
    Activity activity;
    ArrayList<String> arrayList;

    //Create constructor
    public MainAdapter(Activity activity, ArrayList<String> arrayList){
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer_main, parent, false);
        //Return holder view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Set text on text view
        holder.textView.setText(arrayList.get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get clicked item position
                int position = holder.getAdapterPosition();
                //Check condition
                switch (position){
                    case 0:
                        //When position is 0, redirect to home page
                        activity.startActivity(new Intent(activity, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;

                    case 1:
                        //When position is 1, redirect to dashboard page
                        activity.startActivity(new Intent(activity,Notes.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;

                    case 2:
                        //When position is 2, redirect to about page
                        activity.startActivity(new Intent(activity,About.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;

                    case 3:
                        //When position is 3, initialize alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        //Set title:
                        builder.setTitle("Logout");

                        //Set message
                        builder.setMessage("Are you sure you want to logout?");

                        //Answer is yes
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Finish all activity

                                activity.finishAffinity();
                                //Exit app
                                System.exit(0);
                            }
                        });

                        //Answer is no, cancel button
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Dismiss dialog
                                dialog.dismiss();
                            }
                        });
                        //Show dialog
                        builder.show();
                        break;

                }

            }
        });

    }

    @Override
    public int getItemCount() {

        //Return array list size
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Initialize variable
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            textView = itemView.findViewById(R.id.text_view);

        }
    }
}
