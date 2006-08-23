<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
	</head>
	<body>

		<fmt:parseDate var="date" type="both" pattern="yyyyMMddHHmmss">20060201150405</fmt:parseDate>

		<H1>Date Format Test: for 1 Feb 2006 3:04:05PM</H1>
		
		<P>Using: dateStyle="medium" timeStyle="long" (same as leaving dateStyle as default)<BR>

		<fmt:setLocale value="en_AU"/>
		en_AU: <fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="long" /><BR>

		<fmt:setLocale value="en_UK"/>
		en_UK: <fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="long" /><BR>

		<fmt:setLocale value="cy_UK"/>
		cy_UK: <fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="long" /><BR>

		<fmt:setLocale value="en_GB"/>
		en_GB: <fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="long" /><BR>

		<fmt:setLocale value="cy_GB"/>
		cy_GB: <fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="long" /><BR>

		<fmt:setLocale value="en_US"/>
		en_US: <fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="long" /><BR>

		<fmt:setLocale value="es_ES"/>
		es_ES: <fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="long" /><BR>

		<fmt:setLocale value="zh_CN"/>
		zh_CN: <fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="long" />

		</p>
					
		<P>Using: dateStyle="long" timeStyle="full"<BR>

		<fmt:setLocale value="en_AU"/>
		en_AU: <fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="full" /><BR>

		<fmt:setLocale value="en_UK"/>
		en_UK: <fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="full" /><BR>

		<fmt:setLocale value="cy_UK"/>
		cy_UK: <fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="full" /><BR>

		<fmt:setLocale value="en_GB"/>
		en_GB: <fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="full" /><BR>

		<fmt:setLocale value="cy_GB"/>
		cy_GB: <fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="full" /><BR>

		<fmt:setLocale value="en_US"/>
		en_US: <fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="full" /><BR>

		<fmt:setLocale value="zh_CN"/>
		zh_CN: <fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="full" />

		</p>
	</body>
</html>

