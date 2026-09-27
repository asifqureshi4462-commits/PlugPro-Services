package com.plugpro.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.plugpro.R;
import com.plugpro.data.model.ChatChannel;
import com.plugpro.utils.DateTimeUtil;
import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(ChatChannel channel);
    }

    private List<ChatChannel> channels = new ArrayList<>();
    private final boolean isProvider;
    private final OnChatClickListener listener;

    public ChatListAdapter(boolean isProvider, OnChatClickListener listener) {
        this.isProvider = isProvider;
        this.listener = listener;
    }

    public void setChannels(List<ChatChannel> list) {
        this.channels = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatChannel channel = channels.get(position);

        String partnerName = isProvider ? channel.getCustomerName() : channel.getProviderName();
        holder.tvPartnerName.setText(partnerName);
        holder.tvLastMessage.setText(channel.getLastMessage());
        holder.tvTime.setText(channel.getLastTimestamp() != null ? DateTimeUtil.formatChatTime(channel.getLastTimestamp()) : "");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onChatClick(channel);
        });
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvPartnerName, tvLastMessage, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivChatAvatar);
            tvPartnerName = itemView.findViewById(R.id.tvChatPartnerName);
            tvLastMessage = itemView.findViewById(R.id.tvChatLastMessage);
            tvTime = itemView.findViewById(R.id.tvChatTime);
        }
    }
}
