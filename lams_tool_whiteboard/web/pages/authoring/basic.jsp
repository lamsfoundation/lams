<%@ include file="/common/taglibs.jsp"%>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="forum.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <form:input path="whiteboard.title" cssClass="form-control"/>
</div>

<div class="form-group">
    <label for="whiteboard.instructions">
    	<fmt:message key="label.authoring.basic.instruction" />&nbsp;
        <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="" data-original-title="<fmt:message key="label.authoring.basic.instructions.tooltip"/>"></i>        
    </label>
    <lams:CKEditor id="whiteboard.instructions" value="${authoringForm.whiteboard.instructions}" contentFolderID="${authoringForm.contentFolderID}" />
</div>
<c:if test="${not empty whiteboardServerUrl}">
	<div class="full-screen-content-div">
		<div class="full-screen-flex-div">
			<a href="#" class="btn btn-default fixed-button-width pull-right full-screen-launch-button" onclick="javascript:launchIntoFullscreen(this)"
			   title="<fmt:message key='label.fullscreen.open' />">
				<i class="fa fa-arrows-alt" aria-hidden="true"></i>
			</a> 
	       	<a href="#" class="btn btn-default fixed-button-width pull-right full-screen-exit-button" onclick="javascript:exitFullscreen()"
			   title="<fmt:message key='label.fullscreen.close' />">
	       		<i class="fa fa-compress" aria-hidden="true"></i>
	       	</a>
	       	<div class="full-screen-main-div">
				<iframe id="whiteboard-frame"
				          src='${whiteboardServerUrl}/?whiteboardid=${authoringForm.whiteboard.contentId}&username=${authoringForm.authorName}${empty whiteboardAccessToken ? "" : "&accesstoken=".concat(whiteboardAccessToken)}'>
				</iframe>
			</div>
		</div>
	</div>
</c:if>