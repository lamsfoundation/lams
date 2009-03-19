// creates an httpservice and gets video recordings
private function getRecordingsFromServer(sortBy:String, sortDirection:String):void{
	// show an overlay over the video listings
	LamsAjaxOverlayManager.showOverlay(videoList);
	
	// create and set the service
	var videoRecorderActions:HTTPService = new HTTPService();
	videoRecorderActions.url = serverUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";
	videoRecorderActions.request.method = "getRecordingsByToolSessionIdAndUserId";
	videoRecorderActions.request.toolSessionId = toolSessionId;
	videoRecorderActions.request.toolContentId = toolContentId;
	videoRecorderActions.request.userId = userId;
	
	// if we are in teacher mode or are allowing recording visibility, get all recordings
	if(allowLearnerVideoVisibility || mode == "teacher")
		videoRecorderActions.request.getAll = true;
	// otherwise, get only the users own recordings
	else
		videoRecorderActions.request.getAll = false;
	
	videoRecorderActions.request.sortBy = sortBy;
	videoRecorderActions.request.sortDirection = sortDirection;
	videoRecorderActions.request.allowLearnerVideoVisibility = allowLearnerVideoVisibility;
	videoRecorderActions.addEventListener(ResultEvent.RESULT, getRecordingsSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, getRecordingsFaultHandler);
	
	videoRecorderActions.send();
}

// handler for successful get recordings
private function getRecordingsSuccessHandler(e:ResultEvent):void {
	// update the rating from the servlet's returned xml
	videoRecordings = e.result.recording;
	
	// hide the overlay
	LamsAjaxOverlayManager.hideOverlay(videoList);
}

// fault handler for get recordings
  private function getRecordingsFaultHandler(e:FaultEvent):void {
  	// hide the overlay
  	LamsAjaxOverlayManager.hideOverlay(videoList);
  	
  	// show an alert with the error
  	Alert.show(e.toString());
}

// creates an httpservice and saves a video recording
private function saveRecordingToServer(userId:int, title:String, description:String, filename:String, rating:Number, toolSessionId:int, recordingId:int):void{
	// create and set the service
	var videoRecorderActions:HTTPService  = new HTTPService();
	videoRecorderActions.url = serverUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";
	videoRecorderActions.request.method = "saveRecording";
	videoRecorderActions.request.userId = userId;
	videoRecorderActions.request.recordingId = recordingId;
	videoRecorderActions.request.title = title;
	videoRecorderActions.request.rating = rating;
	videoRecorderActions.request.description = description;
	videoRecorderActions.request.filename = filename;
	
	if(!allowUseCamera && allowUseVoice)
		videoRecorderActions.request.isJustSound = true;
	else
		videoRecorderActions.request.isJustSound = false;
	
	videoRecorderActions.request.toolContentId = -1;
	videoRecorderActions.request.saveToLams = false;
	
	videoRecorderActions.request.toolSessionId = toolSessionId;
	videoRecorderActions.addEventListener(ResultEvent.RESULT, saveRecordingSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, saveRecordingFaultHandler);
	
	videoRecorderActions.send();
}

// handler for successful save recording
private function saveRecordingSuccessHandler(e:ResultEvent):void {	        	
	// update the video recordings
	getRecordingsFromServer(sortButtonGroup.sortBy, sortButtonGroup.sortDirection);	
}

// fault handler for save recording
  private function saveRecordingFaultHandler(e:FaultEvent):void {
  	Alert.show(e.toString());
}

// creates an httpservice and saves a comment
private function saveCommentToServer(toolSessionId:int, recordingId:int, userId:int, commentId:int, comment:String):void{
	// show an overlay over the recording information
	LamsAjaxOverlayManager.showOverlay(videoInformation);
	
	// create and set the service
	var videoRecorderActions:HTTPService  = new HTTPService();
	videoRecorderActions.url = serverUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";
	videoRecorderActions.request.method = "saveComment";
	videoRecorderActions.request.userId = userId;
	videoRecorderActions.request.recordingId = recordingId;
	videoRecorderActions.request.comment = comment;
	videoRecorderActions.request.commentId = commentId;
	videoRecorderActions.request.toolSessionId = toolSessionId;

	videoRecorderActions.addEventListener(ResultEvent.RESULT, saveCommentSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, saveCommentFaultHandler);
	
	videoRecorderActions.send();
}

