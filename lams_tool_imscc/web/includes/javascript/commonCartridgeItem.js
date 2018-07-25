	/*
	 This is CommonCartridge Item instrcution area.
	 */
	var instructionTargetDiv = "instructionArea";
    var itemAttachmentTargetDiv = "itemAttachmentArea";
    var singleInstructionHeight = 40;
    
	function removeItemAttachment(idx){
	    removeItemAttachmentLoading();
		$("#" + itemAttachmentTargetDiv).load(
			removeItemAttachmentUrl,
			{
				reqID: new Date()
			}
		);	    
	}
	function removeItemAttachmentLoading(){
		showBusy(itemAttachmentTargetDiv);
	}
	function removeItemAttachmentComplete(){
		hideBusy(itemAttachmentTargetDiv);
	}
	
	function submitCommonCartridgeItem(){
		$("#commonCartridgeItemForm").submit();
		// after submit, it direct to itemlist.jsp, 
		// then refresh "basic tab" commonCartridgelist and close this window.
	}
