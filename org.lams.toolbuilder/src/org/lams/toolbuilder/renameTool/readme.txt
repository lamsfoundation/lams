Readme.txt

This application will try to generate a new lams tool based on an existing 
lams tool by substituting given strings in the existing project with new 
values.  

The program will look for a file called 'tasklist.conf' in the current 
directory.  The file contains two types of directives.

These instructions assume you already have a tool signature for your tool.
If you do not have one, have a look at 
http://wiki.lamsfoundation.org/display/lams/Tool+Signatures.
The tool signature is used in many places, such as the table names and it 
is laborious to change it when you have a lot of code already written.

Source <Directory> 
	The 'Directory' should contain the lams tool that you want to rename.

Rename <Old Value> <New Value>:
	Rename will replace all instances of <Old Value> with <New Value> 
	for all files in the 'Directory' specified.  It executes the replace 
	in the order they are listed.
	
Step 1.
	Determine which project you want to base your tool on.  The Submit tool 
	is good candidate since it is very simple and will require the least
	to remove unwanted functionality.
	
	Make a copy of the lams tool and place it in a new folder.
		
Step 2. 
	Add the strings that need to be replaced to the 'tasklist.conf' file.
	
	These strings are used for the submit tool
	
		Rename lasbmt11 laex11
		Rename SubmitFiles Example
		Rename submitFiles example
		Rename sbmt example
		Rename Sbmt Example
		Rename Submit Example
		Rename submit example

	Note: lasbmt11 and laex11 are the old and new tool signatures.
		
Step 3.
	Compile and execute the java program Main.java
	
	You should see a list of delete and rename operations that tool place.
	
Step 4.
	The 'Directory' should now contain the new lams tool.
	
	Copy this folder to eclipse workspace which contains other lams project.
	
	Import the project into eclipse. In Eclipse do File, New Project, select Java Project, click Next and 
	enter the name of the new project folder. Leave "Contents" set to "Create new project in workspace". 
	Then click Finished. This should create the new project in Eclipse.
	
Step 5.
	Make necessary changes to lams_build to include the new lams tool:
	
	In lams_build/shared.properties, add an entry for your new tool. e.g. tool_example_dir=lams_tool_example

	In lams_build/build.ml find the task "deploy-tools". Add a new section similar to:
		<ant ant file="../${tool_example_dir}/build.ml" target="deploy-tool"
				inherit All="false" >
			<property name="boss.deploy" location="${boss.deploy}"/>
		</ant>

	To shorten the time it takes to rebuild the whole system, you can comment out 
	some of the other tools. If you comment out the tool_Lars_dir or tool_forum_dir
	entries, make sure you also comment out deploy-learning-libraries. The deploy-learning-libraries
	creates the combined share resources/forum activity in authoring and this requires
	the share resources and forum tools.
