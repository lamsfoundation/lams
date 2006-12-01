<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="resource" value="${sessionMap.resource}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

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
			document.getElementById("finishButton").disabled = "disabled";
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function showMessage(url) {
			var area=document.getElementById("reourceInputArea");
			if(area != null){
				area.style.width="100%";
				area.style.height="100%";
				area.src=url;
				area.style.display="block";
			}
		}
	-->        
    </script>
</head>
<body class="stripes">


	<div id="content">
		<h1>
			${resource.title}
		</h1>

		<p>
			${resource.instructions}
		</p>

		<%@ include file="/common/messages.jsp"%>

		<table cellspacing="0" class="alternative-color">
			<tr>
				<th width="70%">
					<fmt:message key="label.resoruce.to.review" />
				</th>
				<th align="center">
					<fmt:message key="label.completed" />
				</th>
			</tr>
			<c:forEach var="item" items="${sessionMap.resourceList}">
				<tr>
					<td>

						<c:choose>
							<c:when test="${not finishedLock}">
								<a href="javascript:;" onclick="viewItem(${item.uid})">
									${item.title} </a>
							</c:when>

							<c:otherwise>
								${item.title}
							</c:otherwise>
						</c:choose>

						<c:if test="${!item.createByAuthor && item.createBy != null}">
								[${item.createBy.loginName}]
							</c:if>
					</td>
					<td align="center">
						<c:choose>
							<c:when test="${item.complete}">
								<img src="<html:rewrite page='/includes/images/tick.gif'/>"
									border="0">
							</c:when>
							<c:otherwise>
								<c:if test="${mode != 'teacher' && (not finishedLock)}">
									<a href="javascript:;"
										onclick="return completeItem(${item.uid})"> <fmt:message
											key="label.completed" /> </a>
								</c:if>
							</c:otherwise>
						</c:choose>
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


		<p>
			<a href="#" onclick="return checkNew()" class="button"> <fmt:message
					key="label.check.for.new" /> </a>
		</p>


		<c:if test="${mode != 'teacher' && (not finishedLock)}">
			<c:if test="${resource.allowAddFiles || resource.allowAddUrls}">

				<h2>
					<fmt:message key="label.suggest.new" />
				</h2>

				<div class="small-space-top">
					<c:choose>
						<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
							<input type="radio" name="suggest" value="url" checked="true"
								onclick="gotoURL()" class="noBorder">
							<fmt:message key="label.authoring.basic.resource.url.input" /> |
										<input type="radio" name="suggest" value="file"
								onclick="gotoFile()" class="noBorder">
							<fmt:message key="label.authoring.basic.resource.file.input" />
						</c:when>

						<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
							<input type="radio" name="suggest" value="file" checked="true"
								onclick="gotoFile()" class="noBorder">
							<fmt:message key="label.authoring.basic.resource.file.input" />
						</c:when>

						<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
							<input type="radio" name="suggest" value="url" checked="true"
								onclick="gotoURL()" class="noBorder">
							<fmt:message key="label.authoring.basic.resource.url.input" />
						</c:when>
					</c:choose>
				</div>

				<iframe
					onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
					id="reourceInputArea" name="reourceInputArea"
					style="width:0px;height:0px;border:0px;display:none"
					frameborder="no" scrolling="no">
				</iframe>



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
		
		
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>${sessionMap.reflectInstructions}</h2>
			
				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em>
								<fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p> <lams:out escapeXml="true" value="${sessionMap.reflectEntry}" />  </p>				
					</c:otherwise>
				</c:choose>
				
				<html:button property="FinishButton"
					onclick="return continueReflect()" styleClass="button">
					<fmt:message key="label.edit" />
				</html:button>											
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when
						test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton"
							onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:button property="FinishButton" styleId="finishButton"
							onclick="return finishSession()" styleClass="button">
							<fmt:message key="label.finished" />
						</html:button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
