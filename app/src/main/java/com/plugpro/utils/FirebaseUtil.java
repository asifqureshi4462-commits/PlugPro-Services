package com.plugpro.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {
    private static FirebaseAuth auth;
    private static FirebaseFirestore firestore;
    private static FirebaseStorage storage;

    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static FirebaseFirestore getFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            firestore.setFirestoreSettings(settings);
        }
        return firestore;
    }

    public static FirebaseStorage getStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();
        }
        return storage;
    }

    public static FirebaseUser getCurrentUser() {
        return getAuth().getCurrentUser();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    public static boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    // Collections
    public static CollectionReference getUsersRef() {
        return getFirestore().collection("users");
    }

    public static CollectionReference getProvidersRef() {
        return getFirestore().collection("providers");
    }

    public static CollectionReference getServicesRef() {
        return getFirestore().collection("services");
    }

    public static CollectionReference getBookingsRef() {
        return getFirestore().collection("bookings");
    }

    public static CollectionReference getReviewsRef() {
        return getFirestore().collection("reviews");
    }

    public static CollectionReference getFavoritesRef() {
        return getFirestore().collection("favorites");
    }

    public static CollectionReference getChatsRef() {
        return getFirestore().collection("chats");
    }

    public static StorageReference getProfileImagesRef() {
        return getStorage().getReference().child("profile_images");
    }
}
