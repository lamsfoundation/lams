<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>

<lams:head>
	<title><fmt:message key="learner.title" />
	</title>

	<lams:css />
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript"
		src="${lams}includes/javascript/common.js"></script>
</lams:head>

<body class="stripes">
	<c:set var="displayPrintButton"><lams:Configuration key="DisplayPrintButton"/></c:set>
	<c:set var="lastName"><lams:user property="lastName"/></c:set>
	<c:set var="firstName"><lams:user property="firstName"/></c:set>
	
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet" />
	<style type="text/css">
		.grid-holder .ui-jqgrid {
		    margin-left: auto;
		    margin-right: auto;
		}
		
		h3, h4 {
			text-align: center;
		}
	</style> 
	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		function restartLesson(){
			if (confirm('<fmt:message key="message.learner.progress.restart.confirm"/>')) {
				window.location.href = "<lams:WebAppURL/>learner/restartLesson.do?lessonID=${lessonID}";
			}
		}
		
		// submit lesson total mark to the integrated server in case request comes from an integrated server
		if (${not empty lessonFinishUrl}) {
			$.ajax({ 
			    url: "${lessonFinishUrl}",
			    type: "POST",
			    dataType: 'html',
				cache: false,
				async: 'false',
			    success: function (data) {
			    	//log mark has been successfullly pushed to the integrated server
			    	if (data && ((data == 'OK') || (data == 'No Lineitem object found'))) {
			    		$.ajax({ 
			    		    url: "<lams:WebAppURL/>logLessonMarkPushedToIntegrations",
			    		    data: {lessonID: "${lessonID}"},
			    		    type: "POST",
			    			cache: false
			    		});		    		
			    	}
			    },
			    error: function (ajaxContext) {
			        alert("There was an error on trying to submit lesson total mark to the integrated server: " + ajaxContext.responseText)
			    }
			});
		}
	
		<c:if test="${gradebookOnComplete}">
			$(document).ready(function(){
				// Create the organisation view grid with sub grid for users	
				$("#userGradebookGrid").jqGrid({
				    datatype		   : "json",
				    url				   : "<lams:LAMSURL />gradebook/gradebook/getLessonCompleteGridData.do?lessonID=${lessonID}",
				    height			   : "100%",
				    // use new theme
				    guiStyle 		   : "bootstrap",
				    iconSet 		   : 'fontAwesome',
				    autoencode		   : false,
					colNames : [
						'<fmt:message key="gradebook.columntitle.activity"/>',
						'<fmt:message key="gradebook.columntitle.progress"/>',
						'<fmt:message key="gradebook.columntitle.averageMark"/>',
						'<fmt:message key="gradebook.columntitle.mark"/>'
					],
				    colModel : [
				        {
						   'name'  : 'name', 
						   'index' : 'name',
						   'title' : false
						},
				        {
						   'name'  : 'progress', 
						   'index' : 'progress',
						   'align' : 'center',
						   'width' : 100,
						   'title' : false
					    },
				        {
						   'name'  : 'averageMark', 
						   'index' : 'averageMark',
						   'align' : 'center',
						   'width' : 100,
						   'title' : false
				        },
				        {
						   'name'  : 'mark', 
						   'index' : 'mark',
						   'align' : 'center',
						   'width' : 100,
						   'title' : false
						}
				    ],
				    beforeProcessing : function(data) {
					    // Wrap each activity name in a link to show same pop up
					    // as if the user clicked the activity in progress bar.
					    // The code depends on progressBar.js import in Page.tag
					    $.each(data.rows, function() {
						    this.cell[0] = '<a href="#" onClick="javascript:openActivity(\'' 
							    			+ data.urls[this.id] + '\')">' + this.cell[0] + '</a>';
						});
					},
				    loadComplete : function(data) {
					    // display non-grid data
				    	$('#learnerLessonMark').text(data.learnerLessonMark);
					    $('#averageLessonMark').text(data.averageLessonMark);
					},
				    loadError : function(xhr,st,err) {
				    	$("#userGradebookGrid").clearGridData();
				    	$.jgrid.info_dialog('<fmt:message key="error.title"/>', 
		    					'<fmt:message key="message.lesson.finished.report.load.error"/>',
		    					'<fmt:message key="message.lesson.finished.ok"/>');
				    }
				});
			});
		</c:if>
	
	</script>
	
	<lams:Page type="learner">
		
		<div class="lead" id="lessonFinished"><i class="fa fa-lg fa-check-square-o text-success"></i>&nbsp;
			<fmt:message key="message.lesson.finished">
				<fmt:param>
					<strong><c:out value="${firstName}" escapeXml="true"/>&nbsp;<c:out value="${lastName}" escapeXml="true"/></strong>
				</fmt:param>
			</fmt:message>
		</div>
		
		<div class="voffset10">
			<fmt:message key="message.lesson.finishedCont">
				<fmt:param>
					<strong><c:out value="${learnerprogress.lesson.lessonName}" escapeXml="true"/></strong>
				</fmt:param>
				<fmt:param>
					 <lams:Date value="${learnerprogress.finishDate}" style="short"/>
				</fmt:param>			
			</fmt:message>
		</div>
		
		<c:if test="${learnerprogress.lesson.allowLearnerRestart}">
			<div class="voffset10">
				<fmt:message key="message.lesson.restart" />
				<a class="btn btn-default loffset20" href="#" onClick="javascript:restartLesson()">
					<fmt:message key="message.lesson.restart.button" />
				</a>
			</div>
		</c:if>
		
		<c:if test="${not empty releasedLessons}">
			<div class="voffset10">
				<fmt:message key="message.released.lessons">
					<fmt:param>
						<c:out value="${releasedLessons}" escapeXml="true"/> 
					</fmt:param>		
				</fmt:message>
			</div>
		</c:if>
		
		<c:if test="${displayPrintButton}">
			<div class="pull-right voffset10">
				<a href="#" class="btn btn-default" onclick="JavaScript:window.print();">
					<fmt:message key="label.print" />
				</a>	
			</div>
		</c:if>
		
		<c:if test="${gradebookOnComplete}">
			<div class="lead voffset20"><i class="fa fa-lg fa-list-ol text-success"></i>
			&nbsp;<fmt:message key="gradebook.lesson.complete" /></div>
			
			<h3><fmt:message key="gradebook.learner.lesson.mark" />: <span id="learnerLessonMark"></span></h3>
			<h4><fmt:message key="gradebook.columntitle.averageMark" />: <span id="averageLessonMark"></span></h4>
			
			<div class="grid-holder voffset20">
				<table id="userGradebookGrid"></table>
			</div>
		</c:if>
	</lams:Page>
</body>

</lams:html>
