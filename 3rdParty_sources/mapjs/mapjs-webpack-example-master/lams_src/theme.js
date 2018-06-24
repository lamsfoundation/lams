/*global module */
module.exports = {};
module.exports.default =  {
	'name': 'MindMup V1',
	'node': [{
		'name': 'default',
		'cornerRadius': 10.0,
		'backgroundColor': '#E0E0E0',
		'border': {
			'type': 'surround',
			'line': {
				'color': '#707070',
				'width': 1.0
			}
		},
		'shadow': [{
			'color': '#070707',
			'opacity': 0.4,
			'offset': {
				'width': 2,
				'height': 2
			},
			'radius': 2
		}],
		'text': {
			'margin': 5.0,
			'alignment': 'center',
			'color': '#4F4F4F',
			'lightColor': '#EEEEEE',
			'darkColor': '#000000',
			'font': {
				'lineSpacing': 2.5,
				'size': 9,
				'weight': 'bold'
			}
		},
		'connections': {
			'default': {
				'h': 'center',
				'v': 'center'
			},
			'from': {
				'horizontal': {
					'h': 'nearest-inset',
					'v': 'center'
				}
			},
			'to': {
				'h': 'nearest',
				'v': 'center'
			}
		},
		'decorations': {
			'height': 20,
			'edge': 'top',
			'overlap': true,
			'position': 'end'
		}
	},
	{
		'name': 'level_1',
		'backgroundColor': '#22AAE0'
	},
	{
		'name': 'activated',
		'border': {
			'type': 'surround',
			'line': {
				'color': '#22AAE0',
				'width': 3.0,
				'style': 'dotted'
			}
		}
	},
	{
		'name': 'level_1.activated',
		'border': {
			'type': 'surround',
			'line': {
				'color': '#EEEEEE',
				'width': 3.0,
				'style': 'dotted'
			}
		}
	},
	{
		'name': 'selected',
		'shadow': [{
			'color': '#000000',
			'opacity': 0.9,
			'offset': {
				'width': 2,
				'height': 2
			},
			'radius': 2
		}]
	},
	{
		'name': 'collapsed',
		'shadow': [{
			'color': '#888888',
			'offset': {
				'width': 0,
				'height': 1
			},
			'radius': 0
		},
		{
			'color': '#FFFFFF',
			'offset': {
				'width': 0,
				'height': 3
			},
			'radius': 0
		},
		{
			'color': '#888888',
			'offset': {
				'width': 0,
				'height': 4
			},
			'radius': 0
		},
		{
			'color': '#FFFFFF',
			'offset': {
				'width': 0,
				'height': 6
			},
			'radius': 0
		},
		{
			'color': '#888888',
			'offset': {
				'width': 0,
				'height': 7
			},
			'radius': 0
		}
		]
	},
	{
		'name': 'collapsed.selected',
		'shadow': [{
			'color': '#FFFFFF',
			'offset': {
				'width': 0,
				'height': 1
			},
			'radius': 0
		},
		{
			'color': '#888888',
			'offset': {
				'width': 0,
				'height': 3
			},
			'radius': 0
		},
		{
			'color': '#FFFFFF',
			'offset': {
				'width': 0,
				'height': 6
			},
			'radius': 0
		},
		{
			'color': '#555555',
			'offset': {
				'width': 0,
				'height': 7
			},
			'radius': 0
		},
		{
			'color': '#FFFFFF',
			'offset': {
				'width': 0,
				'height': 10
			},
			'radius': 0
		},
		{
			'color': '#333333',
			'offset': {
				'width': 0,
				'height': 11
			},
			'radius': 0
		}
		]
	},
	{
		'name': 'attr_group_supporting',
		'backgroundColor': '#00FF00'
	},
	{
		'name': 'attr_group_opposing',
		'backgroundColor': '#FF0000'
	}
	],
	'connector': {
		'default': {
			'type': 'quadratic',
			'controlPoint': {
				'above': {'width': 0, 'height': 0.75},
				'below': {'width': 0, 'height': 0.75},
				'horizontal': {'width': 0, 'height': 0}
			},
			'line': {
				'color': '#707070',
				'width': 1.0
			}
		}
	}
};

