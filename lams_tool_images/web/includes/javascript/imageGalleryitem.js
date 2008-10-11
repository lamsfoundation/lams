    var itemAttachmentTargetDiv = "itemAttachmentArea";
	function removeItemAttachment(idx){
		//var id = "instructionItem" + idx;
		//Element.remove(id);
 		var url= removeItemAttachmentUrl;
	    var reqIDVar = new Date();
	    var param = "reqID="+reqIDVar.getTime();
	    removeItemAttachmentLoading();
	    var myAjax = new Ajax.Updater(
		    	itemAttachmentTargetDiv,
		    	url,
		    	{
		    		method:'post',
		    		parameters:param,
		    		onComplete:removeItemAttachmentComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function removeItemAttachmentLoading(){
		showBusy(itemAttachmentTargetDiv);
	}
	function removeItemAttachmentComplete(){
		hideBusy(itemAttachmentTargetDiv);
	}
	
	function cancelImageGalleryItem(){
		window.top.hideMessage();
	}
