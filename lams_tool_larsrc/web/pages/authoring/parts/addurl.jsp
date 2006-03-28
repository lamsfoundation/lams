<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
	<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
	<!-- this is the custom CSS for hte tool -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page='/includes/css/rsrc.css'/>" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/prototype.js'/>"></script>
	
<script type="text/javascript">
    var insTargetDiv = "instructionArea";
    var remvTargetDiv = "instructionArea";
	function removeInstruction(idx){
		//var id = "instructionItem" + idx;
		//Element.remove(id);
 		var url="<c:url value='/authoring/removeInstruction.do'/>";
	    var reqIDVar = new Date();
	    var param = Form.serialize("instructionForm")+"&removeIdx="+idx+"&reqID="+reqIDVar.getTime();
	    removeInstructionLoading();
	    var myAjax = new Ajax.Updater(
		    	remvTargetDiv,
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
		var url="<c:url value='/authoring/newInstruction.do'/>";
	    var reqIDVar = new Date();
	    var param = Form.serialize("instructionForm")+"&reqID="+reqIDVar.getTime();
		addInstructionLoading();
	    var myAjax = new Ajax.Updater(
		    	insTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:addInstructionComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function removeInstructionLoading(){
		showBusy(remvTargetDiv);
	}
	function removeInstructionComplete(){
		hideBusy(remvTargetDiv);
	
	}
	function addInstructionLoading(){
		showBusy(insTargetDiv);
	}
	function addInstructionComplete(){
		hideBusy(insTargetDiv);
	
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
</script>

</head>
<body class="tabpart">
	<table class="forms">
		<!-- Basic Info Form-->
		<tr>
			<td>
			<table class="innerforms">
				<tr>
					<td colspan="2"><h2><fmt:message key="label.authoring.basic.add.url"/></h2></td>
				</tr>
				<tr>
					<td width="130px"><fmt:message key="label.authoring.basic.resource.title.input"/></td>
					<td><input type="text" name="title" size="55"></td>
				</tr>
				<tr>
					<td width="130px"><fmt:message key="label.authoring.basic.resource.url.input"/></td>
					<td><input type="text" name="url" size="55"></td>
				</tr>	
			</table>
			</td>
		</tr>
		<tr>
			<td><hr></td>
		</tr>
		<tr>
			
			<!-- Instructions -->
			<td>
				<table class="innerforms">
					<tr>
						<td>
							<%@ include file="instructions.jsp" %>
						</td>
						<td width="100%" align="right" valign="bottom">
							<input type="button" name="add" value="<fmt:message key="label.authoring.basic.add.url"/>" class="buttonStyle">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</body>
</html>