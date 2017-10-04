/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

//Register a snippets definition set named "grid_snippets".
CKEDITOR.addSnippets( 'bootsnippets-text', {
	// The name of sub folder which hold the shortcut preview images of the snippets.
	imagesPath: "/lams/www/public/ckeditor-templates/images/",

	// The templates definitions.
	snippets: [
	{
		title: 'Paragraph (full width)',
		image: 'paragraph.png',
		description: '',
		css: '',
		html: 
		'<div class="container-fluid">' +
		'	<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis et euismod arcu. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vivamus consectetur interdum metus at ultricies. Quisque eget felis arcu. Integer nec vestibulum augue. Duis viverra dignissim ante quis dictum. Donec tempus nulla eros, at consectetur nulla pellentesque quis. Phasellus egestas odio eget tempor malesuada. Aliquam porta libero elit. Ut rhoncus placerat dui, sed posuere ipsum porttitor a.</p>' +
		'</div>'
	},
	{
		title: 'Paragraph and heading',
		image: 'paragraph_with_heading.png',
		description: '',
		css: '',
		html: 
		'<div class="container-fluid">' +
		'	<h1>Heading</h1>' +
		'	<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis et euismod arcu. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vivamus consectetur interdum metus at ultricies. Quisque eget felis arcu. Integer nec vestibulum augue. Duis viverra dignissim ante quis dictum. Donec tempus nulla eros, at consectetur nulla pellentesque quis. Phasellus egestas odio eget tempor malesuada. Aliquam porta libero elit. Ut rhoncus placerat dui, sed posuere ipsum porttitor a.</p>' +
		'</div>'
	},
	{
		title: 'Two columns',
		image: 'two_columns.png',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
		'	<div class="col-sm-6">' +
		'		<h3>Column 1</h3>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Minima expedita incidunt rerum.</p>' +
		'	</div>' +
	
		'	<div class="col-sm-6">' +
		'		<h3>Column 2</h3>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptate placeat suscipit maxime tenetur officiis asperiores quae molestias fugiat praesentium dolorum.</p>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Three columns',
		image: 'three_columns.png',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
		'	<div class="col-sm-4">' +
		'		<h3>Column 1</h3>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Minima expedita incidunt rerum.</p>' +
		'	</div>' +
	
		'	<div class="col-sm-4">' +
		'		<h3>Column 2</h3>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptate placeat suscipit maxime tenetur officiis asperiores quae molestias fugiat praesentium dolorum.</p>' +
		'	</div>' +
	
		'	<div class="col-sm-4">' +
		'		<h3>Column 3</h3>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta, sunt rerum corporis. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta, sunt rerum corporis. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta.</p>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Statement',
		image: 'statement.png',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
		'	<div class="col-md-6 col-md-offset-3">' +
		'		<hr>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta, sunt rerum corporis. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta, sunt rerum corporis. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta.</p>' +
		'		<hr>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Note',
		image: 'note.png',
		description: '',
		css: '',
		html: 
		'<div class="alert alert-success">' +
		'  <strong>Success!</strong> Indicates a successful or positive action.' +
		'</div>' +
	
		'<div class="alert alert-info">' +
		'  <strong>Info!</strong> Indicates a neutral informative change or action.' +
		'</div>' +
	
		'<div class="alert alert-warning">' +
		'  <strong>Warning!</strong> Indicates a warning that might need attention.' +
		'</div>' +
	
		'<div class="alert alert-danger">' +
		'  <strong>Danger!</strong> Indicates a dangerous or potentially negative action.' +
		'</div>'
	},
	{
		title: 'Quote (center picture) paragraph above',
		image: 'quote_paragraph_below.png',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
		'	<div class="col-md-6 col-md-offset-3">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</div>' +
		'</div>' +
		'<div class="row">' +
		'	<div class="text-center voffset20">' +
		'		<img src="https://www.w3schools.com/bootstrap/img_avatar1.png" style="width:60px">' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Table',
		image: 'panel_table.png',
		description: '',
		css: 'panel_table.css',
		html: 
		'<div class="container"><div class="row"><div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">' +
		'	 <div class="panel panel-default panel-table">' +
		'	     <div class="panel-heading">' +
		'	         <div class="tr">' +
		'	             <div class="td">heading</div>' +
		'	             <div class="td">heading</div>' +
		'	             <div class="td">heading</div>' +
		'	         </div>' +
		'	     </div>' +
		'	     <div class="panel-body">' +
		'	         <div class="tr">' +
		'	             <div class="td">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique facere necessitatibus quo laboriosam consequuntur</div>' +
		'	             <div class="td">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Excepturi aliquam placeat odit quasi autem distinctio veritatis ex numquam nihil</div>' +
		'	             <div class="td">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Excepturi aliquam placeat odit quasi autem distinctio veritatis ex numquam nihil nulla tempora a dolorem omnis beatae facilis perspiciatis doloribus? Error dolore!</div>' +
		'	         </div>' +
		'	     </div>' +
		'	     <div class="panel-footer">' +
		'	         <div class="tr">' +
		'	             <div class="td">footer</div>' +
		'	             <div class="td">footer</div>' +
		'	             <div class="td">footer</div>' +
		'	         </div>' +
		'	     </div>' +
		'	 </div>' +
		'</div></div></div>'
	},
	{
		title: 'Panel (heading and body)',
		image: 'panel.png',
		html: 
		'<script type="text/javascript" src="/lams/includes/javascript/clickable.js"></script>' +
		'<div class="panel panel-primary lams-bootpanel">' +
		'    <div class="panel-heading">' +
		'        <h3 class="panel-title">' +
		'            Panel title' +
		'		</h3>' +
		'        <span class="pull-right" style="display:none;"><i class="fa fa-minus"></i></span>' +
		'    </div>' +
		'    <div class="panel-body">' +
		'		<div>' +
		'			Panel content' +
		'		</div>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Panel (heading, body and footer)',
		image: 'panel_footer.png',
		html: 
			'<script type="text/javascript" src="/lams/includes/javascript/clickable.js"></script>' +
			'<div class="panel panel-primary lams-bootpanel">' +
			'    <div class="panel-heading">' +
			'        <h3 class="panel-title">' +
			'            Panel title' +
			'		</h3>' +
			'        <span class="pull-right" style="display:none;"><i class="fa fa-minus"></i></span>' +
			'    </div>' +
			'    <div class="panel-body">' +
			'		<div>' +
			'			Panel content' +
			'		</div>' +
			'	</div>' +
			'	<div class="panel-footer">Panel footer</div>' +
			'</div>'
	},
	{
		title: 'Jumbotron with text',
		image: 'jumbotron_with_text.png',
		html: 
		'	<div class="jumbotron">' +
		'   	<h1>Header</h1>' +
		'    	<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique facere necessitatibus quo laboriosam consequuntur</p>' +
		'	</div>'
	}
	
	]
} );

