#!/bin/sh

cd /usr/local/lams/db/
/usr/local/lams/ant/bin/ant  -logfile database.log insert-data
unzip /usr/local/lams/app/app.zip -d /Applications
/usr/bin/open /Applications/LAMS\ 2\ Server.app
