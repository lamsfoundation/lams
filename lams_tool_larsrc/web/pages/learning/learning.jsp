<%@ include file="/common/taglibs.jsp"%>
<html:html locale="true">
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script>
	<!--
		function gotoURL(){
			parent.newResourceFrame.location = "<c:url value="/pages/learning/addurl.jsp"/>";
	      	parent.newResourceFrame.focus();
		}
		function gotoFile(){
			parent.newResourceFrame.location = "<c:url value="/pages/learning/addfile.jsp"/>";
	      	parent.newResourceFrame.focus();
		}
	-->        
    </script>
</head>
<body>
	<table border="0" align="center" class="forms" width="95%">
		<tr>
			<td>
				<h1>
					<fmt:message key="label.learning.heading" />
				</h1>
			</td>
		</tr>
		<tr>
			<td class="formlabel">
				${resource.instructions}
			</td>
		</tr>
	</table>
	<table border="0" align="center" class="forms" width="95%">
		<tr>
			<th scope="col">
				<fmt:message key="label.resoruce.to.review" />
			</th>
			<th scope="col" width="50px">
				<fmt:message key="label.completed" />
			</th>
			<th scope="col" style="width:200px">
				<a href="javascript:;" style="width:120px" class="button"><fmt:message key="label.check.for.new" /></a>
			</th>
		</tr>
		<c:forEach var="item" items="${resourceList}">
			<tr>
				<td></td>
				<td></td>
				<td>
					<a href="javascript:;" class="button"><fmt:message key="label.view" /></a>
					<a href="javascript:;" class="button"><fmt:message key="label.completed" /></a>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="3" align="right">
				<a href="javascript:;" class="button"><fmt:message key="label.finished" /></a>
			</td>
		</tr>
		<c:if test="${resource.minViewNumber}">
			<tr>
				<td colspan="4" align="left">
					<b><fmt:message key="label.learning.minmum.review" /></b>
				</td>
			</tr>
		</c:if>
	</table>
	<table border="0" align="center" class="forms" width="95%">
		<tr>
			<td style="align:left" >
				<fmt:message key="label.suggest.new"/> 
				<input type="radio" name="suggest" value="url" checked="true" onclick="gotoURL()"><fmt:message key="label.authoring.basic.resource.url.input"/> |
				<input type="radio" name="suggest" value="file"  onclick="gotoFile()"><fmt:message key="label.authoring.basic.resource.file.input"/> 
			</td>
		</tr>
	</table>
</body>
</html:html>