// handler for successful save recording
private function saveCommentSuccessHandler(e:ResultEvent):void {
	// update the comments with the xml sent back from the servlet
	videoInformation.printComments(XMLList(e.result));
	
	// update the video recordings
	getRecordingsFromServer(sortButtonGroup.sortBy, sortButtonGroup.sortDirection);	
	
	// scroll the video information box to the position of the "Comments:" label
	videoInformation.verticalScrollPosition = videoInformation.addCommentButton.y;
	
	// hide the overlay
  	LamsAjaxOverlayManager.hideOverlay(videoInformation);
}

// fault handler for save recording
  private function saveCommentFaultHandler(e:FaultEvent):void {
	// hide the overlay
  	LamsAjaxOverlayManager.hideOverlay(videoInformation);
  	
  	// print the error
  	Alert.show(e.toString());
}
       
// creates an httpservice and saves a rating
private function saveRatingToServer(toolSessionId:int, ratingId:int, userId:int, rating:Number, recordingId:int):void{
	// show an overlay over the recording information
	LamsAjaxOverlayManager.showOverlay(videoInformation);
	
	// create and set the service	
	var videoRecorderActions:HTTPService  = new HTTPService();
	videoRecorderActions.url = serverUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";
	videoRecorderActions.request.method = "saveRating";
	videoRecorderActions.request.userId = userId;
	videoRecorderActions.request.rating = rating;
	videoRecorderActions.request.ratingId = ratingId;
	videoRecorderActions.request.toolSessionId = toolSessionId;
	videoRecorderActions.request.recordingId = recordingId;

	videoRecorderActions.addEventListener(ResultEvent.RESULT, saveRatingSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, saveRatingFaultHandler);
	
	videoRecorderActions.send();
}

// handler for successful save rating
private function saveRatingSuccessHandler(e:ResultEvent):void {
	// set the last rating clicked to voted
	ratingClicked.voted = true;
	
	// set its average value
	ratingClicked.value = e.result.rating;
	
	// set the user's voted value
	ratingClicked.votedValue = e.result.userRating;
	
	// fix the tooltip
	ratingClicked.toolTip = dictionary.getLabelAndConcatenate("videorecorder.tooltip.already.rated", [" ", String(e.result.userRating)]);
	
	// hide the overlay
  	LamsAjaxOverlayManager.hideOverlay(videoInformation);
  	
	// update the video recordings
	getRecordingsFromServer(sortButtonGroup.sortBy, sortButtonGroup.sortDirection);
}

// fault handler for save rating
  private function saveRatingFaultHandler(e:FaultEvent):void {
  	// hide the overlay
  	LamsAjaxOverlayManager.hideOverlay(videoInformation);
  	
  	// show the error
  	Alert.show(e.toString());
}

// creates an httpservice and saves a rating
private function deleteRecordingFromServer(recordingId:int):void{
	var videoRecorderActions:HTTPService  = new HTTPService();
	videoRecorderActions.url = serverUrl;
	videoRecorderActions.method = "POST";
	videoRecorderActions.resultFormat = "e4x";
	videoRecorderActions.request.method = "deleteRecording";
	videoRecorderActions.request.recordingId = recordingId;

	videoRecorderActions.addEventListener(ResultEvent.RESULT, deleteRecordingSuccessHandler);
	videoRecorderActions.addEventListener(FaultEvent.FAULT, deleteRecordingFaultHandler);
	
	videoRecorderActions.send();
}

// handler for successful delete recording
private function deleteRecordingSuccessHandler(e:ResultEvent):void {
	// update the video recordings
	getRecordingsFromServer(sortButtonGroup.sortBy, sortButtonGroup.sortDirection);
	
	// reset video information
	videoInformation.resetInformation();
	
	videoDisplay.reset();
}

// fault handler for detele recording
  private function deleteRecordingFaultHandler(e:FaultEvent):void {
  	Alert.show(e.toString());
}

// handler for successful save preview image
private function savePreviewImageSuccessHandler(e:ResultEvent):void {
	Alert.show(e.toString());
}

// fault handler for save preview image
  private function savePreviewImageFaultHandler(e:FaultEvent):void {
  	Alert.show(e.toString());
}