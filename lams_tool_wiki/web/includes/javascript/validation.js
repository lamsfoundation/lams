		$(function(){
						
			$('form input[name="title"]').blur(function () {
			    var title = $(this).val();
				var re = /^[^<>^#()/\\|'\",{}`]*$/;
				if (re.test(title)) {			    
				    $('.title').hide();
				} else {
				    $('.title').show();
				    $(this).focus();
				    $(this).select();
				}
		
			});


			$('form input[name="newPageTitle"]').blur(function () {
			    var newPageTitle = $(this).val();
				var re = /^[^<>^#()/\\|'\",{}`]*$/;
				if (re.test(newPageTitle)) {			    
				    $('.newPageTitle').hide();
				} else {
				    $('.newPageTitle').show();
				    $(this).focus();
				    $(this).select();
				}
		
			});

			
		});
