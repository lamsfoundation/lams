#!/bin/sh

rm -r build/lams-unix-installer-2.0.2

mkdir build/lams-unix-installer-2.0.2
mkdir build/lams-unix-installer-2.0.2/log
mkdir build/lams-unix-installer-2.0.2/sql-scripts
mkdir build/lams-unix-installer-2.0.2/doc

cp -r ant ant-scripts conf bin database install-lams.sh assembly wrapper readme license doc/lams.properties build/lams-unix-installer-2.0.2
cp doc/setLamsConfiguration.sql build/lams-unix-installer-2.0.2/doc

echo "\nBuild Completed\n"


