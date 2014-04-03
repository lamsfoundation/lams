<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%-- If you change this file, remember to update the copy made for CNG-36 --%>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/mobileheader.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="resource" value="${sessionMap.resource}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
	<c:set var="newUrl" ><c:url value="/learning/addurl.do"/>?sessionMapID=${sessionMapID}&mode=${mode}</c:set>
	<c:set var="newFile" ><c:url value="/learning/addfile.do"/>?sessionMapID=${sessionMapID}&mode=${mode}</c:set>

	<script type="text/javascript">
		$(document).bind('pageinit', function(){
			//File Upload on  Mobile devices (taken from http://viljamis.com/blog/2012/file-upload-support-on-mobile/)
			var isFileInputSupported = (function () {
				// Handle devices which falsely report support
				if (navigator.userAgent.match(/(Android (1.0|1.1|1.5|1.6|2.0|2.1))|(Windows Phone (OS 7|8.0))|(XBLWP)|(ZuneWP)|(w(eb)?OSBrowser)|(webOS)|(Kindle\/(1.0|2.0|2.5|3.0))/)) {
			   		return false;
				}
				// Create test element
				var el = document.createElement("input");
				el.type = "file";
				return !el.disabled;
			})();

			// hide Add File button if not supported
			if (!isFileInputSupported) {
				$(".new-file-container").hide();
			}
		});
	
		function checkNew(checkFinishedLock){
 		    var reqIDVar = new Date();
				
			document.location.href = "<c:url value="/learning/start.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();				
				
 		    return false;
		}
		function viewItem(itemUid){
			var myUrl = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=" + itemUid;
			launchPopup(myUrl,"LearnerView");
		}
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		

    </script>
</lams:head>
<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${resource.title}" escapeXml="true"/>
		</h1>
	</div>

	<div data-role="content">

		<h2>
			<c:out value="${resource.instructions}" escapeXml="false"/>
		</h2>

		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
				<div class="info">
					<c:choose>
						<c:when test="${sessionMap.userFinished}">
							<fmt:message key="message.activityLocked" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</div>
		</c:if>

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
						<a href="#" onclick="viewItem(${item.uid})">
							<c:out value="${item.title}" escapeXml="true"/></a>
						
						<c:if test="${!item.createByAuthor && item.createBy != null}">
								[<c:out value="${item.createBy.loginName}" escapeXml="true"/>]
						</c:if>
					</td>
					<td align="center">
						<c:choose>
							<c:when test="${item.complete}">
								<img src="<html:rewrite page='/includes/images/tick.gif'/>"
									border="0">
							</c:when>
							<c:otherwise>
								-
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


		<c:if test="${mode != 'teacher'}">
			<p>
				<a href="#nogo" onclick="return checkNew();" data-role="button" data-icon="refresh"> 
					<fmt:message key="label.check.for.new" /> 
				</a>
			</p>
		</c:if>

		<c:if test="${mode != 'teacher' && (not finishedLock)}">
			<c:if test="${resource.allowAddFiles || resource.allowAddUrls}">

				<h2>
					<fmt:message key="label.suggest.new" />
				</h2>

				<div class="ui-grid-a small-space-top">
					<c:choose>
						<c:when test="${resource.allowAddFiles && resource.allowAddUrls}">
							<div class="ui-block-a">
								<a name="newUrl" href="${newUrl}" data-rel="dialog"  data-role="button"
										 data-theme="c" data-icon="plus" onclick="this.href += '&reqID=' + (new Date()).getTime();">
									<fmt:message key="label.authoring.basic.resource.url.input" />
								</a>
							</div>
							<div class="ui-block-b new-file-container">	
								<a name="newFile" href="${newFile}" data-rel="dialog"  data-role="button"
										 data-theme="c" data-icon="plus" onclick="this.href += '&reqID=' + (new Date()).getTime();">
									<fmt:message key="label.authoring.basic.resource.file.input" />
								</a>
							</div>
						</c:when>

						<c:when test="${resource.allowAddFiles && !resource.allowAddUrls}">
							<div class="new-file-container">
								<a name="newFile" href="${newFile}" data-rel="dialog"  data-role="button"
										 data-theme="c" data-icon="plus" onclick="this.href += '&reqID=' + (new Date()).getTime();">
									<fmt:message key="label.authoring.basic.resource.file.input" />
								</a>
							</div>
						</c:when>

						<c:when test="${!resource.allowAddFiles && resource.allowAddUrls}">
							<a name="newUrl" href="${newUrl}" data-rel="dialog"  data-role="button"
								 data-theme="c" data-icon="plus" onclick="this.href += '&reqID=' + (new Date()).getTime();">
								<fmt:message key="label.authoring.basic.resource.url.input" />
							</a>
						</c:when>
					</c:choose>
				</div>
			</c:if>
		</c:if>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h3><fmt:message key="title.reflection" /></h3>
				<strong>
					<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
				</strong>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>

				<c:if test="${mode != 'teacher'}">
					<html:button property="FinishButton" onclick="return continueReflect()">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

	</div>

	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
				<c:if test='${sessionMap.mode != "teacher"}'>
					<c:choose>
						<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
							<button name="FinishButton" onclick="return continueReflect();" data-icon="arrow-r">
								<fmt:message key="label.continue" />
							</button>
						</c:when>
		
						<c:otherwise>
							<button name="FinishButton" id="finishButton" data-icon="arrow-r" onclick="return finishSession();">
								<span class="nextActivity">
									<c:choose>
				 						<c:when test="${sessionMap.activityPosition.last}">
				 							<fmt:message key="label.submit" />
				 						</c:when>
				 						<c:otherwise>
				 		 					<fmt:message key="label.finished" />
				 						</c:otherwise>
				 					</c:choose>
								</span>
							</button>
						</c:otherwise>
					</c:choose>
				</c:if>
		</span>
	</div>

</div>
</body>
</lams:html>
