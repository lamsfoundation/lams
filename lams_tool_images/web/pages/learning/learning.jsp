<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title>
		<fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
	<c:set var="mediumImageDimensions" value="${sessionMap.mediumImageDimensions}" />
	<c:set var="thumbnailImageDimensions" value="${empty sessionMap.thumbnailImageDimensions ? 100 : sessionMap.thumbnailImageDimensions}" />

	<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>css/thickbox.css">
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/fotorama.css'/>"/>
	<style media="screen,projection" type="text/css">
		@media (max-width: 750px) {
		  	#aside {
		    	float: none;
		    	width: auto;
		    	position: static;
		  	}
		}
		#check-for-new-button {
			margin-top: 20px;
		}
		#check-for-new-button, #add-new-image-button, #delete-button {
		}
		.extra-controls-inner {
			float: right;
		}
		.caption{
			color:#0087e5; 
			font:italic 14px georgia,serif;
		}
		.description{ 
			font-style:italic; 
			font-size:11px;
		}
		#extra-controls {
			text-align: center;
			float: right;
			clear: both;
			padding-top: 20px;
		}
		[id^=comments-area] {
			width: 400px;
			float: right;
			clear: both;
			padding: 10px 0 20px;
		}
		[id^=comment-textarea] {
			width: 370px;
		}
		.button.add-comment {
			margin-right: 2px;
			margin-top: 10px;
		}
		table.forum {
			border-bottom: none;
			margin-bottom: 0;
		}
    	.fotorama__thumb {
		    background-color: #000;
		}
		.fotorama__wrap {
			margin: auto;
		}
		.fotorama__caption {
			text-align: left;
		}
		.fotorama-container {
			text-align: center;
			padding-top: 10px;
		}
		#image-info:after {
		   content: " ";
		   display: block; 
		   height: 0; 
		   clear: both;
		}
		#kkk{
			float:right;
		}
		.space-bottom-top {
			padding-top: 40px;
		}
    </style>
    <link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
    
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
 	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
 	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/fotorama.js'/>"></script>
	<script type="text/javascript">
	
		$(document).ready(function(){ 
			$('.fotorama')
				.on('fotorama:show ',
					function (e, fotorama, extra) {
					    loadImageData(fotorama.activeFrame.id);
					}
				)
				.fotorama({
					//TODO history   : true,
					width: ${mediumImageDimensions + 60},
					height: Math.round(${mediumImageDimensions + 60}*9/16),
					maxwidth: '100%',
					//ratio: 16/9,
					allowfullscreen: true,
					keyboard: true,
					nav: 'thumbs',
					fit: 'scaledown',
					//thumbfit: 'contain',
					loop: true,
					thumbwidth: 80,//${thumbnailImageDimensions},
					thumbheight: 80,//${thumbnailImageDimensions},
				    data: [
						<c:forEach var="image" items="${sessionMap.imageGalleryList}" varStatus="status">
							<c:if test="${!image.createByAuthor && (image.createBy != null)}">
								<c:set var="imageAddedBy" ><div class="description"><fmt:message key="label.learning.added.by" /> <c:out value="${image.createBy.firstName} ${commentsImage.createBy.lastName}" escapeXml="true"/></div></c:set>
							</c:if>					
						
							{
								img: '<html:rewrite page="/download/?uuid="/>${image.mediumFileUuid}&preferDownload=false',
								//thumb: '<html:rewrite page="/download/?uuid="/>${image.thumbnailFileUuid}&preferDownload=false',
								full: '<html:rewrite page="/download/?uuid="/>${image.originalFileUuid}&preferDownload=false',
								id: '${image.uid}', // Custom anchor is used with the hash:true option.
								caption: '<div class="caption">${image.titleEscaped}</div>'
									+ '<span class="description">${image.descriptionEscaped}</span>'
									+'${imageAddedBy}',
								//html: $('selector'), // ...or '<div>123</div>'. Custom HTML inside the frame.
								//fit: 'cover' // Override the global fit option.
								//any: 'Any data relative to the frame you want to store'
							},
						</c:forEach>
					]
				}
			);
	
		});

		function checkNew(){
			document.location.href = "<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}";
 		    return false;
		}
		function finishSession(){
			$('#finishButton').attr('disabled', true);
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect() {
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function deleteImage(imageUid) {
			var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");
			
			if (deletionConfirmed) {
				document.location.href = "<c:url value="/learning/deleteImage.do"/>?sessionMapID=${sessionMapID}&imageUid=" + imageUid;
			}
			return false;
		}

		function loadImageData(imageUid) {
			
			$("#image-info").load(
				"<c:url value="/learning/loadImageData.do"/>",
				{
					imageUid: imageUid, 
					sessionMapID: "${sessionMapID}"
				}
			);
		}  
    </script>
   
</lams:head>
<body class="stripes">
	<div id="content">

		<%--ImageGallery information-----------------------------------%>

		<h1>
			<c:out value="${imageGallery.title}" escapeXml="true"/>
		</h1>
		<p>
			<c:out value="${imageGallery.instructions}" escapeXml="false"/>
		</p>
		
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
		
		<!-- Rating limits info -->
		<c:if test="${imageGallery.allowRank && (imageGallery.minimumRates ne 0 || imageGallery.maximumRates ne 0)}">
		
			<div class="info">
				<c:choose>
					<c:when test="${imageGallery.minimumRates ne 0 and imageGallery.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder">
							<fmt:param value="${imageGallery.minimumRates}"/>
							<fmt:param value="${imageGallery.maximumRates}"/>
						</fmt:message>
					</c:when>
					
					<c:when test="${imageGallery.minimumRates ne 0 and imageGallery.maximumRates eq 0}">
						<fmt:message key="label.rate.limits.reminder.min">
							<fmt:param value="${imageGallery.minimumRates}"/>
						</fmt:message>
					</c:when>
					
					<c:when test="${imageGallery.minimumRates eq 0 and imageGallery.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder.max">
							<fmt:param value="${imageGallery.maximumRates}"/>
						</fmt:message>
					</c:when>
				</c:choose>
				<br>
						
				<fmt:message key="label.rate.limits.topic.reminder">
					<fmt:param value="<span id='count-rated-items'>${sessionMap.countRatedItems}</span>"/>
				</fmt:message>
			</div>
			
		</c:if>
		
		<%@ include file="/common/messages.jsp"%>
		
		<%--Main image---------------------------------------------------%>
		
		<div class="fotorama-container">
			<div class="fotorama"></div>
		</div>
			
		<%--Comments & Ranking/Voting area----------------------------------------------%>	
	 	
		<div id="image-info">
			<%@ include file="/pages/learning/parts/commentsarea.jsp"%>
		</div>
 
		<%--Reflection--------------------------------------------------%>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2><fmt:message key="title.reflection" /></h2>
				<strong>
					<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
				</strong>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> 
								<fmt:message key="message.no.reflection.available" />
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
					<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>
		
		<%--Bottom buttons-------------------------------------------%>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right" id="learner-submit"
				<c:if test="${imageGallery.minimumRates ne 0 && empty sessionMap.currentImage}">style="display:none;"</c:if>
			>
				<c:choose>
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button" >
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:link href="#nogo" property="FinishButton" styleId="finishButton"	onclick="return finishSession()" styleClass="button" >
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
						</html:link>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>	

	</div>
	<%--closes content--%>

	<div id="footer">
	</div>
	<%--closes footer--%>

</body>
</lams:html>
