<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title>
		<fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/headerWithoutPrototype.jsp"%>

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
	<c:set var="thumbnailImageDimensions" value="${sessionMap.thumbnailImageDimensions}" />
	<c:set var="windowMinimumWidth" value="${thumbnailImageDimensions*5 + 160}" />
	<c:if test="${imageGallery.allowShareImages && (mode != 'teacher')}">
		<c:set var="windowMinimumWidth" value="${windowMinimumWidth + 120}" />
	</c:if>
	<c:if test="${(mediumImageDimensions + 200) > windowMinimumWidth}">
		<c:set var="windowMinimumWidth" value="${mediumImageDimensions + 200}" />
	</c:if>

	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jquery.jcarousel.css'/>" />
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jquery.jcarousel.skin.css' />" />
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/galleria.css'/>" >
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jquery.rating.css'/>"/>
	<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
	<style media="screen,projection" type="text/css">
		.galleria_container{position:relative;margin-top:2em;}
		.gallery_demo{width:${mediumImageDimensions + 62}px;margin:0 auto 0 0;}
		.gallery_demo li{width:${thumbnailImageDimensions}px;height:${thumbnailImageDimensions}px;border:3px double #111;margin: 0 2px;background:#000;}
		.gallery_demo li div{left:240px}
		.gallery_demo li div .caption{font:italic 0.7em/1.4 georgia,serif;}
		
		.caption{position:absolute;top:0px;left:0px;width:150px;font-size:12px;color:#0087e5;}
		#description{position:absolute; top:1000px; left:1000px; width:150px; font-style:italic;}
		#openOriginalSizeLink{position:absolute; top:1000px; left:1000px;}
		#delete_button{position:absolute; top:10px; left:1000px; z-index: 1000;opacity:0.4;filter:alpha(opacity=40); display:none;}
		#delete_button:hover {opacity:1;filter:alpha(opacity=100)}
		#rating_stars{position: absolute; top: 1000px; left: 1000px; width:150px; margin-top: 10}		
		
		#main_image{margin: 0 auto 20 0; height: ${(mediumImageDimensions*3)/4 + 40}px; width: ${mediumImageDimensions}px;}
		#main_image img{margin-bottom: 10px; border: 1px solid #111;}
		
		.check_for_new{position: relative; top: ${-thumbnailImageDimensions - 23}px; left: ${thumbnailImageDimensions*5 + 160}px;}
		.add_new_image{position: relative; top: ${-thumbnailImageDimensions + 10}px; left: ${thumbnailImageDimensions*5 + 160}px;}
		
		.after_main_image{text-align: left; margin: 30px 0; padding-top: 30px; clear:both;}
		
		/* parameters borrowed from jquery.jcarousel.skin.css */
		.jcarousel-skin-tango .jcarousel-container-horizontal {
    		width: ${thumbnailImageDimensions*5 + 75}px;
    	}
    	.jcarousel-skin-tango .jcarousel-clip-horizontal {
		    width:  ${thumbnailImageDimensions*5 + 75}px;
		    height: ${thumbnailImageDimensions + 8}px;
		}
		.jcarousel-skin-tango .jcarousel-item {
		    width: ${thumbnailImageDimensions}px;
		    height: ${thumbnailImageDimensions}px;
		}
		.jcarousel-skin-tango .jcarousel-next-horizontal {
	    	top: ${thumbnailImageDimensions/2}px;
	    }	
		.jcarousel-skin-tango .jcarousel-prev-horizontal {
    		top: ${thumbnailImageDimensions/2}px;
    	}	    
    </style>
    
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>   
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.galleria.js'/>" ></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.jcarousel.pack.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.MetaData.js'/>"></script>
 	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.rating.1.1.js'/>"></script>
 	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
 	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/thickbox.js'/>"></script>	
 	
	<script type="text/javascript">
	
	$(document).ready(function(){ 
		$('ul.gallery_demo').galleria({
			history   : true, // activates the history object for bookmarking, back-button etc.
			clickNext : ${fn:length(sessionMap.imageGalleryList) > 1}, // sets helper for making the image clickable only if there is more than 1 image
			insert    : '#main_image', // the containing selector for our main image
			onImage   : function(image,caption,thumb) {
				
				//do further calculations only after picture is loaded so we know its width and height
				image.load(function (){
					// fetch the thumbnail container
					var _li = thumb.parents('li');
					// fade out inactive thumbnail
					_li.siblings().children('img.selected').fadeTo(500,0.3);
					// fade in active thumbnail
					thumb.fadeTo('fast',1).addClass('selected');
					// add a title for the clickable image
					image.attr('title','Next image >>');

					//positioning image title, description and rating
					caption.css('left',image.width() + 20);
					$('#description').html("");
					$("#description").css('left', image.width() + 20);
					$("#description").css('top', caption.height() + 10);

					var linkLeftPosition = ($("#openOriginalSizeLink_size").width() >= image.width()) ? 0 : ((image.width() - $("#openOriginalSizeLink_size").width())/2);
					$("#openOriginalSizeLink").css('left', linkLeftPosition);
					$("#openOriginalSizeLink").css('top', image.height() + 10);
					$("#openOriginalSizeLink").width(image.width());
					
					$("#delete_button").hide();
					$("#delete_button").css('left', image.width() - 30);
							
					setStarRatingChecked(0);
					$("#rating_stars").css('left', image.width() + 20);					
					$("#rating_stars").css('top', image.height() - 40);
					
					//adjust #main_image height to real image size
					var newHeight = (image.height() >= ${(mediumImageDimensions*3)/4}) ? ${mediumImageDimensions} : ${(mediumImageDimensions*3)/4};
				    $('#main_image').css('height', newHeight + 40);

				    loadImageData(thumb.attr('id'));
				});

			},
			onThumb : function(thumb) { // thumbnail effects goes here
				// fetch the thumbnail container
				var _li = thumb.parents('li');
				// if thumbnail is active, fade all the way.
				var _fadeTo = _li.is('.active') ? '1' : '0.3';
				// fade in the thumbnail when finnished loading
				thumb.css({display:'none',opacity:_fadeTo}).fadeIn(1500);
				// hover effects
				thumb.hover(
					function() { thumb.fadeTo('fast',1); },
					function() { _li.not('.active').children('img').fadeTo('fast',0.3); } // don't fade out if the parent is active
				)
			}
		});
		
		$('#mycarousel').jcarousel();

		$('#votingForm_vote').click(function() {
	    	var options = { 
	    		success: afterRatingSubmit  // post-submit callback
	        }; 				
		    				
			$('#votingForm').ajaxSubmit(options);
			return false;
		});		

		//to prevent scroll bars overlapping
		//var nav = navigator.appName;
		//if (nav == "Microsoft Internet Explorer") {
			//alert(parent.document.getElementById("contentFrame").width);
			//parent.document.getElementById("contentFrame").width = "2000px";
		//}

	});

	<!--
		function checkNew(){
			document.location.href = "<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}";
 		    return false;
		}
		function finishSession(){
			$('#finishButton').attr('disabled', true);
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function deleteImage(){
			var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");
			
			if (deletionConfirmed) {
				var imageUid = $('#commentsArea_imageUid').val();
				document.location.href = "<c:url value="/learning/deleteImage.do"/>?sessionMapID=${sessionMapID}&imageUid=" + imageUid;
			}
			return false;
		}

		function addNewComment(currentImageUid, comment){
			var url = "<c:url value="/learning/addNewComment.do"/>";
			$(commentsAreaTargetDiv).load(
				url,
				{
					currentImageUid: currentImageUid, 
					comment: comment,
					sessionMapID: "${sessionMapID}"
				}
			);
		}

		//The panel of imageGallery list panel
		var commentsAreaTargetDiv = "#commentsArea";
		function loadImageData(imageUid){
			var url = "<c:url value="/learning/loadImageData.do"/>";
			$(commentsAreaTargetDiv).load(
				url,
				{
					imageUid: imageUid, 
					sessionMapID: "${sessionMapID}"
				},
				afterImageDataLoaded
			);
		}

		function afterImageDataLoaded() {
			var description = $('#commentsArea_addedBy').val() + $('#commentsArea_description').val();
			$('#description').html(description);

			var title = $('#commentsArea_title').val();
			$("#openOriginalSizeLink a").attr('title', title);
			
			var href = "<html:rewrite page='/download/?preferDownload=false'/>";
			href = href + "&uuid=" + $('#commentsArea_originalFileUuid').val();
			href = href + "&dbWidth=" + $('#commentsArea_originalImageWidth').val();
			href = href + "&dbHeight=" + $('#commentsArea_originalImageHeight').val();
			$("#openOriginalSizeLink a").attr('href', href);
			
			var imageUid = $('#commentsArea_imageUid').val();
			if (${imageGallery.allowRank && (mode != 'teacher')}) {
				//adjust #rating_stars top position to description height 
				if ($('#main_image img').height() < $('span.caption').height() + $("#description").height() + 60) {
					$("#rating_stars").css('top', $('span.caption').height() + $("#description").height() + 20);	
				}

				var numberRatings = $('#commentsArea_numberRatings').val();
				$('#numberRatings').html(numberRatings);
				
				var currentRating = $('#commentsArea_currentRating').val();
				setStarRatingChecked(currentRating);

				var title = "<fmt:message key='label.learning.average.rating'/> " + $('#commentsArea_averageRating').val();
				$('.star a').attr('title', title);

				$('#ratingForm_imageUid').attr('value', imageUid);
			}

			if (${imageGallery.allowVote && (mode != 'teacher') && (not finishedLock)}) {	
				var isVoted = $('#commentsArea_isVoted').val();
				var votingFormLabel;
				if (isVoted == "true") {

					$('#votingForm_vote').attr('disabled', false);
					$('#votingForm_vote').attr('checked', true);
					votingFormLabel = "<fmt:message key='label.learning.unvote'/>";					
					 
				} else {
					
					$('#votingForm_vote').attr('disabled', false);
					$('#votingForm_vote').attr('checked', false);
					votingFormLabel = "<fmt:message key='label.learning.vote.here'/>";
										
				}
				$('#votingForm_label').text(votingFormLabel);
				$('#votingForm_imageUid').attr('value', imageUid);
			} else if (${finishedLock}) {
				var isVoted = $('#commentsArea_isVoted').val();
				if (isVoted == "true") {
					$('#votingForm_vote').attr('checked', true);
				} else {
					$('#votingForm_vote').attr('checked', false);
				}
			}
			
			//set visibility of "Delete image" button 
			var isAuthor = $('#commentsArea_isAuthor').val();
			if (isAuthor == "true") {
				$("#delete_button").show();
			}
			
		}
		function setStarRatingChecked(currentRating){
			$('#rating_stars_inputs').empty();
			for (var i = 1; i <= 5; i = i + 1) {
				var checked = (currentRating == i) ? "checked = 'checked' " : "";
				$('#rating_stars_inputs').append('<input class="star" type="radio" name="rating" value="' + i + '"' + checked + '/>');
			}
			$('input[type=radio].star').rating({
				readOnly: ${finishedLock},
				callback: function() {
			    	var options = { 
			    		success: afterRatingSubmit  // post-submit callback
			        }; 				
			    				
					$('#ratingForm').ajaxSubmit(options);
					return false;
				}
			});
		}		

		// post-submit callback 
		function afterRatingSubmit(responseText, statusText)  { 
			var imageUid = $('#commentsArea_imageUid').val();
			loadImageData(imageUid);
		} 
	-->        
    </script>
   
</lams:head>
<body class="stripes">
	<div id="content" style="min-width: ${windowMinimumWidth}px;">

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
		<%@ include file="/common/messages.jsp"%>
		
		<%--Main image---------------------------------------------------%>
		
		<div class="galleria_container">
			<div id="description"></div>
					
			<div id="main_image"></div>
			
			<div id="openOriginalSizeLink">
				<a href="" title="Enlarge" class="thickbox" >
					<fmt:message key="label.learning.open.original.size" />
				</a>
			</div>
			
			<%--Delete button---------------------------------------%>

			<div id="delete_button">
				<img src="<lams:WebAppURL />includes/images/delete.png"
					title="<fmt:message key="label.learning.delete.image" />"
					onclick="return deleteImage();" />
			</div>
			
			<%--Ranking/Voting area---------------------------------------%>

			<c:if test="${(mode != 'teacher') && (not empty sessionMap.imageGalleryList)}">				
				<div id="rating_stars">
					<c:if test="${imageGallery.allowRank}">
						<html:form action="learning/saveOrUpdateRating" method="post" styleId="ratingForm">
							<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
							<input type="hidden" name="imageUid" id="ratingForm_imageUid"/>
							
							<div id="rating_stars_inputs"> 
								<input class="star" type="radio" name="rating" value="1" />
								<input class="star" type="radio" name="rating" value="2" />
								<input class="star" type="radio" name="rating" value="3" />
								<input class="star" type="radio" name="rating" value="4" />
								<input class="star" type="radio" name="rating" value="5" />						
							</div>
							
							<br>
							<p>
								<span id="numberRatings"></span>
								<fmt:message key="label.learning.ratings" />
							</p>						
						</html:form>							
					</c:if>
					
					<c:if test="${imageGallery.allowVote && (mode != 'teacher')}">
						<html:form action="learning/vote" method="post" styleId="votingForm">
							<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
							<input type="hidden" name="imageUid" id="votingForm_imageUid"/>
							
							<p class="small-space-top">
								<html:checkbox property="vote" styleClass="noBorder" styleId="votingForm_vote" >	</html:checkbox>
	
								<label for="votingForm_vote" id="votingForm_label"></label>
							</p>
							
						</html:form>							
					</c:if> 			
				</div>
			</c:if>		
			
			<%--Image scroller-------------------------------------------------%>			
			
			<ul class="gallery_demo jcarousel-skin-tango" id="mycarousel">
				<c:forEach var="image" items="${sessionMap.imageGalleryList}" varStatus="status">
					<c:choose>
						<c:when test="${status.index == 0}">
							<li class="active">
						</c:when>
						<c:otherwise>
							<li>
						</c:otherwise>
					</c:choose>
					<c:set var="title">
						<c:out value="${image.title}" escapeXml="true"/>	
				        </c:set>
						<a href="<html:rewrite page='/download/?uuid='/>${image.mediumFileUuid}&preferDownload=false" title="${title}" id="${image.uid}" width="${image.mediumImageWidth}" height="${image.mediumImageHeight}">
							<img src="<html:rewrite page='/download/?uuid='/>${image.thumbnailFileUuid}&preferDownload=false" alt="${title}" id="${image.uid}">
						</a>
					</li>
				</c:forEach>
			</ul>
			
			<%--"Check for new" and "Add new image" buttons---------------%>
			
			<c:if test="${imageGallery.allowShareImages && (mode != 'teacher')}">
				<a href="#nogo" onclick="return checkNew()" class="button check_for_new"> 
					<fmt:message key="label.check.for.new" /> 
				</a>
			</c:if>
			<c:if test="${imageGallery.allowShareImages && (mode != 'teacher') && (not finishedLock)}">
				<br>
				<a href="<html:rewrite page='/learning/newImageInit.do?sessionMapID='/>${sessionMapID}&KeepThis=true&TB_iframe=true&height=540&width=480&modal=true" class="button add_new_image thickbox">  
					<fmt:message key="label.learning.add.new.image" />
				</a>
			</c:if>		
			
		</div>
		<div class="after_main_image"></div>
		
		<%--Comments area----------------------------------------------%>	
 		
		<div id="commentsArea">
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
			<div class="space-bottom-top align-right" >
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
		
		<div id="openOriginalSizeLink_size" style="display:none;" >
			<fmt:message key="label.learning.open.original.size" />
		</div>		

	</div>
	<%--closes content--%>

	<div id="footer">
	</div>
	<%--closes footer--%>

</body>
</lams:html>
