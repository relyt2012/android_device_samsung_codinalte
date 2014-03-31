# -*- Mode: Makefile -*-
# vim:syntax=make:

LOCAL_PATH:= $(call my-dir)

include $(LOCAL_PATH)../../shared/makefiles/SharedConfig.mk
include $(LOCAL_PATH)/AudioFlags.mk

#TODO: figure out where this is or if it's needed
CPPFLAGS+=  -I$(COMPONENT_TOP_DIR)/include -I$(COMPONENT_TOP_DIR)/common/include 

CPPFLAGS+=  -I$(LOCAL_PATH)../audiolibs/include -I$(LOCAL_PATH)../audiolibs/common/include -I$(LOCAL_PATH)../audiolibs
