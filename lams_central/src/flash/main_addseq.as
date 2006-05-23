import org.lamsfoundation.lams.wizard.Application;
import org.lamsfoundation.lams.common.util.StringUtils;

//Temp values to be removed / repplaced at deployment
/**/
if(StringUtils.isEmpty(serverURL)){
	//_root.serverURL = "http://dolly.uklams.net:8080/lams/";
	_root.serverURL = "http://localhost:8080/lams/";
	Debugger.log('serverURL is not defined, using defualt:'+_root.serverURL ,Debugger.CRITICAL,'main','ROOT');			
}

if(StringUtils.isEmpty(userID)){
	_root.userID = 4;
	Debugger.log('userID is not defined, using defualt:'+_root.userID ,Debugger.CRITICAL,'main','ROOT');			
}

if(StringUtils.isEmpty(mode)){
	_root.mode = 1;
	Debugger.log('Mode is not defined, using defualt:'+_root.mode,Debugger.CRITICAL,'main','ROOT');			
}

if(StringUtils.isEmpty(courseID)){
	_root.courseID = 2;	// Playpen (test)
	Debugger.log('CourseID is not defined, using defualt:'+_root.courseID,Debugger.CRITICAL,'main','ROOT');			
}
/**
if(StringUtils.isEmpty(classID)){
	_root.classID = 3;	// Everybody (test)
	Debugger.log('ClassID is not defined, using defualt:'+_root.classID,Debugger.CRITICAL,'main','ROOT');			
}
*/
//Set stage alignment to top left and prent scaling
Stage.align = "TL";
Stage.scaleMode = "noScale";


//Start the application, passing in the top level clip, i.e. _root
var app:Application = Application.getInstance();
app.main(this);

//Make app listener for stage resize events
Stage.addListener(app);




