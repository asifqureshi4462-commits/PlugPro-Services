package com.plugpro.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.plugpro.R;
import com.plugpro.data.model.ChatChannel;
import com.plugpro.data.repository.ChatRepository;
import com.plugpro.ui.adapters.ChatListAdapter;
import com.plugpro.ui.chat.ChatActivity;
import com.plugpro.utils.FirebaseUtil;
import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView rvChatList;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private ChatListAdapter adapter;
    private ChatRepository chatRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        chatRepository = new ChatRepository();

        rvChatList = view.findViewById(R.id.rvChatList);
        progressBar = view.findViewById(R.id.progressBarChatList);
        layoutEmpty = view.findViewById(R.id.layoutEmptyChats);

        rvChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(false, channel -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("channel", channel);
            startActivity(intent);
        });
        rvChatList.setAdapter(adapter);

        loadChats();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChats();
    }

    private void loadChats() {
        progressBar.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);

        String userId = FirebaseUtil.getCurrentUserId();
        chatRepository.getUserChats(userId, false, new ChatRepository.ChannelsCallback() {
            @Override
            public void onSuccess(List<ChatChannel> channels) {
                progressBar.setVisibility(View.GONE);
                if (channels.isEmpty()) {
                    layoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    layoutEmpty.setVisibility(View.GONE);
                    adapter.setChannels(channels);
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}
