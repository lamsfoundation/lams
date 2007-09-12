LAMS 2 and .LRN 2.3 Integration packages
----------------------------------------
@creation-date 2007-09-12
@cvs-id $Id$

These packages are released under GNU GPL version 2.0 (http://lamsfoundation.org/license/lams/2.0/)

Packages:

* dotlrn-lams2int

  LAMS2 dotLRN portlet

* lams2conf

 Configuration settings

* lams2int

  Integration logic

* lams2int-portlet

  dotLRN portlet


These packages have to be places in your <dotlrn-instance-folder>/packages 


Installation Steps - .LRN Side
-----------------------------

   1. Download lams2-dotLRN packages from the downloads page.
   2. Place the lams2 packages in your packages folder under your .LRN instance folder
   3. Navigate to Administration > Install Packages > Install from Local
   4. Select the dotLRN LAMS Integration Applet
   5. And install the four packages
   6. Once installed, reboot your AOLServer and go to the Site Map to configure the lams2conf parameters


Configuring the LAMS Side
-------------------------

   1. Follow the instructions to install LAMS. You can either build LAMS from source or follow the instructions for Window Installers or Unix Installers.
   2. Login to LAMS as sysadmin.
   3. Goto Sys Admin->Maintain integrated servers->Add New Server.
   4. Setup the fields to match those that you put on the .LRN side
   5. The id, key and name should map to the "LAMS SERVER URL", "LAMS SERVER ID" and "LAMS SERVER SECRET KEY" respectively on the LAMS properties page in .LRN (see above).
   6. Fill in a description of the .LRN server.
   7. The "Prefix" field denotes a prefix that will be added to all users from .LRN, for example if you put lrn as the prefix and accessed LAMS with a user called "user", their name in LAMS would be "lrn_user".
   8. The 'disabled' checkbox disables this integration, you can select this later if you wish to disable the connection.
   9. You can select an existing group to add .LRN users to, or add a new one.
  10. Enter the User Information URL, this corresponds to the "USER DATA CALLBACK URL". In .LRN these usually is http://<your-server-name>/lams2conf/userinfo?ts=%timestamp%&un=%username%&hs=%hash%
  11. You can specify a timeout page that LAMS will go to if there is an error.


Help?
---

LAMS Wiki:

     http://wiki.lamsfoundation.org/display/lams

LAMS Documentation

     http://wiki.lamsfoundation.org/display/lamsdocs

LAMS dotLRN Integration

     http://wiki.lamsfoundation.org/display/lams/dotLRN
     http://wiki.lamsfoundation.org/display/lams/Integrations

Further help:

	http://lamscommunity.org



Contact Details:
---------------

Tech Info: Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)

LAMS Foundation Limited
Level 1, Dow Corning Building
3 Innovation Road
Macquarie University
NSW 2109
Australia
Email: info@lamsfoundation.org for general enquiries and information.
Phone +61 2 9850 7522 for LAMS Foundation.
