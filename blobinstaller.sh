#!/sbin/sh
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
#

CODENAME=""
VERSION=""

# ui_print by Chainfire
OUTFD=$(/sbin/busybox ps | /sbin/busybox grep -v "grep" | /sbin/busybox grep -o -E "update_binary(.*)" | /sbin/busybox cut -d " " -f 3);
ui_print() {
  if [ "$OUTFD" != "" ]; then
    echo "ui_print ${1} " 1>&$OUTFD;
    echo "ui_print " 1>&$OUTFD;
  else
    echo "${1}";
  fi;
}
ui_print "----------------------------------------"
ui_print "VENDOR FILES INSTALLER: CODINA VARIANTS|"
ui_print "                             Meticulus |"
ui_print "----------------------------------------"
#mount modemfs
mkdir -p /modemfs
mount -t auto /dev/block/mmcblk0p12 /modemfs
#check for NV.DAT
if [ ! -f /modemfs/PERM/PROT/NV.DAT ]
then
	ui_print "Error: Baseband version could not be found! (@check for NV.DAT)"
	exit 9
fi

#read NV.DAT into usable form
NVDAT=$(cat /modemfs/PERM/PROT/NV.DAT | awk -F 'NV_' '{print $2}' | while read LINE; do if [ "$LINE" != "" ]; then echo $LINE; fi;done)

umount /modemfs

#pars NV.DAT values and look for MODEM_SW_VER
if echo "$NVDAT" |grep -q MODEM_SW_VER
then
	for LINE in $NVDAT
	do
		PROPERTY=$(echo $LINE| awk -F'_I' '{print $1}')
		VALUE=$(echo $LINE| awk -F'_I' '{print $2}')
		if [[ "$PROPERTY" == "MODEM_SW_VER" ]]
		then
			ui_print "BASEBAND = $VALUE"
			if [[ "$VALUE" == "T599NUVAMD4" ]]
			then
				CODENAME="codinamtr"
				VERSION="SGH-T599N"
			elif [[ "$VALUE" == "T599UVAMB5" ]]
			then
				CODENAME="codinatmo"
				VERSION="SGH-T599"
			elif [[ "$VALUE" == "T599VVLAME3" ]]
			then
				CODENAME="codinavid"
				VERSION="SGH-T599V"
			else
				ui_print "Error: Unsupported baseband detected! (@ MODEM_SW_VER value)"
				exit 9
			fi
		fi
	done
else
	ui_print "Error: Unable to detect baseband! (@ check for MODEM_SW_VER)"
	exit 9
fi

ui_print "CODENAME = $CODENAME"
ui_print "MODEL    = $VERSION"
ui_print ""
ui_print "Installing vendor files..."
if [[ -e "/tmp/$CODENAME" ]]
then
	cp -rf /tmp/$CODENAME/* /system
else
	ui_print "Vendor files for $CODENAME not found! (abort)"
	exit 9
fi

ui_print "Modifying build vars..."
rm -f /tmp/build.prop
cat /system/build.prop | while read LINE
do
	PROPERTY=$(echo "$LINE" | cut -d "=" -f1)
	VALUE=$(echo "$LINE" | cut -d "=" -f2)
	if [[ "$PROPERTY" == "ro.product.model" ]]
	then
		VALUE=$VERSION
		LINE="$PROPERTY=$VALUE"
	elif [[ "$PROPERTY" == "ro.build.product" ]]
	then
		VALUE=$CODENAME
		LINE="$PROPERTY=$VALUE"
	elif [[ "$PROPERTY" == "ro.product.device" ]]
	then
		VALUE=$CODENAME
		LINE="$PROPERTY=$VALUE"
	fi
	echo "$LINE" >> /tmp/build.prop
done 

cp -f /tmp/build.prop /system/build.prop
exit
