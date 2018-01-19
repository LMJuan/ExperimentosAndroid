package com.lme.android.experimentosjava;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvName, tvLastName, tvEmail;

        UserViewHolder(View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.img_itemlist_user_thumb);
            tvName = itemView.findViewById(R.id.tv_itemlist_user_name);
            tvLastName = itemView.findViewById(R.id.tv_itemlist_user_lastname);
            tvEmail = itemView.findViewById(R.id.tv_itemlist_user_email);
        }
    }

    private List<User> userList;

    UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.imgThumb.setImageBitmap(user.getPicture());
        holder.tvName.setText(user.getName());
        holder.tvLastName.setText(user.getLastName());
        holder.tvEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
