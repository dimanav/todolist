package com.example.mytodoapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private  RoomDB database;

    AlertDialog.Builder builder;



    public MainAdapter(List<MainData> dataList, Activity context) {
        this.dataList = dataList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        MainData data=dataList.get(position);
        database=RoomDB.getInstance(context);
        holder.textView.setText(data.getText());


        holder.btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder= new AlertDialog.Builder(v.getContext());

                builder.setMessage("Подтвердить выполнение задачи?")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                MainData d=dataList.get(holder.getAdapterPosition());

                                database.mainDao().delete(d);
                                int position = holder.getAdapterPosition();
                                dataList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,dataList.size());

                                System.out.println(d.getID());
                                Intent intent = new Intent(context, AlarmReceiver.class);

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, d.getID(), intent, 0);

                                AlarmManager alarmManager = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                                }

                                alarmManager.cancel(pendingIntent);

                                Toast.makeText(context,"Задача выполнена!",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Подтверждение");
                alert.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView btCheck;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view);
            btCheck=itemView.findViewById(R.id.bt_check);
        }
    }
}
