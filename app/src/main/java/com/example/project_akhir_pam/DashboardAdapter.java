package com.example.project_akhir_pam;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardAdapter extends FirebaseRecyclerAdapter<Note,DashboardAdapter.myviewholder>
{

    public DashboardAdapter(@NonNull FirebaseRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DashboardAdapter.myviewholder holder, int position, @NonNull Note model) {
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getDate());

        //click layout
        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NewsDetails.class);
            intent.putExtra("id", (CharSequence) FirebaseDatabase.getInstance().getReference().child("news")
                    .child(getRef(position).getKey()));
            v.getContext().startActivity(intent);
        });

        //button update
        holder.update.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UpdateActivity.class);
            intent.putExtra("id", (CharSequence) FirebaseDatabase.getInstance().getReference().child("news")
                    .child(getRef(position).getKey()));
            v.getContext().startActivity(intent);
        });

        //button delete
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("news")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        LinearLayout layout;
        ImageView update,delete, img;
        TextView title,date;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            layout = itemView.findViewById(R.id.main_layout);
            img = (ImageView) itemView.findViewById(R.id.img);
            update = (ImageView)itemView.findViewById(R.id.btn_update);
            delete = (ImageView)itemView.findViewById(R.id.btn_delete);

            title = (TextView)itemView.findViewById(R.id.tv_title);
            date = (TextView)itemView.findViewById(R.id.tv_date);
        }
    }
}