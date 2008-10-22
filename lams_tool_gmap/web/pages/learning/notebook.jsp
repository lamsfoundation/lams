<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
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
		
		<link href="${tool}pages/learning/gmap_style.css" rel="stylesheet" type="text/css">
		<lams:css/>
		
		<link href="${lams}css/fckeditor_style.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
		
	</lams:head>
	
	<script type="text/javascript">
			function disableFinishButton() {
				document.getElementById("finishButton").disabled = true;
			}
		</script>
	
	<body class="stripes">
	
			<div id="content">
			
			<h1>
				${gmapDTO.title}
			</h1>
		
			<html:form action="/learning" method="post" onsubmit="disableFinishButton();">
				<html:hidden property="toolSessionID" styleId="toolSessionID"/>
				<html:hidden property="markersXML" />
				<html:hidden property="mode" value="${mode}" />
				
				<p class="small-space-top">
					<lams:out value="${gmapDTO.reflectInstructions}" />
				</p>
		
				<html:textarea cols="60" rows="8" property="entryText"
					styleClass="text-area"></html:textarea>
		
				<div class="space-bottom-top align-right">
					<html:hidden property="dispatch" value="submitReflection" />
					<html:submit styleClass="button" styleId="finishButton">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
				</div>
			</html:form>
				
			</div>
			<div class="footer"></div>
	</body>
</lams:html>
