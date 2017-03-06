	/*
	 This is Resource Item instruction area.
	 */
	var instructionTargetDiv = "instructionArea";
    var itemAttachmentTargetDiv = "itemAttachmentArea";
    var singleInstructionHeight = 74;
	
	function removeInstruction(idx){
		//prepare lams_textarea value to Ajax submit
		$('textarea').trigger('change');
		
	    var param = $("#instructionForm").serialize() + "&removeIdx="+idx;
	    removeInstructionLoading();
	    $.post(
	    	removeInstructionUrl, 
	    	param, 
	    	function(xml) {
	    		removeInstructionComplete();	
	    		document.getElementById("instructionArea").innerHTML = xml;
	    	}
	    );
	}
	
	function removeItemAttachment(idx){
	    removeItemAttachmentLoading();
		$("#" + itemAttachmentTargetDiv).load(
			removeItemAttachmentUrl,
			{
				reqID: new Date()
			},
			removeItemAttachmentComplete
		);	    
	}
	
	function addInstruction(){
		//prepare lams_textarea value to Ajax submit
		$('textarea').trigger('change');
		
	    var param = $("#instructionForm").serialize();
	    disableButtons();
		addInstructionLoading();
		
	    $.post(
	    	addInstructionUrl, 
	    	param, 
	    	function(xml) {
               $('#instructionArea').html(xml);
               enableButtons();
	    	}
	    );
	    
	    return false;
	}
	
	function upItem(itemIdx){
		//prepare lams_textarea value to Ajax submit
		$('textarea').trigger('change');
		
		if(itemIdx == 0)
			return;
		var currId = "instructionItemDesc" + itemIdx;
		var repId = "instructionItemDesc" + (--itemIdx);
		switchValue(currId,repId);
	}
	function downItem(itemIdx,maxSize){
		//prepare lams_textarea value to Ajax submit
		$('textarea').trigger('change');
		
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
	}
	
	function submitResourceItem(){
		//prepare lams_textarea value to Ajax submit and add instructions to form
		$('textarea').trigger('change');
		
		// validation will fail with spaces
		var urlField = document.getElementById("url");
		if ( urlField ) {
			urlField.value = urlField.value.trim();
		}
		
		if ( ! $("#resourceItemForm").valid() ) 
			return false;

		document.getElementById("instructionList").value = $("#instructionForm").serialize();
		var formData = new FormData(document.getElementById("resourceItemForm"));
		disableButtons();
		showBusy(instructionTargetDiv);
		// after submit, it direct to itemlist.jsp, 
		// then refresh "basic tab" resource list and close this window.
	    $.ajax({ // create an AJAX call...
			data: formData, 
	        processData: false, // tell jQuery not to process the data
	        contentType: false, // tell jQuery not to set contentType
           	type: $("#resourceItemForm").attr('method'),
			url: $("#resourceItemForm").attr('action'),
			success: function(data) {
               $('#resourceInputArea').html(data);
			},
			complete: function() {
				enableButtons();
			}
	    });
	    
	}