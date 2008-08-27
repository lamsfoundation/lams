LAMS 2.1.1 - Learning Activity Management System
----------------------------------------------

This is a pre-packaged copy of LAMS 2.1.1 for Windows 2000, XP, or 2003.
For the source code, please see the 'Building LAMS' wiki at 

	http://wiki.lamsfoundation.org/display/lams/Building+LAMS 

for more information.

For release notes goto: http://wiki.lamsfoundation.org/display/lams/2.1.1

NOTE TO SYSADMINS: If you are doing an upgrade and your users report problems in 
authoring, they may need to clear their browser cache. If you have a user see 
the message "Unknown toolbar item "UniversalKey" or if the user is sees the 
message "Congratulations, your content saved successfully!" but the close button
doesn't work, then please tell them to clear their cache.


1. Startup and Shutdown
=======================
To use LAMS 2, first make sure it is started - use the 'Start LAMS' shortcut
in the LAMSv2 Start Menu folder if you're not sure.  MySQL must be running.
For chat rooms to function, Wildfire must also be running.

While LAMS 2 is running, you may access the login page using the 'Access 
LAMS' short cut in the LAMSv2 Start Menu folder.

To stop LAMS 2, use the 'Stop LAMS' shortcut in the LAMSv2 Start Menu 
folder.

Note to System Administrators: 


2. Server Configuration
=======================
For single user installations (i.e., personal use on your own desktop), please
ignore this section.

For server deployments, you will probably want to configure the default JVM 
memory settings for your server.  In C:\lams\jboss-4.0.2\conf\wrapper.conf (or 
wherever you installed LAMS 2), find the lines that say

	# Initial Java Heap Size (in MB)
    wrapper.java.initmemory=256
    
    # Maximum Java Heap Size (in MB)
    wrapper.java.maxmemory=512	
    
and adjust the minimum and maximum amount of memory allocated there.

We also recommend increasing the -XX:MaxPermSize option if you have more than
a couple of users.  You can do that by adding this line underneath.

	wrapper.java.additional.2=-XX:MaxPermSize=256m

For Sun JDK versions before 1.5.0_07, the default is 64m.  If your Sun JDK 
version is 1.5.0_07 or later, the default is 256m.  


3. Web Resources
================
Windows Installer Help wiki (includes download links)

	http://wiki.lamsfoundation.org/display/lamsdocs/Windows+Installer+Help
	
Backing up LAMS and reverting LAMS

    http://wiki.lamsfoundation.org/display/lamsdocs/Revert+To+Windows+Backup
    http://wiki.lamsfoundation.org/display/lamsdocs/Revert+To+Other+Backup
	
LAMS 2 developer-oriented information

	http://wiki.lamsfoundation.org/display/lams

LAMS 2 help documents

	http://wiki.lamsfoundation.org/display/lamsdocs

Educational and technical forums for help and discussion
				
	http://www.lamscommunity.org

LAMS 2 bug tracker			

	https://bugs.lamsfoundation.org

LAMS Foundation					
	
	http://www.lamsfoundation.org
