<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--
	var mode = "${mode}";

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}

	function validateForm() {
	}
	
	var popupWindow = null;
	
	function openPixlr(url)
	{
		url += "&target=" + escape("${returnURL}");
		url += "&image=" + escape("${currentImageURL}");
		openPopup(url, 570, 796);
	}
	
	function openImage(url)
	{
		openPopup(url, origImageHeight, origImageWidth)
	}
	
	function openPopup(url, height, width)
	{	
		if(popupWindow && popupWindow.open && !popupWindow.closed){
			popupWindow.close();
		}
		popupWindow = window.open(url,'popupWindow','resizable,width=' +width+ ',height=' +height+ ',scrollbars');
	}
	
	function refresh()
	{
		window.location.href = "<lams:WebAppURL/>/learning.do?mode=${mode}&toolSessionID=${toolSessionID}&redoQuestion=true";
	}

//-->
</script>



<div id="content">
	<h1>
		${pixlrDTO.title}
	</h1>

	<p>
		${pixlrDTO.instructions}
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
		<img id="image" border="1" title='<fmt:message key="tooltip.openfullsize" />' onclick="openImage('${currentImageURL}')" src="${currentImageURL}" />
		
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
	//resizing image to thumbnail size
	var image = document.getElementById("image");
	var origImageHeight = image.height;
	var origImageWidth =  image.width;
	
	if (image.height >= image.width)
	{
		if (image.height > 300)
		{
			image.height = 300;
		}
	}
	else
	{
		if (image.width > 300)
		{
			image.width = 300;
		}
	}
//-->
</script>
