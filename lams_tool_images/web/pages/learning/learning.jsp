<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
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
	<c:set var="imageGallery" value="${sessionMap.imageGallery}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jquery.jcarousel.css'/>" />
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jquery.jcarousel.skin.css'/>" />
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/galleria.css'/>" >
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jquery.rating.css'/>"/>
	<style media="screen,projection" type="text/css">
		#content{width:1000;}
		body{width:1000;}
		.stripes{width:1000;}
	
		.galleria_container{position:relative;margin-top:2em;}
		.gallery_demo{width:702px;margin:0 auto 0 0;}
		.gallery_demo li{width:100px;height:100px;border:3px double #111;margin: 0 2px;background:#000;}
		.gallery_demo li div{left:240px}
		.gallery_demo li div .caption{font:italic 0.7em/1.4 georgia,serif;}
		
		.caption{position:absolute;top:0px;left:0px;width:150px;font-size:12px;color:#0087e5;}
		#description{position:absolute; top:1000px; left:1000px; width:150px; font-style:italic;}
		.rating_stars{position: absolute; top: 1000px; left: 1000px; margin-top: 10}		
		
		#main_image{margin: 0 auto 20 0; height: 500px; width: 640px;}
		#main_image img{margin-bottom: 10px; border: 1px solid #111;}
		
		.check_for_new{position: relative; top: -123px; left: 660px;}
		.add_new_image{position: relative; top: -90px; left: 660px;}
		
		.after_main_image{text-align: left; margin: 30px 0; padding-top: 30px; clear:both;}
    </style>

	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery-1.2.6.pack.js'/>" ></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.galleria.js'/>" ></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.jcarousel.pack.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.MetaData.js'/>"></script>
 	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.rating.pack.js'/>"></script>
 	
	<script type="text/javascript">
	$(document).ready(function(){

		$('ul.gallery_demo').galleria({
			history   : true, // activates the history object for bookmarking, back-button etc.
			clickNext : true, // helper for making the image clickable
			insert    : '#main_image', // the containing selector for our main image
			onImage   : function(image,caption,thumb) {

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
				$("#description").css('left', image.width() + 20);
				$("#description").css('top', caption.height() + 10);
				$(".rating_stars").css('left', image.width() + 20);
				//adjust .rating_stars top position to description height 
				var newTopPosition = (image.height() > caption.height() + $("#description").height() + 60) 
					? image.height() - 40 
					: caption.height() + $("#description").height() + 20;
				$(".rating_stars").css('top', newTopPosition);
				
				//adjust #main_image height to real image size
				var newHeight = (image.height() >= 480) ? 640 : 480;
			    $('#main_image').css('height', newHeight + 20);


			    showComments(thumb.attr('id'));

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
	});

	<!--
		function checkNew(){
 		    var reqIDVar = new Date();
			document.location.href = "<c:url value="/learning/start.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();
 		    return false;
		}
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}

		//The panel of imageGallery list panel
		var commentsAreaTargetDiv = "#commentsArea";
		function showComments(imageUid){
			var url = "<c:url value="/learning/showComments.do"/>";
			$(commentsAreaTargetDiv).load(
				url,
				{
					imageUid: imageUid, 
					sessionMapID: "${sessionMapID}"
				}
			);
		}
		function addNewComment(currentImageUid, comment){
			var url = "<c:url value="/learning/addNewComment.do"/>";
			var param = "currentImageUid=" + currentImageUid + "&comment=" + comment + "&sessionMapID=${sessionMapID}";

			$(commentsAreaTargetDiv).load(
				url,
				{
					currentImageUid: currentImageUid, 
					comment: comment,
					sessionMapID: "${sessionMapID}"
				}
			);
		}
		
				
	-->        
    </script>
   
</lams:head>
<body class="stripes">


	<div id="content" width="1000" style="width:1000;">
		<h1  width="1000" style="width:1000;">
			${imageGallery.title}
		</h1>

		<p>
			${imageGallery.instructions}
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
		
		
	

		

		<div class="galleria_container">
			<div id="description">
				Description
			</div>		
			<div id="main_image"></div>
			
			<div class="rating_stars">
				<input class="star {required: true}" type="radio" name="test-2-rating-4" value="1" title="" checked="checked"/>
				<input class="star" type="radio" name="test-2-rating-4" value="2" title=""/>
				<input class="star" type="radio" name="test-2-rating-4" value="3" title=""/>
				<input class="star" type="radio" name="test-2-rating-4" value="4" title=""/>
				<input class="star" type="radio" name="test-2-rating-4" value="5" title=""/>
				<br>
				<p>
					15 <fmt:message key="label.learning.ratings" />
				</p>
			</div>			
			
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
				
						<a href="<html:rewrite page='/download/?uuid='/>${image.mediumFileUuid}&preferDownload=false" title="${image.title}" id="${image.uid}">
							<img src="<html:rewrite page='/download/?uuid='/>${image.thumbnailFileUuid}&preferDownload=false" alt="${image.title}" id="${image.uid}">
						</a>
					</li>
				</c:forEach>
			</ul>
			
			<c:if test="${(mode != 'teacher') }"> <!--&& imageGallery.allowShareImages-->
					<a href="#" onclick="return checkNew()" class="button check_for_new"> 
						<fmt:message key="label.check.for.new" /> 
					</a>
			</c:if>
			<c:if test="${mode != 'teacher' && (not finishedLock)}"> <!--&& imageGallery.allowShareImages-->
				<br>
					<a href="#" onclick="return checkNew()" class="button add_new_image"> 
						<fmt:message key="label.learning.add.new.image" />
					</a>
			</c:if>		
			
		</div>
		<div class="after_main_image"></div>

	


	
		
		





		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
			
		
		


		
		
		
 		
<div id="commentsArea">
	<%@ include file="/pages/learning/parts/commentsarea.jsp"%> 
</div>
 
		
		
		


		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>
					${sessionMap.reflectInstructions}
				</h2>

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
					<html:button property="FinishButton"
						onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right" >
				<c:choose>
					<c:when
						test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton"
							onclick="return continueReflect()" styleClass="button" >
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:button property="FinishButton" styleId="finishButton"
							onclick="return finishSession()" styleClass="button" >
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

