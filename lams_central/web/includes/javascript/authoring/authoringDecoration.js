﻿/**
 * This file contains methods for Decoration (annotations, containers) manipulation on canvas.
 */


/**
 * Stores different Decoration types structures.
 */
var DecorationDefs = {
	/**
	 * Abstract class for Region, Optional and Floating ActivityDefs.
	 */
	Container : function(id, uiid, title) {
		this.id = +id || null;
		this.uiid = +uiid || (layout.ld ? ++layout.ld.maxUIID : null);
		this.title = title;
		this.childActivities = [];
		
		this.drawContainer = DecorationDefs.methods.container.draw;
		this.fit = DecorationDefs.methods.container.fit;
	},
	
	
	/**
	 * Constructor for label annotation.
	 */
	Label : function(id, uiid, x, y, title){
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		// set a default title, if none provided
		this.title = title || LABELS.DEFAULT_ANNOTATION_LABEL_TITLE;
		
		this.draw = DecorationDefs.methods.label.draw;
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.labelProperties;
		}
		
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for region annotation.
	 */
	Region : function(id, uiid, x, y, x2, y2, title, color) {
		DecorationDefs.Container.call(this, id, uiid, title);
		// we don't use it for region
		this.childActivities = null;
		
		this.draw = DecorationDefs.methods.region.draw;
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.regionProperties;
		}
		
		this.draw(x, y, x2, y2, color);
	},
	
	
	methods : {
		container : {
			
			draw : function(x, y, x2, y2, color){
				// check for new coordinates or just take them from the existing shape
				var box = this.items   ? this.items.shape.getBBox() : null,
					x = x != undefined ? x : box.x,
					y = y != undefined ? y : box.y,
					// take into account minimal size of rectangle
					x2 = x2 ? Math.max(x2, x + layout.conf.regionEmptyWidth) : x + box.width,
					y2 = y2 ? Math.max(y2, y + layout.conf.regionEmptyHeight) : y + box.height,
					color =  color ? color : this.items.shape.attr('fill');
	
				if (box) {
					this.items.remove();
				}
				
				// the label
				this.items = Snap.set();
				if (this.title) {
					var label = paper.text(x + 7, y + 14, this.title)
									 .attr(layout.defaultTextAttributes)
					 				 .attr('text-anchor', 'start');
					if (!isReadOnlyMode){
						label.attr('cursor', 'pointer');
					}
					
					GeneralLib.toBack(label);
					this.items.push(label);
					
					// make sure title fits
					x2 = Math.max(x2, label.getBBox().x2 + 5);
				}
				
				// the rectangle
				this.items.shape = paper.path(Snap.format('M {x} {y} H {x2} V {y2} H {x} z',
														  {
														   'x'  : x,
														   'y'  : y,
														   'x2' : x2,
														   'y2' : y2
														  }))
						 			    .attr({
						 			    	'stroke' : layout.colors.activityBorder,
						 			    	'fill'   : color
						 			    });
				GeneralLib.toBack(this.items.shape);
				this.items.push(this.items.shape);
				
				if (isReadOnlyMode){
					if (activitiesOnlySelectable) {
						this.items.shape.attr('cursor', 'pointer');
						GeneralLib.applyToSet(this.items, 'click',[HandlerLib.itemClickHandler]);
					}
				} else {
					this.items.shape.attr('cursor', 'pointer');
					GeneralLib.applyToSet(this.items, 'mousedown',[HandlerDecorationLib.containerMousedownHandler]);
					GeneralLib.applyToSet(this.items, 'click',[HandlerLib.itemClickHandler]);
				}
			},	
	
			
			/**
			 * Adjust the annotation so it envelops its child activities and nothing more.
			 */
			fit : function() {
				var childActivities = DecorationLib.getChildActivities(this.items.shape);
				if (childActivities.length == 0) {
					return;
				}
	
				ActivityLib.removeSelectEffect(this);
				
				var allElements = Snap.set();
				$.each(childActivities, function(){
					allElements.push(this.items.shape);
				});
				// big rectangle enveloping all child activities
				var box = allElements.getBBox();
				
				// add some padding
				this.draw(box.x - layout.conf.containerActivityPadding,
						  box.y - layout.conf.containerActivityPadding,
						  box.x2 + layout.conf.containerActivityPadding,
						  box.y2 + layout.conf.containerActivityPadding);
			}
		},
		
		
		/**
		 * Label methods
		 */
		label : {
			draw : function(x, y) {
				var x = x ? x : this.items.shape.attr('x'),
					y = y ? y : this.items.shape.attr('y');
				
				if (this.items) {
					this.items.remove();
				}
				
				this.items = Snap.set();
				this.items.shape = paper.text(x, y, this.title)
										.attr(layout.defaultTextAttributes)
										.attr('text-anchor', 'start');
				this.items.push(this.items.shape);
				
				this.items.shape.attr('cursor', 'pointer');
				GeneralLib.applyToSet(this.items, 'click',[HandlerLib.itemClickHandler]);
				if (!isReadOnlyMode){
					this.items.shape.mousedown(HandlerDecorationLib.labelMousedownHandler);
				}
				
				GeneralLib.applyToSet(this.items, 'data', ['parentObject', this]);
			}
		},
		
		
		/**
		 * Region methods
		 */
		region : {
			draw : function(x, y, x2, y2, color){
				this.drawContainer(x, y, x2, y2, color);
				
				var box = this.items.shape.getBBox();
				
				if (!isReadOnlyMode){
					this.items.fitButton = paper.circle(box.x2 - 10, box.y + 10, 5)
										 .attr({
											'stroke'  : null,
											'fill'    : 'blue',
											// hide it for now
											'display' : 'none',
											'cursor'  : 'pointer',
											'title'   : LABELS.REGION_FIT_BUTTON_TOOLTIP
										 })
										 .click(function(event){
											event.stopImmediatePropagation();
											var region = this.data('parentObject');
											region.fit();
											ActivityLib.addSelectEffect(region, true);
										 });
					this.items.push(this.items.fitButton);
					
					this.items.resizeButton = paper.path(Snap.format('M {x} {y} v {side} h -{side} z',
																	 {
																 	  'x' : box.x2,
																 	  'y' : box.y2 - 15,
																 	  'side' : 15
																	 })
														)
												   .attr({
													 'stroke' : null,
													 'fill'   : 'blue',
													 // hide it for now
													 'display' : 'none',
													 'cursor' : 'se-resize'
												   });
					
					this.items.resizeButton.mousedown(HandlerDecorationLib.resizeRegionStartHandler);
					this.items.push(this.items.resizeButton);
				}
				
				GeneralLib.applyToSet(this.items, 'data', ['parentObject', this]);
			}
		}
	}
},



