<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<html:base />
	<lams:css/>
	<title><fmt:message key="activity.title" /></title>
	
</lams:head>

<body class="stripes">
	
	<html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
	
		<div id="content">
		
		<h1>
			<fmt:message key="label.view.reflection"/>
		</h1>

			<table>
				<tr>
					<td>
						<h2>
							<c:out value="${voteGeneralLearnerFlowDTO.userName}" escapeXml="true"/>
						</h2>
					</td>
				</tr>
				<tr>
					<td>
						<p><lams:out value="${voteGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true"/></p>
					</td>
				</tr>
			</table>

			<table cellpadding="0">
				<tr>
					<td>
						<a href="javascript:window.close();" class="button">
							<fmt:message key="label.close"/></a>
					</td>
				</tr>
			</table>

		
		</div>
	</html:form>	
	
	<div id="footer"></div>


</body>
</lams:html>







