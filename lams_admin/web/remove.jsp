<%@ include file="/taglibs.jsp"%>

<form>

<logic:empty name="orgId">
	<c:url var="cancel" value="usersearch.do" />
</logic:empty>
<logic:notEmpty name="orgId">
	<c:url var="cancel" value="usermanage.do">
		<c:param name="org" value="${orgId}" />
	</c:url>
</logic:notEmpty>

<logic:equal name="method" value="disable">
	<div class="panel panel-default" >
		<div class="panel-heading">
			<span class="panel-title">
				<fmt:message key="admin.user.disable"/>
			</span>
		</div>
		<div class="panel-body">     
			<p>
				<fmt:message key="msg.disable.user.1"/>&nbsp;&nbsp;
				<fmt:message key="msg.disable.user.2"/>&nbsp;&nbsp;
				<fmt:message key="msg.disable.user.3"/><br />
				<fmt:message key="msg.disable.user.4"/>
			</p>
			<c:url var="disableaction" value="user.do">
				<c:param name="method" value="disable" />
				<c:param name="userId" value="${userId}" />
				<c:param name="orgId" value="${orgId}" />
			</c:url>
			<div class="pull-right">
				<input class="btn btn-primary" type="button" value="Disable" onClick="javascript:document.location='<c:out value="${disableaction}"/>'" />
				<input class="btn btn-default" type="button" value="Cancel" onClick="javascript:document.location='<c:out value="${cancel}"/>'" />
			</div>
		</div>
	</div>	
</logic:equal>

<logic:equal name="method" value="delete">
	<div class="panel panel-default" >
		<div class="panel-heading">
			<span class="panel-title">
				<fmt:message key="admin.user.delete"/>
			</span>
		</div>
		<div class="panel-body">     
			<p><fmt:message key="msg.delete.user.1"/>&nbsp;&nbsp;<fmt:message key="msg.delete.user.2"/></p>
			<c:url var="deleteaction" value="user.do">
					<c:param name="method" value="delete" />
					<c:param name="userId" value="${userId}" />
					<c:param name="orgId" value="${orgId}" />
				</c:url>
			<div class="pull-right">
				<input class="btn btn-default" type="button" value="Delete" onClick="javascript:document.location='<c:out value="${deleteaction}"/>'" />
				<input class="btn btn-default" type="button" value="Cancel" onClick="javascript:document.location='<c:out value="${cancel}"/>'" />
			</div>
		</div>
	</div>
</logic:equal>
</form>

<div id="footer"/>