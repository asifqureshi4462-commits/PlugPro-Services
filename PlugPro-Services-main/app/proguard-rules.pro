# Add project specific ProGuard rules here.
# minifyEnabled is currently false for both build types in app/build.gradle,
# so this file is not actively applied — kept here so the file path
# referenced by build.gradle always resolves, and as a starting point if
# you enable code shrinking later.

# Keep Firestore model classes intact (they're de/serialized via reflection).
-keepclassmembers class com.plugpro.data.model.** {
    *;
}
