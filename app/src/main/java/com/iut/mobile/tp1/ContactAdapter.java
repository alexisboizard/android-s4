package com.iut.mobile.tp1;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iut.mobile.tp1.model.UserData;

import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    public ArrayList<UserData> contactList;
    public Context context;


    public ContactAdapter(Context context, ArrayList<UserData> contactList) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_item_contact,parent,false);
        return new ContactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        UserData ud = contactList.get(position);
        holder.tv_name.setText(ud.firstname + " " + ud.name.toUpperCase());
        holder.tv_phone.setText(ud.phone);
        if(ud.avatar_path == ""){
            holder.iv_avatar.setImageResource(R.drawable.default_avatar);
        }else{
            holder.iv_avatar.setImageURI(Uri.parse(ud.avatar_path));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,DetailActivity.class);
                i.putExtra(DetailActivity.KEY_DATA, ud);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_phone;
        ImageView iv_avatar;
        ArrayList<UserData> contactList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_title);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

}