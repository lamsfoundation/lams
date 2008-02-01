Blackboard CE 6 (WebCt) - LAMS Integration Module
=================================================

Written By Luke Foxton <lfoxton@melcoe.mq.edu.au>
LAMS Foundation Copyright (C) 2008 (http://lamsfoundation.org)

For more information on and help, please visit the wiki at:
http://wiki.lamsfoundation.org/display/lamsdocs/Blackboard+CE+6

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

Pre-Steps

There is some setup before we can commence the installation of the Powerlink. 

   1. Follow the steps at in the appropriate Blackboard CE 6.x Setup guide to install Blackboard CE. Make sure you follow the correct setup guide for your version as the installation steps can change from version to version. The setup guides can be found at https://behind.blackboard.com/s/developer/refcenter/docs/.
   2. Install LAMS 2.0.6 or higher on a separate server.
   3. Ensure that each server can access the web addresses of the other, this can simply be done by accessing the LAMS or Blackboard CE login page.
   4. Set up a web server address to store the images used by the Powerlink. The easiest way to do this is to copy it into the LAMS server images folder.
      Copy the 'treeicons' folder found in /path/to/lams2-webct-integration-1.0.0/deploy/web/tigra/ into /path/to/jboss-4.0.2/server/default/deploy/lams.ear/lams-central.war/images.
   5. Test that the images are accessible on the web by opening a web browser and going to http://<your lams server>:8080/lams/images/tigra_base.gif.
   6. Set up a MySql database which is accessible from the Blackboard CE server (This can be the same database as used for the LAMS server if appropriate).
   7. Set up a special MySql user which the Powerlink can use to talk to the database. Make sure the user has cross-server permission if the MySql server is not located on the same server as Blackboard CE. This can be done using a command on the MySql command line:
      GRANT ALL PRIVILEGES ON <database name> TO '<username>'@'<host>' IDENTIFIED BY '<user password>.
   8. You can test the cross-server MySql access by installing a MySql client and the typing the following command:
      mysql -h <host> -u <user> -p <password> <database name>;

Installation of Powerlink

   1. Download lams2-webct-integration-1.0.0.zip from the downloads page. (Not yet available for download)
   2. Unzip to a temporary location.
   3. The package should contain the following files:

    * apache-ant-1.7.0 (folder)
    * db (folder)
    * deploy (folder)
    * lib (folder)
    * build.properties
    * build.xml
    * DeployableComponentConfig.xml
    * license.txt
    * readme.txt
    * release_notes.txt

   4. Open up the build.properties in a text editor, you will need to change these entries to suit your own configuration. 
      NOTE: These settings can be configured in the Blackboard CE admin pages, EXCEPT for the image URL which must be correct, otherwise you will need to re-install the package.
	

4. Configuring WebCt Side
=========================

   1. Login to the Blackboard CE server as serveradmin.
   2. Make sure you are at the server level, by clicking it on the Administration tree one the left.
   3. Click on utilities (the red box) tab, then settings to get to the admin screen.
   4. Under the System Integration list there should now be a link called "LAMS Integration Module", click on that link.
   5. Select the true option of the Enable setting and also ensure you select the "Override Setting at Child Contexts" option to enable it at all contexts.
      NOTE: DO NOT select the "Lock This Setting" Option yet as there have been known issues associated with this, these settings are only visible to the serveradmin user so there is no need to lock them.
   6. You can edit the other configuration fields (except of the image url), but make sure you select the "Override Setting at Child Contexts" option to filter the setting to all contexts. Again, do not lock the settings.
   7. Scroll down to the bottom of the page and click save to save the configuration.
   8. Next, click on the Powerlinks Proxy Tools tab, then click the Create Proxy Tool button.
   9. From the drop down menu, select "LAMS Integration Module".
  10. Enter the proxy tool name, version (1.0) and description, do not lock any of they settings, and do not select the "Release to My WebCT" option.
  11. Click save, the new Proxy Tool should appear in the list.
  12. Now return to the settings screen by clicking on the utilities (red box) tab then clicking settings.
  13. Under tools, a new link should appear with the same name as you used for the proxy tool name. Click it.
  14. The Enable Tool Field should be locked, you will need to unlock it first, then return to the menu again and select true to enable the tool. Make sure you select the "Override Setting at Child Contexts" also.


5. Configuring LAMS Side
========================

   1. Follow the instructions to install LAMS, it is recommended that you install LAMS and Blackboard CE on separate servers to account for heavy memory requirements. You can either build LAMS  from source or follow the instructions for Window Installers  or Unix Installers. 
   2. Login to LAMS as sysadmin.
   3. Goto Sys Admin->Maintain integrated servers->Add New Server.
   4. Setup the fields to match those that you put on the Blackboard side
   5. The id and key should map to the "LAMS SERVER ID" and "LAMS SERVER SECRET KEY" fields respectively on the LAMS properties page in Blackboard (see above).
   6. Fill in a description of the Blackboard server.
   7. The "Prefix" field denotes a prefix that will be added to all users from Blackboard CE, for example if you put webct as the prefix and accessed LAMS with a user called "user", their name in LAMS would be "webct_user".
   8. The 'disabled' checkbox disables this integration, you can select this later if you wish to disable the connection.
   9. The User Information URL is arbitrary for LAMS/Blackboard CE integrations, so put a dummy URL in.
  10. You can specify a timeout page that LAMS will go to if there is an error.

