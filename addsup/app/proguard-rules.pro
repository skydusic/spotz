# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#Admob
-keep public class com.google.firebase.analytics.FirebaseAnalytics {
    public *;
}
-keep public class com.google.android.gms.measurement.AppMeasurement {
    public *;
}

#### -- OkHttp --

-dontwarn com.squareup.okhttp.internal.**

-keep interface com.birbit.android.jobqueue.** { *; }

-keepattributes Signature
-keepattributes *Annotation*

-dontwarn okio.**

-ignorewarnings

