LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libstelpcutils

LOCAL_MODULE_TAGS := optional

LOCAL_PRELINK_MODULE := false

LOCAL_MODULE_PATH := $(PRODUCT_OUT)/$(TARGET_COPY_OUT_VENDOR)/lib

LOCAL_SHARED_LIBRARIES := liblog libcutils

LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

LOCAL_C_INCLUDES += $(LOCAL_PATH)/../libomxil-bellagio/include

LOCAL_SRC_FILES := \
	OMXdebug_specific.c \
	stelp_fatal.c \
	stelp_log.c \
	stelp_time.c \

include $(BUILD_SHARED_LIBRARY)
