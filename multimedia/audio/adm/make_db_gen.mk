#
# Copyright (C) ST-Ericsson SA 2012. All rights reserved.
# This code is ST-Ericsson proprietary and confidential.
# Any use of the code for whatever purpose is subject to
# specific written permission of ST-Ericsson SA.
#
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

ADM_DB_VARIANT_PATH := db/$(ADM_DB_VARIANT)
STE_ADM_GEN_DB := ste-adm-gen-db-$(ADM_DB_VARIANT)

ifeq ($(ADM_NO_EFFECT_CONFIG_SUPPORT), 1)
  LOCAL_CFLAGS += -DADM_NO_EFFECT_CONFIG_SUPPORT
endif

ifeq ($(MULTIMEDIA_SET_PLATFORM),u8500)
  LOCAL_CFLAGS += -DSTE_PLATFORM_U8500
endif

ifeq ($(MULTIMEDIA_SET_PLATFORM),u9540)
  LOCAL_CFLAGS += -DSTE_PLATFORM_U8500
endif

LOCAL_CPP_EXTENSION    := .cc
LOCAL_SRC_FILES        := $(ADM_DB_VARIANT_PATH)/gen_cfgdef.cc
LOCAL_MODULE           := $(STE_ADM_GEN_DB)
LOCAL_MODULE_TAGS      := optional
LOCAL_C_INCLUDES       += \
        $(LOCAL_PATH)/../../shared/omxil \
	$(LOCAL_PATH)/../../shared/utils/include \
	$(LOCAL_PATH)/../../shared/ens_interface/include \
        $(LOCAL_PATH)/../audio_chipset_apis/ \
	$(LOCAL_PATH)/../alsactrl/include/ \
	$(LOCAL_PATH)/../afm/proxy/include/ \
	$(LOCAL_PATH)/../drc/proxy/ \
	$(LOCAL_PATH)/../comfortnoise/proxy/ \
	$(LOCAL_PATH)/../virtual_surround/proxy/ \
	$(LOCAL_PATH)/../noise_reduction/proxy/ \
	$(LOCAL_PATH)/../libeffects/libresampling/include/ \
	$(LOCAL_PATH)/../speech_proc_vcs/wrapper/inc/ \
        $(LOCAL_PATH)/../audiolibs/common/include/ \
        $(LOCAL_PATH)/../audiolibs/fake_dsptools/ \
	$(LOCAL_PATH)/../../shared/ste_shai/include/ \
	$(LOCAL_PATH)/../

ifeq ($(ONE_SHOT_MAKEFILE),)
  ## Source files depend upon STE MM header files installed during STE MM build
  $(addprefix $(LOCAL_PATH)/,$(LOCAL_SRC_FILES)): | st-ericsson-multimedia-package
endif

include $(BUILD_HOST_EXECUTABLE)
