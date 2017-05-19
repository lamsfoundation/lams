<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.mc.McAppConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />

<lams:html>		
	<lams:head>
	<title><fmt:message key="activity.title" /></title>
	
	<%@ include file="/common/header.jsp"%>
	<script type="text/JavaScript">

		function submitMethod(actionMethod) {
			document.McAuthoringForm.dispatch.value=actionMethod; 
			document.McAuthoringForm.submit();
		}
		
		function submitModifyAuthoringQuestion(questionIndexValue, actionMethod) {
			document.McAuthoringForm.questionIndex.value=questionIndexValue; 
			document.McAuthoringForm.dispatch.value=actionMethod;
			
			$('#authoringForm').ajaxSubmit({ 
				data: { 
					sessionMapId: '${sessionMapId}'
				},
				target:  $('#resourceListArea'),
	    		iframe: true,
	    		success:    function() { 
	    			document.McAuthoringForm.dispatch.value="submitAllContent";
	    			refreshThickbox();
	    	    }
		    });
		}

		function updatePass(clickedObj) {
			if (clickedObj.checked) {
				document.McAuthoringForm.passmark.disabled= false;
				
			}else {
				document.McAuthoringForm.passmark.value= ' ';
				document.McAuthoringForm.passmark.disabled= true;
			}
		}

    	var imgRoot="${lams}images/";
	    var themeName="aqua";

        function init() {
			updatePass(document.McAuthoringForm.retries);
				
	        var tag = document.getElementById("currentTab");
		    if(tag.value != "")
		    	selectTab(tag.value);
	        else
	        	selectTab(1); //select the default tab;
        }
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.McAuthoringForm.dispatch.value=method;
        	document.McAuthoringForm.submit();
        }
	</script>
</lams:head>
<body onLoad="init();" class="stripes">

	<html:form  action="/authoring?validate=false" styleId="authoringForm" enctype="multipart/form-data" method="POST" target="_self">
		<html:hidden property="dispatch" value="submitAllContent"/>
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="httpSessionID" value="${sessionMapId}"/>
			
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">

		 <lams:Tabs control="true" title="${title}" helpToolSignature="<%= McAppConstants.MY_SIGNATURE %>" helpModule="authoring">
                    <lams:Tab id="1" key="label.basic" />
                    <lams:Tab id="2" key="label.advanced" />
            </lams:Tabs>   
			
			<lams:TabBodyArea>
				<%@ include file="/common/messages.jsp"%>
              
                <!--  Set up tabs  -->
                <lams:TabBodys>
					<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
 					<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />
                </lams:TabBodys>

				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lamc11" 
					cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${sessionMap.toolContentID}" 
					contentFolderID="${sessionMap.contentFolderID}" accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode=='teacher'}"/>
			</lams:TabBodyArea>
					
		<div id="footer"></div>

	</lams:Page>
	
	</html:form>
	
</body>
</lams:html>