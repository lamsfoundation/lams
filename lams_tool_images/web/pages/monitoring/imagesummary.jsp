<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.monitoring.title" />
		</title>
		
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="imageGallery" value="${sessionMap.imageGallery}" />		
		
		<%@ include file="/common/header.jsp"%>
		<link href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
		
		<script type="text/javascript">
			//var for jquery.jRating.js
			var pathToImageFolder = "${lams}images/css/";
		
			//vars for rating.js
			var AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
			YOUR_RATING_LABEL = '',
			IS_DISABLED =  true,
			COMMENTS_MIN_WORDS_LIMIT = 0,
			MAX_RATES = 0,
			MIN_RATES = 0,
			LAMS_URL = '${lams}',
			COUNT_RATED_ITEMS = 0,
			COMMENT_TEXTAREA_TIP_LABEL = '',
			WARN_COMMENTS_IS_BLANK_LABEL = '',
			WARN_MIN_NUMBER_WORDS_LABEL = '';
		</script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	</lams:head>
	
<body class="stripes">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.monitoring.imagesummary.image" />
			</div>
		</div>
			
		<div class="panel-body">

			<html:form action="/monitoring/updateImage" method="post" styleId="imageGalleryItemForm">
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<html:hidden property="imageUid" />		
				<html:hidden property="sessionMapID" value="${sessionMapID}"/>
				
				<c:set var="mediumImagePath">
			   		<html:rewrite page='/download/?uuid='/>${image.mediumFileUuid}&preferDownload=false
				</c:set>	
				<c:set var="title">
					<c:out value="${image.title}" escapeXml="true"/>
				</c:set>
				<img src="${mediumImagePath}" alt="${title}" title="${title}"/>
					
				<div class="form-group voffset10">
				    <label for="file-title">
				    	<fmt:message key="label.authoring.basic.resource.title.input"/>
				    </label>
				    <html:text property="title" styleClass="form-control" styleId="file-title" tabindex="1"/>
				</div>
				
				<div class="form-group">
					<label for="description">
						<fmt:message key="label.authoring.basic.resource.description.input" />
					</label>
		            	
					<lams:CKEditor id="description" value="${formBean.description}" width="99%" 
						contentFolderID="${sessionMap.imageGalleryForm.contentFolderID}" />
				</div>
			</html:form>			
			<br>
			
			<c:if test="${imageGallery.allowRank}">
				<lams:Rating itemRatingDto="${itemRatingDto}" disabled="true"
						isItemAuthoredByUser="true"
						maxRates="0" 
						countRatedItems="0" />
			</c:if>
			
			<c:if test="${imageGallery.allowVote}">
			
				<c:forEach var="groupSummary" items="${imageSummary}">
					<h4>
						<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary[0].sessionName}	
					</h4>
				
					<div>
						<fmt:message key="label.monitoring.number.votes" />: ${groupSummary[0].numberOfVotesForImage}
					</div>
				
					<table class="table" >
				
						<tr>
							<th width="150px">
								<fmt:message key="label.monitoring.imagesummary.user" />
							</th>			
							<th style="padding-left:0px; text-align:center; width: 100px;">
								<fmt:message key="label.monitoring.imagesummary.voted.for.this.image" />
							</th>
						</tr>
				
						<c:forEach var="userImageContribution" items="${groupSummary}">
						
							<tr>
								<td>
									<c:out value="${userImageContribution.user.firstName} ${userImageContribution.user.lastName}" escapeXml="true"/>
								</td>
													
								<td style="padding-left:0px; text-align:center;">
									<c:choose>
										<c:when test="${userImageContribution.votedForThisImage}">
											<i class="fa fa-check text-success"></i>
										</c:when>
											
										<c:otherwise>
											<i class="fa fa-minus"></i>
										</c:otherwise>
									</c:choose>
								</td>
								
							</tr>
						</c:forEach>
					</table>
				
				</c:forEach>
			</c:if>
			<br>
			
			<div class="voffset10 pull-right">
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-default roffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo" onclick="javascript:document.imageGalleryItemForm.submit();" class="btn btn-default">
					<fmt:message key="label.monitoring.imagesummary.save" /> 
				</a>
			</div>
		</div>
	
		<div id="footer">
		</div>
		<!--closes footer-->
		
	</div>
		
</body>
</lams:html>
