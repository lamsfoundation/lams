<%@ include file="/common/taglibs.jsp"%>

<!-- Add More options menu drop down first (if it contains links) -->
<c:if test="${not empty org.moreLinks}">
	<!-- bootstrap More options dropdown -->
	<div class="course-right-buttons pull-right">
	<div class="btn-group">
	  <button type="button" class="btn btn-primary btn-sm dropdown-toggle ${addTourClass?'tour-more-options':''}" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fa fa-fw fa-sliders" title="<fmt:message key="index.moreActions" />"></i> <span class="caret"></span></button>
	  <ul id="more-links-menu" class="dropdown-menu">
		<c:forEach var="link" items="${org.moreLinks}">
			 <li onClick="${link.url}"
	                      <c:if test="${not empty link.tooltip}">
	                          title="<fmt:message key='${link.tooltip}'/>"
	                      </c:if>
	         >
             	<a href="#"><i class="${link.style}"></i> <fmt:message key="${link.name}" /></a>
             </li>
        </c:forEach>
  	 </ul>
	</div>
	</div>
	<!-- end bootstrap More options dropdown -->
</c:if> 

<c:forEach var="link" items="${org.links}">
	<c:choose>
		<c:when test="${link.name eq 'index.addlesson.single'}">
			<div class="course-right-buttons pull-right">
				<div class="btn-group" role="group">
					<button onClick="<c:out value='${link.url}'/>" type="button" class="btn btn-primary btn-sm ${addTourClass?'tour-add-lesson':''}">
						<i class="${link.style}" title="<fmt:message key="index.addlesson" />"></i>
						<span class="hidden-xs"><fmt:message key="index.addlesson" /></span>
					</button>
					<div class="btn-group" role="group">
						<button id="addSingleActivityLessonDrop" type="button" class="btn btn-primary btn-sm dropdown-toggle ${addTourClass?'tour-add-single-lesson':''}" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" aria-labelledby="openDropButton">
						 	<li class="dropdown-header"><fmt:message key="index.single.activity.lesson.desc" /></li>
							<c:forEach var="tool" items="${tools}">
							        <li onClick="javascript:showAddSingleActivityLessonDialog(${org.id}, ${tool.toolId}, ${tool.learningLibraryId})">
							        	<a href="#"><c:out value="${tool.toolDisplayName}" /></a>
							        </li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</c:when>
		<c:when test="${link.name eq 'index.kumalive.teacher'}">
			<div class="course-right-buttons pull-right">
				<div class="btn-group" role="group" title="<fmt:message key="${link.tooltip}" />">
					<button onClick="<c:out value='${link.url}'/>" type="button" class="btn btn-primary btn-sm">
						<i class="${link.style}"></i>
						<span class="hidden-xs"><fmt:message key="index.kumalive" /></span>
					</button>
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
						 	<li onClick="javascript:openKumalive(${org.id}, 'learner')">
						 		<a href="#"><fmt:message key="index.kumalive.enter.learner" /></a>
						 	</li>
						 	<li onClick="javascript:showKumaliveReportDialog(${org.id})">
						 		<a href="#"><fmt:message key="index.kumalive.report" /></a>
						 	</li>
						 	<li onClick="javascript:showKumaliveRubricsDialog(${org.id})">
						 		<a href="#"><fmt:message key="index.kumalive.rubric" /></a>
						 	</li>
						</ul>
					</div>
				</div>
			</div>
		</c:when>
		<c:when test="${link.name eq 'index.kumalive'}">
			<div class="course-right-buttons pull-right">
                 <c:choose>
	                <c:when test="${fn:contains(link.style, 'disabled')}">
                        <c:set var="kumaliveState" value="disabled"/>
	                 </c:when>
	                 <c:otherwise>
                        <c:set var="kumaliveState" value="enabled"/>
	                 </c:otherwise>
                 </c:choose>                 	
                 <a class="btn btn-success btn-sm ${kumaliveState}"
                     <c:if test="${not empty link.tooltip}">
                     	title="<fmt:message key='${link.tooltip}'/>"
                 	</c:if>
                    <c:if test="${kumaliveState eq 'enabled'}">
	                 	onClick="<c:out value='${link.url}'/>"
                    </c:if>    
                >
					<i class="${link.style}" title="<fmt:message key="${link.name}" />"></i>
					<span class="hidden-xs"><fmt:message key="${link.name}" /></span>
				</a>
			</div>
		</c:when>
		<c:otherwise>
			<div class="course-right-buttons pull-right">
				<a class="btn btn-primary btn-sm tour-${link.id}" onClick="<c:out value='${link.url}'/>"
                     <c:if test="${not empty link.tooltip}">
                     	title="<fmt:message key='${link.tooltip}'/>"
                 	</c:if>
                >
					<i class="${link.style}" title="<fmt:message key="${link.name}" />"></i>
					<span class="hidden-xs"><fmt:message key="${link.name}" /></span>
				</a>
			</div>
		</c:otherwise>
	</c:choose>
</c:forEach>
