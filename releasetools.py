# Copyright (C) 2012 The Android Open Source Project
# Copyright (C) 2012 The CyanogenMod Project
# Copyright (C) 2014 Jonathan Jason Dennis [Meticulus] (theonejohnnyd@gmail.com)
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

"""Custom OTA commands for codina"""

import common
import os
import sys
import time

LOCAL_DIR = os.path.dirname(os.path.abspath(__file__))
TARGET_DIR = os.getenv('OUT')

def addFolderToZip(info, directory, basedir):
    list = os.listdir(directory)
 
    for entity in list:
        each = os.path.join(directory,entity)
 
        if os.path.isfile(each):
            print "Adding vendor file -> "+ os.path.join(basedir, entity)
            info.output_zip.write(each, os.path.join(basedir, entity))
        else:
            addFolderToZip(info,each,os.path.join(basedir, entity))

def FullOTA_Assertions(info):
  sys.setrecursionlimit(100000)

  models = 0

  if os.path.isdir(os.path.join(TARGET_DIR, "codinamtr")):
  	addFolderToZip(info, os.path.join(TARGET_DIR, "codinamtr"),"codinamtr")
	models += 1
  else :
	print "Warning!: Vendor files for codinamtr were not found..."
	time.sleep(2)

  if os.path.isdir(os.path.join(TARGET_DIR, "codinatmo")):
  	addFolderToZip(info, os.path.join(TARGET_DIR, "codinatmo"),"codinatmo")
        models += 1
  else :
	print "Warning!: Vendor files for codinatmo were not found..."
        time.sleep(2)

  if os.path.isdir(os.path.join(TARGET_DIR, "codinavid")):
  	addFolderToZip(info, os.path.join(TARGET_DIR, "codinavid"),"codinavid")
	models += 1
  else :
	print "Warning!: Vendor files for codinavid were not found..."
        time.sleep(2)

  if(models == 0):
	raise Exception("You must have vendor files for at least one variant!")

  info.output_zip.write(os.path.join(TARGET_DIR, "blobinstaller.sh"), "blobinstaller.sh")
  info.output_zip.write(os.path.join(TARGET_DIR, "restorecon.sh"), "restorecon.sh")


  info.script.AppendExtra(
        ('package_extract_dir("codinamtr", "/tmp/codinamtr");\n'
         'set_perm(0, 0, 0777, "/tmp/codinamtr");'))
  info.script.AppendExtra(
        ('package_extract_dir("codinatmo", "/tmp/codinatmo");\n'
         'set_perm(0, 0, 0777, "/tmp/codinatmo");'))
  info.script.AppendExtra(
        ('package_extract_dir("codinavid", "/tmp/codinavid");\n'
         'set_perm(0, 0, 0777, "/tmp/codinavid");'))
  info.script.AppendExtra(
        ('package_extract_file("blobinstaller.sh", "/tmp/blobinstaller.sh");\n'
         'set_perm(0, 0, 0777, "/tmp/blobinstaller.sh");'))
  info.script.AppendExtra(
        ('package_extract_file("restorecon.sh", "/tmp/restorecon.sh");\n'
         'set_perm(0, 0, 0777, "/tmp/restorecon.sh");'))

def FullOTA_InstallEnd(info):
  info.script.AppendExtra('assert(run_program("/tmp/blobinstaller.sh") == 0);')
  info.script.AppendExtra('assert(run_program("/tmp/restorecon.sh") == 0);')

# We have to reset all permissions after install blobs on system and this is the only way I could find to do it.
# It would be better if FullOTA_InstallEnd(info) was called before system permissions are set
# But I am unsure what "other" consequences of doing that would be.
# NOTE: Future bring ups - check updater-script and duplicate permission setup here.
  info.script.AppendExtra('set_metadata_recursive("/system", "uid", 0, "gid", 0, "dmode", 0755, "fmode", 0644, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata_recursive("/system/addon.d", "uid", 0, "gid", 0, "dmode", 0755, "fmode", 0755, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata_recursive("/system/bin", "uid", 0, "gid", 2000, "dmode", 0755, "fmode", 0755, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/app_process", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:zygote_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/clatd", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:clatd_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/debuggerd", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:debuggerd_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/dhcpcd", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:dhcp_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/dnsmasq", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:dnsmasq_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/drmserver", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:drmserver_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/hostapd", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:hostapd_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/installd", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:installd_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/keystore", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:keystore_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/mediaserver", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:mediaserver_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/mksh", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:shell_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/mtpd", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:mtp_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/netcfg", "uid", 0, "gid", 3003, "mode", 02750, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/netd", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:netd_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/ping", "uid", 0, "gid", 0, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:ping_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/pppd", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:ppp_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/racoon", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:racoon_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/rild", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:rild_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/run-as", "uid", 0, "gid", 2000, "mode", 0750, "capabilities", 0xc0, "selabel", "u:object_r:runas_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/sdcard", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:sdcardd_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/servicemanager", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:servicemanager_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/surfaceflinger", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:surfaceflinger_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/vold", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:vold_exec:s0");')
  info.script.AppendExtra('set_metadata("/system/bin/wpa_supplicant", "uid", 0, "gid", 2000, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:wpa_exec:s0");')
  info.script.AppendExtra('set_metadata_recursive("/system/etc/dhcpcd", "uid", 0, "gid", 0, "dmode", 0755, "fmode", 0644, "capabilities", 0x0, "selabel", "u:object_r:dhcp_system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/etc/dhcpcd/dhcpcd-run-hooks", "uid", 1014, "gid", 2000, "mode", 0550, "capabilities", 0x0, "selabel", "u:object_r:dhcp_system_file:s0");')
  info.script.AppendExtra('set_metadata_recursive("/system/etc/init.d", "uid", 0, "gid", 2000, "dmode", 0755, "fmode", 0755, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/etc/init.d", "uid", 0, "gid", 0, "mode", 0755, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata_recursive("/system/etc/ppp", "uid", 0, "gid", 0, "dmode", 0755, "fmode", 0555, "capabilities", 0x0, "selabel", "u:object_r:ppp_system_file:s0");')
  info.script.AppendExtra('set_metadata_recursive("/system/vendor", "uid", 0, "gid", 2000, "dmode", 0755, "fmode", 0644, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/vendor/etc/audio_effects.conf", "uid", 0, "gid", 0, "mode", 0644, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/vendor/lib/libbt-vendor.so", "uid", 0, "gid", 0, "mode", 0644, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata_recursive("/system/xbin", "uid", 0, "gid", 2000, "dmode", 0755, "fmode", 0755, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/xbin/librank", "uid", 0, "gid", 0, "mode", 06755, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/xbin/procmem", "uid", 0, "gid", 0, "mode", 06755, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/xbin/procrank", "uid", 0, "gid", 0, "mode", 06755, "capabilities", 0x0, "selabel", "u:object_r:system_file:s0");')
  info.script.AppendExtra('set_metadata("/system/xbin/su", "uid", 0, "gid", 0, "mode", 06755, "capabilities", 0x0, "selabel", "u:object_r:su_exec:s0");')

