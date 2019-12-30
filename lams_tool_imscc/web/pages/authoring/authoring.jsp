<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>
	
	<%@ include file="/common/tabbedheader.jsp"%>
	
	<link rel="stylesheet" type="text/css" href="${lams}/css/thickbox.css" />
	
 	<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
	<script>
        function init() {
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
	    	
	    	//for advanceTab
	    	if(tabId == 2)
	    		changeViewNum(-1);	    	
        }
        
        function changeViewNum(initVal) {
			var tb = document.getElementById("itemTable");
			var num = tb.getElementsByTagName("tr");
			var sel = document.getElementById("viewNumList");
			var newField = sel.options;
			var len = sel.length;
			
			//when first enter, it should get value from CommonCartridge
			var selIdx=initVal < 0?-1:initVal;
			//there is bug in Opera8.5: if add alert before this loop, it will work,weird.
			for (var idx=0;idx<len;idx++) {
				if(newField[0].selected && selIdx == -1 ) {
					selIdx = newField[0].value;
				}
				sel.removeChild(newField[0]);
			}
		
			for (var i=0;i<=num.length;i++) {
				var opt = document.createElement("option");
				var optT =document.createTextNode(i);
				opt.value=i;
				//get back user choosen value
				if (selIdx > 0 && selIdx==i) {
					opt.selected = true;
				} else {
					opt.selected = false;
				}
				opt.appendChild(optT);
				sel.appendChild(opt);
			}
	}
    </script>
</lams:head>
<body class="stripes" onLoad="init()">
	<c:set var="csrfToken"><csrf:token/></c:set>
	<form:form action="update.do?${csrfToken}" modelAttribute="authoringForm" method="post" id="authoringForm" enctype="multipart/form-data">
	
		<c:set var="title"><fmt:message key="label.author.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
		
			<form:hidden path="commonCartridge.contentId" />
			<form:hidden path="mode" value="${mode}"/>
			<form:hidden path="sessionMapID" />
			<form:hidden path="contentFolderID" />
			<form:hidden path="currentTab" styleId="currentTab" />
	
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= CommonCartridgeConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
			</lams:Tabs>	
		
		 	<lams:TabBodyArea>
		 		<lams:errors/>
		 		
				<!--  Set up tabs  -->
		 		<lams:TabBodys>
   					<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
 					<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
  				</lams:TabBodys>
		
				<!-- Button Row -->
				<div id="saveCancelButtons">
					<lams:AuthoringButton formID="authoringForm"
						clearSessionActionUrl="/clearsession.do" toolSignature="<%=CommonCartridgeConstants.TOOL_SIGNATURE%>"
						toolContentID="${authoringForm.commonCartridge.contentId}"
						accessMode="${mode}" defineLater="${mode=='teacher'}"
						contentFolderID="${authoringForm.contentFolderID}" />
				</div>
			</lams:TabBodyArea>
	
			<div id="footer"></div>
	
		<!-- end page div -->
		</lams:Page>
	
	</form:form>

	<script type="text/javascript">
		changeViewNum(${authoringForm.commonCartridge.miniViewCommonCartridgeNumber});
	</script>
</body>
</lams:html>
