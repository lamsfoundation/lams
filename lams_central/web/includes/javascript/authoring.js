var paper = null;
var templateContainer = null;

$(document).ready(function() {
	paper = Raphael('canvas');
	templateContainer = paper.rect(0, 0, 120, 200).attr({
		'stroke-width' : 3
	});

	drawTemplate(1, 'icon_forum.png', 'Forum');
	drawTemplate(2, 'icon_notebook.png', 'Notebook');
});

function drawTemplate(index, icon, text) {
	var template = paper.set();
	var shape = template.shape = paper.rect(10, 10 + (index - 1) * 55, 100, 50)
			.attr({
				'fill' : 'yellow'
			});
	template.push(shape);

	var icon = paper.image('images/svg/' + icon, shape.attr('x')
			+ shape.attr('width') / 2 - 10, shape.attr('y') + 5, 30, 30);
	template.push(icon);

	var label = paper.text(shape.attr('x') + shape.attr('width') / 2, shape
			.attr('y') + 40, text);
	template.push(label);

	template.drag(function(dx, dy) {
		startDrag(this.helper, dx, dy);
	}, function() {
		this.helper = this.clone();
		this.helper.shape = this.helper[0];
	}, function() {
		var helper = this.helper;
		this.helper = null;
		if (Raphael.isBBoxIntersect(templateContainer.getBBox(), helper
				.getBBox())) {
			helper.remove();
			return;
		}

		endDrag(helper);
		helper.drag(function(dx, dy) {
			startDrag(this, dx, dy);
		}, null, function() {
			endDrag(this, Raphael.isBBoxIntersect(templateContainer.getBBox(),
					this.getBBox()));
		}, helper, null, helper);

	}, template, null, template);
}

function startDrag(activity, dx, dy) {
	activity.transform('t' + dx + ' ' + dy);
}

function endDrag(activity, cancelDrag) {
	if (!cancelDrag) {
		var transformation = activity.shape.attr('transform');
		activity.forEach(function(elem) {
			elem.attr({
				'x' : elem.attr('x') + transformation[0][1],
				'y' : elem.attr('y') + transformation[0][2]
			});
		});
	}
	activity.transform('');
}