<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-36 --%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script lang="javascript">


	function showResourceItem(url) {
		$.ajaxSetup({ cache: true });
		$("#resourceInputArea").load(url, function() {
			$(this).show();
			$("#saveCancelButtons").hide();
		});
	}
	function hideResourceItem(){
		$("#resourceInputArea").hide();
		$("#saveCancelButtons").show();
	}

	function previewItem(type,idx,sessionMapID){
		//1:url, 2:file, 3:website,4:learning object
		//This mode is special for unsaved author page. It is different with the author mode in preview 
		var myUrl = "<c:url value='/reviewItem.do?mode=author_session&itemIndex='/>"+idx+"&sessionMapID="+sessionMapID;
		launchPopup(myUrl,"Review");
	}
	
	function editItem(idx,sessionMapID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editItemInit.do?itemIndex="/>" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		showResourceItem(url);
	}
	//The panel of resource list panel
	var resourceListTargetDiv = "resourceListArea";
	function deleteItem(idx,sessionMapID){
		var url = "<c:url value="/authoring/removeItem.do"/>";
	    var reqIDVar = new Date();
		deleteItemLoading();
	    
		$("#" + resourceListTargetDiv).load(
			url,
			{
				itemIndex: idx,
				reqID: reqIDVar.getTime(), 
				sessionMapID: sessionMapID
			},
			function() {
				deleteItemComplete();
			}
		);
	}
	function deleteItemLoading(){
		showBusy(resourceListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(resourceListTargetDiv);
	}

	function switchItem(resourceItemOrderID1, resourceItemOrderID2, sessionMapID) {
		resourceItemOrderID1++;
		resourceItemOrderID2++;
		
		var url = "<c:url value="/authoring/switchResourceItemPosition.do"/>";
		deleteItemLoading();
		
		$("#" + resourceListTargetDiv).load(
			url,
			{
				sessionMapID: sessionMapID,
				resourceItemOrderID1: resourceItemOrderID1, 
				resourceItemOrderID2: resourceItemOrderID2
			},
			function() {
				deleteItemComplete();
			}
		);
	}

</script>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="forum.title"><fmt:message key="label.authoring.basic.title"/></label>
    <html:text property="resource.title" styleClass="form-control"></html:text>
</div>
<div class="form-group">
    <label for="resource.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="resource.instructions" value="${formBean.resource.instructions}" contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
</div>

<div id="resourceListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
 	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<div class="form-inline">
	<a href="javascript:showResourceItem('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=1"/>');" class="btn btn-default btn-sm">
		<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.url" /></a> 
	<a href="javascript:showResourceItem('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=2"/>');"  class="btn btn-default btn-sm loffset5">
		<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.file" /></a>
	<a href="javascript:showResourceItem('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=3"/>');" class="btn btn-default btn-sm loffset5">
		<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.website" /></a>
	<a href="javascript:showResourceItem('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=4"/>');" class="btn btn-default btn-sm loffset5">
		<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.learning.object" /> </a>
</div>

<div id="resourceInputArea" name="resourceInputArea" class="voffset10"></div>