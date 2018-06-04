/*global require, module, __dirname, process */
const path = require('path');
module.exports = {
	entry: './lams_src/start',
	devtool: 'source-map',
	output: {
		filename: '[name].js',
		path: path.resolve(__dirname, 'site/')
	}
};
