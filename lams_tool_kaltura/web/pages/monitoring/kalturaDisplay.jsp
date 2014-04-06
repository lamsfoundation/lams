<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<link href="${tool}pages/learning/kaltura_style.css" rel="stylesheet" type="text/css">
		<lams:headItems />
		
	</lams:head>
	<body class="stripes">
		<div id="page">	
			
			<div id="content">
				<table>
					<tr>
						<td colspan="2">
							<h2>
								<c:out value="${userDTO.firstName} ${userDTO.lastName }" escapeXml="true"/>
							</h2>
						</td>
					</tr>
					<tr>
						<td class="field-name" width="20%">
							<fmt:message key="label.created" />
						</td>
						<td>
							<lams:Date value="${userDTO.entryDTO.createDate }"></lams:Date>
							
						</td>
					</tr>
					<tr>
						<td class="field-name" width="20%">
							<fmt:message key="label.lastModified" />
						</td>
						<td>
							<lams:Date value="${userDTO.entryDTO.lastModified }"></lams:Date>
						</td>
					</tr>
				
					<tr>
						<td class="field-name">
							<fmt:message key="label.notebookEntry" />
						</td>
						<td>
							<lams:out value="${userDTO.entryDTO.entry}" escapeXml="true"></c:out>
						</td>
					</tr>
				</table>
			</div>
			<div id="footer"></div>
		</div>
	</body>
</lams:html>
