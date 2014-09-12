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
		var win = null;
		if (window.hideMessage) { 
			win = window;
		} else if (window.parent && window.parent.hideMessage) {
			win = window.parent;
		} else {
			win = window.top;
		}
		win.hideMessage();
	}
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area = window.document.getElementById('reourceInputArea');
		var elem = window.document.getElementById("saveCancelButtons");
		if (!area && window.parent) {
			area = window.parent.document.getElementById('reourceInputArea');
			elem = window.parent.document.getElementById("saveCancelButtons");
		}  
		if (!area) {
			obj = window.top.document.getElementById('reourceInputArea');
			elem = window.top.document.getElementById("saveCancelButtons");
		}
		
		if(area != null){
			area.style.width="650px";
			area.src=url;
			area.style.display="block";
		}

		if (elem != null) {
			elem.style.display="none";
		}
		location.hash = "reourceInputArea";
	}	
