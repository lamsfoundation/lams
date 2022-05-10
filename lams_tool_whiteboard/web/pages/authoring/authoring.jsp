<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.whiteboard.WhiteboardConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<style>
		#relativeTimeLimit {
			width: 70px;
		}
		
		#whiteboard-frame {
			width: 100%;
			height: 700px;
			margin-bottom: 20px;
			border: 1px solid #c1c1c1;
		}
		
		.full-screen-launch-button {
			margin-bottom: 5px;
		}
		
		.full-screen-exit-button {
			display: none;
			margin-bottom: 5px;
		}
		
		.full-screen-content-div:fullscreen {
			padding: 20px 0 70px 0;
		}
		
		.full-screen-content-div:fullscreen .full-screen-flex-div {
			margin: 0 2%;
		}
		
		.full-screen-content-div:fullscreen .full-screen-flex-div,
		.full-screen-content-div:fullscreen .full-screen-main-div,
		.full-screen-content-div:fullscreen #whiteboard-frame {
			height: 100%;
			width: 100%;
		}
	</style>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/fullscreen.js"></script>
	<script type="text/javascript">
        function init(){
            var tag = document.getElementById("currentTab");
	    	if (tag.value != "") {
	    		selectTab(tag.value);
	    	} else {
                selectTab(1); //select the default tab;
	    	}
        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        }
        
        $(document).ready(function(){
			$('#timeLimit').change(function() {
				let widget = $(this),
					timeLimit = widget.val();
				if (!timeLimit || timeLimit < 0) {
					widget.val(0);
				}
			});
			
			setupFullScreenEvents();
        });
    </script>
</lams:head>

<body class="stripes" onLoad="init()">
<form:form action="update.do" modelAttribute="authoringForm" method="post" id="authoringForm">
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<form:hidden path="whiteboard.contentId" />
	<form:hidden path="mode" value="${mode}"/>
	<form:hidden path="sessionMapID" />
	<form:hidden path="contentFolderID" />
	<form:hidden path="currentTab" styleId="currentTab" />

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">
	
		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= WhiteboardConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
			<lams:Tab id="2" key="label.authoring.heading.advance" />
		</lams:Tabs>    

		<lams:TabBodyArea>
			<lams:errors/>
		   
		    <!--  Set up tabs  -->
		    <lams:TabBodys>
				<!-- tab content 1 (Basic) -->
				<lams:TabBody id="1" page="basic.jsp" />
				<!-- end of content (Basic) -->
	
				<!-- tab content 2 (Advanced) -->
				<lams:TabBody id="2" page="advance.jsp" />
				<!-- end of content (Advanced) -->
			</lams:TabBodys>
				
			<!-- Button Row -->
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
				toolSignature="<%=WhiteboardConstants.TOOL_SIGNATURE%>" toolContentID="${authoringForm.whiteboard.contentId}" 
				 customiseSessionID="${authoringForm.sessionMapID}" accessMode="${mode}" defineLater="${mode=='teacher'}"
				 contentFolderID="${authoringForm.contentFolderID}" />
		</lams:TabBodyArea>
	
		<div id="footer"></div>

	</lams:Page>
</form:form>
</body>
</lams:html>
