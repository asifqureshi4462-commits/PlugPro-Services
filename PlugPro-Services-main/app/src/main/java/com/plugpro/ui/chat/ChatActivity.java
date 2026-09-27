package com.plugpro.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.ListenerRegistration;
import com.plugpro.R;
import com.plugpro.data.model.ChatChannel;
import com.plugpro.data.model.ChatMessage;
import com.plugpro.data.repository.ChatRepository;
import com.plugpro.ui.adapters.MessageAdapter;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTopName;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private View btnSend;

    private ChatChannel channel;
    private ChatRepository chatRepository;
    private MessageAdapter adapter;
    private PreferenceHelper prefs;
    private ListenerRegistration messageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRepository = new ChatRepository();
        prefs = new PreferenceHelper(this);

        channel = (ChatChannel) getIntent().getSerializableExtra("channel");

        btnBack = findViewById(R.id.btnChatBack);
        tvTopName = findViewById(R.id.tvChatTopName);
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etChatMessage);
        btnSend = findViewById(R.id.btnSendMessage);

        btnBack.setOnClickListener(v -> finish());

        String currentUserId = FirebaseUtil.getCurrentUserId();
        boolean isProvider = "provider".equalsIgnoreCase(prefs.getUserRole());

        if (channel != null) {
            String partnerName = isProvider ? channel.getCustomerName() : channel.getProviderName();
            tvTopName.setText(partnerName);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);

        adapter = new MessageAdapter(currentUserId);
        rvMessages.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendMessage());

        startListeningMessages();
    }

    private void startListeningMessages() {
        if (channel == null || channel.getId() == null) return;

        messageListener = chatRepository.listenMessages(channel.getId(), new ChatRepository.MessagesListener() {
            @Override
            public void onMessagesLoaded(List<ChatMessage> messages) {
                adapter.setMessages(messages);
                if (!messages.isEmpty()) {
                    rvMessages.smoothScrollToPosition(messages.size() - 1);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ChatActivity.this, "Error loading messages: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty() || channel == null) return;

        String currentUserId = FirebaseUtil.getCurrentUserId();
        String currentUserName = prefs.getUserName();
        boolean isProvider = "provider".equalsIgnoreCase(prefs.getUserRole());
        String receiverId = isProvider ? channel.getCustomerId() : channel.getProviderId();

        ChatMessage msg = new ChatMessage(channel.getId(), currentUserId, currentUserName, receiverId, text);
        etMessage.setText("");

        chatRepository.sendMessage(channel.getId(), msg, new ChatRepository.SimpleCallback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(String message) {
                Toast.makeText(ChatActivity.this, "Failed to send: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageListener != null) {
            messageListener.remove();
        }
    }
}
