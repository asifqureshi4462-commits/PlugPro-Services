package com.plugpro.data.repository;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.plugpro.data.model.Favorite;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.utils.FirebaseUtil;
import java.util.ArrayList;
import java.util.List;

public class ProviderRepository {

    public interface ProvidersCallback {
        void onSuccess(List<ServiceProvider> providers);
        void onError(String message);
    }

    public interface ProviderCallback {
        void onSuccess(ServiceProvider provider);
        void onError(String message);
    }

    public interface BooleanCallback {
        void onResult(boolean result);
    }

    public void getRecommendedProviders(ProvidersCallback callback) {
        FirebaseUtil.getProvidersRef()
                .orderBy("rating", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ServiceProvider> list = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        ServiceProvider p = doc.toObject(ServiceProvider.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            list.add(p);
                        }
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void getProvidersByCategory(String categoryId, ProvidersCallback callback) {
        FirebaseUtil.getProvidersRef()
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ServiceProvider> list = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        ServiceProvider p = doc.toObject(ServiceProvider.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            list.add(p);
                        }
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void searchProviders(String query, ProvidersCallback callback) {
        String q = query.trim().toLowerCase();
        FirebaseUtil.getProvidersRef().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ServiceProvider> list = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        ServiceProvider p = doc.toObject(ServiceProvider.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            boolean matchesName = p.getName().toLowerCase().contains(q);
                            boolean matchesProfession = p.getProfession().toLowerCase().contains(q);
                            boolean matchesAbout = p.getAbout().toLowerCase().contains(q);
                            boolean matchesArea = p.getServiceArea().toLowerCase().contains(q);
                            if (matchesName || matchesProfession || matchesAbout || matchesArea) {
                                list.add(p);
                            }
                        }
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void getProviderById(String id, ProviderCallback callback) {
        FirebaseUtil.getProvidersRef().document(id).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        ServiceProvider p = doc.toObject(ServiceProvider.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            callback.onSuccess(p);
                        } else {
                            callback.onError("Provider data is empty.");
                        }
                    } else {
                        callback.onError("Provider not found.");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void registerProvider(ServiceProvider provider, ProviderCallback callback) {
        String docId = provider.getId();
        if (docId == null || docId.isEmpty()) {
            docId = FirebaseUtil.getProvidersRef().document().getId();
            provider.setId(docId);
        }
        FirebaseUtil.getProvidersRef().document(docId).set(provider)
                .addOnSuccessListener(unused -> callback.onSuccess(provider))
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void isFavorite(String customerId, String providerId, BooleanCallback callback) {
        String favId = customerId + "_" + providerId;
        FirebaseUtil.getFavoritesRef().document(favId).get()
                .addOnSuccessListener(doc -> callback.onResult(doc.exists()))
                .addOnFailureListener(e -> callback.onResult(false));
    }

    public void toggleFavorite(String customerId, String providerId, BooleanCallback callback) {
        String favId = customerId + "_" + providerId;
        FirebaseUtil.getFavoritesRef().document(favId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        FirebaseUtil.getFavoritesRef().document(favId).delete()
                                .addOnSuccessListener(unused -> callback.onResult(false));
                    } else {
                        Favorite fav = new Favorite(customerId, providerId);
                        FirebaseUtil.getFavoritesRef().document(favId).set(fav)
                                .addOnSuccessListener(unused -> callback.onResult(true));
                    }
                });
    }
}
