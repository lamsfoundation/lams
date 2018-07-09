<body>
	<form id="SignupForm" name="SignupForm" action="/lams/signup/signup.do" method="post" autocomplete="off" >
 		<c:set var="org.apache.struts.taglib.html.BEAN"  value="${SignupForm}" />
 		<html:hidden property="method" value="login" />
		<html:hidden property="submitted" value="1" />
		<html:hidden property="context" value="${signupOrganisation.context}" />
		<html:hidden property="selectedTab" value="1" />
		<div class="container">
			<div class="row vertical-center-row">
				<div
					class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
					<div class="panel">
						<div class="panel-body">
							<div class="form-group">
								<label for="usernameTab2"><fmt:message key="login.username" /></label>:
								<html:text property="usernameTab2" size="40" maxlength="255"
									styleClass="form-control" />
								<html:errors property="usernameTab2" />
							</div>
							<div class="form-group">
								<label for="passwordTab2"><fmt:message key="login.password" /></label>: <input
									name="passwordTab2" type="password" size="40" maxlength="255"
									class="form-control" autocomplete="off" />
								<html:errors property="passwordTab2" />
							</div>
							<div class="form-group">
								 <label for="courseKeyTab2"><fmt:message key="login.course.key" /></label>:
								<html:text property="courseKeyTab2" size="40" maxlength="255"
									styleClass="form-control" />
								<html:errors property="courseKeyTab2" />
							</div>
							<div class="form-group" align="right">
								<html:submit styleClass="btn btn-sm btn-default voffset5">
									<fmt:message key="login.submit" />
								</html:submit>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>


