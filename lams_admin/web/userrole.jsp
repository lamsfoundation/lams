<%@ include file="/taglibs.jsp"%>

<html-el:form action="/userrolessave.do" method="post">
<html-el:hidden property="userId" />
<html-el:hidden property="orgId" />
<p>
	<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
	<logic:notEmpty name="pOrgId">
		: <a href="orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default"><c:out value="${parentName}" /></a>
		: <a href="usermanage.do?org=<bean:write name="UserRolesForm" property="orgId" />" class="btn btn-default"><c:out value="${orgName}" /></a>
	</logic:notEmpty>
	<logic:empty name="pOrgId">
		<logic:notEqual name="UserRolesForm" property="orgId" value="1">
			: <a href="orgmanage.do?org=<bean:write name="UserRolesForm" property="orgId" />" class="btn btn-default"><c:out value="${orgName}" /></a>
		</logic:notEqual>
		<logic:equal name="UserRolesForm" property="orgId" value="1">
			: <a href="usermanage.do?org=<bean:write name="UserRolesForm" property="orgId" />" class="btn btn-default"><fmt:message key="admin.global.roles.manage" /></a>
		</logic:equal>
	</logic:empty>
</p>

<p><fmt:message key="msg.roles.mandatory"/></p>

<div align="center"><html-el:errors/><html-el:messages id="roles" message="true"><bean:write name="roles" /></html-el:messages></div>

<div class="container-fluid">
<div class="row">
  <div class="col-xs-2"><fmt:message key="admin.user.login"/>:</div>
  <div class="col-xs-10"><bean:write name="login" /></div>
</div>

<div class="row">
  <div class="col-xs-2"><fmt:message key="admin.user.name"/>:</div>
  <div class="col-xs-10"><bean:write name="fullName" /></div>
</div>

<div class="row">
  <div class="col-xs-2"><fmt:message key="admin.user.roles"/>:</div>
  <div class="col-xs-10">            
  	<c:forEach items="${rolelist}" var="role">
    	<html-el:multibox name="UserRolesForm" property="roles" value="${role.roleId}"/>
        <fmt:message>role.<lams:role role="${role.name}" /></fmt:message><br/>
    </c:forEach>
  </div>
</div>
</div>

<div class="pull-right">
	<html-el:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html-el:cancel>
	<html-el:reset styleClass="btn btn-default loffset5"><fmt:message key="admin.reset"/></html-el:reset>
	<html-el:submit styleClass="btn btn-primary loffset5"><fmt:message key="admin.save"/></html-el:submit>
</div>

</html-el:form>
