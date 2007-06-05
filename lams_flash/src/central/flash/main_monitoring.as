import org.lamsfoundation.lams.monitoring.Application;
import org.lamsfoundation.lams.common.util.StringUtils;
import org.lamsfoundation.lams.common.util.Debugger;
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
Debugger.log('lesson launch is set as:'+_root.lessonLaunch ,Debugger.CRITICAL,'main','ROOT');

if (StringUtils.isEmpty(lessonLaunch)){
	_root.lessonLaunch = false;
	Debugger.log('lesson launch is set as:'+_root.lessonLaunch ,Debugger.CRITICAL,'mainin if condition','ROOT');
}
if (StringUtils.isEmpty(editOnFly)){
	_root.editOnFly = false;
	Debugger.log('editOnFly is set as:'+_root.editOnFly ,Debugger.CRITICAL,'mainin if condition','ROOT');
}
if(StringUtils.isEmpty(mode)){
	_root.mode = 1;
	Debugger.log('Mode is not defined, using defualt:'+_root.mode,Debugger.CRITICAL,'main','ROOT');			
}

if(StringUtils.isEmpty(version)){
	_root.version = "undefined";
	Debugger.log('version is not defined.', Debugger.CRITICAL,'main','ROOT');
}

if(StringUtils.isEmpty(lessonID)){
	_root.lessonID = 1;
	Debugger.log('LessonID is not defined, using defualt:'+_root.lessonID,Debugger.CRITICAL,'main','ROOT');			
}

if(StringUtils.isEmpty(langDate)){
	_root.langDate = "01-01-1970";
}

if(StringUtils.isEmpty(actColour)){
	_root.actColour = "true";
}

_root.monitoringURL = "monitoring/monitoring.do?method="

//Set stage alignment to top left and prent scaling
Stage.align = "TL";
Stage.scaleMode = "noScale";


//Start the application, passing in the top level clip, i.e. _root
var app:Application = Application.getInstance();
app.main(this);

//Make app listener for stage resize events
Stage.addListener(app);




