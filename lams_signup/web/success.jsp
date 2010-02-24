<%@ include file="/taglibs.jsp"%>

<h1 align="center">
	<img src="<lams:LAMSURL/>/images/css/lams_login.gif" 
		alt="LAMS - Learning Activity Management System" width="186" height="90" ></img>
</h1>

<p>&nbsp;</p>

<c:if test="${not empty error}">
	<p class="warning">Had trouble sending email.  Error was, <c:out value="${error}" /></p>
</c:if>

<p>
	You've successfully registered an account with LAMS.  Click <a href="<lams:LAMSURL />">here</a> to login.
</p>