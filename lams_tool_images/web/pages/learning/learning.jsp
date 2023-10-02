<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>

<c:set var="lams"><lams:LAMSURL /></c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:choose>
	<c:when test="${sessionMap.mode == 'learner' || sessionMap.mode == 'author'}">
		<!-- ordinary learner or preview -->
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
	</c:otherwise>
</c:choose>
<c:set var="ALLOWED_EXTENSIONS_IMAGE"><%=FileUtil.ALLOWED_EXTENSIONS_IMAGE%></c:set>
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="mediumImageDimensions" value="${sessionMap.mediumImageDimensions}" />
<c:set var="thumbnailImageDimensions" value="${empty sessionMap.thumbnailImageDimensions ? 100 : sessionMap.thumbnailImageDimensions}" />
<c:set var="language"><lams:user property="localeLanguage"/></c:set>

<lams:PageLearner title="${imageGallery.title}" toolSessionID="${toolSessionID}" >
	<link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>/includes/css/fotorama.css"/>
	<link href="<lams:WebAppURL/>includes/css/imageGallery.css" rel="stylesheet" type="text/css">
    <lams:css suffix="jquery.jRating"/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
	<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
	<link href="${lams}css/uppy.custom.css" rel="stylesheet" type="text/css" />
    
    <script type="text/javascript">
		<%-- used for  imageGalleryitem.js --%>
		var UPLOAD_FILE_LARGE_MAX_SIZE = "${UPLOAD_FILE_MAX_SIZE}";
		// convert Java syntax to JSON
		var UPLOAD_ALLOWED_EXTENSIONS = JSON.parse("[" + "${ALLOWED_EXTENSIONS_IMAGE}".replace(/\.\w+/g, '"$&"') + "]");
		var LABEL_ITEM_BLANK = '<fmt:message key="error.resource.item.file.blank"/>';
	</script>
	<lams:JSImport src="includes/javascript/imageGallerycommon.js" relative="true" />
	<lams:JSImport src="includes/javascript/imageGalleryitem.js" relative="true" />
	<script type="text/javascript" src="${lams}includes/javascript/uppy/uppy.min.js"></script>
	<c:choose>
		<c:when test="${language eq 'es'}">
			<script type="text/javascript" src="${lams}includes/javascript/uppy/es_ES.min.js"></script>
		</c:when>
		<c:when test="${language eq 'fr'}">
			<script type="text/javascript" src="${lams}includes/javascript/uppy/fr_FR.min.js"></script>
		</c:when>
		<c:when test="${language eq 'el'}">
			<script type="text/javascript" src="${lams}includes/javascript/uppy/el_GR.min.js"></script>
		</c:when>
	</c:choose>
	<lams:JSImport src="includes/javascript/uploadImageLearning.js" relative="true" />
    <lams:JSImport src="includes/javascript/upload.js" />
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
 	<lams:JSImport src="includes/javascript/fotorama.js" relative="true" />
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionID}', '', finishSession);
	
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
							{
								img: "<lams:WebAppURL />download/?uuid=${image.mediumFileDisplayUuid}&preferDownload=false",
								full: "<lams:WebAppURL />download/?uuid=${image.originalFileDisplayUuid}&preferDownload=false",
								id: '${image.uid}', // Custom anchor is used with the hash:true option.
								caption:'<div class="card-subheader">${image.titleEscaped}</div>' +
										'<div>${image.descriptionEscaped}</div>'										
										<c:if test="${!image.createByAuthor && (image.createBy != null)}">
											<c:set var="portrait"><lams:Portrait userId="${image.createBy.userId}"/></c:set>
											<c:set var="portrait">${fn:replace(portrait, "'", "\\'")}</c:set> <%-- escape the url --%>
											+ '<div class="fst-italic"><fmt:message key="label.learning.added.by" /> <span class="mx-2">${portrait}</span><c:out value="${image.createBy.firstName} ${image.createBy.lastName}" escapeXml="true"/></div>'
										</c:if>
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
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
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
	<div class="container-lg">
	
		<%--Advanced settings and notices-----------------------------------%>
		<div id="instructions" class="instructions">
			<c:out value="${imageGallery.instructions}" escapeXml="false"/>
		</div>
		
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert5 type="danger" id="lock-on-finish" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>
		
		<c:if test="${imageGallery.allowRank && (imageGallery.minimumRates ne 0 || imageGallery.maximumRates ne 0)}">
			<lams:Alert5 type="info" id="rating-limits" close="false">
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
			</lams:Alert5>
		</c:if>
		
		<lams:errors/>
		
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
			<lams:NotebookReedit
				reflectInstructions="${sessionMap.reflectInstructions}"
				reflectEntry="${sessionMap.reflectEntry}"
				isEditButtonEnabled="${mode != 'teacher'}"
				notebookHeaderLabelKey="title.reflection"/>
		</c:if>
		
		<%--Bottom buttons-------------------------------------------%>

		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons" id="learner-submit"
				<c:if test="${imageGallery.minimumRates ne 0 && empty sessionMap.currentImage}">style="display:none;"</c:if>
			>
				<c:choose>
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button type="button" name="FinishButton" onclick="return continueReflect()" class="btn btn-primary na">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<button type="button" name="FinishButton" id="finishButton" class="btn btn-primary na" >
							<c:choose>
				 				<c:when test="${sessionMap.isLastActivity}">
				 					<fmt:message key="label.submit" />
				 				</c:when>
				 				<c:otherwise>
				 		 			<fmt:message key="label.finished" />
				 				</c:otherwise>
				 			</c:choose>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>	

	</div>
</lams:PageLearner>
