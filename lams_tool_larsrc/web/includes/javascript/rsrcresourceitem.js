	/*
	 This is Resource Item instrcution area.
	 */
	var instructionTargetDiv = "instructionArea";
    var itemAttachmentTargetDiv = "itemAttachmentArea";
    var singleInstructionHeight = 40;
    
	function removeInstruction(idx){
	    var param = $("#instructionForm").serialize() + "&removeIdx="+idx;
	    removeInstructionLoading();
	    $.get(removeInstructionUrl, param, function(xml) {
	    	removeInstructionComplete();	
	    	document.getElementById("instructionArea").innerHTML = xml;
	    });
	}
	function removeItemAttachment(idx){
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
	function addInstruction(){
	    var param = $("#instructionForm").serialize();
		addInstructionLoading();
	    $.get(addInstructionUrl, param, function(xml) {
	    	addInstructionComplete();
	    	document.getElementById("instructionArea").innerHTML = xml;
	    });
	    return false;

	}
	function adjustInstructionsDisplayAreaHeight(adjustAmount){
		var obj = window.top.document.getElementById('reourceInputArea');
		obj.style.height=obj.contentWindow.document.body.scrollHeight+adjustAmount+'px';
	}
	function upItem(itemIdx){
		if(itemIdx == 0)
			return;
		var currId = "instructionItemDesc" + itemIdx;
		var repId = "instructionItemDesc" + (--itemIdx);
		switchValue(currId,repId);
	}
	function downItem(itemIdx,maxSize){
		if(itemIdx == (maxSize -1))
			return;
		var currId = "instructionItemDesc" + itemIdx;
		var repId = "instructionItemDesc" + (++itemIdx);
		switchValue(currId,repId);
	}
	function switchValue(currId,repId){
		var temp = document.getElementById(repId).value;
		document.getElementById(repId).value = document.getElementById(currId).value;
		document.getElementById(currId).value = temp;
	}
	function removeInstructionLoading(){
		showBusy(instructionTargetDiv);
	}
	function removeInstructionComplete(){
		hideBusy(instructionTargetDiv);
		adjustInstructionsDisplayAreaHeight(-singleInstructionHeight);
	}
	function removeItemAttachmentLoading(){
		showBusy(itemAttachmentTargetDiv);
	}
	function removeItemAttachmentComplete(){
		hideBusy(itemAttachmentTargetDiv);
	}
	function addInstructionLoading(){
		showBusy(instructionTargetDiv);
	}
	function addInstructionComplete(){
		hideBusy(instructionTargetDiv);
		adjustInstructionsDisplayAreaHeight(singleInstructionHeight);
	}
	
	function submitResourceItem(){
		document.getElementById("instructionList").value = $("#instructionForm").serialize();
		$("#resourceItemForm").submit();
		// after submit, it direct to itemlist.jsp, 
		// then refresh "basic tab" resourcelist and close this window.
	}
	function cancelResourceItem(){
		window.top.hideMessage();
	}
