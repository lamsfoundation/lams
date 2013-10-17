<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title>Flashless Authoring</title>
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/authoring.css" type="text/css" media="screen">

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript">
		
	</script>
	<script type="text/javascript" src="includes/javascript/authoring.js"></script>
</lams:head>
<body>
	<div id="toolbar" class="ui-widget-header ui-corner-all">
		<div>
			<div class="split-ui-button">
				<div>
					<span>Flow</span>
				</div>
				<div>&nbsp;</div>
			</div>
			<ul>
				<li><div onClick="javascript:MenuUtils.addGate()">Gate</div></li>
				<li><div>Branch</div></li>
			</ul>
		</div>
		<div id="groupingButton" class="ui-button" onClick="javascript:MenuUtils.addGrouping()">
			Group
		</div>
	</div>
	<table id="authoringTable">
		<tr>
			<td id="layoutCell"></td>
			<td></td>
		</tr>
		<tr>
			<td id="templateContainerCell">
				<div id="templateContainer">
					<c:forEach var="tool" items="${tools}">
						<div id="template_${tool.toolId}" class="template">
							<c:if test="${not empty tool.iconPath}">
								<img src="${tool.iconPath}" />
							</c:if>
							<div><c:out value="${tool.toolDisplayName}" /></div>
						</div>
					</c:forEach>
				</div>
			</td>
			<td id="canvas"></td>
		</tr>
	</table>
</body>
</lams:html>