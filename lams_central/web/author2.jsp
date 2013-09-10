<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<lams:html>
<lams:head>
	<title>Flashless Authoring</title>
	<style type="text/css">
		table#authoringTable {
			table-layout: fixed;
			width: 100%;
		}
		
		td#templateContainerCell {
			width: 150px;
			border: black thin solid;
			vertical-align: top;
			background-color: rgb(219,230,252);
		}
		
		div#templateContainer {
			height: 800px;
			overflow: auto;
		}
		
		div.template {
			height: 40px;
			border-bottom: black thin solid;
			cursor: pointer;
			vertical-align: middle;
		}
		
		div.template img {
			display: block;
			float: left;
			margin: 5px 8px 5px 5px;
		}
		
		div.template div {
			float: left;
			width: 90px;
			padding-top: 10px;
			font-size: 10pt;
		}
	
		td#canvas {
		}
	</style>

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript">
		
	</script>
	<script type="text/javascript" src="includes/javascript/authoring.js"></script>
</lams:head>
<body>
	<table id="authoringTable">
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