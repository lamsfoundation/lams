<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<lams:html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>LAMS Monitor</title>
		
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap4.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>css/components-monitoring.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>css/components-monitoring-responsive.css">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap4.bundle.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/chart.bundle.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$('.hamburger').click(function(){
				$(this).toggleClass('active');
				$('.component-sidebar, .monitoring-page-content').toggleClass('active');
			});
			
			var ctx = document.getElementById('myChart').getContext('2d');
			var myChart = new Chart(ctx, {
				type : 'doughnut',
				borderWidth : 0,
				data : {
					elements : {
						arc : {
							borderWidth : 0,
							fontSize : 0,
						}
					},
					datasets : [ {
						data : [ 10, 70, 20 ],
						backgroundColor : [ 'rgba(5, 204, 214, 1)',
								'rgba(255, 195, 55, 1)', 'rgba(253, 60, 165, 1)', ],
						label : 'Dataset 1',
						borderWidth : 0,
					} ],
					labels : [ 'Not started', 'In process', 'Completed' ]
				},
				options : {
					responsive : true,
					legend : {
						display : false,
					},
					animation : {
						animateScale : true,
						animateRotate : true
					}
				}
			});
		});
	</script>
</head>
<body class="component">
<div class="monitoring-page-wrapper">
	<div class="component-sidebar">
		<ul>
			<li class="logo-li">
				<a href="#"><img src="<lams:LAMSURL/>images/components/lams_logo_white.png" alt="#" /></a>
			</li>
			<li class="menu-li active">
				<a href="#"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a,.b,.c,.d{fill:none;}.b,.c,.d{stroke:#3c42e0;stroke-width:2px;}.b,.c{stroke-linejoin:round;}.c,.d{stroke-linecap:round;}</style></defs><g transform="translate(-44 -351)"><rect class="a" width="24" height="24" transform="translate(44 351)"/><g transform="translate(1 13)"><rect class="b" width="5" height="5" transform="translate(53 354)"/><rect class="b" width="5" height="5" transform="translate(53 341)"/><rect class="b" width="5" height="5" transform="translate(45 354)"/><rect class="b" width="5" height="5" transform="translate(61 354)"/><path class="c" d="M47.5,353.938V350H63.594v4"/><path class="d" d="M47.5,358v-8" transform="translate(8 -4)"/></g></g></svg></a>
			</li>
			<li class="menu-li">
				<a href="<lams:WebAppURL/>monitor-sequence-tab.jsp"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a,.b{fill:none;}.a{stroke:#acb5cc;stroke-linecap:round;stroke-linejoin:round;stroke-width:2px;}</style></defs><g transform="translate(-45 -200)"><g transform="translate(45.5 199.5)"><path class="a" d="M16.045,27.955V26.136A3.636,3.636,0,0,0,12.409,22.5H5.136A3.636,3.636,0,0,0,1.5,26.136v1.818" transform="translate(0 -7.091)"/><path class="a" d="M14.773,8.136A3.636,3.636,0,1,1,11.136,4.5,3.636,3.636,0,0,1,14.773,8.136Z" transform="translate(-2.364)"/><path class="a" d="M32.727,28.031V26.213A3.636,3.636,0,0,0,30,22.7" transform="translate(-11.227 -7.168)"/><path class="a" d="M24,4.7a3.636,3.636,0,0,1,0,7.045" transform="translate(-8.864 -0.077)"/></g><rect class="b" width="24" height="24" transform="translate(45 200)"/></g></svg></a>
			</li>
			<li class="menu-li">
				<a href="#"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><defs><style>.a{fill:none;}.b{fill:#acb5cc;}</style></defs><path class="a" d="M0,0H24V24H0Z"/><path class="b" d="M18,2H6A2.006,2.006,0,0,0,4,4V20a2.006,2.006,0,0,0,2,2H18a2.006,2.006,0,0,0,2-2V4A2.006,2.006,0,0,0,18,2ZM9,4h2V9l-1-.75L9,9Zm9,16H6V4H7v9l3-2.25L13,13V4h5Z"/></svg></a>
			</li>
		</ul>
		<!-- 
		<div class="user-col active">
			<img src="<lams:LAMSURL/>images/components/img1.png" alt="#" />
			<span></span>
		</div>
		 -->
	</div>
	<div class="component-page-wrapper monitoring-page-content">
		<header class="d-flex justify-content-between">
			<div class="hamburger-box">
				<div class="hamburger">
					<span></span>
					<span></span>
					<span></span>
				</div>
				<p>Lesson Name</p>
			</div>
			<div class="top-menu">
				<form>
					<input type="text" name="" placeholder="Search Student">
					<img src="<lams:LAMSURL/>images/components/search.svg" alt="#" />
				</form>
				<div class="top-menu-btn">
					<a href="#"><img src="<lams:LAMSURL/>images/components/icon1.svg" alt="#" /></a>
					<a href="#"><img src="<lams:LAMSURL/>images/components/icon2.svg" alt="#" /></a>
					<a href="#"><img src="<lams:LAMSURL/>images/components/icon3.svg" alt="#" /></a>
				</div>
			</div>
		</header>
		<div class="row pt-5">
			<div class="col-12 col-md-3 content-left">
				<div class="graph-col">
					<div class="graph-con">
						<canvas id="myChart" class="chartjs-render-monitor"></canvas>
					</div>
					<ul>
						<li>
							<h6>completion</h6>
						</li>
						<li>
							<span class="graph-count"></span> - Not started
						</li>
						<li>
							<span class="graph-count"></span> - In process
						</li>
						<li>
							<span class="graph-count"></span> - Completed
						</li>
					</ul>
				</div>
				<div class="graph-grades">
					<div class="graph-star-col">
						<img src="<lams:LAMSURL/>images/components/star.png" alt="#" />
						<p>grades</p>
					</div>
					<div class="grades-progress-col">
						<div class="grades-left">
							<div class="grades-score d-flex">
								<p><span>High:</span> 99</p>
								<div class="grades-progress-bar">
									<div class="progress-div" id="progress-bar-1" style="width: 99%">
										<span></span>
									</div>
								</div>
							</div>
							<div class="grades-score d-flex">
								<p><span>Median:</span> 68</p>
								<div class="grades-progress-bar">
									<div class="progress-div" id="progress-bar-2" style="width: 68%">
										<span></span>
									</div>
								</div>
							</div>
							<div class="grades-score d-flex">
								<p><span>Low:</span> 28</p>
								<div class="grades-progress-bar">
									<div class="progress-div" id="progress-bar-3" style="width: 28%">
										<span></span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="score-col">
					<div class="row">
						<div class="col-12 col-sm-6">
							<div class="score-col-inner" id="score-col1">
								<h1>8<span>%</span></h1>
								<p>Students <br>at risk</p>
								<img src="<lams:LAMSURL/>images/components/icon1.png" alt="#" />
							</div>
						</div>
						<div class="col-12 col-sm-6">
							<div class="score-col-inner" id="score-col2">
								<h1>98<span>%</span></h1>
								<p>Average <br>Score</p>
								<img src="<lams:LAMSURL/>images/components/icon2.png" alt="#" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-12 col-md-9 content-right">
				<div class="user-map-col h-100">
					<img src="<lams:LAMSURL/>images/components/user-map.png" alt="#" />
					<div class="map-pn">
						<span><img src="<lams:LAMSURL/>images/components/plus.svg" alt="#" /></span>
						<span><img src="<lams:LAMSURL/>images/components/minus.svg" alt="#" /></span>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-12 col-md-6 mb-4">
				<div class="monitoring-panel tasks-col">
					<h6>required tasks</h6>
					<div class="row">
						<div class="col-3">
							Gate
						</div>
						<div class="col-9">
							<select>
								<option>Open now</option>
								<option>Open now</option>
								<option>Open now</option>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-3">
							Grouping
						</div>
						<div class="col-9">
							<button type="button">Set Groups</button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-12 col-md-6 mb-4">
				<div class="monitoring-panel insight-col">
					<h6>insights</h6>
					<div class="insight-col-content">
						<p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem</p>
						<p><a href="#">read more...</a></p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

</body>
</lams:html>