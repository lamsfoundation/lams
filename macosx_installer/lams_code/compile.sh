#!/bin/sh
cd lams_common/
echo "Rebuilding db"
ant rebuild-db
cd ../lams_build/
echo "Assembling EAR"
ant assemble-ear
echo "Deploying EAR"
ant deploy-ear
echo "Deploying Tools"
ant deploy-tools
echo "Copying files"
ant copyfiles
