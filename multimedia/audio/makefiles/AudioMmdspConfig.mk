# -*- Mode: Makefile -*-
# vim:syntax=make:

#-------------------------------
# Default Config
#-------------------------------
ifndef CORE
    CORE=mmdsp
endif
LOCAL_PATH:= $(call my-dir)

include $(LOCAL_PATH)../../shared/makefiles/SharedConfig.mk

