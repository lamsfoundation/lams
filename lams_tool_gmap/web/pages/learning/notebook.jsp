<!DOCTYPE html>
            
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>

	<lams:head>  
	
		<title>
			<fmt:message>pageTitle.monitoring.notebook</fmt:message>
		</title>
		
		<lams:css/>
		
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		
	</lams:head>
	
	<body class="stripes">
	
			<lams:Page type="learner" title="${gmapDTO.title}">
			
			<html:form action="/learning" method="post">
				<html:hidden property="toolSessionID" styleId="toolSessionID"/>
				<html:hidden property="markersXML" />
				<html:hidden property="mode" value="${mode}" />
				
				<p class="small-space-top">
					<lams:out value="${gmapDTO.reflectInstructions}" escapeHtml="true"/>
				</p>
		
				<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control"></html:textarea>
		
				<html:hidden property="dispatch" value="submitReflection" />
				<html:link href="#nogo" styleClass="btn btn-primary voffset10 pull-right na" styleId="finishButton" 
			          onclick="javascript:document.learningForm.submit();return false">
						<c:choose>
		 					<c:when test="${activityPosition.last}">
		 						<fmt:message key="button.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="button.finish" />
		 					</c:otherwise>
			 			</c:choose>
				</html:link>
			</html:form>
				
			</lams:Page>
			<div class="footer"></div>
			
	<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
	</script>
		
	</body>
</lams:html>
