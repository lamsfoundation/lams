#!/bin/bash
#Removes all the CVS and *.bak files from the build directory

. ./lams.properties

cd build
find . -name CVS | xargs rm -rf
find . -name *.bak | xargs rm -rf

tar -czf lams-unix-installer-$LAMS_VERSION.tar.gz lams-unix-installer-$LAMS_VERSION
