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
		
		<lams:css/>
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>

		
	</lams:head>
	
	
	<body class="stripes">
	
			<div id="content">
			
				<h1>
					<fmt:message>pageTitle.monitoring.notebook</fmt:message>
				</h1>
				
				<table>
					<tr>
						<td>
							<h2>
								<c:out value="${gmapUserDTO.firstName} ${gmapUserDTO.lastName}" escapeXml="true"/>
							</h2>
						</td>
					</tr>
					<tr>
						<td>
							<p>
								<lams:out value="${gmapUserDTO.notebookEntry}" escapeHtml="true"/>
							</p>
						</td>
					</tr>
				</table>
				
			</div>
			<div class="footer"></div>
	</body>
</lams:html>



