<%@ include file="/common/taglibs.jsp"%>
<html:html locale="true">
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
	<!--
		function gotoURL(){
 		    var reqIDVar = new Date();
			var gurl = "<c:url value="/pages/learning/addurl.jsp"/>?&reqID="+reqIDVar.getTime();
	      	showMessage(gurl);
	      	return false;
		}
		function gotoFile(){
 		    var reqIDVar = new Date();
 		    var gurl = "<c:url value="/pages/learning/addfile.jsp"/>?&reqID="+reqIDVar.getTime();
	      	showMessage(gurl);
	      	return false;
		}
		function checkNew(){
 		    var reqIDVar = new Date();
			document.location.href = "<c:url value="/learning/start.do"/>?toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();
 		    return false;
		}
		function viewItem(itemUid){
			var myUrl = "<c:url value="/reviewItem.do"/>?itemUid=" + itemUid;
			launchPopup(myUrl,"LearnerView");
		}
		function completeItem(itemUid){
			document.location.href = "<c:url value="/learning/completeItem.do"/>?itemUid=" + itemUid;
			return false;
		}
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function showMessage(url) {
			var area=document.getElementById("reourceInputArea");
			area.style.width="100%";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
	-->        
    </script>
</head>
<body>
	<table border="0" align="center" class="forms" width="95%">
		<tr>
			<td>
				<h1>
					${resource.title}
				</h1>
				<h2>
					${resource.instructions}
				</h2>
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
				<a href="#" style="width:120px" class="button" onclick="return checkNew()"><fmt:message key="label.check.for.new" /></a>
			</th>
		</tr>
		<c:forEach var="item" items="${resourceList}">
			<tr>
				<td>${item.title}
				<c:if test="${!item.createByAuthor}">
					[${item.createBy.loginName}]
				</c:if>
				</td>
				<td align="center">
					<c:if test="${item.complete}">
						<img src="<html:rewrite page='/includes/images/tick.gif'/>" border="0">
					</c:if>
				
				</td>
				
				<td>
					<a href="#" class="button" onclick="return completeItem(${item.uid})"><fmt:message key="label.completed" /></a>
					<a href="javascript:;" class="button" onclick="viewItem(${item.uid})" ><fmt:message key="label.view" /></a>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="3" align="right">
				<a href="#" class="button" onclick="return finishSession()">
					<fmt:message key="label.finished" />
				</a>
			</td>
		</tr>
		<c:if test="${resource.miniViewResourceNumber > 0}">
			<tr>
				<td colspan="4" align="left">
					<b>${resource.miniViewNumberStr}</b>
				</td>
			</tr>
		</c:if>
	</table>
	<c:if test="${resource.allowAddFiles || resource.allowAddUrls}">
		<table border="0" align="center" class="forms" width="95%">
			<tr>
				<td style="align:left" >
					<fmt:message key="label.suggest.new"/> 
					<c:choose>
						<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
							<input type="radio" name="suggest" value="url" checked="true" onclick="gotoURL()"><fmt:message key="label.authoring.basic.resource.url.input"/> |
							<input type="radio" name="suggest" value="file"  onclick="gotoFile()"><fmt:message key="label.authoring.basic.resource.file.input"/> 
						</c:when>
						<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
							<input type="radio" name="suggest" value="file"  checked="true" onclick="gotoFile()"><fmt:message key="label.authoring.basic.resource.file.input"/> 
						</c:when>
						<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
							<input type="radio" name="suggest" value="url" checked="true" onclick="gotoURL()"><fmt:message key="label.authoring.basic.resource.url.input"/>
						</c:when>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'" 
						id="reourceInputArea" name="reourceInputArea" style="width:0px;height:0px;border:0px;display:none" frameborder="no" scrolling="no">
					</iframe>
				</td>
			</tr>			
		</table>
	</c:if>
	<c:choose>
		<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
			<script type="text/javascript">
				<!--
					showMessage("<c:url value='/pages/learning/addurl.jsp'/>");
				-->
			</script>
		</c:when>
		<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
			<script type="text/javascript">
				<!--
					showMessage("<c:url value='/pages/learning/addfile.jsp'/>");
				-->
			</script>
		</c:when>
		<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
			<script type="text/javascript">
				<!--
					showMessage("<c:url value='/pages/learning/addurl.jsp'/>");
				-->
			</script>
		</c:when>
	</c:choose>		
</body>
</html:html>

