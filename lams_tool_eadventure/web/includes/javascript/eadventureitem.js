	/*
	 This is Eadventure Item instrcution area.
	 */
	var instructionTargetDiv = "instructionArea";
    var gameAttachmentTargetDiv = "gameAttachmentArea";
    var singleInstructionHeight = 40;
	
    
	function removeInstruction(idx){
	    var param = $("#instructionForm").serialize() + "&removeIdx="+idx;
	    removeInstructionLoading();
	    $.get(removeInstructionUrl, param, function(xml) {
	    	removeInstructionComplete();	
	    	document.getElementById("instructionArea").innerHTML = xml;
	    });
	}
	function removeGameAttachment(sessionMapID){
 		var url= removeGameAttachmentUrl;
	    var reqIDVar = new Date();
	    var param = "sessionMapID="+sessionMapID;
	    removeGameAttachmentLoading();
	    var myAjax = new Ajax.Updater(
		    	gameAttachmentTargetDiv,
		    	url,
		    	{
		    		method:'post',
		    		parameters:param,
		    		onComplete:removeGameAttachmentComplete,
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
		var obj = window.document.getElementById('reourceInputArea');
		if (!obj && window.parent) { 
			 obj = window.parent.document.getElementById('reourceInputArea');
		}  
		if (!obj) {
			obj = window.top.document.getElementById('reourceInputArea');
		}
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
	function removeGameAttachmentLoading(){
		showBusy(gameAttachmentTargetDiv);
	}
	function removeGameAttachmentComplete(){
		hideBusy(gameAttachmentTargetDiv);
	}
	function addInstructionLoading(){
		showBusy(instructionTargetDiv);
	}
	function addInstructionComplete(){
		hideBusy(instructionTargetDiv);
		adjustInstructionsDisplayAreaHeight(singleInstructionHeight);
	}
	
	function submitEadventureGame(){
		$("#eadventureGameForm").submit();
		
	}
	
	function cancelEadventureGame(){
		var win = null;
		if (window.hideMessage) { 
			win = window;
		} else if (window.parent && window.parent.hideMessage) {
			win = window.parent;
		} else {
			win = window.top;
		}
		win.hideMessage();
		win.showGame();
	}