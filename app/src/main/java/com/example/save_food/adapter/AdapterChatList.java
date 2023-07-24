package com.example.save_food.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.save_food.R;
import com.example.save_food.chat;
import com.example.save_food.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.Myholder> {

	Context context;
	FirebaseAuth firebaseAuth;
	String uid;

	public AdapterChatList(Context context, List<ModelUser> users) {
		this.context = context;
		this.usersList = users;
		lastMessageMap = new HashMap<>();
		firebaseAuth = FirebaseAuth.getInstance();
		uid = firebaseAuth.getUid();
	}

	List<ModelUser> usersList;
	private final HashMap<String, String> lastMessageMap;

	@NonNull
	@Override
	public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
		return new Myholder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull Myholder holder, final int position) {

		final String hisuid = usersList.get(position).getUid();
		String strImage = usersList.get(position).getImage();
		String strName = usersList.get(position).getName();
		String lastmess = lastMessageMap.get(hisuid);
		holder.name.setText(strName);
		holder.block.setImageResource(R.drawable.ic_baseline_block_24);
		
		// if no last message then Hide the layout
		if (lastmess == null || lastmess.equals("default")) {
			holder.lastmessage.setVisibility(View.GONE);
		} else {
			holder.lastmessage.setVisibility(View.VISIBLE);
			holder.lastmessage.setText(lastmess);
		}
		try {
			// loading profile pic of user
			Glide.with(context).load(strImage).placeholder(R.drawable.person).into(holder.profile);		} catch (Exception e) {
			Glide.with(context).load(R.drawable.person).into(holder.profile);
		}
		
		// redirecting to chat activity on item click
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, chat.class);
				
				// putting uid of user in extras
				intent.putExtra("hisUid", hisuid);
				context.startActivity(intent);
			}
		});

	}

	// setting last message sent by users.
	public void setlastMessageMap(String userId, String lastmessage) {
		lastMessageMap.put(userId, lastmessage);
	}

	@Override
	public int getItemCount() {
		return usersList.size();
	}

	static class Myholder extends RecyclerView.ViewHolder {
		ImageView profile, status, block, seen;
		TextView name, lastmessage;

		public Myholder(@NonNull View itemView) {
			super(itemView);
			profile = itemView.findViewById(R.id.profileimage);
			status = itemView.findViewById(R.id.onlinestatus);
			name = itemView.findViewById(R.id.nameonline);
			lastmessage = itemView.findViewById(R.id.lastmessge);
			block = itemView.findViewById(R.id.blocking);
			seen = itemView.findViewById(R.id.seen);
		}
	}
}
