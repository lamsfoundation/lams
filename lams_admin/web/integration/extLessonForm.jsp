<%@ include file="/taglibs.jsp"%>

		<h2><fmt:message key="sysadmin.lesson.default" /></h2>
			<div class="form-group">
				<div class="form-check">
					<form:checkbox id="gradebookOnComplete" path="gradebookOnComplete" cssClass="form-check-input"/>
					<label class="form-check-label" for="gradebookOnComplete">
						<fmt:message key="sysadmin.lesson.gradebook.complete" />
					</label>
				</div>	
					
				<div class="form-check">
					<form:checkbox path="forceLearnerRestart" id="forceLearnerRestart" cssClass="form-check-input"/>
					<label class="form-check-label" for="forceLearnerRestart">
						<fmt:message key="sysadmin.lesson.force.restart" />
					</label>
				</div>	

				<div class="form-check">
					<form:checkbox id="allowLearnerRestart" path="allowLearnerRestart" cssClass="form-check-input"/>
					<label class="form-check-label" for="allowLearnerRestart">
						<fmt:message key="sysadmin.lesson.allow.restart" />
					</label>
				</div>	
		    
				<div class="form-check">
					<form:checkbox id="liveEditEnabled" path="liveEditEnabled" cssClass="form-check-input"/>
					<label class="form-check-label" for="liveEditEnabled">
						<fmt:message key="sysadmin.lesson.liveedit" />
					</label>
				</div>	

				<div class="form-check">
					<form:checkbox id="enableLessonNotifications" path="enableLessonNotifications" cssClass="form-check-input"/>
					<label class="form-check-label" for="enableLessonNotifications">
						<fmt:message key="sysadmin.lesson.notification" />
					</label>
				</div>	

				<div class="form-check">
					<form:checkbox id="learnerPresenceAvailable" path="learnerPresenceAvailable" cssClass="form-check-input" styleId="learnerPresenceAvailable" />
					<label class="form-check-label" for="learnerPresenceAvailable">
						<fmt:message key="sysadmin.lesson.presence" />
					</label>
				</div>	

				<div class="form-check">
					<form:checkbox id="learnerImAvailable" path="learnerImAvailable" cssClass="form-check-input" styleId="learnerImAvailable" />
					<label class="form-check-label" for="learnerImAvailable">
						<fmt:message key="sysadmin.lesson.im" />
					</label>
				</div>	
			</div>

