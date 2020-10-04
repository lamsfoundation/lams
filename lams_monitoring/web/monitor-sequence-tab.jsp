<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<lams:html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Monitor: Learners</title>
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components-timeline.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap4.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>css/components-monitoring.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>css/components-monitoring-responsive.css">
	<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
	
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap4.bundle.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.bundle.min.js"></script>
	<script type="text/javascript">
		document.getElementsByTagName("html")[0].className += " js";
		
		// utils.js
		window.chartColors = {
			red: '#C772E6',
			orange: 'rgb(255, 159, 64)',
			yellow: 'rgb(255, 205, 86)',
			green: 'rgb(75, 192, 192)',
			blue: 'rgb(54, 162, 235)',
			purple: 'rgb(153, 102, 255)',
			grey: 'rgb(201, 203, 207)',
			blue: '#57B2F5'
		};

		(function(global) {
			var MONTHS = [
				'January',
				'February',
				'March',
				'April',
				'May',
				'June',
				'July',
				'August',
				'September',
				'October',
				'November',
				'December'
			];

			var COLORS = [
				'#4dc9f6',
				'#f67019',
				'#f53794',
				'#537bc4',
				'#acc236',
				'#166a8f',
				'#00a950',
				'#58595b',
				'#8549ba'
			];

			var Samples = global.Samples || (global.Samples = {});
			var Color = global.Color;

			Samples.utils = {
				// Adapted from http://indiegamr.com/generate-repeatable-random-numbers-in-js/
				srand: function(seed) {
					this._seed = seed;
				},

				rand: function(min, max) {
					var seed = this._seed;
					min = min === undefined ? 0 : min;
					max = max === undefined ? 1 : max;
					this._seed = (seed * 9301 + 49297) % 233280;
					return min + (this._seed / 233280) * (max - min);
				},

				numbers: function(config) {
					var cfg = config || {};
					var min = cfg.min || 0;
					var max = cfg.max || 1;
					var from = cfg.from || [];
					var count = cfg.count || 8;
					var decimals = cfg.decimals || 8;
					var continuity = cfg.continuity || 1;
					var dfactor = Math.pow(10, decimals) || 0;
					var data = [];
					var i, value;

					for (i = 0; i < count; ++i) {
						value = (from[i] || 0) + this.rand(min, max);
						if (this.rand() <= continuity) {
							data.push(Math.round(dfactor * value) / dfactor);
						} else {
							data.push(null);
						}
					}

					return data;
				},

				labels: function(config) {
					var cfg = config || {};
					var min = cfg.min || 0;
					var max = cfg.max || 100;
					var count = cfg.count || 8;
					var step = (max - min) / count;
					var decimals = cfg.decimals || 8;
					var dfactor = Math.pow(10, decimals) || 0;
					var prefix = cfg.prefix || '';
					var values = [];
					var i;

					for (i = min; i < max; i += step) {
						values.push(prefix + Math.round(dfactor * i) / dfactor);
					}

					return values;
				},

				months: function(config) {
					var cfg = config || {};
					var count = cfg.count || 12;
					var section = cfg.section;
					var values = [];
					var i, value;

					for (i = 0; i < count; ++i) {
						value = MONTHS[Math.ceil(i) % 12];
						values.push(value.substring(0, section));
					}

					return values;
				},

				color: function(index) {
					return COLORS[index % COLORS.length];
				},

				transparentize: function(color, opacity) {
					var alpha = opacity === undefined ? 0.5 : 1 - opacity;
					return Color(color).alpha(alpha).rgbString();
				}
			};

			// DEPRECATED
			window.randomScalingFactor = function() {
				return Math.round(Samples.utils.rand(-100, 100));
			};

			// INITIALIZATION

			Samples.utils.srand(Date.now());

			// Google Analytics
			/* eslint-disable */
			if (document.location.hostname.match(/^(www\.)?chartjs\.org$/)) {
				(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
				(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
				m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
				})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
				ga('create', 'UA-28909194-3', 'auto');
				ga('send', 'pageview');
			}
			/* eslint-enable */

		}(this));
		
		$(document).ready(function(){
			// main.js
			(function(){
			  // Vertical Timeline - by CodyHouse.co
				function VerticalTimeline( element ) {
					this.element = element;
					this.blocks = this.element.getElementsByClassName("cd-timeline__block");
					this.images = this.element.getElementsByClassName("cd-timeline__img");
					this.contents = this.element.getElementsByClassName("cd-timeline__content");
					this.offset = 0.8;
					this.hideBlocks();
				};

				VerticalTimeline.prototype.hideBlocks = function() {
					if ( !"classList" in document.documentElement ) {
						return; // no animation on older browsers
					}
					//hide timeline blocks which are outside the viewport
					var self = this;
					for( var i = 0; i < this.blocks.length; i++) {
						(function(i){
							if( self.blocks[i].getBoundingClientRect().top > window.innerHeight*self.offset ) {
								self.images[i].classList.add("cd-timeline__img--hidden"); 
								self.contents[i].classList.add("cd-timeline__content--hidden"); 
							}
						})(i);
					}
				};

				VerticalTimeline.prototype.showBlocks = function() {
					if ( ! "classList" in document.documentElement ) {
						return;
					}
					var self = this;
					for( var i = 0; i < this.blocks.length; i++) {
						(function(i){
							if( self.contents[i].classList.contains("cd-timeline__content--hidden") && self.blocks[i].getBoundingClientRect().top <= window.innerHeight*self.offset ) {
								// add bounce-in animation
								self.images[i].classList.add("cd-timeline__img--bounce-in");
								self.contents[i].classList.add("cd-timeline__content--bounce-in");
								self.images[i].classList.remove("cd-timeline__img--hidden");
								self.contents[i].classList.remove("cd-timeline__content--hidden");
							}
						})(i);
					}
				};

				var verticalTimelines = document.getElementsByClassName("js-cd-timeline"),
					verticalTimelinesArray = [],
					scrolling = false;
				if( verticalTimelines.length > 0 ) {
					for( var i = 0; i < verticalTimelines.length; i++) {
						(function(i){
							verticalTimelinesArray.push(new VerticalTimeline(verticalTimelines[i]));
						})(i);
					}

					//show timeline blocks on scrolling
					window.addEventListener("scroll", function(event) {
						if( !scrolling ) {
							scrolling = true;
							(!window.requestAnimationFrame) ? setTimeout(checkTimelineScroll, 250) : window.requestAnimationFrame(checkTimelineScroll);
						}
					});
				}

				function checkTimelineScroll() {
					verticalTimelinesArray.forEach(function(timeline){
						timeline.showBlocks();
					});
					scrolling = false;
				};
			})();
			
			
			// char-user.js
			
			var ctx = document.getElementById('myChart').getContext('2d');
			var myChart = new Chart(ctx, {
			    type: 'doughnut',
			    borderWidth: 0,
			    data: {
			    	elements: {
				        arc: {
				            borderWidth: 0,
				            fontSize: 0,
				        }
				    },
			        datasets: [{
			            data: [70, 10],
			            backgroundColor: [
			                '#1AD9B2',
			                '#FFCF60'
			            ],
			            label: 'Dataset 1',
			            borderWidth: 0,
			        }],
			        labels: ['Not started', 'In process']
			    },
			    options: {
				    responsive: true,
					legend: {
						display: false,
					},
					animation: {
						animateScale: true,
						animateRotate: true
					},
			        scales: {
			            xAxes: [{
			            	gridLines: {
				                color: "rgba(0, 0, 0, 0)",
				                zeroLineColor: 'transparent'
				            },
				            ticks: {
					            display: false
					        }
				        }],
				        yAxes: [{
				            gridLines: {
				                color: "rgba(0, 0, 0, 0)",
				            },
				            ticks: {
				                stepSize: 1,
				                beginAtZero: false,
				                display: false
				            }  
				        }]
			        },
			        gridLines: {
						display:false
					}
			    }
			});
			
			var presets = window.chartColors;
			var utils = Samples.utils;
			var inputs = {
				min: -100,
				max: 100,
				count: 8,
				decimals: 2,
				continuity: 1
			};

			function generateData(config) {
				return utils.numbers(Chart.helpers.merge(inputs, config || {}));
			}

			function generateLabels(config) {
				return utils.months(Chart.helpers.merge({
					count: inputs.count,
					section: 3
				}, config || {}));
			}

			var options = {
				maintainAspectRatio: false,
				spanGaps: false,
				elements: {
					line: {
						tension: 0.000001
					}
				},
				legend: {
					display: false,
				},
				plugins: {
					filler: {
						propagate: false
					}
				},
				scales: {
					xAxes: [{
						ticks: {
							autoSkip: false,
							maxRotation: 0
						}
					}]
				},
				scales: {
	            xAxes: [{
	            	gridLines: {
		                color: "rgba(0, 0, 0, 0)"
		            },
		            ticks: {
			            display: false
			        }
		        }],
		        yAxes: [{
		            gridLines: {
		                color: "rgba(0, 0, 0, 0)",
		            },
		            ticks: {
		                stepSize: 1,
		                display: false
		            }  
		        }]
	        },
	        gridLines: {
				display:false
			}
			};

			[false, 'origin', 'start'].forEach(function(boundary, index) {

				// reset the random seed to generate the same data for all charts
				utils.srand(8);

				new Chart('chart-2', {
					type: 'line',
					data: {
						labels: generateLabels(),
						datasets: [{
							backgroundColor: utils.transparentize(presets.red),
							borderColor: presets.red,
							data: generateData(),
							label: 'Dataset',
							fill: boundary
						},{
							backgroundColor: utils.transparentize(presets.blue),
							borderColor: presets.blue,
							data: generateData(),
							label: 'Dataset',
							fill: boundary
						}]
					},
					options: Chart.helpers.merge(options, {
						title: {
							text: 'fill: ' + boundary,
							display: true
						}
					})
				});
			});

			// eslint-disable-next-line no-unused-vars
			function toggleSmooth(btn) {
				var value = btn.classList.toggle('btn-on');
				Chart.helpers.each(Chart.instances, function(chart) {
					chart.options.elements.line.tension = value ? 0.4 : 0.000001;
					chart.update();
				});
			}

			// eslint-disable-next-line no-unused-vars
			function randomize() {
				var seed = utils.rand();
				Chart.helpers.each(Chart.instances, function(chart) {
					utils.srand(seed);

					chart.data.datasets.forEach(function(dataset) {
						dataset.data = generateData();
					});

					chart.update();
				});
			}
			
			$('.hamburger').click(function(){
				$(this).toggleClass('active');
				$('.sidebar, .main-content').toggleClass('active');
			});
			
			$('.table-sorter-top.row').click(function(){
				var slide = $(this).attr('slide');
				$(slide).slideToggle();
			});
		});
	</script>	
