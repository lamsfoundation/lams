<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

gyig
<%
if(true)
{
	return;
}
%>
fufy
<script type="text/javascript">
	if(window.top != null){
		window.top.location.href = "${lams}index.jsp";
	}else
		location.href = "${lams}index.jsp";
</script>


