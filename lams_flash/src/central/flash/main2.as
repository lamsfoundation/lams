import org.lamsfoundation.lams.learner.Application;
import org.lamsfoundation.lams.common.util.StringUtils;
import com.macromedia.javascript.JavaScriptProxy;
import org.lamsfoundation.lams.common.util.Debugger;

// Temp values to be removed / repplaced at deployment
_global.myRoot = this;

if(StringUtils.isEmpty(serverURL)){
	_root.serverURL = "http://localhost:8080/lams/";
	Debugger.log('serverURL is not defined, using defualt:'+_root.serverURL ,Debugger.CRITICAL,'main','ROOT');			
}

if(StringUtils.isEmpty(mode)){
	_root.mode = 1;
	Debugger.log('Mode is not defined, using defualt:'+_root.mode,Debugger.CRITICAL,'main','ROOT');			
}

if(StringUtils.isEmpty(lessonID)){
	_root.lessonID = 1;
	Debugger.log('Lesson ID is not defined, using defualt:'+_root.lessonID,Debugger.CRITICAL,'main','ROOT');			
}

if(StringUtils.isEmpty(uniqueID)){
	_root.uniqueID = 0;
	Debugger.log('Unique ID is not defined.',Debugger.CRITICAL,'main','ROOT');
}

if(StringUtils.isEmpty(langDate)){
	_root.langDate = "01-01-1970";
}

//Set stage alignment to top left and prent scaling
Stage.align = "TL";
Stage.scaleMode = "noScale";

//Start the application, passing in the top level clip, i.e. _root
var app:Application = Application.getInstance();
app.main(this);

//Local connection to JSPs for progress data
var receive_lc = new LocalConnection();
//Functions to setProgress data, called by the LocalConnection object in learner JSPs
receive_lc.setProgressData = function(attempted, completed, current, lessonID, version, refresh) {
	Debugger.log(arguments.toString(), Debugger.CRITICAL, 'learnerProgress_lc.setProgressData', 'ROOT');
	app.refreshProgress(attempted, completed, current, lessonID, version);
	myRoot.refresh = refresh;
};

var success = receive_lc.connect("learnerProgress_lc_" + uniqueID);

//Make app listener for stage resize events
Stage.addListener(app);