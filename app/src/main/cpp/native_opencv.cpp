#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>

extern "C"
JNIEXPORT void JNICALL
Java_com_example_celestik_NativeBridge_convertToGray(JNIEnv *env, jobject thiz, jlong matAddr) {
    cv::Mat &mat = *(cv::Mat *) matAddr;
    cv::cvtColor(mat, mat, cv::COLOR_BGR2GRAY);
}