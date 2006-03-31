	/*
	 This is Resource Item instrcution area.
	 */
    var instructionTargetDiv = "instructionArea";
// Please set these 2 variables in JSP file for using tag reason:
//    var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
//    var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
	function removeInstruction(idx){
		//var id = "instructionItem" + idx;
		//Element.remove(id);
 		var url= removeInstructionUrl;
	    var reqIDVar = new Date();
	    var param = Form.serialize("instructionForm")+"&removeIdx="+idx+"&reqID="+reqIDVar.getTime();
	    removeInstructionLoading();
	    var myAjax = new Ajax.Updater(
		    	instructionTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:removeInstructionComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function addInstruction(){
		var url= addInstructionUrl;
	    var reqIDVar = new Date();
	    var param = Form.serialize("instructionForm")+"&reqID="+reqIDVar.getTime();
		addInstructionLoading();
	    var myAjax = new Ajax.Updater(
		    	instructionTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:addInstructionComplete,
		    		evalScripts:true
		    	}
	    );
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
		var temp = $(repId).value;
		$(repId).value =  $(currId).value;
		$(currId).value= temp;
	}
	function removeInstructionLoading(){
		showBusy(instructionTargetDiv);
	}
	function removeInstructionComplete(){
		hideBusy(instructionTargetDiv);
	
	}
	function addInstructionLoading(){
		showBusy(instructionTargetDiv);
	}
	function addInstructionComplete(){
		hideBusy(instructionTargetDiv);
	
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
	
	function submitResourceItem(){
		$("instructionList").value = Form.serialize("instructionForm");
		$(resourceItemForm).submit();
	}
