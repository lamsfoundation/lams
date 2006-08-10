<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-bean" prefix="bean" %>

<h2>My Profile</h2>

<p>Name: <bean:write name="fullName" /><br />
Email: <bean:write name="email" />
</p>

<p><a href="profile.do?method=edit">Edit My Profile</a><br />
<a href="password.do">Change my password</a><br />
<a href="">Update my portrait</a><br />
[My Courses]
</p>