//Register a snippets definition set named "image_snippets".
CKEDITOR.addSnippets( 'bootsnippets-image', {
	// The name of sub folder which hold the shortcut preview images of the snippets.
	imagesPath: "/lams/www/public/ckeditor-templates/images/",

	// The templates definitions.
	snippets: [ 
	{
		title: 'Centered image',
		image: 'centered_image.png',
		description: '',
		css: '',
		html: 
		'<div class="col-md-12">' +
			'<img alt="..." class="img-responsive center-block" src="/lams/www/public/ckeditor-templates/images/orangutan_house.jpg">' +
		'</div>'
	},
	{
		title: 'Image full width',
		image: 'image_full_width.png',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
	    ' 	<div class="col-md-12" style="padding-left:0; padding-right:0;">' +
	    '  		<img src="/lams/www/public/ckeditor-templates/images/orangutan_house.jpg" class="img-responsive" style="width: 100%;"/>' +
	    '	</div>' +
	    '</div>'
	},
	{
		title: 'Image and text',
		image: 'image_and_text.png',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
		'	<div class="col-md-4 col-sm-6">' +
		'		<img class="img-responsive" src="/lams/www/public/ckeditor-templates/images/orangutan_house.jpg">' +
		'	</div>' +
	
		'	<div class="col-md-8 col-sm-6">' +
		'		<h4>Heading</h4>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Text on image',
		image: 'text_on_image.png',
		description: '',
		css: '',
		html: 
		'<div class="container">' +
		'	<div class="col-md-6">' +
		'		<img alt="test" class="img-responsive" src="/lams/www/public/ckeditor-templates/images/soaring_umbrellas.jpg">' +
		'		<div class="carousel-caption">' +
		'			<h2>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut eleifend, turpis non luctus tempor</h2>' +
		'		</div>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Image within a panel',
		image: 'image_within_a_panel.png',
		description: '',
		css: '',
		html: 
		'<div class="col-md-6">' +
		'	<div class="panel panel-default">' +
		'		<div class="panel-heading">Image Title</div>' +
			
		'		<div class="panel-body">' +
		'			<img alt="" class="img-responsive center-block" src="/lams/www/public/ckeditor-templates/images/soaring_umbrellas.jpg">' +
		'			<h4>Show the Description by default!</h4>' +
		'			<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in lobortis nisl, vitae iaculis sapien. Phasellus ultrices gravida massa luctus ornare. Suspendisse blandit quam elit, eu imperdiet neque semper et.</p>' +
		'		</div>' +
		
		'		<div class="panel-footer text-center">&nbsp;</div>' +
		'	</div>'
	},
	{
		title: 'Carousel',
		image: 'carousel.png',
		description: '',
		html: 
		'<div id="myCarousel" class="carousel slide" data-ride="carousel">' +
		'    <!-- Indicators -->' +
		'    <ol class="carousel-indicators">' +
		'      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>' +
		'      <li data-target="#myCarousel" data-slide-to="1"></li>' +
		'      <li data-target="#myCarousel" data-slide-to="2"></li>' +
		'    </ol>' +
		
		'    <!-- Wrapper for slides -->' +
		'    <div class="carousel-inner">' +
		
		'      <div class="item active">' +
		'        <img src="/lams/www/public/ckeditor-templates/images/sealine.jpg" alt="Los Angeles" style="width:100%;">' +
		'        <div class="carousel-caption">' +
		'          <h3>Cape Phiolent</h3>' +
		'          <p>Is always so much fun!</p>' +
		'        </div>' +
		'      </div>' +
		
		'      <div class="item">' +
		'        <img src="/lams/www/public/ckeditor-templates/images/desert.jpg" alt="Chicago" style="width:100%;">' +
		'        <div class="carousel-caption">' +
		'          <h3>Iran</h3>' +
		'          <p>Thank you, Iranian desert!</p>' +
		'        </div>' +
		'      </div>' +
		    
		'      <div class="item">' +
		'        <img src="/lams/www/public/ckeditor-templates/images/mountain_ridge.jpg" alt="New York" style="width:100%;">' +
		'        <div class="carousel-caption">' +
		'          <h3>Tehran</h3>' +
		'          <p>We love Tehran!</p>' +
		'        </div>' +
		'      </div>' +
		  
		'    </div>' +
		
		'    <!-- Left and right controls -->' +
		'    <a class="left carousel-control" href="#myCarousel" data-slide="prev">' +
		'      <i class="fa fa-chevron-left" style="top: 50%;position: absolute;"></i>' +
		'      <span class="sr-only">Previous</span>' +
		'    </a>' +
		'    <a class="right carousel-control" href="#myCarousel" data-slide="next">' +
		'      <i class="fa fa-chevron-right" style="top: 50%;position: absolute;"></i>' +
		'      <span class="sr-only">Next</span>' +
		'    </a>' +
		'</div>'
	},
	{
		title: 'Two column images',
		image: 'two_column_images.png',
		description: '',
		html: 
		'<div class="row">' +
		'    <div class="col-md-6">' +
		'	    <div class="thumbnail">' +
		'	        <img src="/lams/www/public/ckeditor-templates/images/sealine.jpg" alt="Lights" style="width:100%">' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	  <div class="col-md-6">' +
		'	    <div class="thumbnail">' +
		'	        <img src="/lams/www/public/ckeditor-templates/images/desert.jpg" alt="Nature" style="width:100%">' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Three column images',
		image: 'three_column_images.png',
		description: '',
		html: 
		'<div class="row">' +
		'    <div class="col-md-4">' +
		'	    <div class="thumbnail">' +
		'	        <img src="/lams/www/public/ckeditor-templates/images/sealine.jpg" alt="Lights" style="width:100%">' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	  <div class="col-md-4">' +
		'	    <div class="thumbnail">' +
		'	        <img src="/lams/www/public/ckeditor-templates/images/desert.jpg" alt="Nature" style="width:100%">' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	  <div class="col-md-4">' +
		'	    <div class="thumbnail">' +
		'	        <img src="/lams/www/public/ckeditor-templates/images/mountain_ridge.jpg" alt="Fjords" style="width:100%">' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	</div>' +
		'</div>'
	}	
	
	]
} );

