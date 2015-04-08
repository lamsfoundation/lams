	/*
	 This is Resource Item instrcution area.
	 */
    var itemAttachmentTargetDiv = "itemAttachmentArea";
// Please set these 1 variables in JSP file for using tag reason:
//removeItemAttachmentUrl
	function removeItemAttachment(){
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
		    		method:'get',
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
	function showBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.show(targetDiv+"_Busy");
		}
	}
	function hideBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.hide(targetDiv+"_Busy");
		}				
	}
	
	function highlightMessage() {
		$('.highlight').filter($('table')).css('background','none');
		$('.highlight').filter($('div')).effect('highlight', {color: "#f4c239"}, 6000);
		$('.highlight').removeClass('highlight');
	}

