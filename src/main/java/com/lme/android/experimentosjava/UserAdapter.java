package com.lme.android.experimentosjava;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements View.OnClickListener {

    private List<User> userList;
    private Context context;
    private int lastPosition = -1;
    private View.OnClickListener listener;

    UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        itemView.setOnClickListener(this);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.imgThumb.setImageBitmap(user.getPicture());
        holder.tvName.setText(user.getName());
        holder.tvLastName.setText(user.getLastName());
        holder.tvEmail.setText(user.getEmail());

        animateItem(holder, position);
    }

    private void animateItem(UserAdapter.UserViewHolder holder, int position) {
        if(position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.left_from_right_short);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumb;
        public TextView tvName, tvLastName, tvEmail;

        public UserViewHolder(View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.img_itemlist_user_thumb);
            tvName = itemView.findViewById(R.id.tv_itemlist_user_name);
            tvLastName = itemView.findViewById(R.id.tv_itemlist_user_lastname);
            tvEmail = itemView.findViewById(R.id.tv_itemlist_user_email);
        }
    }

    public void resetLastPosition() {
        lastPosition = -1;
    }
}
