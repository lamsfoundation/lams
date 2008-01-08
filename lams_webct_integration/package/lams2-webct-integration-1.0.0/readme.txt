WebCt - LAMS Integration Module
====================================

Written By Luke Foxton <lfoxton@melcoe.mq.edu.au>
LAMS Foundation Copyright (C) 2007 (http://lamsfoundation.org)

Contents
================================================================================
1. Requirements
2. Downloading the package
3. Installation
4. Configuring WebCt Side
5. Configuring LAMS Side


1. REQUIREMENTS
================================================================================

* Blackboard CE 6.x (Also known as WebCt Campus Edition)
* Http Server 
* Ant
* MySql Server 5.0
* CVS Client


2. Downloading the package
================================================================================

Configure your cvs client as follows:

    * access method: pserver
    * user name: anonymous
    * server name: lamscvs.melcoe.mq.edu.au
    * location: /usr/local/cvsroot

Then Checkout the lams_webct_integration project.


3. Installation
================================================================================

1) Install Blackboard CE 6.x on a server.
2) Download the WebCt-LAMS integration source package (see 3. Downloading the 
package).
3) Open up the build.properties file (located in the root project directory) in
a text editor.
4) Follow the instructions to fill in the configuration details. NOTE: If your 
database server is on a different server, then you must create the user and give
them permission to connect from other hosts. In MySql this is done for a user 
named 'monty' with the following statement:
	mysql> GRANT ALL PRIVILEGES ON *.* TO 'monty'@'%' IDENTIFIED BY 'some_pass';
5) Run the create-table task from the Ant build.xml file. This can be done using
the following command
	ant create-table
6) Set up a http server instance (for example Apacher Server) and copy the 
images direcory in a web-accessable directory.
7) Run the Ant deploy task
	ant deploy
	

4. Configuring WebCt Side
=========================




5. Configuring LAMS Side
========================