//Register a snippets definition set named "multimedia_snippets".
CKEDITOR.addSnippets( 'bootsnippets-multimedia', {
	// The name of sub folder which hold the shortcut preview images of the snippets.
	imagesPath: "/lams/www/public/ckeditor-templates/images/",

	// The templates definitions.
	snippets: [ {
		title: 'Video',
		image: 'video.png',
		description: '',
		html:
		'<h2>Responsive Embed</h2>' +
		'<p>Create a responsive video and scale it nicely to the parent element with an 16:9 aspect ratio</p>' +

		'<div class="col-lg-10">' +
		'	<div class="embed-responsive embed-responsive-16by9" data-oembed-url="https://www.youtube.com/embed/">' +
		'		<iframe class="embed-responsive-item" src="https://www.youtube.com/embed/2uTMTyqQxl4"></iframe>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'Video with the side text',
		image: 'video_side_text.png',
		description: '',
		css: '',
		html: 
		'        <div class="col-md-4">' +
		'            <h4>Lorem Ipsum</h4>' +
		'            <p>' +
		'                Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui.' +
		'            </p>' +
		'        </div>' +

		'        <div class="col-md-8">' +
		'            <div class="embed-responsive embed-responsive-16by9" data-oembed-url="https://www.youtube.com/embed/">' +
		'                <iframe class="embed-responsive-item" src="https://www.youtube.com/embed/ghkQoJoipbM"></iframe>' +
		'            </div>' +
		'        </div>'
	},{
		title: 'Audio',
		image: 'audio.png',
		description: '',
		html:
		'<div class="ckeditor-html5-audio" style="padding: 10px; text-align: center;">' +
		'	<audio controls="controls" controlslist="nodownload" src="http://www.sample-videos.com/audio/mp3/wave.mp3">&nbsp;</audio>' +
		'</div>'
	}
	]
} );

