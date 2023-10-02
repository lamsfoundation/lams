//callback method that is invoked after image has been successfully uploaded to the server
function imageUploadedCallback(data) {
   	self.parent.checkNew();
}
        
function hideMessage(){
	$("#new-image-input-area").hide();
    $("#manage-image-buttons, #favourite-button").show();
}

function newImageInit(newImageInitUrl) {
	showMessage(newImageInitUrl);
	$("#manage-image-buttons, #favourite-button").fadeOut();
	$("html, body").animate({ scrollTop: $(document).height() }, "slow");
}
