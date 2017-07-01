package com.example.kaildyhoang.storagedemo;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Microsoft Windows on 23/06/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ShowDataViewHolder>{

    private List<ShowUserDataItemsActivity> userList;

    public static class ShowDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private String mItem;
        private final TextView image_title;
        private final ImageView image_url;
        private final Context context;
//        private final TextView _userID, name,userName, email, password, address, description, gender, idFriendsList, idBookmarksList,
//                countOfPosts ,countOfFriends, countOfBookmarks, birthday, createDay;

        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
            image_title = (TextView) itemView.findViewById(R.id.fetch_title);
            image_url = (ImageView) itemView.findViewById(R.id.fetch_image);
//            _userID = (TextView) itemView.findViewById(R.id.fetch_userId);
//            userName = (TextView) itemView.findViewById(R.id.fetch_userName);
//            name = (TextView) itemView.findViewById(R.id.fetch_name);
//            email = (TextView) itemView.findViewById(R.id.fetch_email);
//            password = (TextView) itemView.findViewById(R.id.fetch_password);
//            address = (TextView) itemView.findViewById(R.id.fetch_address);
//            description = (TextView) itemView.findViewById(R.id.fetch_description);
//            gender = (TextView) itemView.findViewById(R.id.fetch_gender);
//            idFriendsList = (TextView) itemView.findViewById(R.id.fetch_idFriendsList);
//            idBookmarksList = (TextView) itemView.findViewById(R.id.fetch_idBookmarksList);
//            countOfPosts  = (TextView) itemView.findViewById(R.id.fetch_countOfPosts);
//            countOfFriends = (TextView) itemView.findViewById(R.id.fetch_countOfFriends);
//            countOfBookmarks = (TextView) itemView.findViewById(R.id.fetch_countOfBookmarks);
//            birthday = (TextView) itemView.findViewById(R.id.fetch_birthday);
//            createDay = (TextView) itemView.findViewById(R.id.fetch_createDay);
        }

        public void setItem(String item){
            mItem = item;
        }
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Dy:onClick " + "position:" + getPosition() + "mItem:" + mItem);
            Intent mIntent = new Intent(context, UpdateUserActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("_IdBundle",mItem);
            mIntent.putExtras(mBundle);
            context.startActivity(mIntent);
        }
    }

    public UserAdapter(List<ShowUserDataItemsActivity> userList){
        this.userList = userList;
    }

    @Override
    public ShowDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_show_data_items,parent,false);
        return new ShowDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShowDataViewHolder holder, int position) {

        ShowUserDataItemsActivity user = userList.get(position);

//        holder._userID.setText(user.get_idUser());
//        holder.name.setText(user.getName());
//        holder.email.setText(user.getEmail());
//        holder.userName.setText(user.getUserName());
//        holder.password.setText(user.getPassword());
//        holder.address.setText(user.getAddress());
//        holder.gender.setText(user.getGender());
//        holder.idFriendsList.setText(user.getIdFriendsList());
//        holder.idBookmarksList.setText(user.getIdBookmarksList());
//        holder.countOfPosts.setText(String.valueOf(user.getCountOfPosts()));
//        holder.countOfFriends.setText(String.valueOf(user.getCountOfFriends()));
//        holder.countOfBookmarks.setText(String.valueOf(user.getCountOfBookmarks()));
//        holder.birthday.setText(String.valueOf(user.getBirthday()));
//        holder.createDay.setText(String.valueOf(user.getCreateDay()));

        holder.setItem(String.valueOf(user.get_idUser()));
        holder.image_title.setText(
                "ID: " + user.get_idUser() + "\n"
                + "Name: " + user.getName() + "\n"
                + "Email: " +user.getEmail() + "\n"
                + "UserName: " +user.getUserName() + "\n"
                + "Password: " +user.getPassword() + "\n"
                + "Address: " +user.getAddress() + "\n"
                + "Gender: " +user.getGender() + "\n"
                + "Birthday: " +user.getBirthday() + "\n"
                + "CreateDay: " +user.getCreateDay() + "\n"
        );
        Glide.with(holder.image_url.getContext())
                .load(user.getImage_Url())
                .crossFade()
                .placeholder(R.drawable.load_icon)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image_url);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
