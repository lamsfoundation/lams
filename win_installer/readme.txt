LAMS 2.0.3 - Learning Activity Management System
----------------------------------------------

This is a pre-packaged copy of LAMS 2.0.3 for Windows 2000, XP, or 2003.
For the source code, please see the 'Building LAMS' wiki at 

	http://wiki.lamsfoundation.org/display/lams/Building+LAMS 

for more information.


1. Startup and Shutdown
=======================
To use LAMS 2, first make sure it is started - use the 'Start LAMS' shortcut
in the LAMSv2 Start Menu folder if you're not sure.  MySQL must be running.
For chat rooms to function, Wildfire must also be running.

While LAMS 2 is running, you may access the login page using the 'Access 
LAMS' short cut in the LAMSv2 Start Menu folder.

To stop LAMS 2, use the 'Stop LAMS' shortcut in the LAMSv2 Start Menu 
folder.


2. Server Configuration
=======================
For single user installations (i.e., personal use on your own desktop), please
ignore this section.

For server deployments, you will probably want to configure the default JVM 
memory settings for your server.  In C:\lams\jboss-4.0.2\bin\run.bat (or 
wherever you installed LAMS 2), find the line near the bottom that says

	set JAVA_OPTS=%JAVA_OPTS% -Xms128m -Xmx512m
	
and adjust the minimum and maximum amount of memory allocated there.

We also recommend increasing the -XX:MaxPermSize option if you have more than
a couple of users.  e.g.

	set JAVA_OPTS=%JAVA_OPTS% -Xms128m -Xmx512m -XX:MaxPermSize=128m

For Sun JDK versions before 1.5.0_07, the default is 64m.  If your Sun JDK 
version is 1.5.0_07 or later, the default is 256m.  


3. Web Resources
================
Windows Installer Help wiki (includes download links)

	http://wiki.lamsfoundation.org/display/lamsdocs/Windows+Installer+Help
	
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
