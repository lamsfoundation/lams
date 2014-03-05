Blackboard - LAMS Integration Module
====================================

By Luke Foxton <lfoxton@melcoe.mq.edu.au>
LAMS Foundation Copyright (C) 2007 (http://lamsfoundation.org)

Updated for the Blackboard 9.1 SP6 Building block by Richard Stals (www.stals.com.au) Edith Cowan University 2011

Contents
================================================================================
1. Requirements
   1.1 Minimum requirement to install the module
   1.2 Minimum requirement to build the module from source
2. Installing the module in Blackboard
3. Configuring the module in Blackboard
4. Configuring the LAMS Server for Blackboard Integration
5. Creating BB course
6. Creating LAMS lesson
7. Accessing Gradebook marks


1. Requirements
================================================================================
	1.1 Minimum requirement to install the module
		- Blackboard Learning System 9.1

	1.2 Minimum requirement to build the module from source
		- Java 2 Platform, Standard Edition, v 1.6
		- Ant 1.6


2. Installing the module in Blackboard
================================================================================
	1. Download lams2-bb-plugin-1.x.war
	2. Log into Blackboard as Administrator
	3. Click on 
		System Admin->Building Blocks->Installed Tools->Upload Building Block
	4. Click on Browse and select lams2-bb-plugin-1.x.war 
	5. Submit
	6. Select "Available" in the Availability drop-down menu and click ok
	7. Configure the module (See "Configuring the module in Blackboard")


3. Configuring the module in Blackboard
================================================================================
	1. Once the LAMS2 integration Block is installed, Goto
		System Admin->Building Blocks->Installed Tools
	2. Click the "Properties" button in the LAMS Module row
	3. Fill in the LAMS server url, this is the url that points to the login
	   page for LAMS. The same as the server URL set during LAMS installation
	4. Set the LAMS server ID, this should be set the same on the LAMS server
	   (See "Configuring the LAMS Server for Blackboard Integration")
	5. Set the LAMS server secret key, also the same as on the LAMS server
	   (See "Configuring the LAMS Server for Blackboard Integration")
	6. Set the Blackboard request src, this is an arbitrary string that the 
	   LAMS server uses when returning calls to Blackboard
	7. A User data callback URL is shown in this page, this URL provides LAMS 
	   with dynamic user provisioning data. You will need this URL for LAMS
	   configuration 


4. Configuring the LAMS Server for Blackboard Integration
================================================================================
	1. Login to LAMS as sysadmin	
	2. Open the Sys Admin window, the navigate to the "Maintain Integrated 
	   Servers page.
	3. Click "Add New Server"
	4. Put in the same ID and key as blackboard
	   (See steps 3-4 of "Configuring the module in Blackboard")
	5. Enter a name and description for the integration
	6. Enter a prefix, all Blackboard users that use the LAMS module will be
	   created in LAMS with this prefix before the username.
	7. Enter the "User Information URL", this points to the servlet that
	   provides LAMS with Blackboard user information.
	   (See step 7 of "Configuring the module in Blackboard")
	8. You can optionally put a timeout URL and choose an organisation to add
	   the integrated server to.
 
 
5. Creating BB course
================================================================================
	1. Click on My Institution->Launch the Course Creation Wizard and follow all required steps.
	2. To browse available courses head to Courses and there is a "Course List"
	3. In order to be able to add new LAMS lessons in this course (using the module) 
		get inside a course and then on the left side menu select "Customziation" and 
		then "Tool availability". Make sure LAMS Lesson has a tick in front of it. 


6. Creating LAMS lesson
================================================================================
	1. Create BB Course as described above
	2. Go to Courses->${YOUR_COURSE_NAME}->Information->Tools->Lams2. 
	If you don't have Lams2 in the list then something went wrong on installation of lams2-bb-plugin-1.x.war
	3. Enter all necessary information and press submit.


7. Accessing Gradebook marks
================================================================================
	1. Created LAMS lesson.
	Once you created LAMS lesson it automatically adds grade to gradebook using the next algorithm: if LAMS lesson contains MCQ or Assessment
	it creates scorable grade, otherwise - complete/incomplete grade.
	2. Go to Courses->${YOUR_COURSE_NAME}->Grade Center->Full Grade Center. 
	And you'll see there the grade with lesson's name. That is the column which will hold all the grades for that lesson.
	* One note though: only learners receive grades (thus Administrators don't get one)
