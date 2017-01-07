# Resurrection Remix (Marshmallow) for the Galaxy Exhibit / Ace 2e (codinalte)
This device tree can build for one or all of these three devices:

1. Samsung Galaxy Exhibit T599 (T-Mobile) AKA: codinatmo
2. Samsung Galaxy Exhibit T599N (MetroPCS)AKA: codinanewcotmo AKA: codinamtr
3. Samsung Galaxy Ace 2e (VideoTron? Wind?) AKA: codinavid

# How To Build

### Step 1: Setting up the Build Environment.

You'll need Linux to be able to build Resurrection Remix. You have three choices here; you can:

1. Install Linux natively on your computer.
2. Dual boot Linux and Windows.
3. Use virtual machine software ( virtual box, vmware ) to run linux.

Now read this to setup your build environment: https://forum.xda-developers.com/chef-central/android/guide-how-to-configure-ubuntu-15-04-t3091938

NOTE: When I say "read", I mean read and comprehend.

### Step 2: Downloading the Source.

Okay to download the repo you will want to initialize the resurrection remix, To do so. Make sure that you have read that setting up your build environment. If not you will regret it. So be patient, It takes time.

~Commands~
repo init -u https://github.com/ResurrectionRemix/platform_manifest.git -b marshmallow
repo sync -f --force-sync --no-clone-bundle

For a more in depth setup of their repo, take a look at their official github guide.
https://github.com/ResurrectionRemix/platform_manifest/tree/marshmallow

Now let that download, For a 20gb source it will take a while. If you have 3.6MB's internet like me. it took about 1hour to 1 & 1/2 hour.


WARNING: There may be times, towards the end when it seem like, the download is stuck and not making any progress because there are no updates on the screen. BE PATIENT!, open a program that will show how much bandwidth you are using to be sure!

Anyway here is the command to pull the manifest I have precompiled. 

curl --create-dirs -L -o .repo/local_manifests/local_manifest.xml -O -L https://raw.githubusercontent.com/relyt2012/android_device_samsung_codinalte/rr-6.0/manifest.xml

that is all you need to sync, If all the errors that are found. Get fixed. You can just build, If you setup the build environment right. Just use this command to build!

~Build commands~
. build/envsetup.sh
lunch cm_codinalte-userdebug
mka
mka otapackage

Done! If all is good, congratulations. You owe it all to Meticulus(Johnny). 
