		// drag and drop learners onto rankings
		interact('.draggable')
		  .draggable({
		    // enable inertial throwing
		    inertia: true,
		    // keep the element within the area of it's parent
			    restrict: {
		      restriction: '#drag-area',
		      endOnly: true,
		      elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
		    },
			    // enable autoScroll
		    autoScroll: true,
		    // call this function on every dragmove event
		    onmove: dragMoveListener
		  });
		
		function dragMoveListener (event) {
		    var target = event.target,
		        // keep the dragged position in the data-x/data-y attributes
		        x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
		        y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;
		
			target.style.webkitTransform =
			target.style.transform =
			  'translate(' + x + 'px, ' + y + 'px)';
		
		    // update the position attributes
		    target.setAttribute('data-x', x);
		    target.setAttribute('data-y', y);
		}
		
		function resetXY(target) {
			// translate the element
			target.style.webkitTransform = target.style.transform = 'none';
			target.setAttribute('data-x', 0);
			target.setAttribute('data-y', 0);
		}
		
		function addClassWithHighlight(item, styleClass) {
			item.classList.add(styleClass);
			item.classList.add('draggableSelected');
		}
		
		function removeClassWithHighlight(item, styleClass) {
			item.classList.remove(styleClass);
			item.classList.remove('draggableSelected');
		}
		
		// enable draggables to be dropped into this
		  interact('.dropzone').dropzone({
		    // only accept elements matching this CSS selector
		    accept: '.draggable',
		    // Require a 25% element overlap for a drop to be possible
		    overlap: 0.25,

		    // listen for drop related events:
		    ondropactivate: function (event) {
		      // add active dropzone feedback
		      event.target.classList.add('drop-active');
		    },
		    ondragenter: function (event) {
		      var draggableElement = event.relatedTarget,
		          dropzoneElement = event.target;

		      // feedback the possibility of a drop
		      addClassWithHighlight(dropzoneElement, 'drop-target');
		      addClassWithHighlight(draggableElement, 'can-drop');
		    },
		    ondragleave: function (event) {
		      // remove the drop feedback style
		      removeClassWithHighlight(event.target, 'drop-target');
		      removeClassWithHighlight(event.relatedTarget, 'can-drop');
		    },
		    ondrop: function (event) {
		    	doDrop(event);
		    },
		    ondropdeactivate: function (event) {
		      // reset the offset for the div the user was moving
		      resetXY(event.relatedTarget);
		      // remove active dropzone feedback
		      event.target.classList.remove('drop-active');
		      removeClassWithHighlight(event.target, 'drop-target');
		    }
		  });
