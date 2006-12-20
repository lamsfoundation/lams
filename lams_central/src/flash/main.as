import org.lamsfoundation.lams.authoring.Application;
import org.lamsfoundation.lams.common.util.StringUtils;

_global.myRoot = this;
this._lockroot = true;

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

if(StringUtils.isEmpty(lang)){
	_root.lang = "en";
}

if(StringUtils.isEmpty(country)){
	_root.country = undefined;
}

if(StringUtils.isEmpty(build)){
	_root.build = "2.0";
}

if(StringUtils.isEmpty(uniqueID)){
	_root.uniqueID = 0;
	Debugger.log('Unique ID is not defined.',Debugger.CRITICAL,'main','ROOT');
}

if(StringUtils.isEmpty(langDate)){
	_root.langDate = "01-01-1970";
}

if(StringUtils.isEmpty(actColour)){
	_root.actColour = "true";
}

if(StringUtils.isEmpty(requestSrc)) {
	_root.requestSrc = null;
}
				
if(StringUtils.isEmpty(isMac)) {
	_root.isMac = false;
}				

//Set stage alignment to top left and prent scaling
Stage.align = "TL";
Stage.scaleMode = "noScale";


//Start the application, passing in the top level clip, i.e. _root
var app:Application = Application.getInstance();
app.main(this);

//------------------------------Local connection to JSPs for progress data ------------------------------
var receive_lc = new LocalConnection();
//-------------------------------------- Functions to setProgress data, called by the LocalConnection object in learner JSPs
receive_lc.setImportDesign = function(learningDesignID, refresh) {
	Debugger.log(arguments.toString(), 'importUpdate_lc.setImportDesign');
	app.getCanvas().openDesignByImport(learningDesignID);
	myRoot.refresh;
};
var success = receive_lc.connect("importUpdate_lc_" + uniqueID);


//Make app listener for stage resize events
Stage.addListener(app);




