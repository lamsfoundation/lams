
		var validator;
		var appexDeleteMsg =  "<fmt:message key='authoring.tbl.delete.appex.prompt'/>";
		var mcqDeleteMsg = "<fmt:message key='authoring.tbl.delete.mcq.prompt'/>";
		
		// configure the wizard. Call after doing var validator = $("#templateForm").validate({ 
		// screens need to specify the tabNumber for the save button if they are hiding tabs
		function initializeWizard(validatorIn, startValidationOnTab, tblVersion) {
			validator = validatorIn;
			
			if ( typeof startValidationOnTab === "undefined" )
				startValidationOnTab = 0;

	      	$('#rootwizard').bootstrapWizard({
	      		'nextSelector': '.button-next', 
	      		'previousSelector': '.button-previous',
	      		'onTabShow': function(tab, navigation, index) {
	      			var total = navigation.find('li:visible').length;
	      			var current = index+1;
	      			var percent = (current/total) * 100;
	      			$('#rootwizard .progress-bar').css({width:percent+'%'})
 	      			if ( navigation.find('li:last').get(0) == tab.get(0) ) {
	      				$('.button-save').show();
	      				$('.button-next').hide();
	      			} else {
	      				$('.button-next').show();
	      				$('.button-save').hide();
	      			}
 	      		},
 	      		'tabClass': 'nav nav-pills',
 		  		'onNext': function(tab, navigation, index) {
 		  			if ( index >= (startValidationOnTab + 1) ) {
	 		  			var valid = $("#templateForm").valid();
	 		  			if(!valid) {
	 		  				validator.focusInvalid();
	 		  				return false;
	 		  			}
	 		  		}
 		  		}
	      	});
	    }

		// Remove the display:none or the fields won't be validate as jquery validation is set to only valid non hidden fields. 
		// If we allow validation of hidden fields then we cannot have validation on the "next" button as it uses the hidden fields
		// to avoid validating the fields on other tabs. Still should not be seen as visibility is hidden. Also need to catch
		// the editor update and redo validation otherwise the "field blah is blank" error message won't go away when the user enters text.
		function reconfigureCKEditorInstance(instance) {
 			$('#'+instance.name).css('display','inline');
			instance.on('blur', function() { 
				$('#'+instance.name).valid();
			});
		}				

 		function refreshCKEditors() {
			// make sure all the ckeditors are refreshed, not just the validated ones
			for (var i in CKEDITOR.instances) {
           		CKEDITOR.instances[i].updateElement();
       		}
		}

		function validateCK(textarea) {
          	CKEDITOR.instances[textarea.id].updateElement(); // update textarea
          	var editorcontent = textarea.value.replace(/<[^>]*>/gi, ''); // strip tags
          	return editorcontent.length === 0;
		}
		
		function templateInvalidHandler(e, validator) {
			if (validator.numberOfInvalids()) {
				$("#templateErrorMessages").show();
			} else {
				$("#templateErrorMessages").hide();
			}
		}

		function submitForm(form) {
			$('#error-message').empty();
			refreshCKEditors();
       		
             var jqxhr = $.ajax( {
	        		method: $(form).attr('method'),
					url: getSubmissionURL() + '.do',
                	data: $(form).serialize()
	            	})
				.done(function(data) {
        			var learningDesignID = data.learningDesignID;
        			if ( ! learningDesignID) {
        				var errors = data.errors;
        				if ( errors ) {
        					$('#templateErrorMessages').show();
        					$('#error-message').show();
	        				for (var i = 0; i < errors.length; i++) {
	        					$('#error-message').append('<li class="text-danger">'+errors[i]+'</li>');
	        				}
        				} else {
        					var fatal = data.fatal;
        					if ( fatal ) {
        						alert(fatal);
        					} else { 
	        					alert('Save failed (expected parameters missing). Data returned by server was '+data);
	        				}
	        			}
	        			$('.button-save').button('reset');
				    } else {
					    var title = encodeURIComponent(data.title);
					    location.href='<lams:WebAppURL />authoring/template/createresult.jsp?learningDesignID='+learningDesignID
					    		+'&learningDesigntitle='+title;
       				}
       			})
				.fail(function() {
					alert('Save failed. Please see the server logs for more details.\n\n');
					$('.button-save').button('reset');
				});
		}
		
		function getSubmissionURL() {
			return '<lams:WebAppURL />authoring/template/'+$('#template').val().toLowerCase();
		}
		
		function doGotoList() {
		 	if(confirm("<fmt:message key='authoring.msg.list.cancel.save'/>")){
		 		location.href = '<lams:WebAppURL />authoring/template/list.jsp';
		 	}
		 }

		function doCancel() {
		 	if(confirm("<fmt:message key='authoring.msg.close.cancel.save'/>")){
		 		closeWindow();
		 	}
		}
		// Called by save button
		function doSaveForm() {
			$('.button-save').button('loading');
			$('#templateForm').submit();
		}

		// Triggers the import window. The saving is done in a method saveQTI(formHTML, formName, callerID) which should be defined in the main template jsp file.
		// CallerID can be set to define which tab has triggered the QTI import, as TBL has import on both the RAT Questions and App Ex tabs.
		function importQTI(callerID, limit){
		 	var url = '<lams:LAMSURL/>questions/questionFile.jsp?callerID='+callerID;
		 	if ( limit ) {
		 		url = url + '&limitType='+limit;
		 	}
			// open import pop up window, centered horizontally
			var left = ((screen.width / 2) - (500 / 2));
	    	window.open(url,'QuestionFile','width=500,height=370,scrollbars=yes,top=150,left=' + left);
	    }

		function createAssessment(questionType, numAssessmentsFieldname, containingDivName, qbQuestionUid, collapse) {
			var numAssessments = $('#'+numAssessmentsFieldname);
			var type = questionType ? questionType : 'essay';
			var currNum = numAssessments.val();
			var nextNum = +currNum + 1;
			var newDiv = document.createElement("div");
			newDiv.id = containingDivName+'divassess'+nextNum;
			newDiv.className = 'space-top space-sides';
			var url=getSubmissionURL()+"/createAssessment.do?questionNumber="+nextNum+"&questionType="+type+"&containingDivName="+containingDivName;
			if (qbQuestionUid) {
				url += '&qbQuestionUid=' + qbQuestionUid;
			}
			$('#'+containingDivName).append(newDiv);
			$.ajaxSetup({ cache: true });
			$(newDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					newDiv.remove();
				} else {
					numAssessments.val(nextNum);
					if (collapse) {
						$('.collapse', newDiv).collapse('hide');
					} else {
						newDiv.scrollIntoView();
					}
				}
			});
		}		
		
		function createQuestion(numQuestionsFieldname, newDivPrefix, questionDivName, forward, extraParam, collapse) {
			var numQuestions = $('#'+numQuestionsFieldname);
			var currNum = numQuestions.val();
			var nextNum = +currNum + 1;
			var newDiv = document.createElement("div");
			newDiv.id = newDivPrefix+nextNum;
			var url=getSubmissionURL()+"/createQuestion.do?questionNumber="+nextNum;
			if ( forward && forward.length > 0) {
				url = url + "&forward=" + forward;
			}
			if ( extraParam ) {
				url = url + extraParam;
			}
			$('#'+questionDivName).append(newDiv);
			$.ajaxSetup({ cache: true });
			$(newDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					newDiv.remove();
				} else {
					numQuestions.val(nextNum);
					if (collapse) {
						$('.collapse', newDiv).collapse('hide');
					} else {
						newDiv.focus();
						newDiv.scrollIntoView();
					}
				}
			});
		}		

		function createOption(questionNum, maxOptionCount) {
			var currNum = $('#numOptionsQuestion'+questionNum).val();
			var nextNum = +currNum + 1;
			var newDiv = document.createElement("div");
			newDiv.id = 'divq'+questionNum+'opt'+nextNum;
			var optionsDiv=$('#divq'+questionNum+'options');
			var lastChild=optionsDiv.children().filter(':last');
			$(lastChild).after(newDiv);

			var url=getSubmissionURL()+"/createOption.do?questionNumber="+questionNum+"&optionNumber="+nextNum;
			$.ajaxSetup({ cache: true });
			$(newDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					newDiv.remove();
				} else {
					$('#numOptionsQuestion'+questionNum).val(nextNum);
					if ( nextNum >= maxOptionCount  ) {
						$('#createOptionButton'+questionNum).hide();
					}
					// need to add the down button to the previous last option!
					var image = document.getElementById('question'+questionNum+'option'+currNum+'DownButton')
					image.style.display="inline";
					newDiv.scrollIntoView();
					
				}
			});
		}		
		function createAssessmentOption(questionNum, maxOptionCount, containingDivName) {
			var currNum = $('#'+containingDivName+'assmcq'+questionNum+'numOptions').val();
			var nextNum = +currNum + 1;
			var newDiv = document.createElement("div");
			newDiv.id = containingDivName+'divassmcq'+questionNum+'opt'+nextNum;
			var optionsDiv=$('#'+containingDivName+'divassmcq'+questionNum+'options');
			var lastChild=optionsDiv.children().filter(':last');
			$(lastChild).after(newDiv);

			var url=getSubmissionURL()+"/createOption.do?questionNumber="+questionNum+"&optionNumber="+nextNum+"&assess=true&containingDivName="+containingDivName;
			$.ajaxSetup({ cache: true });
			$(newDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					newDiv.remove();
				} else {
					$('#'+containingDivName+'assmcq'+questionNum+'numOptions').val(nextNum);
					if ( nextNum >= maxOptionCount  ) {
						$('#'+containingDivName+'createAssessmentOptionButton'+questionNum).hide();
					}
					// need to add the down button to the previous last option!
					var image = document.getElementById(containingDivName+'assmcq'+questionNum+'option'+currNum+'DownButton')
					image.style.display="inline";
					newDiv.scrollIntoView();
					
				}
			});
		}		

		function getOptionData(questionNum, paramPrefix) {
			var data = { };
			var correctField = paramPrefix + "correct";
			$('#templateForm').find('input, textarea, select').each(function() {
				if ( this.name == correctField ) {
					if ( this.checked ) {
			    			data[this.name] = $(this).val();
					}
				} else if ( this.name.startsWith(paramPrefix) ) {
		    			data[this.name] = $(this).val();
		    		}
		    });
		    return data;
		}
		
		function swapOptions(questionNum, optionNum1, optionNum2, divToLoad, appexContainingDivName) {
			refreshCKEditors() ;

			var paramPrefix  =  appexContainingDivName ? appexContainingDivName  + "assmcq" : "question"; 
			paramPrefix = paramPrefix + questionNum;
			var data = getOptionData(questionNum, paramPrefix);

			var url=getSubmissionURL()+"/swapOption.do?questionNumber="+questionNum+"&optionNumber1="+optionNum1
				+"&optionNumber2="+optionNum2;
			if ( appexContainingDivName ) {
				url += "&containingDivName="+appexContainingDivName+"&assess=true";
			}

			$.ajaxSetup({ cache: true });
			jqueryDivToLoad = divToLoad ? $('#'+divToLoad) : $('#divq'+questionNum+'options');
			jqueryDivToLoad.load(url, data, function( response, status, xhr ) {
				if ( status == "error" ) {
					alert("Swap failed. See server logs for details. "+xhr.statusText);
					console.log( xhr.status + " " + xhr.statusText );
				} 
			});
		}

		function removeOption(questionNum, optionNum, divToLoad, appexContainingDivName) {
			refreshCKEditors() ;

			var paramPrefix  =  appexContainingDivName ? appexContainingDivName  + "assmcq" : "question"; 
			paramPrefix = paramPrefix + questionNum;
			var data = getOptionData(questionNum, paramPrefix);

			var url=getSubmissionURL()+"/deleteOption.do?questionNumber="+questionNum+"&optionNumber="+optionNum;
			if ( appexContainingDivName ) 
				url += "&containingDivName="+appexContainingDivName+"&assess=true";
				
			$.ajaxSetup({ cache: true });
			jqueryDivToLoad = divToLoad ? $('#'+divToLoad) : $('#divq'+questionNum+'options');
			jqueryDivToLoad.load(url, data, function( response, status, xhr ) {
				if ( status == "error" ) {
					alert("Delete failed. See server logs for details. "+xhr.statusText);
					console.log( xhr.status + " " + xhr.statusText );
				} else {
						$('#createOptionButton'+questionNum).show();
				}
			});
		}

		function createForum(numTopicsFieldname, newDivPrefix, forumDivName, forward) {
			var numTopics = $('#'+numTopicsFieldname);
			var currNum = numTopics.val();
			var nextNum = +currNum + 1;
			var newDiv = document.createElement("div");
			newDiv.id = newDivPrefix+nextNum;
			var url=getSubmissionURL()+"/createForum.do?topicNumber="+nextNum;
			if ( forward && forward.length > 0) {
				url = url + "&forward=" + forward;
			}
			$('#'+forumDivName).append(newDiv);
			$.ajaxSetup({ cache: true });
			$(newDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					newDiv.remove();
				} else {
					numTopics.val(nextNum);
					newDiv.focus();
					newDiv.scrollIntoView();
				}
			});
		}		

		function createURL(numURLsFieldname, newDivPrefix, urlDivName, extraParam) {
			var numUrls = $('#'+numURLsFieldname);
			var currNumURLS = numUrls.val();
			var nextNumURLS = +currNumURLS + 1;
			var urlDiv = document.createElement("div");
			urlDiv.id = newDivPrefix+nextNumURLS;
			var url=getSubmissionURL()+"/createResource.do?urlNumber="+nextNumURLS;
			if ( extraParam ) {
				url = url + extraParam;
			}
			$('#'+urlDivName).append(urlDiv);
			$.ajaxSetup({ cache: true });
			$(urlDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					urlDiv.remove();
				} else {
					numUrls.val(nextNumURLS);
				}
			});
		}		
		function createBranch() {
			var currNum = $('#numberOfBranches').val();
			var nextNum = +currNum + 1;
			var branchDiv = document.createElement("div");
			branchDiv.id = 'divbranch'+nextNum;
			var url=getSubmissionURL()+"/createBranch.do?branchNumber="+nextNum;
			$('#divbranches').append(branchDiv);
			$.ajaxSetup({ cache: true });
			$(branchDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					$('#divurls').remove();
				} else {
					$('#numberOfBranches').val(nextNum);
				}
			});
		}		
		
		function createPeerReviewCriteria() {
			var currNum = $('#numRatingCriterias').val();
			var nextNum = +currNum + 1;
			var newDiv = document.createElement("div");
			newDiv.id = 'divrating'+nextNum;
			newDiv.className = 'space-top';
			var url=getSubmissionURL()+"/createRatingCriteria.do?criteriaNumber="+nextNum;
			$('#divratings').append(newDiv);
			$.ajaxSetup({ cache: true });
			$(newDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					newDiv.remove();
				} else {
					$('#numRatingCriterias').val(nextNum);
					newDiv.scrollIntoView();
				}
			});
		}		
		
		function deleteAppexDiv(idOfDivToDelete, idOfTitleField) {
			if ( confirm(appexDeleteMsg.replace("{0}", "\""+$("#"+idOfTitleField).val()+"\"")) ) {
				$("#"+idOfDivToDelete).remove();
			}		
		}

		function deleteMCQDiv(idOfDivToDelete, idOfTitleField) {
			if ( confirm(mcqDeleteMsg.replace("{0}", "\""+$("#"+idOfTitleField).val()+"\"")) ) {
				$("#"+idOfDivToDelete).remove();
			}		
		}
		
		function testURL(urlField) {
			launchPopup($('#'+urlField).val(),'popupUrl');
		}
		
		function validateNoSpecialCharacters(inputText) {
			if ( inputText ) {
				var validator = /^[^<>^*@%$]*$/igm;	
				var result = validator.test(inputText);
				return result;
			} 
			return true;
		}
		
		// Functions used of x-editable.
		// Shown as a fudge the validator to keep x-editable compatible with jquery validator
		function onShownForXEditable(e, editable) {
			$(this).nextAll('i.fa-pencil').hide();
		    var $innerForm = $(this).data('editable').input.$input.closest('form');
	    	var $outerForm = $innerForm.parents('form').eq(0);
	    	$innerForm.data('validator', $outerForm.data('validator'));
		}		
		function onHiddenForXEditable(e, reason) {
			$(this).nextAll('i.fa-pencil').show();
		}
		function validateXEditable(value) {
		    //close editing area on validation failure
            if (!value.trim()) {
                $('.editable-open').editableContainer('hide', 'cancel');
                return 'Can not be empty!';
            }
        }
		