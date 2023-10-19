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
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="displayPrintButton"><lams:Configuration key="DisplayPrintButton"/></c:set>
<c:set var="lastName"><lams:user property="lastName"/></c:set>
<c:set var="firstName"><lams:user property="firstName"/></c:set>
<c:set var="title"><fmt:message key="gradebook.lesson.complete" /></c:set>

<lams:PageLearner toolSessionID="" lessonID="${lessonID}">
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.custom.css" rel="stylesheet">
	<style type="text/css">
		.grid-holder .ui-jqgrid {
		    margin-left: auto;
		    margin-right: auto;
		}
	</style> 

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		function restartLesson(){
			if (confirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.learner.progress.restart.confirm"/></spring:escapeBody>')) {
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
				    width			   : 520,
				    guiStyle		   : "bootstrap4",
					iconSet			   : 'fontAwesomeSolid',
				    autoencode		   : false,
					colNames : [
						'<spring:escapeBody javaScriptEscape="true"><fmt:message key="gradebook.columntitle.activity"/></spring:escapeBody>',
						'<spring:escapeBody javaScriptEscape="true"><fmt:message key="gradebook.columntitle.progress"/></spring:escapeBody>',
						'<spring:escapeBody javaScriptEscape="true"><fmt:message key="gradebook.columntitle.averageMark"/></spring:escapeBody>',
						'<spring:escapeBody javaScriptEscape="true"><fmt:message key="gradebook.columntitle.mark"/></spring:escapeBody>'
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
						    this.cell[0] = '<a target="_blank" title="Open completed activity" href="' 
							    			+ data.urls[this.id] + '">' + this.cell[0] + '</a>';
						});
					},
				    loadComplete : function(data) {
					    // display non-grid data
				    	$('#learnerLessonMark').text(data.learnerLessonMark);
					    $('#averageLessonMark').text(data.averageLessonMark);
					},
				    loadError : function(xhr,st,err) {
				    	$("#userGradebookGrid").clearGridData();
				    	$.jgrid.info_dialog('<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.title"/></spring:escapeBody>', 
		    					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.lesson.finished.report.load.error"/></spring:escapeBody>',
		    					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.lesson.finished.ok"/></spring:escapeBody>');
				    }
				});

		        $(window).bind('resize', function() {
		            resizeJqgrid($(".ui-jqgrid-btable:visible"));
		        });
		        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
			});

			function resizeJqgrid(jqgrids) {
			    jqgrids.each(function(index) {
			        var gridId = $(this).attr('id');
			        var parent = jQuery('#gbox_' + gridId).parent();
			        var gridParentWidth = parent.width();
			        if ( parent.hasClass('grid-holder') ) {
			            	gridParentWidth = gridParentWidth - 2;
			        }
			        jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
			    });
			}
		</c:if>
	</script>
	
	<div id="container-main">
		
		<div class="lead mb-2" id="lessonFinished">
			<i class="fa-regular fa-lg fa-circle-check text-success"></i>&nbsp;
			<fmt:message key="message.lesson.finished">
				<fmt:param>
					<strong><c:out value="${firstName}" escapeXml="true"/>&nbsp;<c:out value="${lastName}" escapeXml="true"/></strong>
				</fmt:param>
			</fmt:message>
		</div>
		
		<div>
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
			<div class="mt-2">
				<fmt:message key="message.lesson.restart" />
				<button type="button" class="btn btn-secondary ms-3" onClick="javascript:restartLesson()">
					<fmt:message key="message.lesson.restart.button" />
				</button>
			</div>
		</c:if>
		
		<c:if test="${not empty releasedLessons}">
			<div class="mt-2">
				<fmt:message key="message.released.lessons">
					<fmt:param>
						<c:out value="${releasedLessons}" escapeXml="true"/> 
					</fmt:param>		
				</fmt:message>
			</div>
		</c:if>
		
		<c:if test="${displayPrintButton}">
			<div class="float-end mt-2">
				<button type="button" class="btn btn-secondary" onclick="JavaScript:window.print();">
					<fmt:message key="label.print" />
				</button>	
			</div>
		</c:if>
		
		<c:if test="${gradebookOnComplete}">
			<br>
			<lams:Alert5 type="success" isIconDisplayed="false">
				<div class="text-center fs-5 fw-semibold">
					<fmt:message key="gradebook.lesson.complete" />
				</div>
				<div class="text-center">
					<fmt:message key="gradebook.learner.lesson.mark" />: <span id="learnerLessonMark"></span><br>
					<fmt:message key="gradebook.columntitle.averageMark" />: <span id="averageLessonMark"></span>
				</div>
				
				<div class="grid-holder mt-3">
					<table id="userGradebookGrid" class=""></table>
				</div>
			</lams:Alert5>
		</c:if>
	</div>
</lams:PageLearner>
