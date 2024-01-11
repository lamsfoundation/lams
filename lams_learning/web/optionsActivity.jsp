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
<%
	if (request.getAttribute("activity") instanceof org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity) {
		request.setAttribute("isOptionsWithSequencesActivity", "true");
	}
%>

<lams:PageLearner title="${optionsActivityForm.title}" toolSessionID="" lessonID="${optionsActivityForm.lessonID}">	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '', ${optionsActivityForm.activityID}, finishActivity);
		
		function finishActivity() {
			document.getElementById('messageForm').submit();
		}

		$(document).ready(function(){
			$('#confirmationModal').on('show.bs.modal', function (event) {
				var button = $(event.relatedTarget) 
				var activityId = button.data('activity-id');
				let activityName = $("#activity-name-" + activityId).text().trim();
		
				var modal = $(this);
				let modalBodyText = '<fmt:message key="label.confirm.branch.selection.body"/>'.replace("{0}", activityName);
				modal.find('.modal-body').html(modalBodyText);
				modal.find('#submitter').click(function(){
					$(this).parent().children('button').each(function(){
						$(this).prop('disabled', true).html($(this).data('loadingText'));
					});
					
					$("#activity-id").val(activityId);
					document.getElementById('activityForm').submit();
				});
			})
		})
	</script>

	<div id="container-main">
		<c:if test="${optionsActivityForm.minimum != 0 && !optionsActivityForm.minimumLimitReached}">
			<lams:Alert5 id="min-liimt" close="false" type="danger">	
				<c:choose>
					<c:when test="${isOptionsWithSequencesActivity}">
						<fmt:message key="message.activity.set.options.activityCount">
							<fmt:param value="${optionsActivityForm.minimum}" />
						</fmt:message>
					</c:when>
					<c:otherwise>
						<fmt:message key="message.activity.options.activityCount">
							<fmt:param value="${optionsActivityForm.minimum}" />
						</fmt:message>
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>
								
		<c:if test="${optionsActivityForm.maximum != 0}">
			<lams:Alert5 id="max-limit" type="danger" close="false">
				<c:choose>
					<c:when test="${isOptionsWithSequencesActivity}">
						<fmt:message key="message.activity.set.options.note.maximum">
							<fmt:param value="${optionsActivityForm.maximum}" />
						</fmt:message>
					</c:when>
					<c:otherwise>
						<fmt:message key="message.activity.options.note.maximum">
							<fmt:param value="${optionsActivityForm.maximum}" />
						</fmt:message>
					</c:otherwise>
				</c:choose>
			
				<c:if test="${optionsActivityForm.maxActivitiesReached}">
					<c:choose>
						<c:when test="${isOptionsWithSequencesActivity}">
							<fmt:message key="label.optional.maxSequencesReached" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.optional.maxActivitiesReached" />
						</c:otherwise>
					</c:choose>
				</c:if>
			</lams:Alert5>
		</c:if>
		
		<c:if test="${optionsActivityForm.hasCompletedActivities}">
			<lams:Alert5 id="can-revisit" type="info" close="true">
				<c:choose>
					<c:when test="${isOptionsWithSequencesActivity}">
						<fmt:message key="message.activity.set.options.note" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.activity.options.note" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>

		<div id="instructions" class="instructions">
			<c:choose>
				<c:when test="${not empty optionsActivityForm.description}">
					<c:out value="${optionsActivityForm.description}" />
				</c:when>
				<c:otherwise>
					<fmt:message key="message.activity.options.noActivitySelected" />
				</c:otherwise>
			</c:choose>
		</div>

		<form:form action="ChooseActivity.do" modelAttribute="activityForm" method="post" id="activityForm">
			<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />">
			<input type="hidden" name="activityID" id="activity-id">
		
			<div class="ltable no-header table-hover">
				<c:forEach items="${optionsActivityForm.activityURLs}" var="activityURL">
					<div class="row align-items-center">
						<div class="col-sm">
							<c:choose>
								<c:when test="${not activityURL.complete and not optionsActivityForm.maxActivitiesReached}">
									<div id="activity-name-${activityURL.activityId}">
										<c:out value="${activityURL.title}" />
									</div>
								</c:when>
									
								<c:when test="${activityURL.complete}">
									<div class="ms-n3">
										<i class="fa fa-lg fa-check-circle text-success radio-button-offset me-1" style="cursor: auto;"></i>
										
										<c:choose>
											<c:when test="${not empty activityURL.url}">
												<a href="${activityURL.url}"><c:out value="${activityURL.title}" /></a>
											</c:when>
												
											<c:when test="${empty activityURL.url}"><!-- sequence activity -->
												<span class="text-muted">
													<c:out value="${activityURL.title}" />
												</span>
												
												<div class="ms-4 ps-1">
													<c:forEach items="${activityURL.childActivities}" var="childActivityURL">
														<div>
															<a href="${childActivityURL.url}"><c:out value="${childActivityURL.title}" /></a>
														</div>
													</c:forEach>
												</div>
											</c:when>
										</c:choose>										
									</div>
								</c:when>
											
								<c:otherwise>
									<div class="ms-3">
										<c:out value="${activityURL.title}" />
									</div>
								</c:otherwise>
							</c:choose>
						</div>

						<c:if test="${!optionsActivityForm.maxActivitiesReached}">
							<div class="col-sm-4">
								<button type="button" class="btn btn-secondary float-end" 
										data-bs-toggle="modal" data-bs-target="#confirmationModal" 
										data-activity-id="${activityURL.activityId}">
									<i class="fa-regular fa-circle-check me-1"></i>
									<fmt:message key="label.activity.options.choose" />
								</button>	
							</div>					
						</c:if>
					</div>
				</c:forEach>
			</div>
		</form:form>
		
		<c:if test="${optionsActivityForm.minimumLimitReached or isPreview}">
			<div class="activity-bottom-buttons mt-5">
				<form:form action="/lams/learning/CompleteActivity.do" modelAttribute="messageForm" method="post" id="messageForm">
					<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />">
					<input type="hidden" name="activityID" value="<c:out value='${optionsActivityForm.activityID}' />">
					<input type="hidden" name="lessonID" value="<c:out value='${optionsActivityForm.lessonID}' />">
					<input type="hidden" name="progressID" value="<c:out value='${optionsActivityForm.progressID}' />">
				
					<button type="submit" class="btn btn-primary na" id="finishButton">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="label.submit.button" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finish.button" />
							</c:otherwise>
						</c:choose>
					</button>
				</form:form>
			</div>
		</c:if>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="confirmationModal" tabindex="-1" data-bs-backdrop="static" data-bs-keyboard="false" aria-hidden="true">
	 	<div class="modal-dialog">
	    	<div class="modal-content">
				<div class="modal-header text-bg-warning">
					<div class="modal-title fs-4">
	        			<fmt:message key="label.confirm.branch.selection.header"/>
	        		</div>
	        		<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      		</div>
	      		
				<div class="modal-body text-center" style="min-height: 60px;">
				</div>
	      
				<div class="modal-footer" style="padding: 8px">
		      		<button type="button" class="btn btn-secondary" data-bs-dismiss="modal"
		        			data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='label.cancel.button' /></span>">
		        		<i class="fa fa-xmark fa-lg me-1"></i>                        
		        		<fmt:message key="label.cancel.button" />
		        	</button>
	        	
					<button type="button" id="submitter" class="btn btn-primary"
							data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='label.group.confirm.button' /></span>">
						<i class="fa fa-check fa-lg me-1"></i>
						<fmt:message key="label.group.confirm.button"/>
					</button>
				</div>
			</div>
		</div>
	</div>
</lams:PageLearner>
