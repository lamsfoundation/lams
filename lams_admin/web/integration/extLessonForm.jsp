<%@ include file="/taglibs.jsp"%>

		<h3><fmt:message key="sysadmin.lesson.default" /></h3>
		<div class="checkbox">
		    <label>
		    <form:checkbox path="gradebookOnComplete" /><fmt:message key="sysadmin.lesson.gradebook.complete" />
		    </label>
		</div>
		<div class="checkbox">
		    <label>
		     <form:checkbox path="forceLearnerRestart" /><fmt:message key="sysadmin.lesson.force.restart" />
		    </label>
		</div>
		<div class="checkbox">
		    <label>
		     <form:checkbox path="allowLearnerRestart" /><fmt:message key="sysadmin.lesson.allow.restart" />
		    </label>
		</div>
		<div class="checkbox">
		    <label>
		     <form:checkbox path="liveEditEnabled" /><fmt:message key="sysadmin.lesson.liveedit" />
		    </label>
		</div>
		<div class="checkbox">
		    <label>
		    <form:checkbox path="enableLessonNotifications" /><fmt:message key="sysadmin.lesson.notification" />
		    </label>
		</div>
		<div class="checkbox">
		    <label>
		     <form:checkbox path="learnerPresenceAvailable" styleId="learnerPresenceAvailable" /><fmt:message key="sysadmin.lesson.presence" />
		    </label>
		</div>
		<div class="checkbox">
		    <label>
		     <form:checkbox path="learnerImAvailable" styleId="learnerImAvailable" /><fmt:message key="sysadmin.lesson.im" />
		    </label>
		</div>