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

<lams:PageLearner title="${title}" toolSessionID="" lessonID="${lessonID}">
	<style type="text/css">
		.user-container {
			padding: 2px;
		}
		.modal-dialog {
			--bs-modal-width: 400px;
		}
	</style>

	<script>
		$(document).ready(function(){
			$('#confirmationModal').on('show.bs.modal', function (event) {
				  var button = $(event.relatedTarget) 
				  var url = button.data('u') 
				  var groupName = button.data('gn')
		
				  var modal = $(this);
				  modal.find('.modal-body').html('<fmt:message key="label.group.confirm.areyoujoining"/>:&nbsp;<strong>' + groupName + '</strong>?')
				  modal.find('#submitter').click(function(){
					$(this).parent().children('button').each(function(){
						$(this).prop('disabled', true).html($(this).data('loadingText'));
					});
					document.getElementById(url).submit();
				  });
			})
		})
	</script>

	<!-- Modal -->
	<div class="modal fade" id="confirmationModal" tabindex="-1" data-bs-backdrop="static" data-bs-keyboard="false" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header bg-warning">
	        	<div class="modal-title fs-4">
	        		<i class="fa fa-sm fa-users"></i> 
	        		<fmt:message key="label.group.confirm.header"/>
	        	</div>
	        	<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      <div class="modal-body text-center" style="min-height: 60px;">
	      </div>
	      <div class="modal-footer" style="padding: 8px">
	      		<button type="button" class="btn  btn-sm btn-secondary" data-bs-dismiss="modal"
	        			data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='label.cancel.button' /></span>">
	        		<fmt:message key="label.cancel.button" />
	        	</button>
				<button id="submitter" onclick="" class="btn btn-sm btn-primary"
						data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='label.group.confirm.button' /></span>">
					<fmt:message key="label.group.confirm.button"/>
				</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div id="container-main">
		<div class="instructions">
			<fmt:message key="label.learner.choice.group.message" />
		</div>
	
		<div class="table-responsive">
			<div class="ltable">
				<div class="row">
					<div class="col-3 first">
						<fmt:message key="label.view.groups.title" />
					</div>
					
					<div class="col-7">
						<c:if test="${viewStudentsBeforeSelection && !RestrictedGroupUserNames}">
							<fmt:message key="label.view.groups.learners" />
						</c:if>
					</div>
					
					<div class="col-2"></div>
				</div>
				
				<c:forEach var="group" items="${groups}">
					<div class="row">
						<div class="col-sm-3 first">
							<strong><c:out value="${group.groupName}" /></strong>
						</div>
						
						<div class="col-sm-7">
							<c:if test="${viewStudentsBeforeSelection && !RestrictedGroupUserNames}">
								<c:forEach items="${group.userList}" var="user">
									<div name="u-${user.userID}" class="user-container">
										<lams:Portrait userId="${user.userID}"/>&nbsp;<c:out value="${user.firstName}" />&nbsp;<c:out value="${user.lastName}" />
									</div>
								</c:forEach>
							</c:if>
						</div>
						
						<div class="col-sm-2 text-end">
							<c:choose>
								<c:when test="${not empty maxLearnersPerGroup and fn:length(group.userList)>=maxLearnersPerGroup}">
									<fmt:message key="label.learner.choice.group.full" />
								</c:when>
								
								<c:otherwise>
									<form:form action="learnerChooseGroup.do?userId=${user.userID}&activityID=${activityID}&groupId=${group.groupID}"
										 modelAttribute="groupingForm" id="form${user.userID}${activityID}${group.groupID}" >
									</form:form>							
									<button type="button" class="btn btn-sm btn-primary" 
											data-bs-toggle="modal" data-bs-target="#confirmationModal" 
											data-u="form${user.userID}${activityID}${group.groupID}" 
											data-gn="<c:out value="${group.groupName}" />">
										<fmt:message key="label.choose.group.button" />
									</button>
								</c:otherwise>							
							</c:choose>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</lams:PageLearner>