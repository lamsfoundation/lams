<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	<lams:css/>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>	
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
</lams:head>

<body class="stripes">
	
	<c:set var="title"><c:out value="${lesson.lessonName}" escapeXml="true"/></c:set>
	<lams:Page type="learner" title="${title}">
		
		<p><c:out value="${lesson.lessonDescription}" escapeXml="false"/></p>	
	
		<c:if test="${displayDesignImage}">
			<div id="sequence-preview" class="voffset10 text-center">
				<i id="ldScreenshotLoading" class="fa fa-refresh fa-spin fa-2x fa-fw"></i>
    			<div id="ldCannotLoadSVG" style="display:none" ><fmt:message key="error.cannot.load.thumbnail" /></div>
    			<div id="ldSVG" style="display:none" ></div>
			</div>
		</c:if>
			
		<div class="voffset10 pull-right">
			<a href="${lams}home/learner.do?lessonID=${lesson.lessonId}&isLessonIntroWatched=true" class="btn btn-primary na">
				<span class="nextActivity"><fmt:message key="label.start.lesson" /></span>
			</a>
		</div>
	
		<div id="footer"></div>
	
	</lams:Page>

</BODY>
	
</lams:html>
