# Carbon ROM (KitKat) for the Galaxy Exhibit / Ace 2e (codinalte)
This device tree can build for one or all of these three devices:

1. Samsung Galaxy Exhibit T599 (T-Mobile) AKA: codinatmo
2. Samsung Galaxy Exhibit T599N (MetroPCS)AKA: codinanewcotmo AKA: codinamtr
3. Samsung Galaxy Ace 2e (VideoTron? Wind?) AKA: codinavid

### Kernel
This device tree is intended to be used with this kernel repo:

See: https://github.com/Meticulus/android_kernel_samsung_codina

branch: kk44
### Vendor
This device tree is intented to be used with one or all of these vendor repos:

codinatmo
See: https://github.com/Meticulus/android_vendor_samsung_codinatmo

branch: kk44

codinamtr
See: https://github.com/Meticulus/android_vendor_samsung_codinamtr

branch: kk44

codinavid
See: https://github.com/Meticulus/android_vendor_samsung_codinavid

branch: kk44
# How To Build

### Step 1: Setting up the Build Environment.

You'll need Linux to be able to build Carbon ROM. You have three choices here; you can:

1. Install Linux natively on your computer.
2. Dual boot Linux and Windows.
3. Use virtual machine software ( virtual box, vmware ) to run linux.

NOTE: I recommend you use Ubuntu 12.04 LTS to build. That's what I use.

Now read this: http://source.android.com/source/initializing.html

NOTE: When I say "read", I mean read and comprehend.

NOTE: Read through the topics here as well: http://wiki.cyanogenmod.org/w/Development
### Step 2: Downloading the Source.

NOTE: Some say that it is better to download the ROM source and put in your local manifest later. I don't know if that's best but that's what we are going to do.

BEFORE YOU BEGIN: You are about to start downloading 5 - 15 Gigs of data. That could take a very long time, so plan accordingly. I like to start just before I go to sleep and let it go overnight! If you have to work, perhaps start it before work and let it go through out the day.

Execute the following commands in a linux terminal:
```bash
mkdir /home/$USER/carbon
cd /home/$USER/carbon
repo init -u https://github.com/CarbonDev/android.git -b kk
repo sync
```
WARNING: There may be times, towards the end when it seem like, the download is stuck and not making any progress because there are no updates on the screen. BE PATIENT!, open a program that will show how much bandwidth you are using to be sure!

### Step 3: Set up local manifest.

The local manifest is different for every device. It contains those repos that are device specific, where as the ROM code you just "repo sync'd" is code that is general to any device.

Execute the following commands in a linux terminal:
```bash
gedit /home/$USER/carbon/.repo/local_manifests/codinalte.xml
```
Assuming you want to build for all three devices copy the following into codinalte.xml, save and close. If not remove the "vendor" trees for the devices you don't want.
```bash
<?xml version="1.0" encoding="UTF-8"?>
<manifest>
  <project name="Meticulus/android_kernel_samsung_codina" path="kernel/samsung/codina" remote="github" revision="kk44"/>
  <project name="Meticulus/android_device_samsung_codinalte" path="device/samsung/codinalte" remote="github" revision="carbon"/>
  <project name="Meticulus/android_vendor_samsung_codinatmo" path="vendor/samsung/codinatmo" remote="github" revision="kk44"/>
  <project name="Meticulus/android_vendor_samsung_codinamtr" path="vendor/samsung/codinamtr" remote="github" revision="kk44"/>
  <project name="Meticulus/android_vendor_samsung_codinavid" path="vendor/samsung/codinavid" remote="github" revision="kk44"/>
</manifest>
```
Execute the following commands in a linux terminal:
```bash
cd /home/$USER/carbon
repo sync
```

NOTE: Yes we are syncing again and No, it shouldn't take quite as long. Every time you repo sync just new data is downloaded. So we are downloading the 4 repo's we just put in and any updates that may have occured to the repo's we already have.

### Step 4: Building

Now you will want to apply the repo patches. These patches modify code in the ROM to work with this device.
Execute the following commands in a linux terminal:
```bash
cd /home/$USER/carbon/device/samsung/codinalte
./patch.sh
```

NOTE: Now you have everything that you need to build Carbon ROM for your Samsung Galaxy Exhibit / Ace 2e. Build times depend on you PC performance specifications. In the following terminal command "-j8" represents the number of concurrent tasks to execute. For low specs machines (such as mine) lowering the value to "-j3" may help speed things up. For high spec'd machines raising the value may speed things up.

NOTE: It may take anywhere from 5 hours to 15 hours depending on system specs for a complete build.
Execute the following commands in a linux terminal:
```bash
cd /home/$USER/carbon
. build/envsetup.sh
lunch cm_m470-userdebug
make -j8 otapackage
```
WARNING: There may be times, towards the end when it seem like, the build is stuck because of a lack of updates on the screen. BE PATIENT! libwebviewchromium.so is a beast and is usually the last file to be build. It takes awhile to complete. I ususally have 15 to 20 minutes of "no screen activity" before it finally finishes building that lib and then continues...
