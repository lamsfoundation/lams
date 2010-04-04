If you need to reset LAMS, you should be able to run this script but take into account that this WILL WIPE OUT ALL THE EXISTING INFORMATION!!

   /usr/local/lams/ant/bin/ant -v -logfile database.log insert-data

Note that if the MySQL password is *not* blank, then you should change this in the db.properties file. 

If you have questions or comments: http://lamscommunity.org

Thanks

Ernie

ernieg@melcoe.mq.edu.au
