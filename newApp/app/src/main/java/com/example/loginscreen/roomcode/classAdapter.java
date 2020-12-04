////////////////////////////////////////////
//TEAM BREAD
//classAdapter.Java
//PROGRAMMERS: Ryan
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.Room.Room;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//Adapter for the classview. Implementation taken from a tutorial.
public class classAdapter extends RecyclerView.Adapter<classAdapter.ViewHolder> {

    //send a list of classes to be shown.
    private ArrayList<Room> classes;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference userRef = database.getReference("Proproct/Classes");
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public classAdapter(ArrayList<Room> c){
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
        Room className = classes.get(position);



        TextView nameView = holder.classNameView;
        nameView.setText(className.roomName);
        TextView codeView = holder.classCodeView;
        codeView.setText("Code: " + className.roomCode);
        

    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classNameView;
        public TextView classCodeView;

        public ViewHolder(final View itemView) {
            super(itemView);
            classNameView = itemView.findViewById(R.id.classListItemText);
            classCodeView = itemView.findViewById(R.id.classListCodeText);
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
