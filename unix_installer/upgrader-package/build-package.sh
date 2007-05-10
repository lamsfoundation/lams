#!/bin/sh

rm -r build/lams-unix-updater-2.0.2
cp build/lams-unix-updater-2.0.2.tar.gz ../lams-unix-updater-2.0.2.tar.gz.bak
rm build/lams-unix-updater-2.0.2.tar.gz
mkdir build/lams-unix-updater-2.0.2
mkdir build/lams-unix-updater-2.0.2/log
mkdir build/lams-unix-updater-2.0.2/bin
cp -r ant ant-scripts assembly language-pack sql-scripts tools license readme update-lams.sh docs/lams.properties build/lams-unix-updater-2.0.2
cp bin/*.class bin/*.sh bin/*.sql build/lams-unix-updater-2.0.2/bin

tar -cf build/lams-unix-updater-2.0.2.tar build/lams-unix-updater-2.0.2
gzip build/lams-unix-updater-2.0.2.tar

echo "\nBuild Completed\n"




