lams_openid
===========

Author: luke foxton, lfoxton@melcoe.mq.edu.au

This web application is made specifically for allowing self-registration and 
OpenID login using the java-OpenID library "joid".There are some steps involved
in setting it up, but it should be fairly easy to do if they are followed
carefully.


Step 1
======

Open the web/config.jsp file to configure this module. You will need to set a 
few configurations here like the server_id and server_key you will use. make 
sure the server url you use ends with "/lams/". Most of the other configs are
fairly self explanatory, but take special care to list all the open id providers
that you want to trust.

Step 2
======

Login to LAMS as sysadmin, and go to the integrated servers page. Create a new 
integration instance - we will use this for SSO from OpenID. Use the same server
id and server key as was used in config.jsp

Step 3
======

Run the build-war ant task to get the lams-openid.war and lams-openid.jar. They should end up in the 
build/lib directory

Step 4
======

Stop the LAMS server and copy lams-openid.war and lams-openid.jar to lams.ear.

Step 5
======

Add this xml into the lams.ear/META-INF/application.xml file so JBOSS can 
recognise the web application:

	<module>
		<web>
			<web-uri>lams-openid.war</web-uri>
			<context-root>/lams/openid</context-root>
		</web>
	</module>

Step 6
======

Edit the lams-central.war/login.jsp to link to the OpenID login page which will 
be:

http://<your server>/lams/openid

Users using OpenID login will need to always go to this page in order to log in.

Step 7
======

Restart LAMS and you should be ready to go.


