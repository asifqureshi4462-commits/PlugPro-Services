package com.plugpro.data.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.plugpro.data.model.ChatChannel;
import com.plugpro.data.model.ChatMessage;
import com.plugpro.utils.FirebaseUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRepository {

    public interface ChannelsCallback {
        void onSuccess(List<ChatChannel> channels);
        void onError(String message);
    }

    public interface ChannelCallback {
        void onSuccess(ChatChannel channel);
        void onError(String message);
    }

    public interface MessagesListener {
        void onMessagesLoaded(List<ChatMessage> messages);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    public void getOrCreateChannel(String customerId, String customerName,
                                   String providerId, String providerName,
                                   ChannelCallback callback) {
        String channelId = customerId + "_" + providerId;
        FirebaseUtil.getChatsRef().document(channelId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        ChatChannel channel = doc.toObject(ChatChannel.class);
                        if (channel != null) {
                            channel.setId(doc.getId());
                            callback.onSuccess(channel);
                            return;
                        }
                    }
                    // Create new
                    ChatChannel newChannel = new ChatChannel(channelId, customerId, customerName, providerId, providerName);
                    FirebaseUtil.getChatsRef().document(channelId).set(newChannel)
                            .addOnSuccessListener(unused -> callback.onSuccess(newChannel))
                            .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void getUserChats(String userId, boolean isProvider, ChannelsCallback callback) {
        String field = isProvider ? "providerId" : "customerId";
        FirebaseUtil.getChatsRef().whereEqualTo(field, userId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ChatChannel> list = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        ChatChannel c = doc.toObject(ChatChannel.class);
                        if (c != null) {
                            c.setId(doc.getId());
                            list.add(c);
                        }
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public ListenerRegistration listenMessages(String chatId, MessagesListener listener) {
        return FirebaseUtil.getChatsRef().document(chatId).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        listener.onError(error.getLocalizedMessage());
                        return;
                    }
                    if (snapshots != null) {
                        List<ChatMessage> list = new ArrayList<>();
                        for (DocumentSnapshot doc : snapshots) {
                            ChatMessage msg = doc.toObject(ChatMessage.class);
                            if (msg != null) {
                                msg.setId(doc.getId());
                                list.add(msg);
                            }
                        }
                        listener.onMessagesLoaded(list);
                    }
                });
    }

    public void sendMessage(String chatId, ChatMessage message, SimpleCallback callback) {
        DocumentReference docRef = FirebaseUtil.getChatsRef().document(chatId).collection("messages").document();
        message.setId(docRef.getId());
        message.setTimestamp(new Date());

        docRef.set(message)
                .addOnSuccessListener(unused -> {
                    // Update chat channel last message
                    FirebaseUtil.getChatsRef().document(chatId)
                            .update("lastMessage", message.getMessage(), "lastTimestamp", new Date());
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }
}
