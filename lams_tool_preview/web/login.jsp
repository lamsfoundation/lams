<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c" %>
<c:set var="lams"><lams:LAMSURL/></c:set>
<script type="text/javascript">
	if(window.top != null){
		window.top.location.href = "${lams}index.jsp";
	}else
		location.href = "${lams}index.jsp";
</script>


