////////////////////////////////////////////
//TEAM BREAD
//examClassAdapter.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V3 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.Room.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//Backend of the exam view, essentially tells the recyclerview how to convert the data into each item.
public class examClassAdapter extends RecyclerView.Adapter<examClassAdapter.ViewHolder> {

    //send a list of classes to be shown.
    private ArrayList<Room> exams;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference userRef = database.getReference("Proproct/Exams");
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public examClassAdapter(ArrayList<Room> e){
        exams = e;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View classView = inflater.inflate(R.layout.exam_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(classView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room examName = exams.get(position);
        TextView nameView = holder.examNameView;
        nameView.setText(examName.roomName);
        TextView codeView = holder.examCodeView;
        codeView.setText("Code: " + examName.roomCode);
        TextView timeView = holder.examTimeView;
        TextView dateView = holder.examDateView;
        DatabaseReference nExam = database.getReference("Proproct/Exams/" + examName.roomCode);
        nExam.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeView.setText(snapshot.child("startTimeHour").getValue().toString() + ":" + snapshot.child("startTimeMin").getValue().toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, YYYY", Locale.getDefault());
                int examDay =  Integer.parseInt(snapshot.child("dateDay").getValue().toString());
                int examMonth = Integer.parseInt(snapshot.child("dateMonth").getValue().toString());
                int examYear = Integer.parseInt(snapshot.child("dateYear").getValue().toString());
                Date examDate = new Date(examYear - 1900, examMonth - 1, examDay);
                String date = dateFormat.format(examDate);
                dateView.setText(date);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        

    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView examNameView;
        public TextView examCodeView;
        public TextView examDateView;
        public TextView examTimeView;

        public ViewHolder(final View itemView) {
            super(itemView);
            examNameView = itemView.findViewById(R.id.examListName);
            examCodeView = itemView.findViewById(R.id.examListCode);
            examDateView = itemView.findViewById(R.id.examListDate);
            examTimeView = itemView.findViewById(R.id.examListTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
