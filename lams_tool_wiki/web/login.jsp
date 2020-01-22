<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%
if(true)
{
	return;
}
%>
<script type="text/javascript">
	if(window.top != null){
		window.top.location.href = "${lams}index.jsp";
	}else
		location.href = "${lams}index.jsp";
</script>


