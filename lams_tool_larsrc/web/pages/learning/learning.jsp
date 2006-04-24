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
		function checkNew(){
			parent.learningFrame.location = "<c:url value="/learning/start.do"/>?toolSessionID=${toolSessionID}";
		}
		function viewItem(itemUid){
			parent.learningFrame.location = "<c:url value="/learning/viewItem.do"/>?itemUid=" + itemUid;
		}
		function completeItem(itemUid){
			parent.learningFrame.location = "<c:url value="/learning/completeItem.do"/>?itemUid=" + itemUid;
		}
		function finishSession(){
			location.href='<c:url value="/learning/finish.do?toolSessionID=${toolSessionID}&mode=learner"/>';
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
	<%@ include file="/common/messages.jsp"%>
	<table border="0" align="center" class="forms" width="95%">
		<tr>
			<th scope="col">
				<fmt:message key="label.resoruce.to.review" />
			</th>
			<th scope="col" width="50px">
				<fmt:message key="label.completed" />
			</th>
			<th scope="col" style="width:200px">
				<a href="javascript:;" style="width:120px" class="button" onclick="checkNew()"><fmt:message key="label.check.for.new" /></a>
			</th>
		</tr>
		<c:forEach var="item" items="${resourceList}">
			<tr>
				<td>${item.title}</td>
				<td>
					<c:if test="${item.complete}">
						<img src="<html:rewrite page='/includes/images/cross.gif'/>" border="0">
					</c:if>
				
				</td>
				
				<td>
					<a href="javascript:;" class="button" onclick="viewItem(${item.uid})" ><fmt:message key="label.view" /></a>
					<a href="javascript:;" class="button" onclick="completeItem(${item.uid})"><fmt:message key="label.completed" /></a>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="3" align="right">
				<a href="javascript:;" class="button" onclick="finishSession()">
					<fmt:message key="label.finished" />
				</a>
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

