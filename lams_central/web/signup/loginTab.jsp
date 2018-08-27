<body>
	<form:form id="SignupForm" modelAttribute="SignupForm" action="/lams/signup/signup.do" method="post" autocomplete="off" >
 		<form:hidden path="method" value="login" />
		<form:hidden path="submitted" value="1" />
		<form:hidden path="context" value="${signupOrganisation.context}" />
		<form:hidden path="selectedTab" value="1" />
		<div class="container">
			<div class="row vertical-center-row">
				<div
					class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
					<div class="panel">
						<div class="panel-body">
							<div class="form-group">
								<label for="usernameTab2"><fmt:message key="login.username" /></label>:
								<form:input path="usernameTab2" size="40" maxlength="255"
									cssClass="form-control" />
								<c:set var="errorKey" value="usernameTab2" /> 
								<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
									<lams:Alert id="error" type="danger" close="false"> 
										<c:forEach var="error" items="${errorMap[errorKey]}"> 
											<c:out value="${error}" /><br /> 
										</c:forEach> 
								    </lams:Alert> 
								</c:if>
							</div>
							<div class="form-group">
								<label for="passwordTab2"><fmt:message key="login.password" /></label>: <input
									name="passwordTab2" type="password" size="40" maxlength="255"
									class="form-control" autocomplete="off" />
								<c:set var="errorKey" value="passwordTab2" /> 
								<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
									<lams:Alert id="error" type="danger" close="false"> 
										<c:forEach var="error" items="${errorMap[errorKey]}"> 
											<c:out value="${error}" /><br /> 
										</c:forEach> 
								    </lams:Alert> 
								</c:if>
								
							</div>
							<div class="form-group">
								 <label for="courseKeyTab2"><fmt:message key="login.course.key" /></label>:
								<form:input path="courseKeyTab2" size="40" maxlength="255"
									cssClass="form-control" />
								 <c:set var="errorKey" value="courseKeyTab2" /> 
								 <c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
								     <lams:Alert id="error" type="danger" close="false"> 
								         <c:forEach var="error" items="${errorMap[errorKey]}"> 
								             <c:out value="${error}" /><br /> 
								         </c:forEach> 
								     </lams:Alert> 
								</c:if>
							</div>
							<div class="form-group" align="right">
								<button class="btn btn-sm btn-default voffset5">
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


