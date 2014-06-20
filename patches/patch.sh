#! /bin/bash
THISDIR=$PWD
find -name *.patch | while read LINE;
do
	echo "patch = $THISDIR/$LINE"
	REPO=$(echo $LINE | cut -d "/" -f2)
        REPO="$(echo $REPO | cut -d "_" -f1)/$(echo $REPO | cut -d "_" -f2)"
	echo "repo = $REPO"
done
