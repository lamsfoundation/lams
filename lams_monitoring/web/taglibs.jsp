<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="csrfguard" prefix="csrf" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-xml" prefix="x" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="ALPHABET" value="${fn:split('a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z', ',')}" scope="request"/>
<c:set var="ALPHABET_CAPITAL_LETTERS" value="${fn:split('A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z', ',')}" scope="request"/>

