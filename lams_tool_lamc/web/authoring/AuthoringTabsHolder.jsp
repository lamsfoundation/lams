<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.mc.McAppConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />

<lams:html>		
	<lams:head>
	<title><fmt:message key="activity.title" /></title>
	
	<%@ include file="/common/header.jsp"%>
	<script type="text/JavaScript">
		$(document).ready(function(){
			//handler for retries checkbox from advanced tab
			$("#retries").click(function() {
				if ($(this).is(':checked')) {
					$("#passmark-container").show(400);	
				} else {
					$("#passmark-container").hide(400);
				}
			});		
		});

		function submitMethod(actionMethod) {
			document.forms.mcAuthoringForm.action=actionMethod+".do"; 
			document.forms.mcAuthoringForm.submit();
		}

		
		function submitModifyAuthoringQuestion(questionIndexValue, actionMethod) {
			document.forms.mcAuthoringForm.questionIndex.value=questionIndexValue; 
			document.forms.mcAuthoringForm.action=actionMethod+".do";
			
			$('#mcAuthoringForm').ajaxSubmit({ 
				data: { 
					sessionMapId: '${sessionMapId}'
				},
				target:  $('#itemArea'),
	    		iframe: true,
	    		success:    function() { 
	    			document.forms.mcAuthoringForm.action="submitAllContent.do";
	    			refreshThickbox();
	    	    }
		    });
		}

        function init() {
	        selectTab(1); //select the default tab;
        }
        
        function doSelectTab(tabId) {
	    	selectTab(tabId);
	    	
	    	//advanced tab is selected
	    	if (tabId == 2) {
	    		var oldValue = $("#passmark").val();
	    		$('#passmark').empty();
	    		
	    		var totalMark = 0;
	    		$("td.question-max-mark").each(function() {
	    			totalMark += eval($(this).text());
	    		});
	    		
	    		for (var i = 1; i<=totalMark; i++) {
	    			var isSelected = (oldValue == i);
	    		    $('#passmark').append( new Option(i, i, isSelected, isSelected) );
	    		}
	    	}
        } 
        
        function doSubmit(method) {
        	document.forms.mcAuthoringForm.action=method+".do";
        	document.forms.mcAuthoringForm.submit();
        }
	</script>
	
</lams:head>
<body onLoad="init();" class="stripes">
	<form:form  action="submitAllContent.do" modelAttribute="mcAuthoringForm" id="mcAuthoringForm" method="POST" target="_self">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="httpSessionID" value="${sessionMapId}"/>
			
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar" formID="mcAuthoringForm">

			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= McAppConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.basic" />
				<lams:Tab id="2" key="label.advanced" />
			</lams:Tabs>   
			
			<lams:TabBodyArea>
				<lams:errors/>
              
                <!--  Set up tabs  -->
                <lams:TabBodys>
					<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
 					<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />
                </lams:TabBodys>

				<lams:AuthoringButton formID="mcAuthoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lamc11" 
					cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${sessionMap.toolContentID}" 
					contentFolderID="${sessionMap.contentFolderID}" accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode=='teacher'}"/>
			</lams:TabBodyArea>
					
			<div id="footer"></div>
		</lams:Page>
	</form:form>
	
</body>
</lams:html>
