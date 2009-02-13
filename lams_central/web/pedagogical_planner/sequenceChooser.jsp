<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-html" prefix="html" %>
<lams:html>
<lams:head>
	  <title><fmt:message key="title.lams"/> :: <fmt:message key="planner.title" /></title>
	  
	  <c:if test="${node.edit}">
	  	<script language="JavaScript" type="text/javascript">
	  		var title = "<c:out value='${node.title}' escapeXml='true' />";
	  		var briefDescription = "<c:out value='${node.briefDescription}' escapeXml='true' />";
	  		var fullDescription = "<c:out value='${node.fullDescription}' escapeXml='true' />";
	  	</script>
	  </c:if>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/pedagogicalPlanner.js"></script>
	  
	  <lams:css style="main" />
	  <link href="<lams:LAMSURL />css/pedagogicalPlanner.css" rel="stylesheet" type="text/css">
</lams:head>
<body class="stripes">
<div id="page">
<div id="content">
	<h1 style="text-align: center" class="small-space-top"><fmt:message key="planner.title" /></h1>
	<table>
		<tr>
			<td colspan="2">
				<%-- This part creates path like "> Root > Some node > Some subnode" 
					 with links to upper level nodes --%>
				<c:url value="/pedagogicalPlanner.do" var="titleUrl">
					<c:param name="method" value="openSequenceNode" />
					<c:param name="edit" value="${node.edit}" />
				</c:url>
				<%-- Always add root node --%>
				<a href="${titleUrl}"><fmt:message key="label.planner.root.node" /></a> &gt;
				<%-- Iterate through subnodes, if any --%>
				<c:forEach var="title" items="${node.titlePath}">
					<c:url value="/pedagogicalPlanner.do" var="titleUrl">
						<c:param name="method" value="openSequenceNode" />
						<c:param name="uid" value="${title[0]}" />
						<c:param name="edit" value="${node.edit}" />
					</c:url>
					<a href="${titleUrl}"><c:out value='${title[1]}' escapeXml='true' /></a> &gt;
				</c:forEach>
				<c:out value='${node.title}' escapeXml='true' />
			
				<%-- Title and full description of the node --%>
				<c:set var="isRootNode" value="${empty node.uid and empty node.parentUid}" />
				<c:choose>
					<c:when test="${isRootNode}">
						<div style="margin: 20px 0px 0px 10px"><fmt:message key="label.planner.root.choose" /></div>
					</c:when>
					<c:otherwise>
						<h3 style="margin-top: 20px"><c:out value='${node.title}' escapeXml='true' /></h3>
						<div style="margin-left: 10px"><c:out value='${node.fullDescription}' escapeXml='true' /></div>
					</c:otherwise>
				</c:choose>

				
			</td>
		</tr>
		<c:choose>
			<c:when test="${empty node.subnodes}">
				<tr>
					<td colspan="2" style="text-align: center">
						<h3><fmt:message key="label.planner.empty.subnode" /></h3>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
			<%-- Iterate through subnodes --%>
				<c:forEach var="subnode" items="${node.subnodes}">
					<tr>
						<td style="width: 15px">
							<%-- Cell with icons (info or actions like remove node)  --%>
							<c:choose>
							<c:when test="${node.edit}">
								<c:url value="/pedagogicalPlanner.do" var="removeNodeUrl">
									<c:param name="method" value="removeSequenceNode" />
									<c:param name="edit" value="true" />
									<c:param name="uid" value="${subnode.uid}" />
								</c:url>
								<img style="height: 15px; width: 15px; cursor: pointer;" src="<lams:LAMSURL/>images/cross.gif"
									title="<fmt:message key="msg.planner.remove.node" />"
									onclick="javascript:removeNode('<fmt:message key="msg.planner.remove.warning" />','${removeNodeUrl}')" />
							</c:when>
							<c:otherwise>
								<c:if test="${not empty node.fileName}">
									<img src="<lams:LAMSURL/>images/circle_filled.gif" alt="<fmt:message key="msg.planner.open.template" />" />
								</c:if>
							</c:otherwise>
							</c:choose>
						</td>
						<td>
							<%-- Link to node and its brief description  --%>
							<c:url value="/pedagogicalPlanner.do" var="subnodeUrl">
								<c:param name="method" value="openSequenceNode" />		
								<c:param name="edit" value="${node.edit}" />
								<c:param name="uid" value="${subnode.uid}" />
							</c:url>
							<a href="${subnodeUrl}"><c:out escapeXml='true' value='${subnode.title}' />
								<c:if test="${node.edit and subnode.locked}">
									(locked)
								</c:if>
							</a> 
							- ${subnode.briefDescription}
						</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	<c:choose>
		<c:when test="${node.edit}">
			<%-- Form for editing the node --%>
			<hr style="margin: auto" />
			
			<%-- Do we edit the current node or create a subnode? --%>
			<c:choose>
				<c:when test="${not node.createSubnode and (isRootNode or node.locked)}">
					<h3 style="text-align: center"><fmt:message key="msg.planner.node.locked"></fmt:message></h3>
				</c:when>
				<c:otherwise>
					<h2 style="text-align: center">
					<c:choose>
						<c:when test="${node.createSubnode}">
							<fmt:message key="label.planner.create.subnode" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.planner.edit.node" />
						</c:otherwise>
					</c:choose>
					</h2>
					<html:form styleId="nodeForm" style="margin-left: 20px" action="/pedagogicalPlanner" method="post" enctype="multipart/form-data">
						<input type="hidden" id="method" name="method" value="saveSequenceNode" />
						<input type="hidden" id="edit" name="edit" value="true" />
						
						<c:choose>
							<c:when test="${node.createSubnode}">
								<input type="hidden" id="parentUid" name="parentUid" value="${node.uid}" />
							</c:when>
							<c:otherwise>
								<input type="hidden" id="uid" name="uid" value="${node.uid}" />
							</c:otherwise>
						</c:choose>
						
						<div class="field-name space-top"><fmt:message key="label.title" /></div>
						<input type="text" id="title" name="title" size="80"
							<c:if test="${not node.createSubnode}">
								value="<c:out escapeXml='true' value='${node.title}' />"
							</c:if>
						 />
						 
						<c:set var="entry">
							<c:if test="${not node.createSubnode}">
								<c:out escapeXml='true' value='${node.briefDescription}' />
							</c:if>
						</c:set>
						<div class="field-name space-top"><fmt:message key="label.planner.description.brief" /></div>
						<textarea id="briefDescription" name="briefDescription" rows="3" cols="60">${fn:trim(entry)}</textarea>
						
						<c:set var="entry">
							<c:if test="${not node.createSubnode}">
								<c:out escapeXml='true' value='${node.fullDescription}' />
							</c:if>
						</c:set>
						<div class="field-name space-top"><fmt:message key="label.planner.description.full" /></div>
						<textarea id="fullDescription" name="fullDescription" rows="3" cols="60">${fn:trim(entry)}</textarea>
					</html:form>
				</c:otherwise>
			</c:choose>
			<div id="buttonArea" style="margin-top: 10px">
				<c:url value="/pedagogicalPlanner.do" var="closeNodeEditorUrl">
					<c:param name="method" value="openSequenceNode" />
					<c:param name="edit" value="false" />
					<c:param name="uid" value="${node.uid}" />
				</c:url>
				<a class="button pedagogicalPlannerButtons" href="javascript:leaveNodeEditor('<fmt:message key="msg.planner.not.saved" />','${closeNodeEditorUrl}');"><fmt:message key="button.close" /></a>
				<c:if test="${not node.createSubnode}">
					<c:url value="/pedagogicalPlanner.do" var="createSubnodeUrl">
						<c:param name="method" value="openSequenceNode" />
						<c:param name="edit" value="true" />
						<c:param name="createSubnode" value="true" />
						<c:param name="uid" value="${node.uid}" />
					</c:url>
					<a class="button pedagogicalPlannerButtons" href="javascript:leaveNodeEditor('<fmt:message key="msg.planner.not.saved" />','${createSubnodeUrl}');"><fmt:message key="label.planner.create.subnode" /></a>
				</c:if>
				<c:if test="${node.createSubnode or not isRootNode }">
					<a class="button pedagogicalPlannerButtons" href="javascript:$('#nodeForm').submit()"><fmt:message key="button.planner.save.node" /></a>
				</c:if>
			</div>
		</c:when>
		<c:otherwise>
			<c:url value="/pedagogicalPlanner.do" var="openEditorUrl">
				<c:param name="method" value="openSequenceNode" />
				<c:param name="edit" value="true" />
				<c:param name="uid" value="${node.uid}" />
			</c:url>
			<div id="buttonArea">
				<a class="button pedagogicalPlannerButtons" href="javascript:closePlanner();"><fmt:message key="button.close" /></a>
				<a class="button pedagogicalPlannerButtons" href="${openEditorUrl}"><fmt:message key="button.planner.editor.open" /></a>
			</div>
		</c:otherwise>
	</c:choose>
</div>
<div id="footer"></div>
</div>
</body>
</lams:html>