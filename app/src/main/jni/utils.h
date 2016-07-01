/*
 * JNI 辅助工具
 * 作者：Libit
 */

#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>
#include <android/log.h>

//#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "JNIMsg", __VA_ARGS__)

void showLog(JNIEnv* env, jstring str);

char* getChars(JNIEnv* env, jstring str);

jstring doGetJMethod(JNIEnv* env, jobject obj, jstring url);

jstring doPostJMethod(JNIEnv* env, jobject obj, jstring url, jstring params);

bool compareJString(JNIEnv* env, jstring param1, jstring param2);

#ifdef __cplusplus
}
#endif
