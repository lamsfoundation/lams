				numGroups: {
					required: "#groupControlGroups:checked",
				 	min: {
						param: 1,
						depends: function(element) {
							return $("#groupControlGroups").is(":checked");
						} },
					max: { 
						param: 99,
						depends: function(element) {
							return $("#groupControlGroups").is(":checked");
						} }
					},
				numLearners: {
					required: "#groupControlLearners:checked",
					min: {
						param: 1,
						depends: function(element) {
							return $("#groupControlLearners").is(":checked");
						} },
					max: { 
						param: 99,
						depends: function(element) {
							return $("#groupControlLearners").is(":checked");
						} }
					}
