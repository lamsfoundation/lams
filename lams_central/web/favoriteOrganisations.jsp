<%@ include file="/common/taglibs.jsp"%>

<div id="favorite-organisations-container" class="tour-organisations-favorites">

	<c:if test="${not empty favoriteOrganisations}">
		<ul class="nav flex-column" id="favorite-organisations">
			<c:forEach var="favoriteOrganisation" items="${favoriteOrganisations}">
				<li id="favorite-li-${favoriteOrganisation.organisationId}" class="nav-item">
					<a data-id="${favoriteOrganisation.organisationId}" href="#nogo" 
							onClick="javascript:selectOrganisation(${favoriteOrganisation.organisationId})"
							class="nav-link <c:if test="${favoriteOrganisation.organisationId == activeOrgId}">active</c:if>">
						${favoriteOrganisation.name}
						<span class="pull-right"><i class="fa fa-star"></i></span>
					</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
            
</div>