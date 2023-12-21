<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>

<!--  Basic Tab Content -->

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {

	    $.ajaxSetup({ cache: true });
		$("#messageArea").load(url, function() {
			var area=document.getElementById("messageArea");
			if(area != null){
				area.style.width="100%";
				area.style.height="100%";
				area.style.display="block";
			}
			
			var elem = document.getElementById("saveCancelButtons");
			if (elem != null) {
				elem.style.display="none";
			}
		
			location.hash = "messageArea";
		});
	}
	function hideMessage(){
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}

		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="block";
		}
	}

	function editTopic(topicIndex, sessionMapID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editTopic.do?topicIndex="/>" + topicIndex +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		showMessage(url);
	}
	//The panel of taskList list panel
	var topicListTargetDiv = "messageListArea";
	function deleteTopic(topicIndex, sessionMapID){

		var	deletionConfirmed = confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.basic.do.you.want.to.delete'/></spring:escapeBody>");
		
		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/deleteTopic.do"/>?<csrf:token/>";
		    var reqIDVar = new Date();
			var param = "topicIndex=" + topicIndex +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
			deleteItemLoading();
			$.ajax({
	            type: 'POST', 
	            url: url,
	            data: param,
	            success: function(data) {
	            	$("#messageListArea").html(data);
	            	deleteItemComplete();
	            }
	        });
 		}
		
	}
	function deleteItemLoading(){
		showBusy(topicListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(topicListTargetDiv);
	}
	
	function upTopic(topicIndex, sessionMapID){
		var url = "<c:url value="/authoring/upTopic.do"/>";
	    var reqIDVar = new Date();
		var param = "topicIndex=" + topicIndex +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
		$.ajax({
            type: 'get',
            url: url,
            data: param,
            success: function(data) {
            	$("#messageListArea").html(data);
            	deleteItemComplete();
            }
        });
	}
	function downTopic(topicIndex, sessionMapID){
		var url = "<c:url value="/authoring/downTopic.do"/>";
	    var reqIDVar = new Date();
		var param = "topicIndex=" + topicIndex +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
		$.ajax({
            type: 'get',
            url: url,
            data: param,
            success: function(data) {
            	$("#messageListArea").html(data);
            	deleteItemComplete();
            }
        });
	}	

	//Packs additional elements and submits the question form
	function submitMessage(){
	
		if ( typeof CKEDITOR !== 'undefined' ) {
			for ( instance in CKEDITOR.instances )
				CKEDITOR.instances[instance].updateElement();
		}

		var formData = new FormData(document.getElementById("topicFormId"));
	    $.ajax({ // create an AJAX call...
			data: formData, 
	        processData: false, // tell jQuery not to process the data
	        contentType: false, // tell jQuery not to set contentType
           	type: $("#topicFormId").attr('method'),
			url: $("#topicFormId").attr('action'),
			success: function(data) {
               	$('#messageArea').html(data);
			}
	    });
	}   
</script>

<div class="form-group">
    <label for="forum.title"><fmt:message key="label.authoring.basic.title"/></label>
    <input type="text" name="forum.title" value="${forumForm.forum.title}" class="form-control"/>
</div>
<div class="form-group">
    <label for="forum.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="forum.instructions" value="${forumForm.forum.instructions}" contentFolderID="${forumForm.contentFolderID}"></lams:CKEditor>
</div>

<!-- Topics List Row -->
<div id="messageListArea">
	<c:set var="sessionMapID" value="${forumForm.sessionMapID}" />
	<%@ include file="/jsps/authoring/message/topiclist.jsp"%>
</div>

<lams:WaitingSpinner id="messageListArea_Busy"/>

<div class="form-inline">
	<a href="javascript:showMessage('<lams:WebAppURL/>authoring/newTopic.do?sessionMapID=${forumForm.sessionMapID}');" id="addTopic" class="btn btn-default btn-sm">
    <i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.create.new.topic" /> 
	</a>
</div>

<div id="messageArea" name="messageArea" class="voffset10"></div>
