package com.example.loginscreen.roomcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginscreen.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class classAdapter extends RecyclerView.Adapter<classAdapter.ViewHolder> {

    //send a list of classes to be shown.
    private ArrayList<String> classes;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference userRef = database.getReference("Proproct/Classes");
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public classAdapter(ArrayList<String> c){
        classes = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View classView = inflater.inflate(R.layout.class_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(classView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String className = classes.get(position);

        TextView tView = holder.nameTextView;
        tView.setText(className);

    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.classListItemText);
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
