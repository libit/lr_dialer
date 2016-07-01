/*
 * JNI 辅助工具
 * 作者：Libit
 */

#ifdef __cplusplus
extern "C" {
#endif
#include "utils.h"

#define THIS_FILE "utils.cpp"

void showLog(JNIEnv* env, jstring str)
{
	char* cStr = (char*) env->GetStringUTFChars(str, 0);
	//LOGD("str is:%s \n", cStr);
	env->ReleaseStringUTFChars(str, cStr);
}

char* getChars(JNIEnv* env, jstring str)
{
	return (char*) env->GetStringUTFChars(str, 0);
}

jstring doGetJMethod(JNIEnv* env, jobject obj, jstring url)
{
	jclass cls = env->GetObjectClass(obj);
	jmethodID doGet = env->GetStaticMethodID(cls, "doGet", "(Ljava/lang/String;)Ljava/lang/String;");

	if (doGet == 0)
	{
		return env->NewStringUTF("Error,Method not Found!");
	}

	jstring strResult = (jstring) env->CallStaticObjectMethod(cls, doGet, url);

	return strResult;
}

jstring doPostJMethod(JNIEnv* env, jobject obj, jstring url, jstring params)
{
	jclass cls = env->GetObjectClass(obj);
	jmethodID doPost = env->GetStaticMethodID(cls, "doPost", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");

	if (doPost == 0)
	{
		return env->NewStringUTF("Error,Method not Found!");
	}

	jstring strResult = (jstring) env->CallStaticObjectMethod(cls, doPost, url, params);

	return strResult;
}

bool compareJString(JNIEnv* env, jstring param1, jstring param2)
{
	char* cParam1 = (char*) env->GetStringUTFChars(param1, 0);
	char* cParam2 = (char*) env->GetStringUTFChars(param2, 0);

	int index1 = 0;
	while (*(cParam1 + index1) != '\0' && *(cParam2 + index1)!= '\0')
	{
		if(*(cParam1 + index1) != *(cParam2 + index1))
		{
			env->ReleaseStringUTFChars(param1, cParam1);
			env->ReleaseStringUTFChars(param2, cParam2);
			return false;
		}
		index1++;
	}

	if(*(cParam2 + index1)!= '\0'||*(cParam1 + index1) != '\0')
	{
		env->ReleaseStringUTFChars(param1, cParam1);
		env->ReleaseStringUTFChars(param2, cParam2);
		return false;
	}

	env->ReleaseStringUTFChars(param1, cParam1);
	env->ReleaseStringUTFChars(param2, cParam2);

	return true;
}

#ifdef __cplusplus
}
#endif
