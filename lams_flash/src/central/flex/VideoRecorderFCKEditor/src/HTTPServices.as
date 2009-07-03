// creates an httpservice and saves a video recording
import mx.controls.Alert;
import mx.graphics.codec.JPEGEncoder;
import mx.graphics.codec.PNGEncoder;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.http.HTTPService;
import mx.utils.Base64Encoder;

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
	
	videoRecorderActions.request.isLocal = true;
	
	if(mic && !cam)
		videoRecorderActions.request.isJustSound = true;
	else
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
	
	// make the movie ready
	videoDisplay.makeReady(filename, "video", offlinePlayback);
	
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
	
	if(mic && !cam)
		videoRecorderActions.request.isJustSound = true;
	else
		videoRecorderActions.request.isJustSound = false;
	
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

// attempts to save a preview image to the content folder
private function savePreviewImage(dir:String, filename:String, ext:String, image:BitmapData):void{
	var videoRecorderActions:HTTPService  = new HTTPService();
	videoRecorderActions.url = toolServletUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";

	videoRecorderActions.request.method = "saveImage";

	videoRecorderActions.addEventListener(ResultEvent.RESULT, savePreviewImageSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, savePreviewImageFaultHandler);
				
	var rawBytes:ByteArray = new ByteArray();
	
	if(ext == "png"){
		var pngEncoder:PNGEncoder = new PNGEncoder();
		rawBytes = pngEncoder.encode(image);
	}
	else if(ext == "jpg"){
		var jpegEncoder:JPEGEncoder = new JPEGEncoder();
		rawBytes = jpegEncoder.encode(image);	
	}
	
	var encoder:Base64Encoder = new Base64Encoder(); 
   	encoder.encodeBytes(rawBytes); 
   	
	videoRecorderActions.request.dir = dir;
   	videoRecorderActions.request.filename = filename;
	videoRecorderActions.request.ext = ext;
	videoRecorderActions.request.data = encoder.flush();
				
	videoRecorderActions.send();
}

// handler for successful save preview image
private function savePreviewImageSuccessHandler(e:ResultEvent):void {
}

// fault handler for save preview image
  private function savePreviewImageFaultHandler(e:FaultEvent):void {
  	Alert.show(e.toString());
}