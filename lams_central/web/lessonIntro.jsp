<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<c:set var="title"><c:out value="${lesson.lessonName}" escapeXml="true"/></c:set>
<lams:PageLearner title="${title}" toolSessionID="" hideHeader="true">
	<c:if test="${displayDesignImage}">
		<script>
		var originalThumbnailWidth = 0,
			originalThumbnailHeight = 0;

		$(document).ready(function(){
			$.ajax({
				dataType : 'text',
				url : '<lams:LAMSURL/>home/getLearningDesignThumbnail.do',
				async : false,
				cache : false,
				data : {
					'ldId'      : '${learningDesignID}',
					'_t'		: new Date().getTime()
				},
				success : function(response) {
					$('#ldScreenshotLoading').css('display', 'none');
					$('#ldSVG').html(response);
					$('#ldSVG').css('display', 'block');
					
					var svg = $('svg','#ldSVG');
					if ( svg ) {
						originalThumbnailWidth = svg.attr('width'),
						originalThumbnailHeight = svg.attr('height');
						resizeSVG();
					}
				},
				error : function(error) {
					$('#ldScreenshotLoading').css('display', 'none');
					$('#ldCannotLoadSVG').css('display', 'block');
				}
			});
			
			$( window ).resize(function() {
				resizeSVG();
			});
			
		});
		
		function resizeSVG() {
			var svg = $('#ldSVG svg.svg-learning-design');
			if ( svg ) {
				var panelWidth = $('.panel-learner-page').width(),
					svgWidth = svg.attr('width'),
					svgHeight = svg.attr('height');
		
				if ( originalThumbnailWidth > panelWidth ) {
					var newWidth = panelWidth > 100 ? panelWidth - 100 : panelWidth;
					svg.attr('width', newWidth);
					svg.attr('height', Math.ceil(originalThumbnailHeight * (newWidth / originalThumbnailWidth)));
				} else {
					svg.attr('width', originalThumbnailWidth);
					svg.attr('height', originalThumbnailHeight);
				}
			}
		}
		</script>
	</c:if>	

	<div id="container-main">
		<div id="instructions" class="instructions">
			<c:out value="${lesson.lessonDescription}" escapeXml="false"/>
		</div>	
	
		<c:if test="${displayDesignImage}">
			<div id="sequence-preview" class="mt-2 text-center">
				<i id="ldScreenshotLoading" class="fa fa-refresh fa-spin fa-2x fa-fw"></i>
    			<div id="ldCannotLoadSVG" style="display:none" ><fmt:message key="error.cannot.load.thumbnail" /></div>
    			<div id="ldSVG" style="display:none" ></div>
			</div>
		</c:if>
			
		<div class="activity-bottom-buttons">
			<a href="${lams}home/learner.do?lessonID=${lesson.lessonId}&isLessonIntroWatched=true" class="btn btn-primary na">
				<fmt:message key="label.start.lesson" />
			</a>
		</div>
	</div>
</lams:PageLearner>
