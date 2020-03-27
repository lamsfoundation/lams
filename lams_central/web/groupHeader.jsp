<%@ include file="/common/taglibs.jsp"%>

<!-- Add More options menu drop down first (if it contains links) -->
<c:if test="${not empty org.moreLinks}">
	<!-- bootstrap More options dropdown -->
	<div class="course-right-buttons pull-right">
	 	<button type="button" class="btn btn-light btn-sm ${addTourClass?'tour-more-options':''}" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			<i class="fa fa-lg fa-cog" title="<fmt:message key="index.moreActions" />"></i> 
	  		<span class="caret"></span>
	  	</button>
	    <div id="more-links-menu" class="dropdown-menu dropdown-menu-right">
			<c:forEach var="link" items="${org.moreLinks}">
				 <button class="dropdown-item" type="button" onClick="${link.url}"
		         	<c:if test="${not empty link.tooltip}">
		            	title="<fmt:message key='${link.tooltip}'/>"
		         	</c:if>
		         >
	             	<i class="${link.style}"></i> <fmt:message key="${link.name}" />
	             </button>
	        </c:forEach>
  	   </div>
	</div>
	<!-- end bootstrap More options dropdown -->
</c:if> 

<c:forEach var="link" items="${org.links}">
	<c:choose>
		<c:when test="${link.name eq 'index.addlesson.single'}">
			<div class="course-right-buttons pull-right">
				<div class="btn-group">
					<button onClick="<c:out value='${link.url}'/>" type="button" class="btn btn-light btn-sm ${addTourClass?'tour-add-lesson':''}">
						<i class="${link.style}" title="<fmt:message key="index.addlesson" />"></i>
						<span class="d-none d-sm-inline"><fmt:message key="index.addlesson" /></span>
					</button>
					
					<button type="button" class="btn btn-light btn-sm dropdown-toggle dropdown-toggle-split ${addTourClass?'tour-add-single-lesson':''}" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" data-display="static">
						<span class="caret"></span>
					</button>
					<div class="dropdown-menu dropdown-menu-right">
						<h6 class="dropdown-header">
							<fmt:message key="index.single.activity.lesson.desc" />
						</h6>
						<c:forEach var="tool" items="${tools}">
							<button class="dropdown-item" type="button" onClick="javascript:showAddSingleActivityLessonDialog(${org.id}, ${tool.toolId}, ${tool.learningLibraryId})">
						        <c:out value="${tool.toolDisplayName}" />
						    </button>
						</c:forEach>
					</div>
				</div>
			</div>
		</c:when>
		
		<c:when test="${link.name eq 'index.kumalive.teacher'}">
			<div class="course-right-buttons pull-right">
				<div class="btn-group" title="<fmt:message key="${link.tooltip}" />">
					<button onClick="<c:out value='${link.url}'/>" type="button" class="btn btn-light btn-sm">
						<i class="${link.style}"></i>
						<span class="d-none d-md-inline"><fmt:message key="index.kumalive" /></span>
					</button>
					
					<button type="button" class="btn btn-light btn-sm dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" data-display="static">
						<span class="caret"></span>
					</button>
					<div class="dropdown-menu dropdown-menu-right">
						<button class="dropdown-item" type="button" onClick="javascript:openKumalive(${org.id}, 'learner')">
							<fmt:message key="index.kumalive.enter.learner" />
						</button>
						<button class="dropdown-item" type="button" onClick="javascript:showKumaliveReportDialog(${org.id})">
						 	<fmt:message key="index.kumalive.report" />
						</button>
						<button class="dropdown-item" type="button" onClick="javascript:showKumaliveRubricsDialog(${org.id})">
						 	<fmt:message key="index.kumalive.rubric" />
						</button>
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
					<span class="d-none d-md-inline"><fmt:message key="${link.name}" /></span>
				</a>
			</div>
		</c:when>
		
		<c:otherwise>
			<div class="course-right-buttons pull-right">
				<button class="btn btn-light btn-sm tour-${link.id}" onClick="<c:out value='${link.url}'/>"
                     <c:if test="${not empty link.tooltip}">
                     	title="<fmt:message key='${link.tooltip}'/>"
                 	</c:if>
                >
					<i class="${link.style}" title="<fmt:message key="${link.name}" />"></i>
					<span class="d-none d-md-inline"><fmt:message key="${link.name}" /></span>
				</button>
			</div>
		</c:otherwise>
	</c:choose>
</c:forEach>
