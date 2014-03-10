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

CODENAME=$(getprop ro.build.product)
VERSION=$(getprop ro.product.model)

# ui_print by Chainfire
OUTFD=$(/sbin/busybox ps | /sbin/busybox grep -v "grep" | /sbin/busybox grep -o -E "update_binary(.*)" | /sbin/busybox cut -d " " -f 3);
ui_print() {
  if [ $OUTFD != "" ]; then
    echo "ui_print ${1} " 1>&$OUTFD;
    echo "ui_print " 1>&$OUTFD;
  else
    echo "${1}";
  fi;
}

ui_print "Detected $CODENAME - $VERSION"

ui_print "Installing vendor files..."
if [[ -e /tmp/$CODENAME ]]
then
	cp -rf /tmp/$CODENAME/* /system
else
	ui_print "Vendor files for $CODENAME not found!"
	exit 9
fi

ui_print "Modifying build vars..."
cat /system/build.prop | while read LINE
do
	PROPERTY=$(echo "$LINE" | cut -d "=" -f1)
	VALUE=$(echo "$LINE" | cut -d "=" -f2)
	if [[ $PROPERTY == "ro.product.model" ]]
	then
		VALUE=$VERSION
		LINE="$PROPERTY=$VALUE"
	elif [[ $PROPERTY == "ro.build.product" ]]
	then
		VALUE=$CODENAME
		LINE="$PROPERTY=$VALUE"
	elif [[ $PROPERTY == "ro.product.device" ]]
	then
		VALUE=$CODENAME
		LINE="$PROPERTY=$VALUE"
	fi
	echo "$LINE" >> /tmp/build.prop
done 

cp -f /tmp/build.prop /system/build.prop
exit
