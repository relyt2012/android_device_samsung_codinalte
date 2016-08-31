#
# Copyright (C) 2013 The Android Open Source Project
# Copyright (C) 2013 Óliver García Albertos (oliverarafo@gmail.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Inherit Carbon common Phone stuff.
$(call inherit-product-if-exists, vendor/carbon/config/common.mk)
$(call inherit-product-if-exists, vendor/carbon/config/gsm.mk)

# Inherit device configuration
$(call inherit-product, device/samsung/codinalte/full_codinalte.mk)

# Device identifier
PRODUCT_DEVICE := codinalte
PRODUCT_NAME := carbon_codinalte
PRODUCT_BRAND := samsung
PRODUCT_MODEL := SGH-T599X
PRODUCT_MANUFACTURER := samsung

TARGET_SCREEN_HEIGHT := 800
TARGET_SCREEN_WIDTH := 480

# Set build fingerprint and ID
BUILD_ID := TRIANA00$(shell date -u +%Y%m%d)
PRODUCT_BUILD_PROP_OVERRIDES += PRODUCT_NAME=cm_codinalte BUILD_FINGERPRINT=cyanogenmod/cm_codinalte/codinalte:4.4.2/$(BUILD_ID) PRIVATE_BUILD_DESC="SGH-T599X 4.4.2"