//Register a snippets definition set named "default".
CKEDITOR.addSnippets( 'bootsnippets-advanced-layout', {
	// The name of sub folder which hold the shortcut preview images of the snippets.
	imagesPath: "/lams/www/public/ckeditor-templates/images/",

	// The templates definitions.
	snippets: [
		{
			title: 'Screen header',
			image: 'screen_header.png',
			html: 
			'<div>' +
	        '	<div>' +
	        '		<h1 class="text-muted">Company Name</h1>' +
	        '	</div>' +
				
				'<nav class="navbar navbar-default">' +
				'	<div class="container-fluid">' +
				'		<div class="navbar-header"><a class="navbar-brand" href="#" target="_blank">Text</a></div>' +
	
				'		<ul class="nav navbar-nav">' +
				'			<li class="active"><a href="#" target="_blank">Home</a></li>' +
				'			<li><a href="#" target="_blank">Page 1</a></li>' +
				'			<li><a href="#" target="_blank">Page 2</a></li>' +
				'			<li><a href="#" target="_blank">Page 3</a></li>' +
				'		</ul>' +
				'	</div>' +
				'</nav>' +
				
			'</div>'
		},
		{
		title: 'Tabs',
		image: 'tabs.png',
		description: '',
		html: 
			'<div class="bootstrap-tabs" data-tab-set-title="Tab Set Title">' +
			'	<ul class="nav nav-tabs" role="tablist"><!-- add tabs here -->' +
			'		<li role="presentation"><a aria-controls="title-tab-1" class="tab-link" data-toggle="tab" href="#title-tab-1" role="tab" target="_blank">Tab 1 Name</a></li>' +
			'		<li role="presentation"><a aria-controls="title-tab-2" class="tab-link" data-toggle="tab" href="#title-tab-2" role="tab" target="_blank">Tab 2 Name</a></li>' +
			'		<li role="presentation"><a aria-controls="title-tab-3" class="tab-link" data-toggle="tab" href="#title-tab-3" role="tab" target="_blank">Tab 3 Name</a></li>' +
			'		<li class="active" role="presentation"><a aria-controls="title-tab-4" class="tab-link" data-toggle="tab" href="#title-tab-4" role="tab" target="_blank">Tab 4 Name</a></li>' +
			'	</ul>' +
		
			'	<div class="tab-content"><!-- add tab panels here -->' +
			'		<div class="tab-pane" id="title-tab-1" role="tabpanel">' +
			'			<div class="tab-pane-content">Tab 1 Content</div>' +
			'		</div>' +
			
			'		<div class="tab-pane" id="title-tab-2" role="tabpanel">' +
			'			<div class="tab-pane-content">Tab 2 Content</div>' +
			'		</div>' +
			
			'		<div class="tab-pane" id="title-tab-3" role="tabpanel">' +
			'			<div class="tab-pane-content">Tab 3 Content</div>' +
			'		</div>' +
			
			'		<div class="tab-pane active" id="title-tab-4" role="tabpanel">' +
			'			<div class="tab-pane-content">Tab 4 Content</div>' +
			'		</div>' +
			'	</div>' +
			'</div>'
		}
	]
} );
