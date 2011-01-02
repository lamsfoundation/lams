#!/bin/bash
#Removes all the CVS and *.bak files from the build directory

. ./lams.properties

cd build
find . -name CVS | xargs rm -rf
find . -name *.bak | xargs rm -rf
find . -name repository*.tar.gz | xargs rm -rf
find . -name secure-* | xargs rm -rf

# ensure scripts have excute permissions
chmod u+x lams-unix-installer-$LAMS_VERSION/ant/bin/*
chmod u+x lams-unix-installer-$LAMS_VERSION/bin/*
chmod u+x lams-unix-installer-$LAMS_VERSION/install-lams.sh

tar -czf lams-unix-installer-$LAMS_VERSION.tar.gz lams-unix-installer-$LAMS_VERSION
