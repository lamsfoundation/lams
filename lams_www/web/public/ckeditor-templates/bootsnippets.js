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
		title: 'textParagraphFullWidth',
		image: 'paragraph.svg',
		description: '',
		css: '',
		html: 
		'<div class="container-fluid">' +
		'	<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis et euismod arcu. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vivamus consectetur interdum metus at ultricies. Quisque eget felis arcu. Integer nec vestibulum augue. Duis viverra dignissim ante quis dictum. Donec tempus nulla eros, at consectetur nulla pellentesque quis. Phasellus egestas odio eget tempor malesuada. Aliquam porta libero elit. Ut rhoncus placerat dui, sed posuere ipsum porttitor a.</p>' +
		'</div>'
	},
	{
		title: 'textParagraphAndHeading',
		image: 'paragraph_with_heading.svg',
		description: '',
		css: '',
		html: 
		'<div class="container-fluid">' +
		'	<h1>Heading</h1>' +
		'	<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis et euismod arcu. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vivamus consectetur interdum metus at ultricies. Quisque eget felis arcu. Integer nec vestibulum augue. Duis viverra dignissim ante quis dictum. Donec tempus nulla eros, at consectetur nulla pellentesque quis. Phasellus egestas odio eget tempor malesuada. Aliquam porta libero elit. Ut rhoncus placerat dui, sed posuere ipsum porttitor a.</p>' +
		'</div>'
	},
	{
		title: 'textTwoColumns',
		image: 'two_columns.svg',
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
		title: 'textThreeColumns',
		image: 'three_columns.svg',
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
		title: 'textStatement',
		image: 'statement.svg',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
		'	<div class="col-sm-8 offset-sm-2">' +
		'		<hr>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta, sunt rerum corporis. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta, sunt rerum corporis. Ab veniam aperiam numquam cupiditate maiores repudiandae ea dicta.</p>' +
		'		<hr>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'textNote',
		image: 'note.svg',
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
        title: 'textLabel',
        image: 'labels.svg',
        description: '',
        css: '',
        html:
        '<span class="badge text-bg-secondary">Default</span>&nbsp;' +
        '<span class="badge text-bg-primary">Primary</span>&nbsp;' +
		'<span class="badge text-bg-success">Success</span>&nbsp;' +
		'<span class="badge text-bg-info">Info</span>&nbsp;' +
		'<span class="badge text-bg-warning">Warning</span>&nbsp;' +
		'<span class="badge text-bg-danger">Danger</span>&nbsp;'
    },
	{
		title: 'textQuote',
		image: 'quote_paragraph_below.svg',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
		'	<div class="col-sm-8 offset-sm-2 lead"><em>"It is the same with people as it is with riding a bike. Only when moving can one comfortably maintain one\'s balance."</em></div>' +
		'</div>' +
		'<div class="row">' +
		'	<div class="col">' +
		'		<img class="rounded-circle shadow mx-auto d-block" src="/lams/www/public/ckeditor-templates/images/albert.jpg" style="width:80px" alt="">' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'textPanel',
		image: 'panel.svg',
		html: 
		'<div class="card">' +
		'    <div class="card-header bg-primary">' +
        '       <h3 class="card-title text-white">Card title</h3>' +
		'    </div>' +
		'    <div class="card-body">' +
		'       <span class="float-end" style="display:none;"><i class="fa fa-minus"></i></span>' +
		'       <div class="card-text">' +
		'		  Card content' +
		'       </div>' +
		'    </div>' +
		'</div>'
	},
	{
		title: 'textPanelFooter',
		image: 'panel_footer.svg',
		html: 
			'<div class="card">' +
			'    <div class="card-header bg-primary">' +
			'       <h3 class="card-title text-white">' +
			'         Card title' +
			'		</h3>' +
			'       <span class="float-end" style="display:none;"><i class="fa fa-minus"></i></span>' +
			'    </div>' +
			'    <div class="card-body">' +
			'		<div class="card-text">' +
			'			Card content' +
			'		</div>' +
			'	</div>' +
			'	<div class="card-footer">Card footer</div>' +
			'</div>'
	},
	{
		title: 'textJumbotron',
		image: 'jumbotron_with_text.svg',
		html: 
		'	<div class="jumbotron">' +
		'   	<h3>Header</h3>' +
		'    	<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique facere necessitatibus quo laboriosam consequuntur</p>' +
		'	</div>'
	},
	{
		title: 'textWaveHeaders',
        description: 'New headers with waves. You can change the colour by double clicking on it.',
		image: 'background-blue3.svg',
		html:
		'<style type="text/css">@import url("https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap");</style>' +
        '<div class="lams-wavepanel wave-blue">' + 
		'<div class="container"><div class="row"><div class="col">' +
		'<h1 style="font-size: 59px;font-family: \'Montserrat\', sans-serif; font-weight: 700;" class="text-white">Lorem Ipsum</h1>' +
		'<p style="font-size: 28px;font-family: \'Montserrat\', sans-serif;">Lorem ipsum dolor sit amet, consectetur adipiscing elit</p></div></div></div></div>' +
		'<div>&nbsp;</div>' +
		'<div style="font-size: 19px;font-family: \'Montserrat\', sans-serif;"><span style="font-size:36px;">Sed ut perspiciatis!&nbsp;</span></div>' +
		'<div style="font-size: 19px;font-family: \'Montserrat\', sans-serif;">&nbsp;</div>' +
		'<div style="font-size: 19px;font-family: \'Montserrat\', sans-serif;">At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi.</div>' +
		'<div style="font-size: 19px;font-family: \'Montserrat\', sans-serif;">&nbsp;</div>' 
		},

	]
} );

