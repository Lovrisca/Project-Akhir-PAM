package com.example.project_akhir_pam;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class DashboardAdapter extends FirebaseRecyclerAdapter<Note,DashboardAdapter.myviewholder>
{    public DashboardAdapter(@NonNull FirebaseRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull Note model) {
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getDate());
        String imgUri = model.getImage();
        Log.e("Adapter get img uri", imgUri);
        if(!imgUri.isEmpty()){
            Picasso.get().load(imgUri).placeholder(R.drawable.logo).into(holder.img);
        }

        //click layout
        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
//                        .setContentHolder(new ViewHolder(R.layout.activity_update))
//                        .setExpanded(true, 1800)
//                        .create();
//
//                View myview = dialogPlus.getHolderView();
//                final EditText title = myview.findViewById(R.id.et_title);
//                final EditText description = myview.findViewById(R.id.et_desc);
//                Button submit = myview.findViewById(R.id.btn_save);
//
//                title.setText(model.getTitle());
//                description.setText(model.getDescription());
//                dialogPlus.show();
//                submit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("title", title.getText().toString());
//                        map.put("description", description.getText().toString());
//                        map.put("date", getCurrentDate() + "-updated");
//
//                        FirebaseDatabase.getInstance().getReference().child("news")
//                                .child(getRef(position).getKey()).updateChildren(map)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(view.getContext(), "Updated", Toast.LENGTH_LONG).show();
//                                        dialogPlus.dismiss();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        dialogPlus.dismiss();
//                                    }
//                                });
//                    }
//                });
//            }
//
            ///////////////////////////////////////////////////////////////////
            @Override
            public void onClick(View view) {
                String ref = String.valueOf(getRef(position).getKey());
                Intent intent = new Intent(view.getContext(), NewsDetails.class);
                intent.putExtra("id", ref);
                Log.e("id reference", ref);
                view.getContext().startActivity(intent);
                ((Activity)view.getContext()).finish();
            }
        });


        //click update
        holder.update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
//                        .setContentHolder(new ViewHolder(R.layout.activity_update))
//                        .setExpanded(true, 1800)
//                        .create();
//
//                View myview = dialogPlus.getHolderView();
//                final EditText title = myview.findViewById(R.id.et_title);
//                final EditText description = myview.findViewById(R.id.et_desc);
//                Button submit = myview.findViewById(R.id.btn_save);
//
//                title.setText(model.getTitle());
//                description.setText(model.getDescription());
//                dialogPlus.show();
//                submit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("title", title.getText().toString());
//                        map.put("description", description.getText().toString());
//                        map.put("date", getCurrentDate() + "-updated");
//
//                        FirebaseDatabase.getInstance().getReference().child("news")
//                                .child(getRef(position).getKey()).updateChildren(map)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(view.getContext(), "Updated", Toast.LENGTH_LONG).show();
//                                        dialogPlus.dismiss();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        dialogPlus.dismiss();
//                                    }
//                                });
//                    }
//                });
//            }

/////////////////////////////////////////////////////////////////////
                        @Override
            public void onClick(View view) {
                String ref = String.valueOf(getRef(position).getKey());
                Intent intent = new Intent(view.getContext(), UpdateActivity.class);
                intent.putExtra("id", ref);
                Log.e("id reference", ref);
                view.getContext().startActivity(intent);
                ((Activity)view.getContext()).finish();
            }
        });
            ///////////////////////////////////////////////////////////////////////////
            //coba cara lain

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
                        Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_LONG).show();
                        ((Activity)view.getContext()).finish();
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
            img = itemView.findViewById(R.id.img);
            update = itemView.findViewById(R.id.btn_update);
            delete = itemView.findViewById(R.id.btn_delete);

            title =itemView.findViewById(R.id.tv_title);
            date = itemView.findViewById(R.id.tv_date);
        }
    }

    //helper
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}