/**
 * Contains utility methods for Decoration manipulation.
 */
DecorationLib = {
	
	/**
	 * Adds a string on the canvas
	 */
	addLabel : function(x, y, title) {
		var label = new DecorationDefs.Label(null, null, x, y, title);
		layout.labels.push(label);
		GeneralLib.setModified(true);
		return label;
	},
		
	
	/**
	 * Adds a coloured rectange on the canvas
	 */
	addRegion : function(x, y, x2, y2, title, color) {
		var region = new DecorationDefs.Region(null, null,
						 x, y, x2, y2, title, color ? color : layout.colors.annotation);
		layout.regions.push(region);
		GeneralLib.setModified(true);
		return region;
	},
	
	
	/**
	 * Get activities enveloped by given container
	 */
	getChildActivities : function(shape){
		var result = [];
		$.each(layout.activities, function(){
			if (shape != this.items.shape) {
				var activityBox = this.items.shape.getBBox(),
					shapeBox = shape.getBBox();
				
				if (Snap.path.isPointInsideBBox(shapeBox,activityBox.x, activityBox.y)
					&& Snap.path.isPointInsideBBox(shapeBox, activityBox.x2, activityBox.y2)) {
					result.push(this);
				}
			}
		});
		
		var parentObject = shape.data('parentObject');
		// store the result in the shape's object
		if (parentObject && !(parentObject instanceof DecorationDefs.Region)) {
			parentObject.childActivities = result;
		}
		return result;
	},
	
	
	removeLabel : function(label) {
		layout.labels.splice(layout.labels.indexOf(label), 1);
		label.items.remove();
		GeneralLib.setModified(true);
	},
	
	
	removeRegion : function(region) {
		layout.regions.splice(layout.regions.indexOf(region), 1);
		region.items.remove();
		GeneralLib.setModified(true);
	}
};

// set prototype hierarchy
DecorationDefs.Region.prototype = new DecorationDefs.Container;
ActivityDefs.ParallelActivity.prototype = new DecorationDefs.Container;
ActivityDefs.OptionalActivity.prototype = new DecorationDefs.Container;
ActivityDefs.FloatingActivity.prototype = new DecorationDefs.Container;