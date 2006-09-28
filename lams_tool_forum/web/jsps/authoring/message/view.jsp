<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">


<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<style type="text/css">
			<!--
			table.forum { 
			width:98%; 
			margin-left:7px; 
			padding-top:5px; 
			margin-bottom:10px;
			background:url('../images/css/greyfade_bg.jpg') repeat-x 3px 49px; 
			text-align:left; 
			border-bottom:1px solid #efefef;
			}
			-->
		</style>
	</head>
	<body>
		<div class="content">
		<table class="forum" >
			<!-- Basic Info Form-->
			<tr>
				<th width="410" class="first">
					<span style="font-size:25px"><c:out value="${topic.message.subject}" /></span>
				</th>
			</tr>
			<tr>
				<th class="first posted-by">
					<fmt:message key="lable.topic.subject.by" />
					<c:out value="${topic.author}" />
					-
					<lams:Date value="${topic.message.created}"/>
				</th>
			</tr>
			<tr>
				<td class="first">
					<c:out value="${topic.message.body}" escapeXml="false" />
				</td>
			</tr>
			<tr>
				<td >
					<c:forEach var="file" items="${topic.message.attachments}">
						<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td  align="center">
					<html:link href="javascript:window.parent.hideMessage()" styleClass="button space-left">
						<b><fmt:message key="button.cancel" /></b>
					</html:link>
					<c:set var="deletetopic">
						<html:rewrite page="/authoring/deleteTopic.do?sessionMapID=${sessionMapID}&topicIndex=${topicIndex}" />
					</c:set>
					<html:link href="${deletetopic}" styleClass="button space-left">
						<b><fmt:message key="label.delete" /></b>
					</html:link>
					<c:set var="edittopic">
						<html:rewrite page="/authoring/editTopic.do?sessionMapID=${sessionMapID}&topicIndex=${topicIndex}&create=${topic.message.created.time}" />
					</c:set>
					<html:link href="${edittopic}" styleClass="button space-left">
						<b><fmt:message key="label.edit" /></b>
					</html:link>
					<BR><BR>
				</td>
			</tr>
		</table>
		</div>
	</body>
</html>

