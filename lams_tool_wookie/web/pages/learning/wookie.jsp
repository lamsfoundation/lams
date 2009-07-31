<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--
	var mode = "${mode}";

	function refresh()
	{
		window.location.href = "<lams:WebAppURL/>/learning.do?mode=${mode}&toolSessionID=${toolSessionID}&redoQuestion=true";
	}
	
	

//-->
</script>



<div id="content">
	<h1>
		${wookieDTO.title}
	</h1>

	<p>
		${wookieDTO.instructions}
	</p>

	<c:if test="${wookieDTO.lockOnFinish and mode == 'learner'}">
		<div class="info">
			<c:choose>
				<c:when test="${finishedActivity}">
					<fmt:message key="message.activityLocked" />
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" />
				</c:otherwise>
			</c:choose>
		</div>
		<br />
	</c:if>

	&nbsp;

	<c:set var="lrnForm"
		value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

	
	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>


</div>

<script type="text/javascript">
<!--
	
	
	
//-->
</script>
