-- LAMS 2 Server.applescript
-- LAMS 2 Server

--  Created by Ernie Ghiglione on 1/04/10.
--  Copyright 2010 LAMS Foundation. All rights reserved.

on awake from nib theObject
	RefreshStatus()
end awake from nib

on clicked theObject
	-- Runs/Stops LAMS server according to application status
	set status to (do shell script "/usr/local/lams/bin/lams status") as string
	
	if status contains "is not running" then
		do shell script "/usr/local/lams/bin/lams start &> /dev/null" with administrator privileges
		open location "file:///usr/local/lams/static/index.html"
	else if status contains "is running" then
		do shell script "/usr/local/lams/bin/lams stop" with administrator privileges
		display dialog "LAMS server is stopped now" attached to window "main" buttons {"OK"} with icon 0
	end if
	RefreshStatus()
end clicked


on RefreshStatus()
	-- This function tries to find the pid. The LAMS_Server/bin/lams bash scripts
	-- creates the file /var/run/lams-1.0.2.pid with the pid number of the LAMS Server 
	-- process. 
	set status to (do shell script "/usr/local/lams/bin/lams status") as string
	set contents of text field "statusLabel" of window "main" to status
	
	if status contains "is not running" then
		set text color of text field "statusLabel" of window "main" to "red"
		set contents of text field "urlLabel" of window "main" to ""
		set title of button "actionButton" of window "main" to "Start"
	else
		set text color of text field "statusLabel" of window "main" to "green"
		set contents of text field "urlLabel" of window "main" to "The URL is: http://localhost:8080/lams"
		set title of button "actionButton" of window "main" to "Stop"
	end if
	
end RefreshStatus

