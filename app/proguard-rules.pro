# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/libit/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include fileName and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-dontoptimize
-dontpreverify

-dontwarn demo.**
#-dontskipnonpubliclibraryclassmembers
-libraryjars libs/pinyin4j-2.5.0.jar
-libraryjars libs/gson-2.2.4.jar
-libraryjars libs/org.apache.http.legacy.jar
-libraryjars libs/signpost-commonshttp4-1.2.1.1.jar
-libraryjars libs/signpost-core-1.2.1.1.jar

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn android.support.**

#-keep public class com.lrcall.models.AppInfo
#-keep public class com.lrcall.models.BlackAppInfo
#-keep public class com.lrcall.models.HideAppInfo
#-keep public class com.lrcall.models.BackupInfo
-keep public class com.lrcall.models.*
-keepclassmembers public class com.lrcall.models.* {
   void set*(***);
   *** get*();
}

#-keep public class com.lrcall.models.* {
#   *;
#}