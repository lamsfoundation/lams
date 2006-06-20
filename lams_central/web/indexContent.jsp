<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<table width=100% border=0>
	<tr>
		<td align="right"> 
			<c:forEach var="headerlink" items="${headerLinks}">
				[<a href="<c:out value='${headerlink.url}'/>"><fmt:message key="${headerlink.name}"/></a>]&nbsp;
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td align="left">
			<table border="0">
				<c:forEach var="orgBean" items="${orgBeans}">
					<tr><td>
						<table border="0">
							<tr><td>
								<c:choose>
								<c:when test="${not empty orgBean.lessons || not empty orgBean.childIndexOrgBeans}">
									<a onclick="toggle(this)"><img src="images/tree_open.gif"/></a>
								</c:when>
								<c:otherwise>
									<img src="images/tree_white.gif"/>
								</c:otherwise>
								</c:choose>&nbsp;
								<strong><c:out value="${orgBean.name}"/></strong>
								<img src="images/spacer.gif" height="10" width="200"/>
								<c:forEach var="link" items="${orgBean.links}">
									[<a href="<c:out value='${link.url}'/>"><fmt:message key="${link.name}"/></a>]&nbsp;
								</c:forEach>
								<div>
									<c:forEach var="lesson" items="${orgBean.lessons}">
										<table border="0">
											<tr>
												<td width="10"></td>
												<td>
													<img src="images/tree_white.gif"/> <c:out value="${lesson.name}"/>
													<img src="images/spacer.gif" height="10" width="200"/>
													<c:forEach var="lessonlink" items="${lesson.links}">
														[<a href="<c:out value='${lessonlink.url}'/>"><fmt:message key="${lessonlink.name}"/></a>]&nbsp;
													</c:forEach>
													<div></div> 
												</td>
											</tr>
										</table>
									</c:forEach>
									<c:forEach var="childOrg" items="${orgBean.childIndexOrgBeans}">
										<table border="0">
											<tr>
												<td width="10"></td>
												<td>
													<c:choose>
													<c:when test="${not empty childOrg.lessons}">
														<a onclick="toggle(this)"><img src="images/tree_open.gif"/></a>
													</c:when>
													<c:otherwise>
														<img src="images/tree_white.gif"/>
													</c:otherwise>
													</c:choose>&nbsp;
													<strong><c:out value="${childOrg.name}"/></strong>
													<img src="images/spacer.gif" height="10" width="200"/>
													<c:forEach var="childlink" items="${childOrg.links}">
														[<a href="<c:out value='${childlink.url}'/>"><fmt:message key="${childlink.name}"/></a>]&nbsp;
													</c:forEach>
													<div>
														<c:forEach var="childLesson" items="${childOrg.lessons}">
															<table border="0">
																<tr>
																	<td width="10"></td>
																	<td>
																		<img src="images/tree_white.gif"/> <c:out value="${childLesson.name}"/>
																		<img src="images/spacer.gif" height="5" width="200"/>
																		<c:forEach var="childlessonlink" items="${childLesson.links}">
																			[<a href="<c:out value='${childlessonlink.url}'/>"><fmt:message key="${childlessonlink.name}"/></a>]&nbsp;
																		</c:forEach>
																		<div></div> 
																	</td>
																</tr>
															</table>
														</c:forEach>
													</div>
												</td>
											</tr>
										</table>
									</c:forEach>
								</div>
							</td></tr>
						</table>
					</td></tr>
				</c:forEach>			
			</table>
		</td>
	</tr>
</table>

