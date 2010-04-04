#!/bin/sh

cd /usr/local/lams/db/
/usr/local/lams/ant/bin/ant  -logfile database.log insert-data
mv -f /usr/local/lams/LAMS\ 2\ Server.app /Applications
