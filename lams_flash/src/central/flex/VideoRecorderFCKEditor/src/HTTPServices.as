// creates an httpservice and saves a video recording
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.http.HTTPService;

private function saveRecordingToServer(title:String, description:String, filename:String, toolContentId:int):void{
	var videoRecorderActions:HTTPService  = new HTTPService();
	videoRecorderActions.url = toolServletUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";
	videoRecorderActions.request.method = "saveRecording";	
	videoRecorderActions.request.title = title;
	videoRecorderActions.request.description = description;
	videoRecorderActions.request.filename = filename;
	videoRecorderActions.request.toolContentId = toolContentId;
	
	videoRecorderActions.request.saveToLams = false;
	videoRecorderActions.request.isJustSound = false;
	videoRecorderActions.request.userId = -1;
	videoRecorderActions.request.toolSessionId = -1;
	videoRecorderActions.request.recordingId = -1;
	videoRecorderActions.request.rating = 0;

	videoRecorderActions.addEventListener(ResultEvent.RESULT, saveRecordingSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, saveRecordingFaultHandler);
	
	videoRecorderActions.send();
}

// success handler for successful save recording
private function saveRecordingSuccessHandler(e:ResultEvent):void {	        	
	// alert complete
	Alert.show(dictionary.getLabel("videorecorder.recording.complete.authoring"));
	
	// change the start recording button's label
	videoDisplay.makeReadyVideo(filename);
	
	// update tooltip
	videoControlBar.recordButton.toolTip = dictionary.getLabel("videorecorder.tooltip.start.recording.again");
					
	// change the panel status
	videoDisplayPanel.status = dictionary.getLabel("videorecorder.ready");	
}

// fault handler for save recording
private function saveRecordingFaultHandler(e:FaultEvent):void {
  	Alert.show(e.toString());
}

// export recording to lams server
private function copyRecordingToLamsServer(src:String, dir:String, filename:String):void{
	var videoRecorderActions:HTTPService  = new HTTPService();
	videoRecorderActions.url = saveToLamsServletUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";
	
	videoRecorderActions.request.urlStr = src;
	videoRecorderActions.request.dir = dir;
	videoRecorderActions.request.filename = filename;

	videoRecorderActions.addEventListener(ResultEvent.RESULT, copyRecordingToLamsServerSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, copyRecordingToLamsServerFaultHandler);
	
	videoRecorderActions.send();
}

// success handler for copy recording
private function copyRecordingToLamsServerSuccessHandler(e:ResultEvent):void {
	// rock on
}
// fault handler for copy recording
private function copyRecordingToLamsServerFaultHandler(e:FaultEvent):void {
	Alert.show(e.toString());
}

// attempts to get lanugage XML
private function getLanguageXMLFromServer():void{
	var videoRecorderActions:HTTPService  = new HTTPService();
	videoRecorderActions.url = saveToLamsServletUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";
	
	videoRecorderActions.request.method = "getLanguageXMLForFCK";

	videoRecorderActions.addEventListener(ResultEvent.RESULT, getLanguageXMLSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, getLanguageXMLFaultHandler);
	
	videoRecorderActions.send();
}

// success handler for get lanugage XML
private function getLanguageXMLSuccessHandler(e:ResultEvent):void {
	setLanguageXML(String(e.result));
}

// fault handler for get lanugage XML
private function getLanguageXMLFaultHandler(e:FaultEvent):void {
	Alert.show(e.toString());
}

