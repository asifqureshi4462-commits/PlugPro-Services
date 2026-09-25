package com.plugpro.data.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.plugpro.data.model.Booking;
import com.plugpro.data.model.Review;
import com.plugpro.utils.FirebaseUtil;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public interface BookingsCallback {
        void onSuccess(List<Booking> bookings);
        void onError(String message);
    }

    public interface BookingCallback {
        void onSuccess(Booking booking);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    public void createBooking(Booking booking, BookingCallback callback) {
        DocumentReference docRef = FirebaseUtil.getBookingsRef().document();
        booking.setId(docRef.getId());

        docRef.set(booking)
                .addOnSuccessListener(unused -> callback.onSuccess(booking))
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void getCustomerBookings(String customerId, BookingsCallback callback) {
        FirebaseUtil.getBookingsRef()
                .whereEqualTo("customerId", customerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Booking> list = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Booking b = doc.toObject(Booking.class);
                        if (b != null) {
                            b.setId(doc.getId());
                            list.add(b);
                        }
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void getProviderBookings(String providerId, BookingsCallback callback) {
        FirebaseUtil.getBookingsRef()
                .whereEqualTo("providerId", providerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Booking> list = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Booking b = doc.toObject(Booking.class);
                        if (b != null) {
                            b.setId(doc.getId());
                            list.add(b);
                        }
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void getBookingById(String bookingId, BookingCallback callback) {
        FirebaseUtil.getBookingsRef().document(bookingId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Booking b = doc.toObject(Booking.class);
                        if (b != null) {
                            b.setId(doc.getId());
                            callback.onSuccess(b);
                        } else {
                            callback.onError("Booking data is empty.");
                        }
                    } else {
                        callback.onError("Booking not found.");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void updateBookingStatus(String bookingId, String newStatus, SimpleCallback callback) {
        FirebaseUtil.getBookingsRef().document(bookingId)
                .update("status", newStatus)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }

    public void addReview(Review review, SimpleCallback callback) {
        DocumentReference docRef = FirebaseUtil.getReviewsRef().document();
        review.setId(docRef.getId());
        docRef.set(review)
                .addOnSuccessListener(unused -> {
                    // Update provider rating statistics
                    FirebaseUtil.getProvidersRef().document(review.getProviderId()).get()
                            .addOnSuccessListener(doc -> {
                                if (doc.exists()) {
                                    Double currentRating = doc.getDouble("rating");
                                    Long currentReviews = doc.getLong("reviewCount");
                                    if (currentRating == null) currentRating = 5.0;
                                    if (currentReviews == null) currentReviews = 0L;

                                    long newCount = currentReviews + 1;
                                    double newRating = ((currentRating * currentReviews) + review.getRating()) / newCount;

                                    FirebaseUtil.getProvidersRef().document(review.getProviderId())
                                            .update("rating", Math.round(newRating * 10.0) / 10.0,
                                                    "reviewCount", newCount);
                                }
                            });
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> callback.onError(e.getLocalizedMessage()));
    }
}
