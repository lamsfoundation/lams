Usage steps:

1.Take a look at the properties files in this folder first and edit them as you need.
  Usually you need only modify the following properties : 
  * TargetServer and httpPort in the master test properties file called test.properties usually 
  * NumberOfLearners in learner test properties file 
  * LearningDesignFile in author test properties file

2.Execute "run" ant target on any platform with Apache Ant installed,
  or go to TestHarness4LAMS2 dir in windows explorer or on command line,
  and execute run.bat if you are on Microsoft Windows platform. 
  
* Modify log.properties according to the comments in it to change level of log details  
  
Disclaimer : 

  This program is created in the hope that it will help estimate how many concurrent
  users a LAMS 2.x server can handle, but WITHOUT ANY GARANTEE  the server can support 
  that number of users in service use.
	
  This program is more a load test tool than a functional test tool, 
  so it does NOT GARANTEE there is no functional bug in the target server.
	

TODO list:

1. Add support for any kind of activitis. 
   
   Currently only support activities which consists of pages only with only one 
   "standard" form whose submit are not triggered by javascript. 
   These activities include:
   
   1) noticeboard
   2) grouping
   3) notebook
   4) multiple choice
   
   Not supported are parallel activities and activities which has pages without 
   forms (such as Share Resources) or forms whose submission are triggered by 
   javascript or with multiple forms ( such as Chat and Optional)

2. Generate formal test report document


TODO JUST FOR FUN list:

1. Add Master/Slave mode support so that a few PCs can collaborate on one testsuite. 
   Persisting test records in Master PC's database could be the easiest way to 
   implement that.

2. Implement GUI based test manager  

3. Implement RMI and WS tests if they are implemented in LAMS 2 in future.
   