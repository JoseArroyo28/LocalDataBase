package com.tekno.localdatabase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tekno.localdatabase.R;
import com.tekno.localdatabase.entities.Users;

import java.util.ArrayList;

public class listUserAdapter extends RecyclerView.Adapter<listUserAdapter.userViewHolder> {
    ArrayList<Users> listUsers;
    public listUserAdapter(ArrayList<Users> listUsers){
        this.listUsers = listUsers;
    }
    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View  view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, null,false);*/
        //return new userViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder  {
        //TextView name = new TextView(this);
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
