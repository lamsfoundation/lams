<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--
	var mode = "${mode}";
	
	
	function openPixlr(url)
	{
		alert("<fmt:message key="message.learner.saveWhenFinished" />");
		
		url += "&target=" + escape("${returnURL}");
		url += "&image=" + escape("${currentImageURL}");
		openPopup(url, 570, 796);
	}

	function refresh()
	{
		window.location.href = "<lams:WebAppURL/>/learning.do?mode=${mode}&toolSessionID=${toolSessionID}&redoQuestion=true";
	}
	
	

//-->
</script>



<div id="content">
	<h1>
		<c:out value="${pixlrDTO.title}" escapeXml="true"/>
	</h1>

	<p>
		<c:out value="${pixlrDTO.instructions}" escapeXml="false"/>
	</p>

	<c:if test="${pixlrDTO.lockOnFinish and mode == 'learner'}">
		<div class="info">
			<c:choose>
				<c:when test="${finishedActivity}">
					<fmt:message key="message.activityLocked" />
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${pixlrDTO.allowViewOthersImages}">
							<fmt:message key="message.warnLockOnFinishViewAll" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
		<br />
	</c:if>

	&nbsp;

	<c:set var="lrnForm"
		value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

	<br />
	<div style="text-align:center;">
		<img id="image" border="1" title="<fmt:message key="tooltip.openfullsize" />" onclick="openImage('${currentImageURL}')" src="${currentImageURL}" />
		
		<br />
		<br />
		<c:choose>
			<c:when test="${contentEditable}">
				<a class="button" href="javascript:openPixlr('${pixlrURL}');"><fmt:message key="learner.edit.image" /></a>
			</c:when>
		</c:choose>
	</div>
	
	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>


</div>

<script type="text/javascript">
<!--
	
	
	
//-->
</script>
