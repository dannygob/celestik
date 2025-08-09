#include <jni.h>
#include <string>
#include <opencv2/core.hpp>

extern "C" {
JNIEXPORT jstring
JNICALL
Java_com_example_celestik_MainActivity_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string version = cv::getVersionString();
    return env->NewStringUTF(("OpenCV version: " + version).c_str());
}
}