//Register a snippets definition set named "image_snippets".
CKEDITOR.addSnippets( 'bootsnippets-image', {
	// The name of sub folder which hold the shortcut preview images of the snippets.
	imagesPath: "/lams/www/public/ckeditor-templates/images/",

	// The templates definitions.
	snippets: [ 
	{
		title: 'imageCenteredImage',
		image: 'centered_image.png',
		description: '',
		css: '',
		html: 
		'<div class="col-md-12">' +
			'<img class="img-fluid mx-auto d-block shadow-sm rounded" src="/lams/www/public/ckeditor-templates/images/centered_image.jpg">' +
		'</div>'
	},
	{
		title: 'imageImageFullWidth',
		image: 'image_full_width.png',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
	    ' 	<div class="col-md-12" style="padding-left:0; padding-right:0;">' +
	    '  		<img src="/lams/www/public/ckeditor-templates/images/image_full_width.jpg" class="img-fluid mx-auto d-block shadow-sm rounded" style="width: 100%;"/>' +
	    '	</div>' +
	    '</div>'
	},
	{
		title: 'imageImageAndText',
		image: 'image_and_text.png',
		description: '',
		css: '',
		html: 
		'<div class="row">' +
		'	<div class="col-md-4 col-sm-6">' +
		'		<img class="img-fluid mx-auto d-block shadow-sm rounded" src="/lams/www/public/ckeditor-templates/images/sanfran.jpg">' +
		'	</div>' +
	
		'	<div class="col-md-8 col-sm-6">' +
		'		<h4>Heading</h4>' +
		'		<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'imageTextOnImage',
		image: 'text_on_image.png',
		description: '',
		css: '',
		html: 
		'<div class="container">' +
		'	<div class="col-xs-12 col-sm-9 offset-sm-1">' +
		'		<img class="img-fluid" style="margin: auto" src="/lams/www/public/ckeditor-templates/images/desk.jpg">' +
		'		<div class="carousel-caption">' +
		'			<h2>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut eleifend, turpis non luctus tempor</h2>' +
		'		</div>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'imageImageWithinPanel',
		image: 'image_within_a_panel.png',
		description: '',
		css: '',
		html: 
		'<div class="col-md-12">' +
		'	<div class="panel panel-default">' +
		'		<div class="panel-heading">Image header</div>' +
			
		'		<div class="panel-body">' +
		'			<img class="img-fluid mx-auto d-block shadow-sm rounded" src="/lams/www/public/ckeditor-templates/images/learn.jpg">' +
		'			<h4>Title</h4>' +
		'			<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in lobortis nisl, vitae iaculis sapien. Phasellus ultrices gravida massa luctus ornare. Suspendisse blandit quam elit, eu imperdiet neque semper et.</p>' +
		'		</div>' +
		
		'		<div class="panel-footer text-center">&nbsp;</div>' +
		'	</div>'
	},
	{
		title: 'imageCarousel',
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
		'        <img src="/lams/www/public/ckeditor-templates/images/sealine.jpg" style="width:100%;">' +
		'        <div class="carousel-caption">' +
		'          <h3>Ireland</h3>' +
		'          <p>Sealine</p>' +
		'        </div>' +
		'      </div>' +
		
		'      <div class="item">' +
		'        <img src="/lams/www/public/ckeditor-templates/images/meduana.jpg" style="width:100%;">' +
		'        <div class="carousel-caption">' +
		'          <h3>Germany</h3>' +
		'          <p>Speicherstadt, Hamburg</p>' +
		'        </div>' +
		'      </div>' +
		    
		'      <div class="item">' +
		'        <img src="/lams/www/public/ckeditor-templates/images/fuji.jpg" style="width:100%;">' +
		'        <div class="carousel-caption">' +
		'          <h3>Japan</h3>' +
		'          <p>Mount Fuji</p>' +
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
		title: 'imageTwoColumnImages',
		image: 'two_column_images.png',
		description: '',
		html: 
		'<div class="row">' +
		'    <div class="col-md-6">' +
		'	    <div class="thumbnail">' +
		'	        <img alt="" src="/lams/www/public/ckeditor-templates/images/stairs.jpg" style="width:100%" class="img-fluid mx-auto d-block shadow-sm rounded" />' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	  <div class="col-md-6">' +
		'	    <div class="thumbnail">' +
		'	        <img alt="" src="/lams/www/public/ckeditor-templates/images/study.jpg" style="width:100%" class="img-fluid mx-auto d-block shadow-sm rounded" />' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'imageThreeColumnImages',
		image: 'three_column_images.png',
		description: '',
		html: 
		'<div class="row">' +
		'    <div class="col-md-4">' +
		'	    <div class="thumbnail">' +
		'	        <img src="/lams/www/public/ckeditor-templates/images/math.jpg" alt="" style="width:100%" class="img-fluid mx-auto d-block shadow-sm rounded" />' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	  <div class="col-md-4">' +
		'	    <div class="thumbnail">' +
		'	        <img src="/lams/www/public/ckeditor-templates/images/ideas.jpg" alt="" style="width:100%" class="img-fluid mx-auto d-block shadow-sm rounded" />' +
		'	        <div class="caption">' +
		'	          <p>Lorem ipsum donec id elit non mi porta gravida at eget metus.</p>' +
		'	        </div>' +
		'	    </div>' +
		'	  </div>' +
		'	  <div class="col-md-4">' +
		'	    <div class="thumbnail">' +
		'	        <img src="/lams/www/public/ckeditor-templates/images/cogs.jpg" alt="" style="width:100%" class="img-fluid mx-auto d-block shadow-sm rounded" />' +
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
		title: 'multimediaVideo',
		image: 'video.png',
		description: '',
		html:
		'<h2>Responsive Embed</h2>' +
		'<p>Create a responsive video and scale it nicely to the parent element with an 16:9 aspect ratio</p>' +

		'<div class="col-lg-12">' +
		'	<div class="embed-responsive embed-responsive-16by9" data-oembed-url="https://www.youtube.com/embed/">' +
		'		<iframe class="embed-responsive-item" src="https://www.youtube.com/embed/nTFEUsudhfs"></iframe>' +
		'	</div>' +
		'</div>'
	},
	{
		title: 'multimediaVideoWithSideText',
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
		title: 'multimediaAudio',
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
			title: 'advancedScreenHeader',
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
		title: 'advancedTabs',
		image: 'tabs.png',
		description: '',
		html: 
			'<div class="bootstrap-tabs" data-tab-set-title="Tab Set Title" style="background-color:#f8f8f8;border: 1px solid #e8e5e5;border-radius: 5px 5px 40px 10px;padding: 5px;">' +
			'	<ul class="nav nav-tabs" role="tablist"><!-- add tabs here -->' +
			'		<li role="presentation"><a aria-controls="title-tab-1" class="tab-link" data-toggle="tab" href="#title-tab-1" role="tab" target="_blank">Tab 1 Name</a></li>' +
			'		<li role="presentation"><a aria-controls="title-tab-2" class="tab-link" data-toggle="tab" href="#title-tab-2" role="tab" target="_blank">Tab 2 Name</a></li>' +
			'		<li role="presentation"><a aria-controls="title-tab-3" class="tab-link" data-toggle="tab" href="#title-tab-3" role="tab" target="_blank">Tab 3 Name</a></li>' +
			'		<li class="active" role="presentation"><a aria-controls="title-tab-4" class="tab-link" data-toggle="tab" href="#title-tab-4" role="tab" target="_blank">Tab 4 Name</a></li>' +
			'	</ul>' +
		
			'	<div class="tab-content"><!-- add tab panels here -->' +
			'		<div class="tab-pane" id="title-tab-1" role="tabpanel">' +
			'			<div class="tab-pane-content" style="padding:1em">Tab 1 Content</div>' +
			'		</div>' +
			
			'		<div class="tab-pane" id="title-tab-2" role="tabpanel">' +
			'			<div class="tab-pane-content" style="padding:1em">Tab 2 Content</div>' +
			'		</div>' +
			
			'		<div class="tab-pane" id="title-tab-3" role="tabpanel">' +
			'			<div class="tab-pane-content" style="padding:1em">Tab 3 Content</div>' +
			'		</div>' +
			
			'		<div class="tab-pane active" id="title-tab-4" role="tabpanel">' +
			'			<div class="tab-pane-content" style="padding:1em">Tab 4 Content</div>' +
			'		</div>' +
			'	</div>' +
			'</div>'
		}
	]
} );