	<!--				
				function initLoadGroup(element, stateId, display) {
					jQuery(element).load(
						"displayGroup.do",
						{
							display: display,
							stateId: stateId, 
							orgId: jQuery(element).attr("id")
						},
						function() {
							if (display == 'header') {
								jQuery("span.j-group-icon", element).html("<img src='images/tree_closed.gif'/>");
							} else if (display == 'group') {
								jQuery("span.j-group-icon", element).html("<img src='images/tree_open.gif'/>");
							}
							toggleGroupContents(element, stateId);
							registerToolTip(element);
							jQuery(element).css("display", "block");
							jQuery("a[class*='thickbox']",element).each(function(){
								tb_init(this);
							});
							initMoreActions(element);
						}
					);
				}
			
				function toggleGroupContents(element, stateId) {
					jQuery("a.j-group-header, span.j-group-icon", element).click(function() {
						var row = jQuery("div.row", element);
						var courseBg = jQuery(row).parent("div.course-bg");
						var orgId = jQuery(courseBg).attr("id");
						var course = jQuery(row).nextAll("div.j-course-contents");
						var groupIcon = jQuery("span.j-group-icon", element);
						if (jQuery(course).html() == null) {
							loadGroupContents(courseBg, stateId);
							saveCollapsed(orgId, "false");
							jQuery(groupIcon).html("<img src='images/tree_open.gif'/>");
						} else {
							var display = course.css("display");
							if (jQuery.browser.msie && jQuery.browser.version == '6.0') {
								course.slideToggle("fast");
							} else {
								course.toggle("fast");
							}
							if (display == "none") {
								saveCollapsed(orgId, "false");
								jQuery(groupIcon).html("<img src='images/tree_open.gif'/>");
							} else if (display == "block") {
								saveCollapsed(orgId, "true");
								jQuery(groupIcon).html("<img src='images/tree_closed.gif'/>");
							}
						}
						makeAllUnsortable();
					});
				}
				
				function saveCollapsed(orgId, collapsed) {
					jQuery.ajax({
						url: "servlet/updateCollapsedGroup",
						data: {
							orgId: orgId, 
							collapsed: collapsed
						}
					});
				}
				
				function loadGroupContents(courseBg, stateId) {
					var orgId = jQuery(courseBg).attr("id");
					jQuery.ajax({
						url: "displayGroup.do",
						data: {
							orgId: orgId, 
							stateId: stateId,
							display: 'contents'
						},
						success: function(html) {
							jQuery(courseBg).append(html);
							registerToolTip(this);
							// unregister and re-register thickbox for this group in order to avoid double
							// registration of thickbox for existing elements (i.e. group 'add lesson' link)
							$('a.thickbox'+jQuery(courseBg).attr("id")).unbind("click");
							tb_init('a.thickbox'+jQuery(courseBg).attr("id"));
						}
					});
				}
				
				function registerToolTip(element) {
					jQuery("a.disabled-sequence-name-link, a.sequence-name-link", element).ToolTip({
						className: 'sequence-description-tooltip',
						position: 'mouse',
						delay: 300
					});
					jQuery("a.disabled-sequence-name-link, a.sequence-name-link", element).each(function(i, element) {
						var title = jQuery(element).attr("title");
						if (title!=null) {
							var newTitle = title.replace(/\r\n/g,"<BR>").replace(/\n/g,"<BR>")
							jQuery(element).attr("title", newTitle);
						}
					});
				}
				
				function initMoreActions(element) {
				    
					var id = jQuery(element).attr("id");
					var menuSelector = "a#more-actions-button-" + id;
					var ulSelector = "ul#more-actions-list-" + id;
				    
				    $(menuSelector).click( function() {
				    	// slide up all other menus
		    		    $("ul[id^=more-actions-list-]:visible:not(" + ulSelector + ")").slideUp("fast");
		    		    				    	
				    	// show this menu
				    	$(ulSelector).css("top", $(this).position().bottom);
				    	$(ulSelector).css("left", $(this).position().left);
				    	$(ulSelector).slideToggle("fast"); return false;
				    });
				    
				    $(window).resize(function(){
				    	$(ulSelector).css("top", $(menuSelector).position().bottom);
				    	$(ulSelector).css("left", $(menuSelector).position().left);
			    	});
			    	

				}
				
				function makeOrgSortable(orgId) {
					var org = jQuery("div.course-bg#"+orgId);
					if (jQuery("div.j-lessons", org).size() > 0) {
						var jLessons = jQuery("div.j-lessons#"+orgId+"-lessons");
						var jLessonsTable = jQuery("table.lesson-table",jLessons);
						makeSortable(jLessonsTable, "j-single-lesson");
						jQuery("div.j-subgroup-lessons>table.lesson-table", org).each(function() {
							makeSortable(jQuery(this), "j-single-subgroup-lesson");
						});
						jQuery("div.mycourses-right-buttons", jLessons).html("<a class=\"sorting\" title=\""+getSortingEnabledText()+"\"><img src=\"images/sorting_enabled.gif\"></a>");
					}
				}
				
				function makeAllUnsortable() {
					jQuery("div.course-bg").each(function() {
						var orgId = this.id;
						if (jQuery("div.j-lessons", this).size() > 0) {
							var jLessons = jQuery("div.j-lessons#"+orgId+"-lessons")
							var jLessonsTable = jQuery("table.lesson-table",jLessons);
							var link = jQuery("div.mycourses-right-buttons", jLessons);
							if (link.html().indexOf(getSortingEnabledText()) >= 0) {
								jLessonsTable.SortableDestroy();
								link.html("<a class=\"sorting\" onclick=\"makeOrgSortable("+orgId+")\" title=\""+getEnableSortingText()+"\"><img src=\"images/sorting_disabled.gif\"></a>");
								jQuery("div.j-subgroup-lessons>table.lesson-table", this).each(function() {
									jQuery(this).SortableDestroy();
								});
							}
						}
					});
				}
				
				function makeSortable(element, acceptClass) {
					jQuery(element).Sortable({
						accept: acceptClass,
						axis: "vertically",
						containment: [jQuery(element).offset().left,
						jQuery(element).offset().top,
						jQuery(element).width(),
						jQuery(element).height()],
						onStop: function() {
							var ids = [];
							jQuery(this).siblings("tr").each(function(i, element) {
								ids.push(element.id);
							});
							var jLessonsId = jQuery(this).parents("div[class$='lessons']").attr("id");
							var dashIndex = jLessonsId.indexOf("-");
							var orgId = (dashIndex>0 ? jLessonsId.substring(0, dashIndex) : jLessonsId);
							jQuery.ajax({
								url: "servlet/saveLessonOrder",
								data: {
									orgId: orgId, 
									ids: ids.join(",")
								},
								error: function(a,b) {
									refresh();
								}
							});
						}
					});
				}
	//-->				