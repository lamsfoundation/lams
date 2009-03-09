<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<lams:html>
<lams:head>
	  <title><fmt:message key="title.lams"/> :: <fmt:message key="planner.title" /></title>

	  <script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/pedagogicalPlanner.js"></script>
	  
	  <lams:css style="main" />
	  <link href="<lams:LAMSURL />css/pedagogicalPlanner.css" rel="stylesheet" type="text/css">
</lams:head>
<body class="stripes">
<div id="page">
<div id="content">
	${lamsFound}
	<%-- We might need to alter that for RTL layout --%>
	<h1 style="text-align: center" class="small-space-top"><fmt:message key="planner.title" /></h1>
	
	<%-- Errors are displayed at the top of the page --%>
	<logic:messagesPresent> 
		<p class="warning">
			<html:messages id="error">
		       <c:out value="${error}" escapeXml="false"/><br/>
		    </html:messages>
	    </p>
	</logic:messagesPresent>
	
	<%-- List of the existing nodes --%>
	<table>
		<tr>
			<td>
				<%-- This part creates path like "> Root > Some node > Some subnode" 
					 with links to upper level nodes --%>
				<c:url value="/pedagogicalPlanner.do" var="rootUrl">
					<c:param name="method" value="openSequenceNode" />
					<c:param name="edit" value="${node.edit}" />
				</c:url>
				<%-- Always add root node at the beginning --%>
				<a href="${rootUrl}"><fmt:message key="label.planner.root.node" /></a> &gt;
				
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
			</td>
			<td class="align-right" style="width: 250px">
				<c:url value="/pedagogicalPlanner.do" var="filterNodeUrl">
					<c:param name="method" value="openSequenceNode" />
					<c:param name="edit" value="${node.edit}" />
					<c:param name="uid" value="${node.uid}" />
				</c:url>
				<span class="space-right float-left"><fmt:message key="label.planner.filter" /></span>
				<input class="float-left" type="text" size="20" id="filterText" value="${filterText}" onkeypress="javascript:filterNodesOnEnter('${filterNodeUrl}')"/>
				<img class="sequenceActionImage" src="<lams:LAMSURL/>images/icons/magnifier.png"
					 onclick="javascript:filterNodes('${filterNodeUrl}',true)" />
				<c:if test="${not empty filterText}">
					<img class="sequenceActionImage" src="<lams:LAMSURL/>images/icons/cross.png"
						 title="<fmt:message key="label.planner.filter.clear" />"
						 onclick="javascript:filterNodes('${filterNodeUrl}',false)" />
				</c:if>
			</td>
		</tr>
	</table>
	
	<c:set var="isRootNode" value="${empty node.uid and empty node.parentUid}" />
	<table>
		<c:choose>
		    <c:when test="${empty filterText}">
		    	 <tr>
					<td colspan="2">
						<%-- Title and full description of the node, only if we are not in filtering mode--%>
						<c:choose>
							<c:when test="${isRootNode}">
								<div class="space-left space-top"><fmt:message key="label.planner.root.choose" /></div>
							</c:when>
							<c:otherwise>
								<h3><c:out value='${node.title}' escapeXml='true' /></h3>
								<div class="space-left">${node.fullDescription}</div>
							</c:otherwise>
						</c:choose>
					  </td>
				</tr>
		    </c:when>
		    <c:otherwise>
			    <tr>
					<td colspan="2" class="align-center">
						<%-- Filter mode: what were we looking for and in what node --%>
						<h3><fmt:message key="label.planner.filter.results.beginning" 
						    /><c:out value='${filterText}' escapeXml='true' 
						    /><fmt:message key="label.planner.filter.results.ending" 
						    />
						    <c:choose>
							    <c:when test="${isRootNode}">
							    	<a href="${rootUrl}"><fmt:message key="label.planner.root.node" /></a>
							    </c:when>
							    <c:otherwise>
									<a href="${filterNodeUrl}"><c:out value='${node.title}' escapeXml='true' /></a>
							    </c:otherwise>
						    </c:choose>
						</h3>
					</td>
				</tr>
		    </c:otherwise>
	    </c:choose>

		<%-- List of subnodes --%>
		<c:choose>
			<%-- If the list of subnodes is empty, we display only a message --%>
			<c:when test="${empty node.subnodes}">
				<tr>
					<td colspan="2" class="align-center">
						<h3><fmt:message key="label.planner.empty.subnode" /></h3>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
			<%-- Iterate through subnodes --%>
				<c:forEach var="subnode" items="${node.subnodes}" varStatus="subnodeStatus">
					<tr>
						<%-- Width is in percent, otherwise IE does not display it as desired --%>
						<td class="align-right" style="width: 10%;" >
							<%-- Cell with icons (info or actions like remove node)  --%>
							<c:choose>
								<c:when test="${node.edit}">
									<c:if test="${empty filterText}">
										<%-- If we are in the edit mode (and not in filter mode), we display remove and move up/down images --%>
										<c:url value="/pedagogicalPlanner.do" var="removeNodeUrl">
											<c:param name="method" value="removeSequenceNode" />
											<c:param name="edit" value="true" />
											<c:param name="uid" value="${subnode.uid}" />
										</c:url>
										<c:url value="/pedagogicalPlanner.do" var="downNodeUrl">
											<c:param name="method" value="moveNodeDown" />
											<c:param name="edit" value="true" />
											<c:param name="uid" value="${subnode.uid}" />
										</c:url>
										<img class="sequenceActionImage" src="<lams:LAMSURL/>images/icons/cross.png"
											title="<fmt:message key="msg.planner.remove.node" />"
											onclick="javascript:leaveNodeEditor('<fmt:message key="msg.planner.remove.warning" />','${removeNodeUrl}')" />
										<c:if test="${not subnodeStatus.first}">
											<c:url value="/pedagogicalPlanner.do" var="upNodeUrl">
												<c:param name="method" value="moveNodeUp" />
												<c:param name="edit" value="true" />
												<c:param name="uid" value="${subnode.uid}" />
											</c:url>
											<img class="sequenceActionImage" src="<lams:LAMSURL/>images/css/up.gif"
												title="<fmt:message key="msg.planner.move.node.up" />"
												onclick="javascript:leaveNodeEditor(null,'${upNodeUrl}')" />
										</c:if>
										<c:if test="${not subnodeStatus.last}">
											<c:url value="/pedagogicalPlanner.do" var="downNodeUrl">
												<c:param name="method" value="moveNodeDown" />
												<c:param name="edit" value="true" />
												<c:param name="uid" value="${subnode.uid}" />
											</c:url>
											<img class="sequenceActionImage" src="<lams:LAMSURL/>images/css/down.gif"
												title="<fmt:message key="msg.planner.move.node.down" />"
												onclick="javascript:leaveNodeEditor(null,'${downNodeUrl}')" />
										</c:if>
									</c:if>
								</c:when>
								<c:otherwise>
									<%-- If we are not in the edit mode and the node leads to a template rather then subnodes,
										we display an icon --%>
									<c:if test="${not empty subnode.fileName}">
										<c:url var="startPreviewUrl" value="/pedagogicalPlanner.do">
											<c:param name="method" value="startPreview" />
											<c:param name="uid" value="${subnode.uid}" />
										</c:url>
										<a class="button" href="javascript:startPreview('${startPreviewUrl}')" 
										   title="<fmt:message key="msg.planner.open.template" />"><fmt:message key="button.planner.preview" /></a>
									</c:if>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<%-- Link to the subnode and its brief description below --%>
							<c:url value="/pedagogicalPlanner.do" var="subnodeUrl">
								<c:param name="method" value="openSequenceNode" />		
								<c:param name="edit" value="${node.edit}" />
								<c:param name="uid" value="${subnode.uid}" />
							</c:url>
							<a href="${subnodeUrl}"><c:out escapeXml='true' value='${subnode.title}' />
								<c:if test="${node.edit and subnode.locked}">
									<fmt:message key="label.planner.locked" />
								</c:if>
							</a>
							
							<c:if test="${not empty filterText}">
								<div  class="space-left small-space-top" style="font-size: smaller;">
									<%-- If we are in filter mode, we display the path where was the subnode found --%>
									<fmt:message key="label.planner.filter.find.location" />
									<%-- Always add root node at the beginning --%>
									<a href="${rootUrl}"><fmt:message key="label.planner.root.node" /></a> &gt;
									<%-- Iterate through subnodes, if any --%>
									<c:forEach var="title" items="${subnode.titlePath}">
										<c:url value="/pedagogicalPlanner.do" var="titleUrl">
											<c:param name="method" value="openSequenceNode" />
											<c:param name="uid" value="${title[0]}" />
											<c:param name="edit" value="${node.edit}" />
										</c:url>
										<a href="${titleUrl}"><c:out value='${title[1]}' escapeXml='true' /></a> &gt;
									</c:forEach>
								</div>
							</c:if>
							
							<div class="space-left small-space-top">${subnode.briefDescription}</div>
						</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	<c:choose>
		<c:when test="${node.edit and empty filterText}">
			<%-- Form for editing the node --%>
			<hr style="margin: auto" />
			
			<%-- Do we edit the current node or create a subnode? --%>
			<c:choose>
				<%-- Import node form --%>
				<c:when test="${node.importNode}">
					<h2 class="align-center">
						<fmt:message key="title.import" />
					</h2>
					<p>
						<fmt:message key="label.planner.import.instruction" />
					</p>
					<html:form styleId="nodeForm" styleClass="space-left" action="/pedagogicalPlanner" method="post" enctype="multipart/form-data">
						<c:set var="formBean"  value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
						<input type="hidden" id="method" name="method" value="importNode" />
						<input type="hidden" name="edit" value="true" />
						<input type="hidden" name="importNode" value="true" />
						<html:file style="float: left" property="file" size="106"/>
						<input style="margin: 0px 0px 20px 10px;" type="submit" value="<fmt:message key="button.import" />" class="button" />
					</html:form>
				</c:when>
				<%-- If the node is locked i.e. comes with LAMS distribution, we do not allow editing --%>
				<c:when test="${not node.createSubnode and (isRootNode or node.locked)}">
					<h3 class="align-center"><fmt:message key="msg.planner.node.locked" /></h3>
				</c:when>
				<c:otherwise>
					<html:form styleId="nodeForm" styleClass="space-left" action="/pedagogicalPlanner" method="post" enctype="multipart/form-data">
						<c:set var="formBean"  value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
						<input type="hidden" name="method" value="saveSequenceNode" />
						<input type="hidden" name="edit" value="true" />
						<html:hidden property="contentFolderId"/>
						
						<c:choose>
							<%-- If we are in the create subnode mode,
								the node we edit is actually the new subnode rather the parent node itself --%>
							<c:when test="${node.createSubnode}">
								<input type="hidden" id="parentUid" name="parentUid" value="${node.uid}" />
								<h2 class="align-center"><fmt:message key="label.planner.create.subnode" /></h2>
							</c:when>
							<c:otherwise>
								<input type="hidden" id="uid" name="uid" value="${node.uid}" />
								<h2 class="align-center"><fmt:message key="label.planner.edit.node" /></h2>
							</c:otherwise>
						</c:choose>
						
						<div class="field-name space-top"><fmt:message key="label.title" /></div>
						<html:text property="title" size="130" />
						 
						<div class="field-name space-top"><fmt:message key="label.planner.description.brief" /></div>
						<lams:FCKEditor id="briefDescription"
							value="${formBean.briefDescription}"
							contentFolderID="${formBean.contentFolderId}"
			                toolbarSet="Custom-Pedplanner" height="150px"
			                width="820px" displayExpanded="false">
						</lams:FCKEditor>
						
						<br /><br /><fmt:message key="label.planner.node.type" /><br />
						<html:radio property="nodeType" styleId="hasSubnodesType" onclick="javascript:onNodeTypeChange();" value="subnodes"><fmt:message key="label.planner.node.type.subnodes" /></html:radio><br />
						<html:radio property="nodeType" onclick="javascript:onNodeTypeChange();" value="template"><fmt:message key="label.planner.node.type.template" /></html:radio>
						
						<%-- DIVs below are displayed/hidden depending of the subnode type:
							 containing subnodes or opening a template --%>
						<c:set var="hasSubnodesType" value="${formBean.nodeType eq 'subnodes'}" />
						
						
						<%-- DIV with full description FCKeditor --%>
						<div id="fullDescriptionArea" class="space-bottom"
							<c:if test="${not hasSubnodesType}">
								style="display: none;"
							</c:if>
						>
							<div class="field-name space-top"><fmt:message key="label.planner.description.full" /></div>
							<lams:FCKEditor id="fullDescription"
								value="${formBean.fullDescription}"
								contentFolderID="${formBean.contentFolderId}"
				                toolbarSet="Custom-Pedplanner" height="150px"
				                width="820px" displayExpanded="false">
							</lams:FCKEditor>
						</div>
						
						<%-- DIV with "Browse" button to find the template --%>
						<div id="fileArea" class="space-top"
							<c:if test="${hasSubnodesType}">
								style="display: none;"
							</c:if>
						>
							<c:if test="${not empty node.fileName}">
								<c:url var="downloadTemplateFile" value="/download/">
									<c:param name="uuid" value="${formBean.fileUuid}" />
									<c:param name="preferDownload" value="true" />
								</c:url>
								<fmt:message key="label.planner.uploaded.template" /> <a href="${downloadTemplateFile}">${node.fileName}</a>
								<br /><br />
								<html:checkbox onclick="javscript:onRemoveFileCheckboxChange()" styleId="removeFile" property="removeFile"><fmt:message key="label.planner.remove.file" /></html:checkbox>
							</c:if>
							<div id="fileInputArea"	class="small-space-top space-bottom">
								<c:choose>
									<c:when test="${empty node.fileName}">
										<fmt:message key="label.planner.choose.file" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.planner.change.file" />
									</c:otherwise>
								</c:choose>
								<br />
								<html:file property="file" size="80" />
							</div>
						</div>
					</html:form>
				</c:otherwise>
			</c:choose>
			
			<%-- Buttons below --%>
			<div id="buttonArea" class="space-top">
				<c:url value="/pedagogicalPlanner.do" var="closeNodeEditorUrl">
					<c:param name="method" value="openSequenceNode" />
					<c:param name="edit" value="false" />
					<c:param name="uid" value="${empty node.fileName ? node.uid : node.parentUid}" />
				</c:url>
				<a class="button pedagogicalPlannerButtons" 
				<c:choose>
					<c:when test="${node.locked}">
						href="javascript:leaveNodeEditor(null,'${closeNodeEditorUrl}');"
					</c:when>
					<c:otherwise>
						href="javascript:leaveNodeEditor('<fmt:message key="msg.planner.editor.not.saved" />','${closeNodeEditorUrl}');"
					</c:otherwise>
				</c:choose>
				><fmt:message key="button.cancel" /></a>
				
				<c:if test="${not node.createSubnode and empty node.fileName}">
					<c:url value="/pedagogicalPlanner.do" var="createSubnodeUrl">
						<c:param name="method" value="openSequenceNode" />
						<c:param name="edit" value="true" />
						<c:param name="createSubnode" value="true" />
						<c:param name="uid" value="${node.uid}" />
					</c:url>
					<a class="button pedagogicalPlannerButtons" href="javascript:leaveNodeEditor(null,'${createSubnodeUrl}');"><fmt:message key="label.planner.create.subnode" /></a>
				</c:if>
				<c:if test="${empty node.parentUid and not empty node.uid and not node.createSubnode}">
					<c:url value="/pedagogicalPlanner.do" var="exportNodeUrl">
						<c:param name="method" value="exportNode" />
						<c:param name="edit" value="true" />
						<c:param name="uid" value="${node.uid}" />
					</c:url>
					<a class="button pedagogicalPlannerButtons" href="javascript:leaveNodeEditor(null,'${exportNodeUrl}');"><fmt:message key="label.planner.export" /></a>
				</c:if>
				<c:if test="${isRootNode}">
					<c:url value="/pedagogicalPlanner.do" var="importNodeUrl">
						<c:param name="method" value="openSequenceNode" />
						<c:param name="edit" value="true" />
						<c:param name="importNode" value="true" />
					</c:url>
					<a class="button pedagogicalPlannerButtons" href="javascript:leaveNodeEditor(null,'${importNodeUrl}');"><fmt:message key="label.planner.import" /></a>
				</c:if>
				<c:if test="${node.createSubnode or not isRootNode }">
					<a class="button pedagogicalPlannerButtons" href="javascript:document.getElementById('nodeForm').submit()"><fmt:message key="button.planner.save.node" /></a>
				</c:if>
			</div>
		</c:when>
		<c:otherwise>
			<div id="buttonArea">
				<a class="button pedagogicalPlannerButtons" href="javascript:closePlanner();"><fmt:message key="button.close" /></a>
				<c:if test="${node.isSysAdmin and not node.edit}">
					<c:url value="/pedagogicalPlanner.do" var="openEditorUrl">
						<c:param name="method" value="openSequenceNode" />
						<c:param name="edit" value="true" />
						<c:param name="uid" value="${node.uid}" />
					</c:url>
					<a class="button pedagogicalPlannerButtons" href="${openEditorUrl}"><fmt:message key="button.planner.editor.open" /></a>
				</c:if>
			</div>
		</c:otherwise>
	</c:choose>
</div>
<div id="footer"></div>
</div>
</body>
</lams:html>