# Project-specific ProGuard rules
# These rules are applied in addition to the default configuration files
# defined in build.gradle (proguardFiles).
#
# For more details, see:
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JavaScript interfaces,
# uncomment the following and replace with the fully qualified
# class name of your JS interface:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve line number information
# for debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ✅ No OpenCV-specific rules are required here.
# The OpenCV JAR and native .so libraries do not need ProGuard rules,
# since they are loaded via JNI and reflection is not used.