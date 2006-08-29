<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html:html locale="true">
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.mode}">
		<c:set var="mode" value="${param.mode}" />
	</c:if>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}"/>
	<c:set var="resource" value="${sessionMap.resource}"/>
	<c:set var="finishedLock" value="${sessionMap.finishedLock}"/>
	
	<script type="text/javascript">
	<!--
		function gotoURL(){
 		    var reqIDVar = new Date();
			var gurl = "<c:url value="/learning/addurl.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
	      	showMessage(gurl);
	      	return false;
		}
		function gotoFile(){
 		    var reqIDVar = new Date();
 		    var gurl = "<c:url value="/learning/addfile.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
	      	showMessage(gurl);
	      	return false;
		}
		function checkNew(){
 		    var reqIDVar = new Date();
			document.location.href = "<c:url value="/learning/start.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();
 		    return false;
		}
		function viewItem(itemUid){
			var myUrl = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=" + itemUid;
			launchPopup(myUrl,"LearnerView");
		}
		function completeItem(itemUid){
			document.location.href = "<c:url value="/learning/completeItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=" + itemUid;
			return false;
		}
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
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
	<div id="page-learner">
		<h1 class="no-tabs-below">
			${resource.title}
		</h1>
		<div id="header-no-tabs-learner">
		</div>
		<!--closes header-->

		<div id="content-learner">
			<h2>
				${resource.instructions}
			</h2>

			<%@ include file="/common/messages.jsp"%>
			<table cellpadding="0" cellspacing="0" class="alternative-color">
				<tr>
					<th width="255px">
						<fmt:message key="label.resoruce.to.review" />
					</th>
					<th width="75px">
						<fmt:message key="label.completed" />
					</th>
					<th align="center">

					</th>
				</tr>
				<c:forEach var="item" items="${sessionMap.resourceList}">
					<tr>
						<td align="center">
							${item.title}
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
							<c:if test="${mode != 'teacher' && (not finishedLock)}">
								<a href="javascript:;" onclick="return completeItem(${item.uid})" class="button">
									<fmt:message key="label.completed"/>
								</a>
							</c:if>
							&nbsp;
							<c:if test="${not finishedLock}">
								<a href="javascript:;"  onclick="viewItem(${item.uid})" class="button">
									<fmt:message key="label.view"/>
								</a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
				<c:if test="${resource.miniViewResourceNumber > 0}">
					<tr>
						<td colspan="3" align="left">
							<b>${resource.miniViewNumberStr}</b>
						</td>
					</tr>
				</c:if>
			</table>
			<div class="left-buttons">
				<html:button property="checkNewButton" onclick="return checkNew()" disabled="${finishedLock}" styleClass="button">
					<fmt:message key="label.check.for.new" />
				</html:button>
			</div>
			<c:if test="${mode != 'teacher'}">
				<div class="right-buttons">
					<html:button property="FinishButton"  onclick="return finishSession()" disabled="${finishedLock}"  styleClass="button">
						<fmt:message key="label.finished" />
					</html:button>
				</div>
			</c:if>
			<P>&nbsp;</P>
			<c:if test="${mode != 'teacher' && (not finishedLock)}">
				<c:if test="${resource.allowAddFiles || resource.allowAddUrls}">
					<table border="0" align="center" width="100%">
						<tr>
							<td style="align:left">
								<fmt:message key="label.suggest.new" />
								<c:choose>
									<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
										<input type="radio" name="suggest" value="url" checked="true" onclick="gotoURL()" >
										<fmt:message key="label.authoring.basic.resource.url.input" /> |
										<input type="radio" name="suggest" value="file" onclick="gotoFile()">
										<fmt:message key="label.authoring.basic.resource.file.input" />
									</c:when>
									<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
										<input type="radio" name="suggest" value="file" checked="true" onclick="gotoFile()">
										<fmt:message key="label.authoring.basic.resource.file.input" />
									</c:when>
									<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
										<input type="radio" name="suggest" value="url" checked="true" onclick="gotoURL()">
										<fmt:message key="label.authoring.basic.resource.url.input" />
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td>
								<iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'" id="reourceInputArea" name="reourceInputArea" style="width:0px;height:0px;border:0px;display:none" frameborder="no" scrolling="no">
								</iframe>
							</td>
						</tr>
					</table>
				</c:if>
				<c:choose>
					<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
						<script type="text/javascript">
					<!--
						showMessage("<c:url value='/learning/addurl.do'/>?sessionMapID=${sessionMapID}&mode=${mode}");
					-->
				</script>
					</c:when>
					<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
						<script type="text/javascript">
					<!--
						showMessage("<c:url value='/learning/addfile.do'/>?sessionMapID=${sessionMapID}&mode=${mode}");
					-->
				</script>
					</c:when>
					<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
						<script type="text/javascript">
					<!--
						showMessage("<c:url value='/learning/addurl.do'/>?sessionMapID=${sessionMapID}&mode=${mode}");
					-->
				</script>
					</c:when>
				</c:choose>
				<%-- end mode != teacher --%>
			</c:if>
		</div>  <!--closes content-->
		
		<div id="footer-learner">
		</div>
		<!--closes footer-->

	</div><!--closes page-->
</body>
</html:html>

