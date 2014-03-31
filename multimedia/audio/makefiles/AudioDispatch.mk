# -*- Mode: Makefile -*-
# vim:syntax=make:

#-----------------------
# List of Directory
#-----------------------

LOCAL_PATH:= $(call my-dir)

include $(LOCAL_PATH)../../shared/makefiles/SharedCheck.mk

DIRECTORIES= $(wildcard lib*) codec effect src standalone

include $(LOCAL_PATH)../../shared/makefiles/SharedDispatch.mk


