<%@ include file="/common/taglibs.jsp"%>

<body>
	<form:form id="SignupForm" modelAttribute="SignupForm" action="/lams/signup/login.do" method="post" autocomplete="off" >
		<form:hidden path="submitted" value="1" />
		<form:hidden path="context" value="${signupOrganisation.context}" />
		<div class="container">
			<div class="row vertical-center-row">
				<div
					class="col-12 col-md-8 offset-md-2 col-lg-6 offset-lg-3">
					<div class="panel">
						<div class="panel-body">
							<div class="form-group">
								<label for="usernameTab2"><fmt:message key="login.username" /></label>:
								<form:input path="usernameTab2" size="40" maxlength="255"
									cssClass="form-control" />
								<lams:errors path="usernameTab2"/>
							</div>
							<div class="form-group">
								<label for="passwordTab2"><fmt:message key="login.password" /></label>: <input
									name="passwordTab2" type="password" size="40" maxlength="255"
									class="form-control" autocomplete="off" />
								<lams:errors path="passwordTab2"/>
							</div>
							<div class="form-group">
								 <label for="courseKeyTab2"><fmt:message key="login.course.key" /></label>:
								<form:input path="courseKeyTab2" size="40" maxlength="255"
									cssClass="form-control" />
								<lams:errors path="courseKeyTab2"/>
							</div>
							<div class="form-group" align="right">
								<button class="btn btn-sm btn-secondary voffset5">
									<fmt:message key="login.submit" />
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>