</head>
<body class="user-page">
<div class="wrapper d-flex">
	<div class="sidebar d-flex align-items-start flex-column">
		<ul class="side-menu mb-auto">
			<li class="logo-li">
				<a href="#"><img src="<lams:LAMSURL/>images/components/lams_logo_white.png" alt="#" /></a>
			</li>
			<li class="menu-li">
				<a href="<lams:WebAppURL/>monitor5.jsp"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a,.b,.c,.d{fill:none;}.b,.c,.d{stroke:#3c42e0;stroke-width:2px;}.b,.c{stroke-linejoin:round;}.c,.d{stroke-linecap:round;}</style></defs><g transform="translate(-44 -351)"><rect class="a" width="24" height="24" transform="translate(44 351)"/><g transform="translate(1 13)"><rect class="b" width="5" height="5" transform="translate(53 354)"/><rect class="b" width="5" height="5" transform="translate(53 341)"/><rect class="b" width="5" height="5" transform="translate(45 354)"/><rect class="b" width="5" height="5" transform="translate(61 354)"/><path class="c" d="M47.5,353.938V350H63.594v4"/><path class="d" d="M47.5,358v-8" transform="translate(8 -4)"/></g></g></svg></a>
			</li>
			<li class="menu-li active">
				<a href="#"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a,.b{fill:none;}.a{stroke:#acb5cc;stroke-linecap:round;stroke-linejoin:round;stroke-width:2px;}</style></defs><g transform="translate(-45 -200)"><g transform="translate(45.5 199.5)"><path class="a" d="M16.045,27.955V26.136A3.636,3.636,0,0,0,12.409,22.5H5.136A3.636,3.636,0,0,0,1.5,26.136v1.818" transform="translate(0 -7.091)"/><path class="a" d="M14.773,8.136A3.636,3.636,0,1,1,11.136,4.5,3.636,3.636,0,0,1,14.773,8.136Z" transform="translate(-2.364)"/><path class="a" d="M32.727,28.031V26.213A3.636,3.636,0,0,0,30,22.7" transform="translate(-11.227 -7.168)"/><path class="a" d="M24,4.7a3.636,3.636,0,0,1,0,7.045" transform="translate(-8.864 -0.077)"/></g><rect class="b" width="24" height="24" transform="translate(45 200)"/></g></svg></a>
			</li>
			<li class="menu-li">
				<a href="#"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a{fill:none;}.b{fill:#acb5cc;}</style></defs><path class="a" d="M0,0H24V24H0Z"/><path class="b" d="M18,2H6A2.006,2.006,0,0,0,4,4V20a2.006,2.006,0,0,0,2,2H18a2.006,2.006,0,0,0,2-2V4A2.006,2.006,0,0,0,18,2ZM9,4h2V9l-1-.75L9,9Zm9,16H6V4H7v9l3-2.25L13,13V4h5Z"/></svg></a>
			</li>
		</ul>
		<div class="user-col active">
			<img src="<lams:LAMSURL/>images/components/img1.png" alt="#" />
			<span></span>
		</div>
	</div>
	<div class="main-content">
		<div class="main-content-inner">
			<header class="header d-flex justify-content-between">
				<div class="menu-hamburger-col d-flex">
					<div class="hamburger">
						<span></span>
						<span></span>
						<span></span>
					</div>
					<p>Lesson Name</p>
				</div>
				<div class="top-menu d-flex">
					<form>
						<input type="text" name="" placeholder="Search Student">
						<img src="<lams:LAMSURL/>images/components/search.svg" alt="#" />
					</form>
					<div class="top-menu-btn d-flex">
						<a href="#"><img src="<lams:LAMSURL/>images/components/icon2.svg" alt="#" /></a>
					</div>
				</div>
			</header>
			<div class="content row">
				<div class="col-12 content-left content-left1">
					<div class="row">
						<div class="col-12 score-col">
							<div class="row">
								<div class="col-12 col-sm-6 col-lg-3">
									<div class="score-col-inner user-score-col d-flex" id="user-score-col1">
										<div class="user-score-icon"></div>
										<div>
											<h1>98%</h1>
											<p>some text</p>
										</div>
									</div>
								</div>
								<div class="col-12 col-sm-6 col-lg-3">
									<div class="score-col-inner user-score-col d-flex" id="user-score-col2">
										<div class="user-score-icon"></div>
										<div>
											<h1>98%</h1>
											<p>some text</p>
										</div>
									</div>
								</div>
								<div class="col-12 col-sm-6 col-lg-3">
									<div class="score-col-inner user-score-col d-flex" id="user-score-col3">
										<div class="user-score-icon"></div>
										<div>
											<h1>98%</h1>
											<p>some text</p>
										</div>
									</div>
								</div>
								<div class="col-12 col-sm-6 col-lg-3">
									<div class="score-col-inner user-score-col d-flex" id="user-score-col4">
										<div class="user-score-icon"></div>
										<div>
											<h1>98%</h1>
											<p>some text</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-12 user-table-main">
					<div class="user-table-data">
						<table class="user-table" width="100%" cellpadding="0" cellspacing="0" border="0">
							<tbody>
								<tr>
									<td>
										<div class="table-sorter-header row">
											<div class="col-6">
												<h1>Name</h1>
											</div>
											<div class="col-6">
												<h1>Progress</h1>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="table-sorter-top row" slide="#slide1">
											<div class="col-6 table-user-name">
												<img src="<lams:LAMSURL/>images/components/img1.png" alt="#" />
												<p>Andrew Barnes</p>
											</div>
											<div class="col-6 table-user-progress">
												<div class="progress-col">
													<div class="progress1" style="width: 45%;"></div>
												</div>
												<p>50%</p>
											</div>
										</div>
										<div class="table-sorter-content row" id="slide1">
											<div class="row m-0">
												<div class="col-12 col-md-4 table-content-left">
													<div class="content-user-info">
														<div class="user-info-top d-flex">
															<img src="<lams:LAMSURL/>images/components/001.png" alt="#" />
															<div>
																<h1>James<br>Summerville</h1>
																<p><a href="mailto: james.sum@gmail.com">james.sum@gmail.com</a></p>
															</div>
														</div>
														<div class="user-info-progress-col d-flex">
															<div class="user-info-progress-bar">
																<div class="user-info-progress" width="50%"></div>
															</div>
															<p>50%</p>
														</div>
													</div>
													<div class="content-user-graph1">
														<h1 class="table-graph-h1">Chart title</h1>
														<canvas id="chart-2" width="392" height="256" class="chartjs-render-monitor" style="display: block; width: 392px; height: 256px;"></canvas>
													</div>
													<div class="content-user-graph2">
														<h1 class="table-graph-h1">Chart title</h1>
														<div class="content-graph2-inner">
															<div class="content-graph2">
																<canvas id="myChart" class="chartjs-render-monitor"></canvas>
															</div>
															<ul>
																<li>
																	<span></span>
																	<p>Param #1</p>
																</li>
																<li>
																	<span></span>
																	<p>Param #2</p>
																</li>
															</ul>
														</div>
													</div>
												</div>
												<div class="col-12 col-md-8 table-content-right pr-0">
													<div class="table-content-right-inner">
														<section class="cd-timeline js-cd-timeline">
														    <div class="container max-width-lg cd-timeline__container">
														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														       <div class="cd-timeline__content text-component">
														          <h2>Readings <span>Top 10</span></h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->
														    </div>
														</section> <!-- cd-timeline -->
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="table-sorter-top row" slide="#slide2">
											<div class="col-6 table-user-name">
												<img src="<lams:LAMSURL/>images/components/002.png" alt="#" />
												<p>Kayla Ellis</p>
											</div>
											<div class="col-6 table-user-progress">
												<div class="progress-col">
													<div class="progress1 red" style="width: 25%;"></div>
												</div>
												<p>50%</p>
											</div>
										</div>
										<div class="table-sorter-content row" id="slide2">
											<div class="row m-0">
												<div class="col-12 col-md-4 table-content-left">
													<div class="content-user-info">
														<div class="user-info-top d-flex">
															<img src="<lams:LAMSURL/>images/components/001.png" alt="#" />
															<div>
																<h1>James<br>Summerville</h1>
																<p><a href="mailto: james.sum@gmail.com">james.sum@gmail.com</a></p>
															</div>
														</div>
														<div class="user-info-progress-col d-flex">
															<div class="user-info-progress-bar">
																<div class="user-info-progress" width="50%"></div>
															</div>
															<p>50%</p>
														</div>
													</div>
													<div class="content-user-graph1">
														<h1 class="table-graph-h1">Chart title</h1>
														<canvas id="chart-3" width="392" height="256" class="chartjs-render-monitor" style="display: block; width: 392px; height: 256px;"></canvas>
													</div>
													<div class="content-user-graph2">
														<h1 class="table-graph-h1">Chart title</h1>
														<div class="content-graph2-inner">
															<div class="content-graph2">
																<canvas id="myChart1" class="chartjs-render-monitor"></canvas>
															</div>
															<ul>
																<li>
																	<span></span>
																	<p>Param #1</p>
																</li>
																<li>
																	<span></span>
																	<p>Param #2</p>
																</li>
															</ul>
														</div>
													</div>
												</div>
												<div class="col-12 col-md-8 table-content-right pr-0">
													<div class="table-content-right-inner">
														<section class="cd-timeline js-cd-timeline">
														    <div class="container max-width-lg cd-timeline__container">
														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														       <div class="cd-timeline__content text-component">
														          <h2>Readings <span>Top 10</span></h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->
														    </div>
														</section> <!-- cd-timeline -->
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="table-sorter-top row" slide="#slide3">
											<div class="col-6 table-user-name">
												<img src="<lams:LAMSURL/>images/components/003.png" alt="#" />
												<p>Caroline Alvarez</p>
											</div>
											<div class="col-6 table-user-progress">
												<div class="progress-col">
													<div class="progress1 red" style="width: 12%;"></div>
												</div>
												<p>50%</p>
											</div>
										</div>
										<div class="table-sorter-content row" id="slide3">
											<div class="row m-0">
												<div class="col-12 col-md-4 table-content-left">
													<div class="content-user-info">
														<div class="user-info-top d-flex">
															<img src="<lams:LAMSURL/>images/components/001.png" alt="#" />
															<div>
																<h1>James<br>Summerville</h1>
																<p><a href="mailto: james.sum@gmail.com">james.sum@gmail.com</a></p>
															</div>
														</div>
														<div class="user-info-progress-col d-flex">
															<div class="user-info-progress-bar">
																<div class="user-info-progress" width="50%"></div>
															</div>
															<p>50%</p>
														</div>
													</div>
													<div class="content-user-graph1">
														<h1 class="table-graph-h1">Chart title</h1>
														<canvas id="chart-4" width="392" height="256" class="chartjs-render-monitor" style="display: block; width: 392px; height: 256px;"></canvas>
													</div>
													<div class="content-user-graph2">
														<h1 class="table-graph-h1">Chart title</h1>
														<div class="content-graph2-inner">
															<div class="content-graph2">
																<canvas id="myChart2" class="chartjs-render-monitor"></canvas>
															</div>
															<ul>
																<li>
																	<span></span>
																	<p>Param #1</p>
																</li>
																<li>
																	<span></span>
																	<p>Param #2</p>
																</li>
															</ul>
														</div>
													</div>
												</div>
												<div class="col-12 col-md-8 table-content-right pr-0">
													<div class="table-content-right-inner">
														<section class="cd-timeline js-cd-timeline">
														    <div class="container max-width-lg cd-timeline__container">
														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														       <div class="cd-timeline__content text-component">
														          <h2>Readings <span>Top 10</span></h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->
														    </div>
														</section> <!-- cd-timeline -->
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="table-sorter-top row" slide="#slide4">
											<div class="col-6 table-user-name">
												<img src="<lams:LAMSURL/>images/components/img1.png" alt="#" />
												<p>Andrew Barnes</p>
											</div>
											<div class="col-6 table-user-progress">
												<div class="progress-col">
													<div class="progress1 " style="width: 45%;"></div>
												</div>
												<p>50%</p>
											</div>
										</div>
										<div class="table-sorter-content row" id="slide4">
											<div class="row m-0">
												<div class="col-12 col-md-4 table-content-left">
													<div class="content-user-info">
														<div class="user-info-top d-flex">
															<img src="<lams:LAMSURL/>images/components/001.png" alt="#" />
															<div>
																<h1>James<br>Summerville</h1>
																<p><a href="mailto: james.sum@gmail.com">james.sum@gmail.com</a></p>
															</div>
														</div>
														<div class="user-info-progress-col d-flex">
															<div class="user-info-progress-bar">
																<div class="user-info-progress" width="50%"></div>
															</div>
															<p>50%</p>
														</div>
													</div>
													<div class="content-user-graph1">
														<h1 class="table-graph-h1">Chart title</h1>
														<canvas id="chart-5" width="392" height="256" class="chartjs-render-monitor" style="display: block; width: 392px; height: 256px;"></canvas>
													</div>
													<div class="content-user-graph2">
														<h1 class="table-graph-h1">Chart title</h1>
														<div class="content-graph2-inner">
															<div class="content-graph2">
																<canvas id="myChart3" class="chartjs-render-monitor"></canvas>
															</div>
															<ul>
																<li>
																	<span></span>
																	<p>Param #1</p>
																</li>
																<li>
																	<span></span>
																	<p>Param #2</p>
																</li>
															</ul>
														</div>
													</div>
												</div>
												<div class="col-12 col-md-8 table-content-right pr-0">
													<div class="table-content-right-inner">
														<section class="cd-timeline js-cd-timeline">
														    <div class="container max-width-lg cd-timeline__container">
														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														       <div class="cd-timeline__content text-component">
														          <h2>Readings <span>Top 10</span></h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->
														    </div>
														</section> <!-- cd-timeline -->
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="table-sorter-top row" slide="#slide5">
											<div class="col-6 table-user-name">
												<img src="<lams:LAMSURL/>images/components/002.png" alt="#" />
												<p>Kayla Ellis</p>
											</div>
											<div class="col-6 table-user-progress">
												<div class="progress-col">
													<div class="progress1 red" style="width: 25%;"></div>
												</div>
												<p>25%</p>
											</div>
										</div>
										<div class="table-sorter-content row" id="slide5">
											<div class="row m-0">
												<div class="col-12 col-md-4 table-content-left">
													<div class="content-user-info">
														<div class="user-info-top d-flex">
															<img src="<lams:LAMSURL/>images/components/001.png" alt="#" />
															<div>
																<h1>James<br>Summerville</h1>
																<p><a href="mailto: james.sum@gmail.com">james.sum@gmail.com</a></p>
															</div>
														</div>
														<div class="user-info-progress-col d-flex">
															<div class="user-info-progress-bar">
																<div class="user-info-progress" width="50%"></div>
															</div>
															<p>50%</p>
														</div>
													</div>
													<div class="content-user-graph1">
														<h1 class="table-graph-h1">Chart title</h1>
														<canvas id="chart-6" width="392" height="256" class="chartjs-render-monitor" style="display: block; width: 392px; height: 256px;"></canvas>
													</div>
													<div class="content-user-graph2">
														<h1 class="table-graph-h1">Chart title</h1>
														<div class="content-graph2-inner">
															<div class="content-graph2">
																<canvas id="myChart4" class="chartjs-render-monitor"></canvas>
															</div>
															<ul>
																<li>
																	<span></span>
																	<p>Param #1</p>
																</li>
																<li>
																	<span></span>
																	<p>Param #2</p>
																</li>
															</ul>
														</div>
													</div>
												</div>
												<div class="col-12 col-md-8 table-content-right pr-0">
													<div class="table-content-right-inner">
														<section class="cd-timeline js-cd-timeline">
														    <div class="container max-width-lg cd-timeline__container">
														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														       <div class="cd-timeline__content text-component">
														          <h2>Readings <span>Top 10</span></h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->
														    </div>
														</section> <!-- cd-timeline -->
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="table-sorter-top row" slide="#slide6">
											<div class="col-6 table-user-name">
												<img src="<lams:LAMSURL/>images/components/003.png" alt="#" />
												<p>Caroline Alvarez</p>
											</div>
											<div class="col-6 table-user-progress">
												<div class="progress-col">
													<div class="progress1 red" style="width: 15%;"></div>
												</div>
												<p>15%</p>
											</div>
										</div>
										<div class="table-sorter-content row" id="slide6">
											<div class="row m-0">
												<div class="col-12 col-md-4 table-content-left">
													<div class="content-user-info">
														<div class="user-info-top d-flex">
															<img src="<lams:LAMSURL/>images/components/001.png" alt="#" />
															<div>
																<h1>James<br>Summerville</h1>
																<p><a href="mailto: james.sum@gmail.com">james.sum@gmail.com</a></p>
															</div>
														</div>
														<div class="user-info-progress-col d-flex">
															<div class="user-info-progress-bar">
																<div class="user-info-progress" width="50%"></div>
															</div>
															<p>50%</p>
														</div>
													</div>
													<div class="content-user-graph1">
														<h1 class="table-graph-h1">Chart title</h1>
														<canvas id="chart-7" width="392" height="256" class="chartjs-render-monitor" style="display: block; width: 392px; height: 256px;"></canvas>
													</div>
													<div class="content-user-graph2">
														<h1 class="table-graph-h1">Chart title</h1>
														<div class="content-graph2-inner">
															<div class="content-graph2">
																<canvas id="myChart5" class="chartjs-render-monitor"></canvas>
															</div>
															<ul>
																<li>
																	<span></span>
																	<p>Param #1</p>
																</li>
																<li>
																	<span></span>
																	<p>Param #2</p>
																</li>
															</ul>
														</div>
													</div>
												</div>
												<div class="col-12 col-md-8 table-content-right pr-0">
													<div class="table-content-right-inner">
														<section class="cd-timeline js-cd-timeline">
														    <div class="container max-width-lg cd-timeline__container">
														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														       <div class="cd-timeline__content text-component">
														          <h2>Readings <span>Top 10</span></h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->
														    </div>
														</section> <!-- cd-timeline -->
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="table-sorter-top row" slide="#slide7">
											<div class="col-6 table-user-name">
												<img src="<lams:LAMSURL/>images/components/004.png" alt="#" />
												<p>Diana Perez</p>
											</div>
											<div class="col-6 table-user-progress">
												<div class="progress-col">
													<div class="progress1 green" style="width: 70%;"></div>
												</div>
												<p>70%</p>
											</div>
										</div>
										<div class="table-sorter-content row" id="slide7">
											<div class="row m-0">
												<div class="col-12 col-md-4 table-content-left">
													<div class="content-user-info">
														<div class="user-info-top d-flex">
															<img src="<lams:LAMSURL/>images/components/001.png" alt="#" />
															<div>
																<h1>James<br>Summerville</h1>
																<p><a href="mailto: james.sum@gmail.com">james.sum@gmail.com</a></p>
															</div>
														</div>
														<div class="user-info-progress-col d-flex">
															<div class="user-info-progress-bar">
																<div class="user-info-progress" width="50%"></div>
															</div>
															<p>50%</p>
														</div>
													</div>
													<div class="content-user-graph1">
														<h1 class="table-graph-h1">Chart title</h1>
														<canvas id="chart-8" width="392" height="256" class="chartjs-render-monitor" style="display: block; width: 392px; height: 256px;"></canvas>
													</div>
													<div class="content-user-graph2">
														<h1 class="table-graph-h1">Chart title</h1>
														<div class="content-graph2-inner">
															<div class="content-graph2">
																<canvas id="myChart6" class="chartjs-render-monitor"></canvas>
															</div>
															<ul>
																<li>
																	<span></span>
																	<p>Param #1</p>
																</li>
																<li>
																	<span></span>
																	<p>Param #2</p>
																</li>
															</ul>
														</div>
													</div>
												</div>
												<div class="col-12 col-md-8 table-content-right pr-0">
													<div class="table-content-right-inner">
														<section class="cd-timeline js-cd-timeline">
														    <div class="container max-width-lg cd-timeline__container">
														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														       <div class="cd-timeline__content text-component">
														          <h2>Readings <span>Top 10</span></h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--picture">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-picture.svg" alt="Picture">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--location">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-location.svg" alt="Location">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->

														      <div class="cd-timeline__block">
														        <div class="cd-timeline__img cd-timeline__img--movie">
														          <img src="<lams:LAMSURL/>images/components/cd-icon-movie.svg" alt="Movie">
														        </div> <!-- cd-timeline__img -->

														        <div class="cd-timeline__content text-component">
														          <h2>Readings</h2>
														          <p class="color-contrast-medium">Time Taken <span>40 min</span></p>
														          <p class="color-contrast-medium">Score <span>9/10</span></p>
														          <span class="cd-timeline__date">13 Aug 2020</span>
														        </div> <!-- cd-timeline__content -->
														      </div> <!-- cd-timeline__block -->
														    </div>
														</section> <!-- cd-timeline -->
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
						
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</lams:html>