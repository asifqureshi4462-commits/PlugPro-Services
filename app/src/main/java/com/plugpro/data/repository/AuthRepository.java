package com.plugpro.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.plugpro.data.model.User;
import com.plugpro.utils.FirebaseUtil;

public class AuthRepository {

    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    public void login(String email, String password, AuthCallback callback) {
        FirebaseUtil.getAuth().signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        fetchUserProfile(firebaseUser.getUid(), callback);
                    } else {
                        callback.onError("User authentication failed.");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void signup(String name, String email, String phone, String password, String role, AuthCallback callback) {
        FirebaseUtil.getAuth().createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        User user = new User(firebaseUser.getUid(), name, email, phone, role);
                        FirebaseUtil.getUsersRef().document(user.getId()).set(user)
                                .addOnSuccessListener(unused -> callback.onSuccess(user))
                                .addOnFailureListener(e -> callback.onError("Failed to save profile: " + e.getMessage()));
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void resetPassword(String email, SimpleCallback callback) {
        FirebaseUtil.getAuth().sendPasswordResetEmail(email.trim())
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void fetchUserProfile(String uid, AuthCallback callback) {
        FirebaseUtil.getUsersRef().document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        callback.onSuccess(user);
                    } else {
                        callback.onError("User profile document not found.");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void logout() {
        FirebaseUtil.getAuth().signOut();
    }
}
