LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog

LOCAL_MODULE    := appcallsdk
LOCAL_SRC_FILES := \
utils.cpp \
main.cpp \

include $(BUILD_SHARED_LIBRARY)
