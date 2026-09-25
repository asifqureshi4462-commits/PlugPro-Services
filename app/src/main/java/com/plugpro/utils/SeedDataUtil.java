package com.plugpro.utils;

import com.google.firebase.firestore.DocumentSnapshot;
import com.plugpro.data.model.ServiceCategory;
import com.plugpro.data.model.ServiceProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeedDataUtil {

    public static void checkAndSeedData() {
        FirebaseUtil.getServicesRef().limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                seedCategories();
            }
        });

        FirebaseUtil.getProvidersRef().limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                seedInitialProviders();
            }
        });
    }

    private static void seedCategories() {
        List<ServiceCategory> categories = new ArrayList<>();
        categories.add(new ServiceCategory("cat_repair", "Repairing", "Appliance, switch, and circuit repair services", "ic_services", 299));
        categories.add(new ServiceCategory("cat_install", "Installation", "Fan, TV mount, inverter, and appliance setup", "ic_services", 349));
        categories.add(new ServiceCategory("cat_rewire", "Rewiring", "Full house wiring and fuse replacements", "ic_services", 499));
        categories.add(new ServiceCategory("cat_plumbing", "Plumbing", "Pipe leakages, taps, and bathroom fixtures", "ic_services", 249));
        categories.add(new ServiceCategory("cat_ac", "AC Repair", "AC servicing, gas refilling, and cooling repair", "ic_services", 399));
        categories.add(new ServiceCategory("cat_clean", "Cleaning", "Deep home and kitchen deep cleaning", "ic_services", 599));
        categories.add(new ServiceCategory("cat_paint", "Painting", "Wall painting, waterproofing, and touch-ups", "ic_services", 899));
        categories.add(new ServiceCategory("cat_carpenter", "Carpentry", "Furniture repair, locks, and custom woodwork", "ic_services", 349));

        for (ServiceCategory category : categories) {
            FirebaseUtil.getServicesRef().document(category.getId()).set(category);
        }
    }

    private static void seedInitialProviders() {
        ServiceProvider pro1 = new ServiceProvider(
                "pro_james_carter",
                "user_james",
                "James Carter",
                "Master Electrician",
                "cat_repair",
                8,
                450,
                "Certified master electrician with 8+ years of residential and commercial experience. Specialized in high-voltage panels, short circuit troubleshooting, and smart home automation.",
                "Downtown & Metro Suburbs",
                "+1 (555) 234-5678",
                "james.carter@plugpro.com"
        );
        pro1.setRating(4.9);
        pro1.setReviewCount(128);
        pro1.setCompletedJobs(210);
        pro1.setVerifiedStatus("verified");
        pro1.setProfileImageUrl("https://images.unsplash.com/photo-1560250097-0b93528c311a?w=400&auto=format&fit=crop&q=80");
        pro1.setAvailableDays(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
        pro1.setAvailableTimeSlots(Arrays.asList("09:00 AM", "11:00 AM", "02:00 PM", "04:00 PM", "06:00 PM"));
        FirebaseUtil.getProvidersRef().document(pro1.getId()).set(pro1);

        ServiceProvider pro2 = new ServiceProvider(
                "pro_sarah_miller",
                "user_sarah",
                "Sarah Miller",
                "Plumbing Specialist",
                "cat_plumbing",
                6,
                380,
                "Licensed plumber handling all pipe repair, bathroom fittings, water heater installations, and emergency leakage fixes.",
                "North & West Metro",
                "+1 (555) 345-6789",
                "sarah.miller@plugpro.com"
        );
        pro2.setRating(4.8);
        pro2.setReviewCount(94);
        pro2.setCompletedJobs(145);
        pro2.setVerifiedStatus("verified");
        pro2.setProfileImageUrl("https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80");
        pro2.setAvailableDays(Arrays.asList("Monday", "Wednesday", "Thursday", "Friday", "Saturday"));
        pro2.setAvailableTimeSlots(Arrays.asList("10:00 AM", "01:00 PM", "03:00 PM", "05:00 PM"));
        FirebaseUtil.getProvidersRef().document(pro2.getId()).set(pro2);

        ServiceProvider pro3 = new ServiceProvider(
                "pro_david_chen",
                "user_david",
                "David Chen",
                "HVAC & AC Technician",
                "cat_ac",
                10,
                500,
                "HVAC certified technician specializing in central AC diagnostics, split AC cleaning, coil replacements, and energy efficiency audits.",
                "South City & Central",
                "+1 (555) 456-7890",
                "david.chen@plugpro.com"
        );
        pro3.setRating(5.0);
        pro3.setReviewCount(167);
        pro3.setCompletedJobs(310);
        pro3.setVerifiedStatus("verified");
        pro3.setProfileImageUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80");
        pro3.setAvailableDays(Arrays.asList("Monday", "Tuesday", "Wednesday", "Friday", "Sunday"));
        pro3.setAvailableTimeSlots(Arrays.asList("08:00 AM", "11:00 AM", "02:00 PM", "05:00 PM"));
        FirebaseUtil.getProvidersRef().document(pro3.getId()).set(pro3);
    }
}
