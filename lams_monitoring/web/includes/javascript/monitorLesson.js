// ********** GLOBAL VARIABLES **********

// current tab that should be displayed after a refresh, defaults to Sequence tab
var currentTab = sessionStorage.getItem("lamsMonitoringCurrentTab") || 'sequence',
// copy of lesson SVG so it does no need to be fetched every time
    originalSequenceCanvas = null,
// DIV container for lesson SVG
// it gets accessed so many times it's worth to cache it here
    sequenceCanvas = null,
// which learner was selected in the search box
    sequenceSearchedLearner = null,
// which page of learners' progress is getting displayed
    learnersTabPage = 1,
    learnersTabSortedByProgress = false,
// currently opened EventSources
    eventSources = [],

// double tap support
    tapTimeout = 500,
    lastTapTime = 0,
    lastTapTarget = null,

// popup window size
    popupWidth = 1280,
    popupHeight = 720,

// cached gate icon data
    gateOpenIconPath   = 'images/svg/gateOpen.svg',
    gateOpenIconData   = null,

    fileDownloadCheckTimer = null,

    tempusDominusDefaultOptions = {
        restrictions: {
            minDate : new Date()
        },
        display : {
            components : {
                useTwentyfourHour : true
            },
            buttons : {
                today : true,
                close : true
            },
            sideBySide : true
        }
    },
    tempusDominusDateFormatter = function(date) {
        return date ? date.year + '-' + date.monthFormatted + '-' + date.dateFormatted + ' ' + date.hoursFormatted + ':' + date.minutesFormatted : '';
    }
dateFormatter = function(date) {
    return ("0" + date.getDate()).slice(-2) + "-" + ("0" + (date.getMonth() + 1)).slice(-2) + "-" +
        date.getFullYear() + " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2);
},

// colours to use on the page
    COLORS = {
        blue: '#0175E2',
        yellow: '#F9F871',
        green: '#00914A',
        red: '#D73961',
        gray: '#6c757d'
    },
    GRAPH_COLORS = {
        blue: 'rgba(1, 117, 226, 0.85)',
        yellow: 'rgba(249, 248, 113, 0.85)',
        green: 'rgba(0, 145, 74, 0.85)'
    };


$(document).ready(function(){
    initCommonElements();
    loadTab(currentTab);
});

function initCommonElements(){
    // customise jqGrid's Boostrap theme
    $.extend(true, $.jgrid.guiStyles.bootstrap4, {
        pager : {
            pagerSelect : 'form-control-select'
        },
        searchToolbar : {
            clearButton : 'btn btn-sm'
        },
        titleButton : "btn btn-xs"
    });

    //turn to inline mode for x-editable.js
    $.fn.editable.defaults.mode = 'inline';
    // do not cancel on clicking outside of box
    $.fn.editable.defaults.onblur = 'ignore';

    // assign action to navigation buttons
    $('#edit-lesson-btn').click(function(){
        $('.lesson-properties').toggleClass('active');
        $('.component-sidebar').toggleClass('expanded');
    });

    $('.component-menu-btn a[data-tab-name]').click(function(){
        loadTab($(this).data('tab-name'), this);
    });

    // initialise dialogs

    // search for users with the term the Monitor entered
    $("#sequenceSearchPhrase").autocomplete( {
        'source' : LAMS_URL + "monitoring/monitoring/autocomplete.do?scope=lesson&lessonID=" + lessonId,
        'delay'  : 700,
        'response' : function(event, ui) {
            $.each(ui.content, function(){
                // only add portrait if user has got one
                let valueParts = this.value.split('_');
                this.value = valueParts[0];
                // portrait div will be added as text, then in open() function below we fix it
                this.portrait = definePortrait(valueParts.length > 1 ? valueParts[1] : null, this.value, STYLE_SMALL, true, LAMS_URL);
                this.rawLabel = this.label;
                this.label += this.portrait;
            });
        },
        'open'   : function(event, ui) {
            $('.ui-menu-item-wrapper', $(this).autocomplete( "widget" )).each(function(){
                let menuItem = $(this);
                // portrait, if exists, was added as text; now we make it proper html
                menuItem.html(menuItem.text());
                let portrait = menuItem.children('div');
                if (portrait.length == 0) {
                    // no portrait, nothing to do
                    return;
                }
                // rearrange item contents
                portrait.detach();
                let label = $('<p />').text(menuItem.text());
                // this extra class makes it a flex box
                menuItem.empty().addClass('autocomplete-menu-item-with-portrait');
                menuItem.append(label).append(portrait);
            });
        },
        'select' : function(event, ui){
            // put the learner first name, last name and login into the box
            $(this).val(ui.item.rawLabel);
            // mark the learner's ID and make him highlighted after the refresh
            sequenceSearchedLearner = ui.item.value;
            loadTab();
            return false;
        }
    });

    var learnerGroupDialogContents = $('#learnerGroupDialogContents');
    $('#learnerGroupDialogForceCompleteButton, #learnerGroupDialogForceCompleteAllButton', learnerGroupDialogContents).click(function() {
        var dialog = $('#learnerGroupDialog'),
            // are we moving selected learners or all of learners who are currently in the activity
            moveAll = $(this).attr('id') == 'learnerGroupDialogForceCompleteAllButton',
            selectedLearners = moveAll ? null : $('.dialogTable .dialogListItemSelected', dialog),
            // go to "force complete" mode, similar to dragging user to an activity
            activityId = dialog.data('ajaxProperties').data.activityID,
            dropArea = sequenceCanvas.add('#completedLearnersContainer');
        dropArea.css('cursor', 'pointer')
            .one('click', function(event) {
                dropArea.off('click').css('cursor', 'default');
                if (moveAll) {
                    // setting learners as 'true' is a special switch meaning "move all"
                    forceComplete(activityId, true, event.pageX, event.pageY);
                } else {
                    var learners = [];
                    selectedLearners.each(function(){
                        var learner = $(this);
                        learners.push({
                            'id'     : learner.attr('userId'),
                            'name'   : learner.text()
                        });
                    });
                    forceComplete(activityId, learners, event.pageX, event.pageY);
                }
            });
        dialog.modal('hide');

        if (moveAll) {
            showToast(LABELS.FORCE_COMPLETE_CLICK.replace('[0]', ''));
        } else {
            var learnerNames = '';
            selectedLearners.each(function(){
                learnerNames += $(this).text() + ', ';
            });
            learnerNames = learnerNames.slice(0, -2);
            showToast(LABELS.FORCE_COMPLETE_CLICK.replace('[0]', '"' + learnerNames + '"'));
        }
    });

    $('#learnerGroupDialogViewButton', learnerGroupDialogContents).click(function() {
        var dialog = $('#learnerGroupDialog'),
            selectedLearner = $('.dialogTable .dialogListItemSelected', dialog);
        if (selectedLearner.length == 1) {
            // open pop up with user progress in the given activity
            openPopUp(selectedLearner.attr('viewUrl'), "LearnActivity", popupHeight, popupWidth, true);
        }
    });

    $('#learnerGroupDialogEmailButton', learnerGroupDialogContents).click(function() {
        var dialog = $('#learnerGroupDialog'),
            selectedLearner = $('.dialogTable .dialogListItemSelected', dialog);
        if (selectedLearner.length == 1) {
            showEmailDialog(selectedLearner.attr('userId'));
        }
    });

    $('#learnerGroupDialogCloseButton', learnerGroupDialogContents).click(function(){
        $('#learnerGroupDialog').modal('hide');
    });

    // initialise lesson dialog
    var learnerGroupDialog = showDialog('learnerGroupDialog',{
        'autoOpen'  : false,
        'height'	: 860,
        'resizable' : true,
        'open'      : function(){
            // until operator selects an user, buttons remain disabled
            $('button.learnerGroupDialogSelectableButton').blur().prop('disabled', true);
        },
        'close'		: function(){
        }
    }, false);

    $('.modal-body', learnerGroupDialog).empty().append(learnerGroupDialogContents.show()).closest('.modal-dialog').addClass('modal-xl');;

    // search for users with the term the Monitor entered
    $('.dialogSearchPhrase', learnerGroupDialog).autocomplete({
        'source' : LAMS_URL + "monitoring/monitoring/autocomplete.do?scope=lesson&lessonID=" + lessonId,
        'delay'  : 700,
        'select' : function(event, ui){
            var phraseField = $(this),
                dialog = $('#learnerGroupDialog');
            // learner's ID in ui.item.value is not used here
            phraseField.val(ui.item.label);
            $('.dialogSearchPhraseClear', dialog).css('visibility', 'visible');
            // reset to page 1
            dialog.data('ajaxProperties').data.pageNumber = 1;
            showLearnerGroupDialog();
            return false;
        }
    })
        // run the real search when the Monitor presses Enter
        .keypress(function(e){
            if (e.which == 13) {
                var phraseField = $(this),
                    dialog = $('#learnerGroupDialog');

                phraseField.autocomplete("close");
                if (phraseField.val()) {
                    $('.dialogSearchPhraseClear', dialog).css('visibility', 'visible');
                }
                // reset to page 1
                dialog.data('ajaxProperties').data.pageNumber = 1;
                showLearnerGroupDialog();
            }
        });

    var forceBackwardsDialogContents = $('#forceBackwardsDialogContents');
    showDialog('forceBackwardsDialog', {
        'autoOpen'	: false,
        'resizable' : true,
        'height'	: 300,
        'width'  	: 400,
        'title'		: LABELS.FORCE_COMPLETE_BUTTON,
        'close'		: function(){
        }
    }, false);
    // only need to do this once as then it updates the msg field directly.
    $('.modal-body', '#forceBackwardsDialog').empty().append($('#forceBackwardsDialogContents').show());

    $('#forceBackwardsRemoveContentNoButton', forceBackwardsDialogContents).click(function(){
        var forceBackwardsDialog = $('#forceBackwardsDialog'),
            learners = forceBackwardsDialog.data('learners'),
            moveAll = learners === true;
        forceCompleteExecute(moveAll ? null : learners,
            moveAll ? forceBackwardsDialog.data('currentActivityId') : null,
            forceBackwardsDialog.data('activityId'),
            false);
        forceBackwardsDialog.modal('hide');
    });

    $('#forceBackwardsRemoveContentYesButton', forceBackwardsDialogContents).click(function(){
        var forceBackwardsDialog = $('#forceBackwardsDialog');
        learners = forceBackwardsDialog.data('learners'),
            moveAll = learners === true;
        forceCompleteExecute(moveAll ? null : learners,
            moveAll ? forceBackwardsDialog.data('currentActivityId') : null,
            forceBackwardsDialog.data('activityId'),
            true);
        forceBackwardsDialog.modal('hide');
    });

    $('#forceBackwardsCloseButton', forceBackwardsDialogContents).click(function(){
        $('#forceBackwardsDialog').modal('hide');
    });

    //enable renaming of lesson title
    $('#lesson-name').editable({
        type: 'text',
        pk: lessonId,
        url: LAMS_URL + 'monitoring/monitoring/renameLesson.do?' + csrfTokenName + '=' + csrfTokenValue,
        validate: function(value) {
            //close editing area on validation failure
            if (!value.trim()) {
                $('.editable-open').editableContainer('hide', 'cancel');
                return 'Can not be empty!';
            }
        },
        //assume server response: 200 Ok {status: 'error', msg: 'field cannot be empty!'}
        success: function(response, newValue) {
            if(response.status == 'error') {
                return response.msg; //msg will be shown in editable form
            }
        }
    })

    $('#editLessonNameButton').click(function(e) {
        e.stopPropagation();
        $('#lesson-name').editable('toggle');
    });

    // sets up dialog for editing class
    var classDialog = showDialog('classDialog',{
        'autoOpen'  : false,
        'width'     : 950,
        'height'	: 860,
        'title' 	: LABELS.LESSON_EDIT_CLASS,
        'resizable' : true,
        'close' : function(){
            loadTab();
        }
    }, false);

    $('.modal-body', classDialog).empty().append($('#classDialogContents').show()).closest('.modal-dialog').addClass('modal-xl');

    // search for users in the organisation with the term the Monitor entered
    $('.dialogSearchPhrase', classDialog).autocomplete({
        'source' : LAMS_URL + "monitoring/monitoring/autocomplete.do?scope=organisation&lessonID=" + lessonId,
        'delay'  : 700,
        'select' : function(event, ui){
            var phraseField = $(this),
                dialog = $('#classDialog');
            // learner's ID in ui.item.value is not used here
            phraseField.val(ui.item.label);
            // show the "clear search phrase" button
            $('.dialogSearchPhraseClear', dialog).css('visibility', 'visible');
            // reset to page 1
            dialog.data('LearnerAjaxProperties').data.pageNumber = 1;
            showClassDialog('Learner');
            return false;
        }
    })
        // run the real search when the Monitor presses Enter
        .keypress(function(e){
            if (e.which == 13) {
                var phraseField = $(this),
                    dialog = $('#classDialog');

                phraseField.autocomplete("close");
                if (phraseField.val()) {
                    $('.dialogSearchPhraseClear', dialog).css('visibility', 'visible');
                }
                // reset to page 1
                dialog.data('LearnerAjaxProperties').pageNumber = 1;
                showClassDialog('Learner');
            }
        });

    var emailProgressDialog = showDialog('emailProgressDialog',{
        'autoOpen'  : false,
        'width'     : 510,
        'height'	: 700,
        'title' 	: LABELS.PROGRESS_EMAIL_TITLE,
        'resizable' : false,
        'close'		: function(){}
    }, false);
    $('.modal-body', emailProgressDialog).empty().append($('#emailProgressDialogContents').show());


    // sets gradebook on complete functionality
    $('#gradebookOnCompleteButton').change(function(){
        var checked = $(this).prop('checked'),
            data = {
                'gradebookOnComplete' : checked,
                'lessonID'      : lessonId
            };
        data[csrfTokenName] = csrfTokenValue;
        $.ajax({
            url : LAMS_URL + 'monitoring/monitoring/gradebookOnComplete.do',
            type : 'POST',
            cache : false,
            data : data,
            success : function() {
                if (checked) {
                    showToast(LABELS.LESSON_ACTIVITY_SCORES_ENABLE_ALERT);
                } else {
                    showToast(LABELS.LESSON_ACTIVITY_SCORES_DISABLE_ALERT);
                }
            }
        });
    });


    // update lesson details
    $.ajax({
        dataType : 'json',
        url : LAMS_URL + 'monitoring/monitoring/getLessonDetails.do',
        cache : false,
        data : {
            'lessonID'  : lessonId
        },

        success : function(response) {
            // update lesson state label
            lessonStateId = +response.lessonStateID;
            var label = null,
                labelColour = 'warning';
            switch (lessonStateId) {
                case 1:
                    label = LABELS.LESSON_STATE_CREATED;
                    labelColour = 'warning';
                    break;
                case 2:
                    label = LABELS.LESSON_STATE_SCHEDULED;
                    labelColour = 'warning';
                    break;
                case 3:
                    label = LABELS.LESSON_STATE_STARTED;
                    labelColour = 'success';
                    break;
                case 4:
                    label = LABELS.LESSON_STATE_SUSPENDED;
                    labelColour = 'danger';
                    break;
                case 5:
                    label = LABELS.LESSON_STATE_FINISHED;
                    labelColour = 'danger';
                    break;
                case 6:
                    label = LABELS.LESSON_STATE_ARCHIVED;
                    labelColour = 'danger';
                    break;
                case 7:
                    label = LABELS.LESSON_STATE_REMOVED;
                    labelColour = 'danger';
                    break;
            }
            $('#lessonStateLabel').attr('class', 'btn btn-sm btn-' + labelColour).html(label + ' <i class="fa-solid fa-angles-down"></i>');

            // update available options in change state dropdown menu
            var selectField = $('#lessonStateField');
            // remove all except "Select status" option
            selectField.children('option:not([value="-1"])').remove();
            switch (lessonStateId) {
                case 3:
                    if ( ! ( lessonEndDate && lessonEndDate > "") ) {
                        $('<option />').attr('value', 4).text(LABELS.LESSON_STATE_ACTION_DISABLE).appendTo(selectField);
                    }
                    $('<option />').attr('value', 6).text(LABELS.LESSON_STATE_ACTION_ARCHIVE).appendTo(selectField);
                    $('<option />').attr('value', 7).text(LABELS.LESSON_STATE_ACTION_REMOVE).appendTo(selectField);
                    break;
                case 4:
                    $('<option />').attr('value', 3).text(LABELS.LESSON_STATE_ACTION_ACTIVATE).appendTo(selectField);
                    $('<option />').attr('value', 6).text(LABELS.LESSON_STATE_ACTION_ARCHIVE).appendTo(selectField);
                    $('<option />').attr('value', 7).text(LABELS.LESSON_STATE_ACTION_REMOVE).appendTo(selectField);
                    break;
                case 5:
                    break;
                case 6:
                    $('<option />').attr('value', 3).text(LABELS.LESSON_STATE_ACTION_ACTIVATE).appendTo(selectField);
                    $('<option />').attr('value', 7).text(LABELS.LESSON_STATE_ACTION_REMOVE).appendTo(selectField);
                    break;
            }

            // show/remove widgets for lesson scheduling
            var scheduleControls = $('#scheduleDatetimeField, #scheduleLessonButton, #startLessonButton, #lessonScheduler, #scheduleDisableLessonButton, #disableLessonButton'),
                startDateField = $('#lessonStartDateSpan'),
                lessonFinishDateSpan = $('#lessonFinishDateSpan'),
                lessonStateChanger = $('#lessonStateChanger'),
                stateLabel = $('#lessonStateLabel');
            switch (lessonStateId) {
                //created but not started lesson
                case 1:
                    scheduleControls.css('display','inline');
                    if ( response.finishDate ) {
                        lessonFinishDateSpan.text(LABELS.LESSON_FINISH.replace("%0",response.finishDate)).css('display','inline');
                        $("#scheduleDisableLessonButton").html(LABELS.RESCHEDULE);
                        $("#disableLessonButton").css('display', 'none');
                    } else {
                        lessonFinishDateSpan.hide();
                    }
                    startDateField.hide();
                    lessonStateChanger.hide();
                    break;
                //scheduled lesson
                case 2:
                    scheduleControls.css('display','inline');
                    startDateField.text(LABELS.LESSON_START.replace("%0",response.startDate)).add('#startLessonButton').css('display','inline');
                    $("#scheduleLessonButton").html(LABELS.RESCHEDULE);
                    if ( response.finishDate ) {
                        lessonFinishDateSpan.text(LABELS.LESSON_FINISH.replace("%0",response.finishDate)).css('display','block');
                        $("#scheduleDisableLessonButton").html(LABELS.RESCHEDULE);
                        $("#disableLessonButton").css('display', 'none');
                    } else {
                        lessonFinishDateSpan.css('display','none');
                    }
                    lessonStateChanger.hide();
                    break;
                //started lesson
                default:
                    startDateField.text("").css('display','none'); // we may have just started the lesson and needed to clear the scheduled date message
                    if ( response.finishDate ) {
                        scheduleControls.css('display','inline');
                        $("#lessonStartApply").css('display','none');
                        lessonFinishDateSpan.text(LABELS.LESSON_FINISH.replace("%0",response.finishDate)).css('display','inline');
                        $("#scheduleDisableLessonButton").html(LABELS.RESCHEDULE);
                    } else {
                        scheduleControls.css('display','none');
                        $("#scheduleDisableLessonButton").html(LABELS.SCHEDULE);
                        lessonFinishDateSpan.text("").css('display','none');
                    }

                    lessonStateChanger.css('display','inline');
                    stateLabel.attr('title',response.startDate);
                    break;
            }

            $('#lesson-name-strong').html(response.lessonName);
            $('#lesson-instructions').html(response.lessonInstructions);
        }
    });

    //initialize datetimepickers
    new tempusDominus.TempusDominus(document.getElementById('scheduleDatetimeField'), tempusDominusDefaultOptions)
        .dates.formatInput = tempusDominusDateFormatter;

    new tempusDominus.TempusDominus(document.getElementById('disableDatetimeField'), tempusDominusDefaultOptions)
        .dates.formatInput = tempusDominusDateFormatter;

    let datePickerElement = $('#emaildatePicker'),
        datePicker = new tempusDominus.TempusDominus(datePickerElement[0], tempusDominusDefaultOptions);

    datePicker.dates.formatInput = tempusDominusDateFormatter;
    datePickerElement.data('datePicker', datePicker);
}

/**
 * Loads given tab content
 */
function loadTab(tabName, button) {
    if (tabName) {
        currentTab = tabName;
    } else {
        tabName = currentTab;
    }

    sessionStorage.setItem("lamsMonitoringCurrentTab", currentTab);

    $('.navigate-btn-container a.btn, .lesson-properties').removeClass('active');
    $('.component-sidebar').removeClass('expanded');
    if (button) {
        $(button).addClass('active');
    }

    clearEventSources();


    let tabContent = $('.monitoring-page-content .tab-content'),
        searchStudentWidget = $('#sequenceSearchPhraseContainer');

    $('.is-countdown', tabContent).countdown('destroy');
    tabContent.empty();

    $('#sequenceSearchPhraseButton').prop('disabled', sequenceSearchedLearner == null);
    $('#sequenceSearchPhraseIcon').toggle(sequenceSearchedLearner == null);
    $('#sequenceSearchPhraseClearIcon').toggle(sequenceSearchedLearner != null);

    switch(tabName) {
        case 'sequence': {
            tabContent.load(LAMS_URL + 'monitoring/monitoring/displaySequenceTab.do', function(){
                openEventSource(LAMS_URL + 'monitoring/monitoring/getLearnerProgressUpdateFlux.do?lessonId=' +  lessonId,
                    function (event) {
                        if ("doRefresh" == event.data && $('#sequence-tab-content').length === 1){
                            updateSequenceTab();
                        }
                    });
                $("#load-sequence-tab-btn").addClass('active');
            });
            searchStudentWidget.show();
        }
            break;

        case 'learners': {
            tabContent.load(LAMS_URL + 'monitoring/monitoring/displayLearnersTab.do', function(){
                $('#learnes-order-by-completion').change(function(){
                    learnersPageShift(0);
                })
                updateLearnersTab();
            });
            searchStudentWidget.show();
        }
            break;

        case 'gradebook': {
            tabContent.load(LAMS_URL + 'monitoring/monitoring/displayGradebookTab.do', function(){
                openEventSource(LAMS_URL + 'monitoring/monitoring/getGradebookUpdateFlux.do?lessonId=' +  lessonId,
                    function (event) {
                        if ("doRefresh" == event.data && $('#gradebookDiv').length === 1){
                            let expandedGridIds = [],
                                userGrid = $('#userView'),
                                activityGrid = $('#activityView');
                            // do not update if grid is being edited by the teacher
                            if (userGrid.data('isCellEdited') === true || activityGrid.data('isCellEdited') === true) {
                                return;
                            }

                            $("tr:has(.sgexpanded)", userGrid).each(function () {
                                let num = $(this).attr('id');
                                expandedGridIds.push(num);
                            });
                            userGrid.data('expandedGridIds', expandedGridIds).trigger("reloadGrid");

                            expandedGridIds = [];
                            $("tr:has(.sgexpanded)", activityGrid).each(function () {
                                let num = $(this).attr('id');
                                expandedGridIds.push(num);
                            });
                            activityGrid.data('expandedGridIds', expandedGridIds).trigger("reloadGrid");
                        }
                    });

                updateGradebookTab();
            });
            searchStudentWidget.show();
        }
            break;

        case 'teams': {
            tabContent.load(LAMS_URL + 'monitoring/tblmonitor/teams.do?lessonID=' + lessonId);
            searchStudentWidget.show();
        }
            break;

        case 'irat': {
            tabContent.load(LAMS_URL + 'tool/laasse10/tblmonitoring/iraAssessment.do?toolContentID=' + iraToolContentId);
            searchStudentWidget.hide();
        }
            break;

        case 'iratStudentChoices': {
            tabContent.load(LAMS_URL + 'tool/laasse10/tblmonitoring/iraAssessmentStudentChoices.do?toolContentID=' + iraToolContentId);
            searchStudentWidget.hide();
        }
            break;

        case 'trat': {
            tabContent.load(LAMS_URL + 'tool/lascrt11/tblmonitoring/tra.do?toolContentID=' + traToolContentId);
            searchStudentWidget.hide();
        }
            break;

        case 'tratStudentChoices': {
            tabContent.load(LAMS_URL + 'tool/lascrt11/tblmonitoring/traStudentChoices.do?toolContentID=' + traToolContentId);
            searchStudentWidget.hide();
        }
            break;

        case 'burningQuestions': {
            tabContent.load(LAMS_URL + 'tool/lascrt11/tblmonitoring/burningQuestions.do?toolContentID=' + traToolContentId);
            searchStudentWidget.hide();
        }
            break;

        case 'aes': {
            tabContent.load(LAMS_URL + 'monitoring/tblmonitor/aes.do?',
                {
                    aeToolContentIds: aeToolContentIds,
                    aeToolTypes: aeToolTypes,
                    aeActivityTitles: aeActivityTitles
                });
            searchStudentWidget.hide();
        }
            break;

        case 'peerReview': {
            tabContent.load(LAMS_URL + 'tool/laprev11/tblmonitoring/peerreview.do?toolContentID=' + peerreviewToolContentId);
            searchStudentWidget.hide();
        }
            break;
    }
}

//********** LESSON DETAILS FUNCTIONS **********


/**
 * Shows all learners in the lesson class.
 */
function showLessonLearnersDialog() {
    var ajaxProperties = {
        url : LAMS_URL + 'monitoring/monitoring/getLessonLearners.do',
        data : {
            'lessonID'  : lessonId
        }
    };

    showLearnerGroupDialog(ajaxProperties, LABELS.LESSON_GROUP_DIALOG_CLASS, true, false, false, true);
}

/**
 * Lesson state field changed but the apply button not yet pressed
 */
function lessonStateFieldChanged() {

    //state chosen in the dropdown menu
    var state = +$('#lessonStateField').val();
    switch (state) {
        //'disable' is chosen
        case 4:
            $('#lessonScheduler').show();
            $('#lessonStartApply').hide();
            $('#lessonStateApply').hide();
            $("#scheduleDisableLessonButton").html(LABELS.SCHEDULE);
            $("#scheduleDisableLessonButton").css('display', 'block'); // must be inline or it will be wrong size
            $("#disableLessonButton").show();
            $('#lessonDisableApply').show();
            break;
        default:
            $('#lessonDisableApply').hide();
            $('#lessonStateApply').show();
            break;
    }
}

/**
 * Apply the lesson state change and update widgets.
 */
function changeLessonState(){
    var method = null;

    //state chosen in the dropdown menu
    var state = +$('#lessonStateField').val();
    switch (state) {

        //'activate' is chosen
        case 3:
            switch (lessonStateId) {
                case 4: //SUSPENDED
                    method = "unsuspendLesson";
                    break;
                case 6: //ARCHIVED
                    method = "unarchiveLesson";
                    break;
            }
            break;


        //'disable' is handled by scheduleDisableLesson, disableLesson
        // case 4:

        //'archive' is chosen
        case 6:
            method = "archiveLesson";
            break;

        //'remove' is chosen
        case 7:
            showConfirm(LABELS.LESSON_REMOVE_ALERT, function() {
                showConfirm(LABELS.LESSON_REMOVE_DOUBLECHECK_ALERT, function() {
                    method = "removeLesson";
                });
            });
            break;
    }

    if (method) {
        applyStateChange(state, method)
    }
}

function scheduleDisableLesson() {
    var date = $('#disableDatetimeField').val();
    if (date) {
        if ( checkScheduleDate(lessonStartDate, date) ) {
            applyStateChange(4, "suspendLesson", date);
        } else {
            alert(LABELS.LESSON_ERROR_START_END_DATE);
        }
    } else {
        alert(LABELS.LESSON_ERROR_SCHEDULE_DATE);
    }
}

function disableLesson() {
    applyStateChange(4, "suspendLesson");
}

function applyStateChange(state, method, newLessonEndDate) {
    var params = {
        'lessonID'      : lessonId
    };
    params[csrfTokenName] = csrfTokenValue;

    if (newLessonEndDate) {
        params.lessonEndDate = newLessonEndDate;
    }

    $.ajax({
        url : LAMS_URL + 'monitoring/monitoring/' + method + ".do",
        data: params,
        type: "POST",
        cache : false,
        success: function() {
            if (state == 7) {
                // user chose to finish the lesson, close monitoring
                document.location.href = "/";
            } else {
                document.location.reload();
            }
        }
    });
}

function drawLessonCompletionChart(){
    $.ajax({
        url : LAMS_URL + 'monitoring/monitoring/getLessonChartData.do?lessonID=' + lessonId,
        dataType : 'json',
        cache : false,
        success : function(response) {
            if (!response || $.isEmptyObject(response)) {
                // if there is no data to display
                return;
            }

            let chartDiv = $('#completion-chart'),
                lessonCompletionChart = chartDiv.data('chart'),
                percent = [],
                labels = [],
                raw = [];

            $(response.data).each(function(){
                labels.push(this.name);
                percent.push(this.value);
                raw.push(this.raw);
            });

            if (lessonCompletionChart != null) {
                // chart already exists, just update data
                lessonCompletionChart.data.datasets[0].data = percent;
                lessonCompletionChart.lessonCompletionChartRawData = raw;
                lessonCompletionChart.update();
                return;
            }

            let ctx = chartDiv[0].getContext('2d');
            lessonCompletionChart = new Chart(ctx, {
                type : 'doughnut',
                data : {
                    datasets : [ {
                        data : percent,
                        backgroundColor : [
                            GRAPH_COLORS.blue,
                            GRAPH_COLORS.yellow,
                            GRAPH_COLORS.green
                        ],
                        borderWidth : 1,
                        borderColor : COLORS.gray
                    } ],
                    labels : labels
                },
                options : {
                    responsive : false,
                    animation : {
                        animateScale : true,
                        animateRotate : true,
                        duration : 1000
                    },
                    plugins : {
                        tooltip : {
                            enabled : true,
                            callbacks: {
                                label : function(context) {
                                    let index =  context.dataIndex,

                                        rawData = context.chart.lessonCompletionChartRawData,
                                        percent = context.dataset.data,

                                        label = labels[index],
                                        value = percent[index],
                                        rawValue = rawData[index];

                                    return " " + rawValue + " (" + value + "%)";
                                }
                            }
                        },
                        legend : {
                            position: 'bottom',
                            align: 'start',
                            labels : {
                                font : {
                                    size: 15
                                },
                                generateLabels : function(chart) {
                                    var data = chart.data;
                                    if (data.labels.length && data.datasets.length) {
                                        return data.labels.map(function(label, i) {
                                            let meta = chart.getDatasetMeta(0),
                                                style = meta.controller.getStyle(i),
                                                value = data.datasets[0].data[i],
                                                rawData = chart.lessonCompletionChartRawData || raw,
                                                rawValue = rawData[i];

                                            return {
                                                text: label + ": " + rawValue + " (" + value + "%)",
                                                fillStyle: style.backgroundColor,
                                                strokeStyle: style.borderColor,
                                                lineWidth: 0,
                                                hidden: isNaN(value) || meta.data[i].hidden,

                                                // Extra data used for toggling the
                                                // correct item
                                                index: i
                                            };
                                        });
                                    }
                                    return [];
                                }
                            }
                        }
                    }
                }
            });

            lessonCompletionChart.lessonCompletionChartRawData = raw;
            chartDiv.data('chart', lessonCompletionChart);
        }
    });
}

function checkScheduleDate(startDateString, endDateString) {
    var startDate = startDateString && startDateString > "" ? Date.parse(startDateString) : 0;
    var endDate =  endDateString && endDateString > "" ? Date.parse(endDateString) : 0;
    return ( endDate == 0 || startDate < endDate );
}

function scheduleLesson(){
    var date = $('#scheduleDatetimeField').val();
    if (date) {
        if ( checkScheduleDate (date, lessonEndDate) ) {
            $.ajax({
                url : LAMS_URL + 'monitoring/monitoring/startOnScheduleLesson.do',
                cache : false,
                data : {
                    'lessonID'        : lessonId,
                    'lessonStartDate' : date
                },
                success : function() {
                    document.location.reload();
                }
            });
        } else {
            alert(LABELS.LESSON_ERROR_START_END_DATE);
        }
    } else {
        alert(LABELS.LESSON_ERROR_SCHEDULE_DATE);
    }
}


function startLesson(){
    var data = {
        'lessonID': lessonId
    };
    data[csrfTokenName] = csrfTokenValue;
    $.ajax({
        dataType : 'text',
        url : LAMS_URL + 'monitoring/monitoring/startLesson.do',
        data : data,
        cache : false,
        type : 'POST',
        success : function() {
             document.location.reload();
        }
    });
}


/**
 * Stringifies user IDs who were selected in Edit Class dialog.
 */
function getSelectedClassUserList(containerId) {
    var list = [];
    $('#' + containerId).children('tr.dialogListItem').each(function(){
        if ($('input:checked', this).length > 0){
            list.push($(this).attr('userId'));
        }
    });
    return list;
}

function showEmailDialog(userId){

    //check whether current window is a top level one (otherwise it's an iframe or popup)
    var isTopLevelWindow = window.top == window.self;
    //calculate width and height based on the dimensions of the window to which dialog is added
    var dialogWindow = isTopLevelWindow ? $(window) : $(window.parent);

    showDialog("dialogEmail", {
        'autoOpen'  : true,
        'height'    : Math.max(380, Math.min(700, dialogWindow.height() - 30)),
        'width'     : Math.max(380, Math.min(700, dialogWindow.width() - 60)),
        'resizable' : true,
        'title'     : LABELS.EMAIL_TITLE,
        //dialog needs to be added to a top level window to avoid boundary limitations of the interim iframe
        "isCreateInParentWindow" : !isTopLevelWindow,
        'open'      : function(){
            var dialog = $(this);
            // load contents after opening the dialog
            $('iframe', dialog).attr('src',
                LAMS_URL + 'emailUser/composeMail.do?lessonID=' + lessonId
                + '&userID=' + userId);
        },
        'close' : function(){
            $(this).remove();
        }
    }, false, true);
}

function updateContributeActivities(contributeActivities) {
    let requiredTasksPanel = $('#required-tasks'),
        requiredTasksContent = $('#required-tasks-content', requiredTasksPanel);

    if (!contributeActivities || contributeActivities.length === 0) {
        requiredTasksPanel.remove();
        return;
    }

    $('.contribute-row', requiredTasksContent).remove();

    /*
    // special case - add a Live Edit option. This does not directly map to an activity
    if ( lockedForEdit && lockedForEditUserId == userId) {
        // show Live Edit task only if currently editing myself, not if someone else is editing.
        // put it at the top of the contribution list
        var cell = $('<div />').addClass('contributeActivityCell').text(LABELS.LIVE_EDIT_BUTTON);
        var row = $('<div />').addClass('contributeRow').insertAfter(row).append(cell);
        var entryContent = LABELS.LIVE_EDIT_TOOLTIP
            + '<span class="btn btn-xs btn-primary pull-right" onClick="javascript:openLiveEdit()" title="' + LABELS.CONTRIBUTE_TOOLTIP
            + '">' + LABELS.CONTRIBUTE_BUTTON + '</span>';
        cell = $('<div />').addClass('contributeEntryCell').html(entryContent);
        row = row.append(cell);
    }
    */
    if (contributeActivities) {
        $.each(contributeActivities, function(){
            let contributeActivity = this;

            let row = $('<div />').addClass('row contribute-row' + (contributeActivity.title ? ' ml-1' : ''))
                .appendTo(requiredTasksContent);

            $.each(this.contributeEntries, function(){
                var entryContent = '<div class="col label">' + (contributeActivity.title ? '<b>' + contributeActivity.title + '</b><br>(<small>' : '');
                switch (this.contributionType) {
                    case 3  : entryContent += LABELS.CONTRIBUTE_GATE; break;
                    case 6  : entryContent += LABELS.CONTRIBUTE_GROUPING; break;
                    case 7  : entryContent += LABELS.CONTRIBUTE_TOOL; break;
                    case 9  : entryContent += LABELS.CONTRIBUTE_BRANCHING; break;
                    case 11 : entryContent += LABELS.CONTRIBUTE_CONTENT_EDITED; break;
                    case 12 : entryContent += LABELS.CONTRIBUTE_GATE_PASSWORD; break;
                }
                if (contributeActivity.title) {
                    entryContent += ')</small>';
                }
                entryContent += '</div><div class="col text-right">';
                switch (this.contributionType) {
                    case 3  :
                    case 12 : if (this.isComplete) {
                        entryContent += '<div class="pull-right"><button class="contribute-gate-opened-button btn btn-success" '
                            + 'onClick="javascript:openGateSelectively(\'' + this.url  + '\')" '
                            + 'title="' + LABELS.CONTRIBUTE_OPENED_GATE_TOOLTIP + '">'
                            + LABELS.CONTRIBUTE_OPENED_GATE
                            + '</button></div>';
                    } else {
                        entryContent += '<div class="btn-group dropdown-center"><button onClick="javascript:openGateNow('
                            + contributeActivity.activityID + ')" type="button" class="btn btn-primary" title="'
                            + LABELS.CONTRIBUTE_OPEN_GATE_NOW_TOOLTIP + '">'
                            + LABELS.CONTRIBUTE_OPEN_GATE_NOW_BUTTON
                            + '</button><button  class="btn btn-primary dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown" '
                            + 'type="button" aria-haspopup="true" aria-expanded="false"></button>'
                            + '<ul class="dropdown-menu"><li class="bg-primary rounded">'
                            + '<a href="#" class="dropdown-item text-white" onClick="javascript:openGateSelectively(\'' + this.url + '\')" title="'
                            + LABELS.CONTRIBUTE_OPEN_GATE_TOOLTIP + '">'
                            + LABELS.CONTRIBUTE_OPEN_GATE_BUTTON + '</a></li></ul></div>';

                    }
                        break;
                    case 6 : entryContent += '<a class="btn btn-primary contribute-go-button" href="'
                        + this.url + '" title="' + LABELS.CONTRIBUTE_TOOLTIP + '">' + LABELS.CONTRIBUTE_BUTTON + '</a>';
                        break;
                    default : entryContent += '<button type="button" class="btn btn-primary contribute-go-button" onClick="javascript:openPopUp(\''
                        + this.url + '\',\'ContributeActivity\', 800, 1280, true)" title="' + LABELS.CONTRIBUTE_TOOLTIP
                        + '">' + LABELS.CONTRIBUTE_BUTTON + '</button>';
                }

                row.html(entryContent + "</div>");
            });
        });

        requiredTasksPanel.show();
    } else {
        requiredTasksPanel.hide();
    }
}

/**
 * Set up when the progress emails should be sent to monitors
 */
function configureProgressEmail(){
    fillEmailProgress();
    $('#emailProgressDialog').modal('show');
}

/**
 * Adds/removes date to the set of progress report emailing dates
 */
function editEmailProgressDate(dateCheckbox){
    var dateid = dateCheckbox.closest('.dialogListItem').attr('dateid'),
        add = dateCheckbox.is(':checked'),
        data = {
            'lessonID' : lessonId,
            'id' : dateid,
            'add' : add
        };
    data[csrfTokenName] = csrfTokenValue;
    $.ajax({
        url : LAMS_URL + 'monitoring/emailProgress/updateEmailProgressDate.do',
        type : 'POST',
        cache : false,
        data : data,
        success : function( dateObj ) {
            if (dateObj.id) {
                dateCheckbox.closest('.dialogListItem')
                    .attr('dateid', dateObj.id)
                    .find('label').text(dateFormatter(new Date(dateObj.id)));
            }
        }
    });
}

/**
 * Fills the dates from the server for the email progress
 */
function fillEmailProgress() {
    var dialog = $('#emailProgressDialog'),
        table = $('#emailProgressDialogTable', dialog),
        list = $('.dialogTable', table).empty(),
        dates = null;
    ajaxProperties = dialog.data('ajaxProperties'),
        dates = null;

    if (!ajaxProperties) {
        // initialise ajax config
        ajaxProperties = {
            dataType : 'json',
            url : LAMS_URL + 'monitoring/emailProgress/getEmailProgressDates.do',
            cache : false,
            async : false,
            data : {
                'lessonID'  : lessonId
            }};
        dialog.data('ajaxProperties', ajaxProperties);
    }

    ajaxProperties.success = function(response) {
        dates = response.dates;
    }

    $.ajax(ajaxProperties);

    $.each(dates, function(dateIndex, date) {
        addUserCheckbox(date.id, list, true);
    });
}

function addUserCheckbox(dateId, list, checked) {
    // check for an existing matching date
    var alreadyExists = false,
        existingDivs = $("tr", list);
    $.each(existingDivs, function(divIndex, div) {
        if ( div.getAttribute('dateid') == dateId) {
            alreadyExists = true;
            return false;
        }
    });
    if ( alreadyExists )
        return;

    // does not exist so add to list
    var checkboxId = 'email-progress-date-' + dateId,
        checkbox = $('<input />').attr({
            'type' : 'checkbox',
            'id'   : checkboxId
        }).addClass('form-check-input me-1')
            .change(function(){
                editEmailProgressDate($(this));
            }),


        dateString = $('<label />').addClass('form-check-label').attr('for', checkboxId).text(dateFormatter(new Date(dateId))),

        dateRow = $('<tr />').attr({
            'dateid'  : dateId
        })
            .addClass('dialogListItem')
            .appendTo(list);

    $('<td />').append(checkbox).appendTo(dateRow);
    $('<td />').append(dateString).appendTo(dateRow);


    checkbox.prop('checked', checked);
    return checkbox;
}

function sendProgressEmail() {
    showConfirm(LABELS.PROGRESS_EMAIL_SEND_NOW_QUESTION, function() {
        $.ajax({
            dataType : 'json',
            type : 'post',
            url : LAMS_URL + 'monitoring/emailProgress/sendLessonProgressEmail.do',
            type: 'post',
            cache : false,
            data : {
                'lessonID'  : lessonId
            },
            success : function(response) {
                if ( response.error || ! response.sent > 0 )
                    showToast(LABELS.PROGRESS_EMAIL_SEND_FAILED+"\n"+(response.error ? response.error : ""));
                else
                    showToast(LABELS.PROGRESS_EMAIL_SUCCESS.replace('[0]',response.sent));
            }
        });
    });
}

function addEmailProgressDate() {
    var table = $('#emailProgressDialogTable', '#emailProgressDialog'),
        list = $('.dialogTable', table),
        datePickerElement = $('#emaildatePicker'),
        datePicker = datePickerElement.data('datePicker'),
        date = datePicker.viewDate;

    datePickerElement.val('');

    if (date != null ) {
        if (date.getTime() < Date.now()  ) {
            alert(LABELS.ERROR_DATE_IN_PAST);
        } else {
            var checkbox = addUserCheckbox(date.getTime(), list, true);
            editEmailProgressDate(checkbox); // update back end
            addEmailProgressSeries(false, table);
        }
    } else {
        alert(LABELS.PROGRESS_SELECT_DATE_FIRST);
    }
}

function addEmailProgressSeries(forceQuestion, table) {
    if ( ! table ) {
        table = $('#emailProgressDialogTable', '#emailProgressDialog');
    }
    var list = $('.dialogTable', table),
        items = $('.dialogListItem', list);

    if ( forceQuestion && items.length < 2 ) {
        alert(LABELS.PROGRESS_ENTER_TWO_DATES_FIRST);
    } else if ( items.length == 2 || forceQuestion ) {
        var numDates = prompt(LABELS.PROGRESS_EMAIL_GENERATE_ONE+"\n\n"+LABELS.PROGRESS_EMAIL_GENERATE_TWO);
        if ( numDates > 0 ) {
            var dates=[];
            var maxDate = 0;
            items.each( function() {
                var nextDate = $(this).attr('dateid');
                dates.push($(this).attr('dateid'));
                if ( maxDate < nextDate )
                    maxDate = nextDate;
            });
            if ( dates[1] < dates[0] ) {
                var swap = dates[1];
                dates[1] = dates[0];
                dates[0] = swap;
            }
            var diff = dates[1] - dates[0];
            if ( diff > 0 ) {
                var genDateMS = maxDate;
                for (var i = 0; i < numDates; i++) {
                    genDateMS = +genDateMS + +diff;
                    addUserCheckbox(genDateMS, list, false);
                }
            }
        }
    }
}


//********** SEQUENCE TAB FUNCTIONS **********


function openGateNow(activityId) {
    var data = {
        'activityId' : activityId
    };
    data[csrfTokenName] = csrfTokenValue;
    $.ajax({
        'type' : 'post',
        'url'  : LAMS_URL + 'monitoring/gate/openGate.do',
        'data'  : data,
        'success' : function(){
            loadTab();
        }
    });
}

function openGateSelectively(url){
    showDialog("dialogGate", {
        'autoOpen'  : true,
        'height'    : 820,
        'resizable' : false,
        'title'     : LABELS.CONTRIBUTE_OPEN_GATE_BUTTON,
        'open'      : function(){
            var dialog = $(this);
            // load contents after opening the dialog
            $('iframe', dialog).attr('src', url);
        },
        'close' : function(){
            loadTab();
        }
    }, false, true).addClass('modal-lg');
}

function showIntroductionDialog(lessonId) {

    showDialog('introductionDialog', {
        'height'	: 450,
        'width'  	: Math.max(380, Math.min(800, $(window).width() - 60)),
        'resizable' : false,
        'title'		: LABELS.LESSON_INTRODUCTION,
        'open'      : function(){
            $('iframe', this).attr('src', LAMS_URL + 'editLessonIntro/edit.do?lessonID='+lessonId);
            $('iframe', this).css('height', '360px');
        },
        'close' 	: function(){
            closeIntroductionDialog()
        }
    }, false);
}

function closeIntroductionDialog() {
    $('#introductionDialog').remove();
}

/**
 * Updates learner progress in sequence tab
 */
function updateSequenceTab() {
    drawLessonCompletionChart();

    sequenceCanvas = $('#sequenceCanvas');
    sequenceCanvas.css('visibility', 'hidden');

    if (originalSequenceCanvas) {
        // put bottom layer, LD SVG
        sequenceCanvas.html(originalSequenceCanvas);
    } else {
        var exit = loadLearningDesignSVG();
        if (exit) {
            // when SVG gets re-created, this update method will be run again
            return;
        }
    }

    // clear all learner icons
    $('.learner-icon, .more-learner-icon', '#canvas-container').remove();

    let lessonInstructionsSource = $('#lesson-instructions-source');
    if (lessonInstructionsSource.length === 1) {
        $('#lesson-instructions-content').html(lessonInstructionsSource.html())
            .closest('#lesson-instructions-panel').removeClass('d-none');
    }

    $.ajax({
        dataType : 'json',
        url : LAMS_URL + 'monitoring/monitoring/getLessonProgress.do',
        cache : false,
        data : {
            'lessonID'  : lessonId,
            'searchedLearnerId' : sequenceSearchedLearner
        },
        success : function(response) {
            // activities have uiids but no ids, set it here
            $.each(response.activities, function(){
                $('g[uiid="' + this.uiid + '"]', sequenceCanvas).attr('id', this.id);
            });

            var learnerCount = 0;
            $.each(response.activities, function(index, activity){
                var activityGroup = $('g[id="' + activity.id + '"]', sequenceCanvas),
                    isGate = [3,4,5,14].indexOf(activity.type) > -1;

                learnerCount += activity.learnerCount;

                if (isGate) {
                    var gateClosedIcon = activityGroup.find('.gateClosed');

                    if (activity.gateOpen && gateClosedIcon.length > 0) {
                        if (!gateOpenIconData) {
                            // if SVG is not cached, get it synchronously
                            $.ajax({
                                url : LAMS_URL + gateOpenIconPath,
                                async : false,
                                dataType : 'text',
                                success : function(response) {
                                    gateOpenIconData = response;
                                }
                            });
                        }

                        $(gateOpenIconData).clone().attr({
                            x : gateClosedIcon.attr('x'),
                            y : gateClosedIcon.attr('y'),
                            width : gateClosedIcon.attr('width'),
                            height : gateClosedIcon.attr('height'),
                        }).appendTo(activityGroup)
                            .show();

                        gateClosedIcon.remove();
                    } else {
                        gateClosedIcon.show();
                    }
                }

                if (response.contributeActivities) {
                    $.each(response.contributeActivities, function(){
                        if (activity.id == this.activityID) {
                            $.each(this.contributeEntries, function(){
                                if (!this.isComplete) {
                                    activity.requiresAttention = true;
                                    return false;
                                }
                            });
                            return false;
                        }
                    });
                }

                // put learner and attention icons on each activity shape
                addActivityIcons(activity);
            });

            // modyfing SVG in DOM does not render changes, so we need to reload it
            sequenceCanvas.html(sequenceCanvas.html());

            // only now show SVG so there is no "jump" when resizing
            sequenceCanvas.css('visibility', 'visible');

            if (sequenceSearchedLearner != null && !response.searchedLearnerFound) {
                // the learner has not started the lesson yet, display an info box
                sequenceClearSearchPhrase();
                showToast(LABELS.PROGRESS_NOT_STARTED);
            }

            var learnerTotalCount = learnerCount + response.completedLearnerCount;
            $('#learner-started-count').text(learnerTotalCount);
            $('#learner-total-count').text(response.numberPossibleLearners);
            addCompletedLearnerIcons(response.completedLearners, response.completedLearnerCount, learnerTotalCount);

            $.each(response.activities, function(activityIndex, activity){
                addActivityIconsHandlers(activity);

                if (activity.url) {
                    let activityGroup = $('g[id="' + activity.id + '"]'),
                        isGrouping = activity.type == 2,
                        isGate = [3,4,5,14,16].indexOf(activity.type) > -1;

                    activityGroup.css('cursor', 'pointer');

                    // double click on activity shape to open Monitoring for this activity
                    if (isGate) {
                        dblTap(activityGroup, function(){
                            openGateSelectively(LAMS_URL + activity.url);
                        });
                    } else if (isGrouping) {
                        dblTap(activityGroup, function(){
                            document.location.href = LAMS_URL + activity.url;
                        });
                    } else {
                        dblTap(activityGroup, function(){
                            openPopUp(LAMS_URL + activity.url, "MonitorActivity", popupHeight, popupWidth, true, true);
                        });
                    }
                }
            });

            // remove any existing popovers
            $('.popover[role="tooltip"]').remove();

            initializePortraitPopover(LAMS_URL, 'large', 'right');

            // update the cache global values so that the contributions & the Live Edit buttons will update
            lockedForEdit = response.lockedForEdit;
            lockedForEditUserId = response.lockedForEditUserId;
            lockedForEditUsername = response.lockedForEditUsername;
            updateLiveEdit();

            updateContributeActivities(response.contributeActivities);

            // set up flux for updating time limits on dashboard
            if (response.timeLimits) {
                let timeLimitFluxUrl = LAMS_URL + 'monitoring/monitoring/getTimeLimitUpdateFlux.do?';
                $.each(response.timeLimits, function(){
                    // it is a list of tool content IDs to which the dashboard will react and update time limits
                    timeLimitFluxUrl += 'toolContentIds=' + this.toolContentId + '&';
                });

                // remove from eventSources items which contain "getTimeUpdateFlux" in the URL
                $.each(eventSources, function(index, eventSource){
                    if (eventSource.url.indexOf('getTimeLimitUpdateFlux.do') > -1) {
                        try {
                            eventSource.close();
                        } catch(e) {
                            console.error("Error while closing Event Source", e);
                        }
                        eventSources.splice(index, 1);
                    }
                });

                openEventSource(timeLimitFluxUrl,
                    function (event) {
                        if ("doRefresh" == event.data && $('#sequence-tab-content').length === 1){
                            updateTimeLimits();
                        }
                    });
            }
        }
    });
}

/**
 Gets running absolute time limits for the lesson and displays them as countdown timers
 */
function updateTimeLimits(){
    $.ajax({
        dataType : 'json',
        url : LAMS_URL + 'monitoring/monitoring/getTimeLimits.do',
        cache : false,
        data : {
            'lessonID'  : lessonId
        },
        success : function(timeLimits) {
            let timeLimitsDiv = $('#lesson-time-limits').toggleClass('d-none', timeLimits.length === 0);
            $('.is-countdown', timeLimitsDiv).countdown('destroy').closest('.row').remove();

            $.each(timeLimits, function(){
                let timeLimit = this,
                    row = $('<div class="row mb-2" />').appendTo(timeLimitsDiv);
                $('<div class="col text-end" />').text(timeLimit.activityTitle).appendTo(row);
                $('<div class="col text-start" />')
                    .appendTo(row)
                    .countdown({
                        until: '+' + timeLimit.secondsLeft +'S',
                        format: 'hMS',
                        compact: true,
                        alwaysExpire : false,
                        onTick: function(periods) {
                            // check for 30 seconds or less and display timer in red
                            var secondsLeft = $.countdown.periodsToSeconds(periods);
                            if (secondsLeft <= 30) {
                                $(this).addClass('countdown-timeout');
                            } else {
                                $(this).removeClass('countdown-timeout');
                            }
                        }
                    });
            });
        }
    });


}

function updateLiveEdit() {
    if ( liveEditEnabled ) {
        if ( lockedForEdit ) {
            if ( +userId == lockedForEditUserId ) {
                $("#liveEditButton").removeClass('btn-primary');
                $("#liveEditButton").addClass('btn-warning');
                $("#liveEditButton").show();
                $("#liveEditWarning").hide();
            } else {
                $("#liveEditButton").hide();
                $("#liveEditWarning").text(LABELS.LIVE_EDIT_WARNING.replace("%0",lockedForEditUsername));
                $("#liveEditWarning").show();
            }
        } else {
            $("#liveEditButton").removeClass('btn-warning');
            $("#liveEditButton").addClass('btn-primary');
            $("#liveEditButton").show();
            $("#liveEditWarning").hide();
        }
    } else {
        $("#liveEditButton").hide();
        $("#liveEditWarning").hide();
    }
}

function loadLearningDesignSVG() {
    var exit = false;
    // fetch SVG just once, since it is immutable
    $.ajax({
        dataType : 'text',
        url : LAMS_URL + 'home/getLearningDesignThumbnail.do?',
        async : false,
        cache : false,
        data : {
            'ldId'      : ldId
        },
        success : function(response) {
            originalSequenceCanvas = response;
            sequenceCanvas = $('#sequenceCanvas').html(originalSequenceCanvas);
        },
        error : function(error) {
            exit = true;
            // the LD SVG is missing, try to re-generate it; if it is an another error, fail
            if (error.status != 404) {
                return;
            }

            // iframe just to load Authoring for a single purpose, generate the SVG
            var frame = $('<iframe />').appendTo('body').css('visibility', 'hidden');
            frame.on('load', function(){
                // disable current onload handler as closing the dialog reloads the iframe
                frame.off('load');

                // call svgGenerator.jsp code to store LD SVG on the server
                var win = frame[0].contentWindow || frame[0].contentDocument;
                $(win.document).ready(function(){
                    // when LD opens, make a callback which save the thumbnail and displays it in current window
                    win.GeneralLib.openLearningDesign(ldId, function(){
                        result = win.GeneralLib.saveLearningDesignImage();
                        frame.remove();
                        if (result) {
                            // load the image again
                            updateSequenceTab();
                        }
                    });
                });
            });
            // load svgGenerator.jsp to render LD SVG
            frame.attr('src', LAMS_URL + 'authoring/generateSVG.do?selectable=false');
        }
    });

    return exit;
}


/**
 * Forces given learners to move to activity indicated on SVG by coordinated (drag-drop)
 */
function forceComplete(currentActivityId, learners, x, y) {
    var foundActivities = [],
        targetActivity = null,
        // if "true", then we are moving all learners from the given activity
        // otherwise it is a list of selected learners IDs
        moveAll = learners === true;
    // check all activities and "users who finished lesson" bar
    $('g.svg-activity', sequenceCanvas).add('#completedLearnersContainer').each(function(){
        // find which activity learner was dropped on
        var act = $(this),
            coord = {
                'x' : act.offset().left,
                'y' : act.offset().top
            }
        if (act.is('g')) {
            var box = act[0].getBBox();
            coord.width = box.width;
            coord.height = box.height;
        } else {
            // end of lesson container
            coord.width = act.width();
            coord.height = act.height();
        }

        coord.x2 = coord.x + coord.width;
        coord.y2 = coord.y + coord.height;

        if (x >= coord.x && x <= coord.x2 && y >= coord.y && y <= coord.y2) {
            foundActivities.push(act);
        }
    });

    $.each(foundActivities, function(){
        if (this.hasClass('svg-activity-floating')) {
            // no force complete to support activities
            targetActivity = null;
            return false;
        }
        // the enveloping OptionalActivity has priority
        if (targetActivity == null || this.hasClass('svg-activity-optional')) {
            targetActivity = this;
        }
    });

    if (!targetActivity) {
        return;
    }

    var targetActivityId = null,
        isEndLesson = !targetActivity.is('g'),
        learnerNames = '';

    if (!moveAll) {
        $.each(learners, function(){
            learnerNames += this.name + ', ';
        });
        learnerNames = '"' + learnerNames.slice(0, -2) + '"';
    }


    if (isEndLesson) {
        if (currentActivityId) {
            showConfirm(LABELS.FORCE_COMPLETE_END_LESSON_CONFIRM.replace('[0]', learnerNames), function() {
                forceCompleteExecute(moveAll ? null : learners, moveAll ? currentActivityId : null, targetActivityId, false);
            });
        }
        return;
    }

    var targetActivityId = +targetActivity.attr('id');
    if (currentActivityId != targetActivityId) {
        var targetActivityName = targetActivity.hasClass('svg-activity-gate') ? "Gate" : targetActivity.find('.svg-activity-title-label').text(),
            moveBackwards = currentActivityId == null;

        // check if target activity is before current activity
        if (currentActivityId) {
            $.ajax({
                dataType : 'text',
                url : LAMS_URL + 'monitoring/monitoring/isActivityPreceding.do',
                async : false,
                cache : false,
                data : {
                    'activityA' 	 :  targetActivityId,
                    'activityB'		 :  currentActivityId
                },
                success : function(response) {
                    moveBackwards = response == 'true';
                }
            });
        }

        // check if the target activity was found or we are moving the learner from end of lesson
        if (moveBackwards) {
            // move the learner backwards
            var msgString = LABELS.FORCE_COMPLETE_REMOVE_CONTENT
                .replace('[0]', learnerNames).replace('[1]', targetActivityName);
            $('#forceBackwardsMsg', '#forceBackwardsDialog').html(msgString);
            $('#forceBackwardsDialog').data({
                'learners' : learners,
                'currentActivityId' : currentActivityId,
                'activityId': targetActivityId});
            $('#forceBackwardsDialog').modal('show');
            return;
        }

        // move the learner forward
        showConfirm(LABELS.FORCE_COMPLETE_ACTIVITY_CONFIRM.replace('[0]', learnerNames).replace('[1]', targetActivityName), function() {
            forceCompleteExecute(moveAll ? null : learners, moveAll ? currentActivityId : null, targetActivityId, false);
        });
    }
}


/**
 * Tell server to force complete the learner.
 */
function forceCompleteExecute(learners, moveAllFromActivityId, activityId, removeContent) {
    $('.svg-learner-draggable-area').addClass('force-completing');

    var learnerIds = '';
    if (learners) {
        $.each(learners, function() {
            learnerIds += this.id + ',';
        });
        learnerIds = learnerIds.slice(0, -1);
    }

    var data={
        'lessonID'   		    : lessonId,
        // either we list selected learners to move
        // or we move all learners from the given activity
        'learnerID'  		    : learnerIds,
        'moveAllFromActivityID' : moveAllFromActivityId,
        'activityID' 		    : activityId,
        'removeContent'		    : removeContent
    };
    data[csrfTokenName] = csrfTokenValue;

    $.ajax({
        url : LAMS_URL + 'monitoring/monitoring/forceComplete.do',
        type : 'POST',
        dataType : 'text',
        cache : false,
        data : data,
        success : function(response) {
            // inform user of result
            showToast(response);

            // progress changed, show it to monitor
            loadTab();
        }
    });
}


/**
 * Draw user and attention icons on top of activities.
 */
function addActivityIcons(activity) {
    if (activity.learnerCount == 0 && !activity.requiresAttention) {
        return;
    }

    // fint the activity in SVG
    var coord = getActivityCoordinates(activity);
    if (!coord) {
        return;
    }

    // add group of users icon
    var learningDesignSvg = $('svg.svg-learning-design', sequenceCanvas),
        isTool = activity.type == 1,
        isGrouping = activity.type == 2,
        // branching and gates require extra adjustments
        isBranching =  [10,11,12,13].indexOf(activity.type) > -1,
        isGate = [3,4,5,14,16].indexOf(activity.type) > -1,
        isContainer = [6,7].indexOf(activity.type) > -1,
        activityGroup = $('g[id="' + activity.id + '"]', learningDesignSvg),
        requiresAttentionIcon = activity.requiresAttention ?
            $('<img />')
                .attr({
                    'id'    : 'act' + activity.id + 'attention',
                    'src'   : LAMS_URL + 'images/exclamation.svg',
                    'title' : LABELS.CONTRIBUTE_ATTENTION
                })
                .addClass('activity-requires-attention')
            : null,
        allLearnersIcon = activity.learnerCount > 0 ?
            $('<div />')
                .attr('id', 'act' + activity.id + 'learnerGroup')
                .addClass('more-learner-icon')
            : null;


    if (isTool || isGrouping) {
        if (activity.learnerCount > 0) {
            // if learners reached the activity, make room for their icons: make activity icon and label smaller and move to top
            $('svg', activityGroup).attr({
                'x'     : coord.x + 20,
                'y'     : coord.y + 3,
                'width' : '30px',
                'height': '30px'
            });

            // switch from wide banner to narrow one
            $('.svg-tool-banner-narrow', activityGroup).show();
            $('.svg-tool-banner-wide', activityGroup).hide();

            $('.svg-activity-title-label', activityGroup).parent('foreignObject').remove();
            $('<text>').text(activity.title.length < 20 ? activity.title : activity.title.substring(0, 20) + '...')
                .attr({
                    'x' : coord.x + 55,
                    'y' : coord.y + 20
                })
                .addClass('svg-activity-title-label svg-activity-title-label-small')
                .appendTo(activityGroup);

            var learnersContainer = $('<div />').addClass('learner-icon-container');
            $('<foreignObject />').append(learnersContainer).appendTo(activityGroup).attr({
                'x' : coord.x + 20,
                'y' : coord.y + 40,
                'width'  : 184,
                'height' : 40
            });

            $.each(activity.learners, function(learnerIndex, learner){
                if (learnerIndex >= 5) {
                    return false;
                }
                $(definePortrait(learner.portraitId, learner.id, STYLE_SMALL, true, LAMS_URL))
                    .css({
                        'left'        : learnerIndex * (activity.learnerCount < 4 ? 46 : 28)  + 'px',
                        'z-index'     : 100 + learnerIndex,
                        'padding-top' : '2px'
                    })
                    .addClass('new-popover learner-icon')
                    .attr({
                        'id'            : 'act' + activity.id + 'learner' + learner.id,
                        'data-id'       : 'popover-' + learner.id,
                        'data-toggle'   : 'popover',
                        'data-portrait' : learner.portraitId,
                        'data-fullname' : getLearnerDisplayName(learner)
                    })
                    .appendTo(learnersContainer);
            });

            allLearnersIcon
                .css({
                    'left'     : '140px',
                    'z-index'  : 108,
                    'margin-top' : '1px'
                })
                .text(activity.learnerCount)
                .appendTo(learnersContainer);
        }

        if (requiresAttentionIcon) {
            $('<foreignObject />').append(requiresAttentionIcon).appendTo(activityGroup).attr({
                'x' : coord.x + 180,
                'y' : coord.y - 1,
                'width'  : 20,
                'height' : 21
            });
        }
    } else if (isGate) {
        if (activity.learnerCount > 0) {
            $('<foreignObject />').append(allLearnersIcon).appendTo(activityGroup).attr({
                'x' : coord.x + 20,
                'y' : coord.y + 20,
                'width' : 40,
                'height' : 40
            });
            allLearnersIcon.text(activity.learnerCount);
        }

        if (requiresAttentionIcon) {
            $('<foreignObject />').append(requiresAttentionIcon).appendTo(activityGroup).attr({
                'x' : coord.x + 35,
                'y' : coord.y - 10,
                'width'  : 20,
                'height' : 25
            });
        }
    } else if (isBranching) {
        if (activity.learnerCount > 0) {
            $('<foreignObject />').append(allLearnersIcon).appendTo(activityGroup).attr({
                'x' : coord.x,
                'y' : coord.y,
                'width' : 40,
                'height' : 40
            });
            allLearnersIcon.text(activity.learnerCount);
        }

        if (requiresAttentionIcon) {
            $('<foreignObject />').append(requiresAttentionIcon).appendTo(activityGroup).attr({
                'x' : coord.x + 8,
                'y' : coord.y - 28,
                'width'  : 20,
                'height' : 20
            });
        }
    } else if (isContainer) {
        if (activity.learnerCount > 0) {
            $('<foreignObject />').append(allLearnersIcon).appendTo(activityGroup).attr({
                'x' : coord.x + coord.width - 20,
                'y' : coord.y - 17,
                'width' : 40,
                'height' : 40
            });
            allLearnersIcon.text(activity.learnerCount);
        }

        if (requiresAttentionIcon) {
            $('<foreignObject />').append(requiresAttentionIcon).appendTo(activityGroup).attr({
                'x' : coord.x,
                'y' : coord.y + 15,
                'width'  : 20,
                'height' : 20
            });
        }
    }
}


/**
 * After SVG refresh, add click/dblclick/drag handlers to icons.
 */
function addActivityIconsHandlers(activity) {
    if (activity.learnerCount == 0 && !activity.requiresAttention) {
        return;
    }

    // gate activity does not allows users' view
    var usersViewable = [3,4,5,14].indexOf(activity.type) == -1;

    if (activity.learners){
        $.each(activity.learners, function(learnerIndex, learner){
            let learnerIcon = $('div#act' + activity.id + 'learner' + learner.id, sequenceCanvas)
                .css('cursor', 'pointer')
                // drag learners to force complete activities
                .draggable({
                    'appendTo'    : '.svg-learner-draggable-area',
                    'containment' : '.svg-learner-draggable-area',
                    'distance'    : 20,
                    'scroll'      : false,
                    'cursorAt'	  : {'left' : 10, 'top' : 15},
                    'helper' : "clone",
                    'stop' : function(event, ui) {
                        var learners = [{
                            'id'   : learner.id,
                            'name' : getLearnerDisplayName(learner, true)
                        }];
                        // jQuery droppable does not work for SVG, so this is a workaround
                        forceComplete(activity.id, learners, ui.offset.left, ui.offset.top);
                    }
                });

            if (usersViewable) {
                learnerIcon.click(function(event){
                    // double click on learner icon to see activity from his perspective
                    var url = LAMS_URL + 'monitoring/monitoring/getLearnerActivityURL.do?userID='
                        + learner.id + '&activityID=' + activity.id + '&lessonID=' + lessonId;
                    openPopUp(url, "LearnActivity", popupHeight, popupWidth, true);
                });
            }

            if (learner.id == sequenceSearchedLearner){
                // do it here instead of addActivityIcons()
                // as in that method the icons are added to the document yet
                // and they have no offset for calculations
                highlightSearchedLearner(learnerIcon);
            }
        });
    }

    if (activity.learnerCount > 0){
        $('div#act' + activity.id + 'learnerGroup', sequenceCanvas)
            .click(function(){
                // double click on learner group icon to see list of learners
                var ajaxProperties = {
                    url : LAMS_URL + 'monitoring/monitoring/getCurrentLearners.do',
                    data : {
                        'activityID' : activity.id
                    }
                };
                showLearnerGroupDialog(ajaxProperties, activity.title, false, true, usersViewable, false);
            });
    }
}


/**
 * Add learner icons in "finished lesson" bar.
 */
function addCompletedLearnerIcons(learners, learnerCount, learnerTotalCount) {
    var iconsContainer = $('#completedLearnersContainer');
    $('.learner-icon, .more-learner-icon', iconsContainer).remove();

    if (learners) {
        // create learner icons, along with handlers
        $.each(learners, function(learnerIndex, learner){
            if (learnerIndex >= 23) {
                // display only first few learners, not all of them
                return false;
            }

            let icon = $(definePortrait(learner.portraitId, learner.id, STYLE_SMALL, true, LAMS_URL))
                .addClass('new-popover learner-icon')
                .attr({
                    'id'            : 'learner-complete-' + learner.id,
                    'data-id'       : 'popover-' + learner.id,
                    'data-toggle'   : 'popover',
                    'data-portrait' : learner.portraitId,
                    'data-fullname' : getLearnerDisplayName(learner)
                })
                // drag learners to force complete activities
                .draggable({
                    'appendTo'    : '.svg-learner-draggable-area',
                    'containment' : '.svg-learner-draggable-area',
                    'distance'    : 20,
                    'scroll'      : false,
                    'cursorAt'	  : {'left' : 10, 'top' : 15},
                    'helper'      : function(){
                        // copy of the icon for dragging
                        return icon.clone();
                    },
                    'stop' : function(event, ui) {
                        var learners = [{
                            'id'   : learner.id,
                            'name' : getLearnerDisplayName(learner, true)
                        }];
                        // jQuery droppable does not work for SVG, so this is a workaround
                        forceComplete(null, learners, ui.offset.left, ui.offset.top);
                    }
                })
                .appendTo(iconsContainer);

            if (learner.id == sequenceSearchedLearner){
                highlightSearchedLearner(icon);
            }
        });


        $('<div />')
            .addClass('more-learner-icon')
            .text(learnerCount + '/' + learnerTotalCount)
            .appendTo(iconsContainer)
            .click(function(){
                var ajaxProperties = {
                    url : LAMS_URL + 'monitoring/monitoring/getCurrentLearners.do',
                    data : {
                        'lessonID'   : lessonId
                    }
                };
                showLearnerGroupDialog(ajaxProperties, LABELS.LEARNER_FINISHED_DIALOG_TITLE, false, true, false, false);
            });
    }
}


/**
 * Extracts activity using SVG attributes.
 */
function getActivityCoordinates(activity){
    // fix missing coordinates, not set by Flash Authoring
    if (!activity.x) {
        activity.x = 0;
    }
    if (!activity.y) {
        activity.y = 0;
    }

    var group = $('g[id="' + activity.id + '"]', sequenceCanvas);
    if (group.length == 0) {
        return;
    }

    return {
        'x'     : +group.data('x'),
        'y'     : +group.data('y'),
        'x2'    : +group.data('x') + +group.data('width'),
        'y2'    : +group.data('y') + +group.data('height'),
        'width' : +group.data('width'),
        'height': +group.data('height'),
    }

}


/**
 * Shows where the searched learner is.
 */
function highlightSearchedLearner(icon) {
    // border and z-index are manipulated via CSS
    icon.addClass('learner-searched');

    toggleInterval = setInterval(function(){
        icon.toggle();
    }, 500);

    setTimeout(function(){
        clearInterval(toggleInterval);
        icon.show();
        //if the search box was cleared during blinking, act accordingly
        if (!sequenceSearchedLearner) {
            icon.removeClass('learner-searched');
        }
    }, 3000);
}


/**
 * Cancels the performed search.
 */
function sequenceClearSearchPhrase(refresh){
    $('#sequenceSearchPhrase').val('');
    $('#sequenceSearchPhraseButton').prop('disabled', true);
    $('#sequenceSearchPhraseClearIcon').hide();
    $('#sequenceSearchPhraseIcon').show();
    $('#sequenceSearchedLearnerHighlighter').hide();
    sequenceSearchedLearner = null;
    if (refresh) {
        loadTab();
    }
}


/**
 * Shows Edit Class dialog for class manipulation.
 */
function showClassDialog(role){
    // fetch available and already participating learners and monitors
    if (!role) {
        // first time show, fill both lists
        fillClassList('Learner', false);
        fillClassList('Monitor', true);

        $('#classDialog').modal('show');
    } else {
        // refresh after page shift or search
        fillClassList(role, role.toLowerCase() == 'monitor');
    }
}


/**
 * Fills class member list with user information.
 */
function fillClassList(role, disableCreator) {
    var dialog = $('#classDialog'),
        table = $('#class' + role + 'Table', dialog),
        list = $('.dialogTable', table).empty(),
        searchPhrase = role == 'Learner' ? $('.dialogSearchPhrase', table).val().trim() : null,
        ajaxProperties = dialog.data(role + 'AjaxProperties'),
        users = null,
        userCount = null;

    if (!ajaxProperties) {
        // initialise ajax config
        ajaxProperties = {
            dataType : 'json',
            url : LAMS_URL + 'monitoring/monitoring/getClassMembers.do',
            cache : false,
            async : false,
            data : {
                'lessonID'  : lessonId,
                'role'      	 : role.toUpperCase(),
                'pageNumber'	 : 1,
                'orderAscending' : true
            }
        };

        dialog.data(role + 'AjaxProperties', ajaxProperties);
    }

    // add properties for this call only
    if (!searchPhrase){
        searchPhrase = null;
    }
    ajaxProperties.data.searchPhrase = searchPhrase;
    ajaxProperties.success = function(response) {
        users = response.users;
        userCount = response.userCount;
    }

    $.ajax(ajaxProperties);

    // hide unnecessary controls
    togglePagingCells(table, ajaxProperties.data.pageNumber, Math.ceil(userCount / 10));

    $.each(users, function(userIndex, user) {
        var checkboxId = 'class-list-' + role + '-' + user.id,
            checkbox = $('<input />').attr({
                'type' : 'checkbox',
                'id'   : checkboxId
            }).addClass('form-check-input me-1')
                .change(function(){
                    editClassMember($(this));
                }),

            userRow = $('<tr />').attr({
                'userId'  : user.id
            })
                .addClass('dialogListItem')
                .appendTo(list);

        $('<td />').append(checkbox).appendTo(userRow);
        $('<td />').append($('<label />').addClass('form-check-label').attr('for', checkboxId).text(getLearnerDisplayName(user)))
            .appendTo(userRow);

        if (user.classMember) {
            checkbox.prop('checked', 'checked');
            if (user.readonly) {
                // user creator must not be deselected
                checkbox.attr('disabled', 'disabled');
            }
        }

        if (disableCreator && user.lessonCreator) {
            userRow.addClass('dialogListItemDisabled');
        }
    });
}

/**
 * Adds/removes a Learner/Monitor to/from the class.
 */
function editClassMember(userCheckbox){
    var data={
        'lessonID' : lessonId,
        'userID'   : userCheckbox.closest('.dialogListItem').attr('userId'),
        'role'     : userCheckbox.parents('table.dialogTable')
            .parents('table.table').is('#classMonitorTable') ? 'MONITOR' : 'LEARNER',
        'add'      : userCheckbox.is(':checked')
    };
    data[csrfTokenName] = csrfTokenValue;

    $.ajax({
        url : LAMS_URL + 'monitoring/monitoring/updateLessonClass.do',
        type : 'POST',
        cache : false,
        data : data
    });
}

/**
 * Adds all learners to the class.
 */
function addAllLearners(){
    showConfirm(LABELS.CLASS_ADD_ALL_CONFIRM, function() {
        $.ajax({
            url : LAMS_URL + 'monitoring/monitoring/addAllOrganisationLearnersToLesson.do',
            type : 'POST',
            cache : false,
            data : {
                'lessonID' : lessonId
            },
            success : function(){
                showToast(LABELS.CLASS_ADD_ALL_SUCCESS);
                $('#classDialog').modal('hide');
            }
        });
    });
}

/**
 * Opens Authoring for live edit.
 */
function openLiveEdit(){
    showConfirm(LABELS.LIVE_EDIT_CONFIRM, function() {
        $.ajax({
            dataType : 'text',
            url : LAMS_URL + 'monitoring/monitoring/startLiveEdit.do',
            cache : false,
            async : false,
            data : {
                'ldId'      : ldId
            },
            success : function(response) {
                if (response) {
                    showToast(response);
                } else {
                    openAuthoring(ldId, lessonId);
                }
            }
        });
    });
}

/**
 * Adjusts sequence canvas (SVG) based on space available in the dialog.
 */
function resizeSequenceCanvas(width, height){
    var svg = $('svg.svg-learning-design', sequenceCanvas);

    if (svg.length === 0){
        // skip resizing if the SVG has not loaded (yet)
        return;
    }

    var viewBoxParts = svg.attr('viewBox').split(' '),
        svgHeight = +viewBoxParts[3],
        sequenceCanvasHeight = Math.max(svgHeight + 10, height - 140);

    // By default sequenceCanvas div is as high as SVG, but for SVG vertical centering
    // we want it to be as large as available space (iframe height minus toolbars)
    // or if SVG is higher, then as high as SVG
    sequenceCanvas.css({
        'height'  : sequenceCanvasHeight + 'px'
    });
}


/**
 * Refreshes the existing progress bars.
 */
function updateLearnersTab(){
    let learnersAccordion = $('#learners-accordion').empty(),
        itemTemplate = $('.learners-accordion-item-template').clone().removeClass('learners-accordion-item-template d-none'),
        pager = $('#learners-pager');

    $.ajax({
        'url' : LAMS_URL + 'monitoring/monitoring/getLearnerProgressPage.do',
        'data': {
            lessonID: lessonId,
            pageNumber: learnersTabPage,
            isProgressSorted: learnersTabSortedByProgress,
            searchedLearnerID: sequenceSearchedLearner
        },
        'dataType' : 'json',
        'success'  : function(response) {
            // hide/show pager elements depending if they are needed
            if (sequenceSearchedLearner) {
                pager.addClass('d-none');
            } else {
                let learnersCount = +response.learnerPossibleNumber;
                if (learnersCount < 10) {
                    $('.pager-element', pager).addClass('d-none');
                } else {
                    $('.pager-element', pager).removeClass('d-none');
                    $('#learners-previous-page', pager).toggleClass('d-none', learnersTabPage < 2);
                    let pageCount = Math.ceil(learnersCount / 10);
                    $('#learners-next-page', pager).toggleClass('d-none', learnersTabPage >= pageCount);

                    $('#learnes-order-by-completion', pager).prop('checked', learnersTabSortedByProgress);
                    $('#learners-page', pager).text(learnersTabPage + '/' + pageCount);
                }
                pager.removeClass('d-none');
            }

            $(response.learners).each(function(){
                let learner = this,
                    itemHeaderId = 'learners-accordion-heading-' + learner.id,
                    itemCollapseId = 'learners-accordion-collapse-' + learner.id,
                    item = itemTemplate.clone().data('user-id', learner.id).attr('id', 'learners-accordion-item-' + learner.id).appendTo(learnersAccordion),
                    portraitSmall = $(definePortrait(learner.portraitId, learner.id, STYLE_SMALL, true, LAMS_URL)).addClass('me-2'),
                    portraitLarge = learner.portraitId ? $(definePortrait(learner.portraitId, learner.id, STYLE_LARGE, false, LAMS_URL)) : null,
                    itemHeader = $('.accordion-header', item).attr('id', itemHeaderId);

                $('.accordion-button', itemHeader)
                    .attr('data-bs-target', '#' + itemCollapseId)
                    .attr('aria-controls', itemCollapseId)
                    .prepend('<span>' + learner.lastName + ', ' + learner.firstName + '</span>')
                    .prepend(portraitSmall);
                $('.accordion-completed-activity-count', itemHeader).text(learner.completedActivityCount).tooltip();
                $('.accordion-completed-lesson', itemHeader).toggle(learner.completedLesson).tooltip();

                $('.learners-accordion-name', item).text(learner.lastName + ', ' + learner.firstName);
                $('.learners-accordion-login', item).html('<i class="fa-regular fa-user"></i>' + learner.login);
                $('.learners-accordion-email', item).html('<i class="fa-regular fa-envelope"></i><a href="mailto:' + learner.email + '">'
                    + learner.email + '</a>');
                if (portraitLarge) {
                    $('.learners-accordion-portrait', item).append(portraitLarge);
                } else {
                    $('.learners-accordion-portrait', item).remove();
                }

                $('.accordion-collapse', item).attr('id', itemCollapseId).attr('data-bs-parent', '#learners-accordion')
                    .on('show.bs.collapse', function () {
                        clearEventSources();

                        let learnerId = $(this).closest('.accordion-item').data('user-id');
                        openEventSource(LAMS_URL + 'learning/learner/getLearnerProgressUpdateFlux.do?lessonId='
                            +  lessonId + '&userId=' + learnerId,
                            function (event) {
                                if ($('#learners-accordion-item-' + learnerId).length === 1) {
                                    drawLearnerTimeline(learnerId, decodeURIComponent(event.data));
                                }
                            });

                    });
            });
        }
    });
}

function drawLearnerTimeline(learnerId, data) {
    let item = $('#learners-accordion-item-' + learnerId),
        timelineContainer = $('.vertical-timeline-container', item),
        timeline = $('.vertical-timeline', timelineContainer).empty(),
        noProgressLabel = $('.no-progress', item);

    if (!data) {
        noProgressLabel.show();
        return;
    }
    noProgressLabel.hide();
    data = JSON.parse(data);
    let activityEntryTemplate = $('.learners-timeline-entry-template').clone().removeClass('learners-timeline-entry-template d-none');

    $(data.activities).each(function(){
        let activity = this,
            entry = activityEntryTemplate.clone().appendTo(timeline),
            icon = $('.timeline-icon', entry),
            iconURL = null,
            activityBox = ('.timeline-label', entry),
            durationCell = $('.timeline-activity-duration', activityBox),
            markCell = $('.timeline-activity-mark', activityBox);

        $('.timeline-title', entry).text(activity.name);

        switch(activity.status){
            case 0: entry.addClass('activity-current');
                icon.addClass('border-primary');
                break;
            case 1: icon.addClass('border-success');
                entry.addClass('activity-complete');
                break;
        }

        if (activity.iconURL) {
            iconURL = activity.iconURL;
        } else if (activity.type === 'g') {
            iconURL = 'images/svg/gateClosed.svg';
        } else if (activity.type === 'o') {
            iconURL = 'images/svg/branchingStart.svg';
        } else if (activity.isGrouping) {
            iconURL = 'images/svg/grouping.svg';
        }

        if (iconURL) {
            $('<img />').attr('src', LAMS_URL + iconURL).appendTo(icon);
        }

        if (typeof activity.mark !== 'undefined') {
            markCell.text(activity.mark + (activity.maxMark ? ' / ' + activity.maxMark : ''));
        } else {
            markCell.closest('tr').remove();
        }

        if (activity.duration) {
            durationCell.text(activity.duration);
        } else {
            durationCell.closest('tr').remove();
        }

        if (activity.url && activity.type != 'g') {
            activityBox.css('cursor', 'pointer');
            dblTap(activityBox, function(){
                // open pop up with user progress in the given activity
                openPopUp(activity.url, "LearnActivity", popupHeight, popupWidth, true);
            });
        }
    });

    timelineContainer.show();
}

/**
 * Handler for shift page numbers bar.
 */
function learnersPageShift(increment){
    learnersTabPage += +increment;
    learnersTabSortedByProgress = $('#learnes-order-by-completion').prop('checked');
    updateLearnersTab();
}

/**
 * Clears previous run search for phrase, in Edit Class dialog.
 */
function classClearSearchPhrase(){
    var dialog = $('#classDialog');
    $('.dialogSearchPhrase', dialog).val('').autocomplete("close");
    dialog.data('LearnerAjaxProperties').data.pageNumber = 1;
    showClassDialog('Learner');
    $('.dialogSearchPhraseClear', dialog).css('visibility', 'hidden');
}


/**
 * Clears previous run search for phrase, in Learner Group dialogs.
 */
function learnerGroupClearSearchPhrase(){
    var dialog = $('#learnerGroupDialog');
    $('.dialogSearchPhrase', dialog).val('').autocomplete("close");
    dialog.data('ajaxProperties').data.pageNumber = 1;
    showLearnerGroupDialog();
    $('.dialogSearchPhraseClear', dialog).css('visibility', 'hidden');
}
//********** GRADEBOOK TAB FUNCTIONS **********


/**
 * Refreshes Gradebook Tab.
 */
function updateGradebookTab() {
    $("#gradebookLoading").show();
    $("#gradebookDiv").load(LAMS_URL + 'gradebook/gradebookMonitoring.do?isTab=true&lessonID=' + lessonId, function() {
        $("#gradebookLoading").hide();
    });
}

function fixPagerInCenter(pagername, numcolshift) {
    $('#'+pagername+'_right').css('display','inline');
    if ( numcolshift > 0 ) {
        var marginshift = - numcolshift * 12;
        $('#'+pagername+'_center table').css('margin-left', marginshift+'px');
    }
}

// launches a popup from the page
function launchPopup(url,title) {
    var wd = null;
    if(wd && wd.open && !wd.closed){
        wd.close();
    }

    var left = ((screen.width / 2) - (popupWidth / 2));
    var top = ((screen.height / 2) - (popupHeight / 2));

    wd = window.open(url,title,'resizable,width='+popupWidth+',height='+popupHeight
        +',scrollbars'
        + ",top=" + top + ",left=" + left);
    wd.window.focus();
}

/* gradebook dialog windows	on the ipad do not update the grid width properly using setGridWidth. Calling this is
-- setting the grid to parentWidth-1 and the width of the parent to parentWidth+1, leading to growing width window
-- that overflows the dialog window. Keep the main grids slightly smaller than their containers and all is well.
*/

function resizeJqgrid(jqgrids) {
    jqgrids.each(function(index) {
        var gridId = $(this).attr('id');
        var parent = jQuery('#gbox_' + gridId).parent();
        var gridParentWidth = parent.width();
        if ( parent.hasClass('grid-holder') ) {
            gridParentWidth = gridParentWidth - 2;
        }
        jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
    });
}


/* Based on jqgrid internal functions */
function displayCellErrorMessage(table, iRow, iCol, errorLabel, errorMessage, buttonText ) {
    setTimeout(function () {
        try {
            var frozenRows = table.grid.fbRows,
                tr = table.rows[iRow];
            tr = frozenRows != null && frozenRows[0].cells.length > iCol ? frozenRows[tr.rowIndex] : tr;
            var td = tr != null && tr.cells != null ? $(tr.cells[iCol]) : $(),
                rect = td[0].getBoundingClientRect();
            $.jgrid.info_dialog.call(table, errorLabel, errorMessage, buttonText, {left:rect.left-200, top:rect.top});
        } catch (e) {
            alert(errorMessage);
        }
    }, 50);
}

function blockExportButton(areaToBlock, exportExcelUrl) {
    var token = new Date().getTime(),
        area = $('#' + areaToBlock).css('cursor', 'wait'),
        buttons = $('.btn', area).prop('disabled', true),
        form = $('<form></form>');

    fileDownloadCheckTimer = window.setInterval(function () {
        var cookieValue = $.cookie('fileDownloadToken');
        if (cookieValue == token) {
            //unBlock export button
            window.clearInterval(fileDownloadCheckTimer);
            $.cookie('fileDownloadToken', null); //clears this cookie value

            area.css('cursor', 'auto');
            buttons.prop('disabled', false);
            form.remove();
        }
    }, 1000);

    //dynamically create a form and submit it
    form.attr("method", "post");
    form.attr("action", exportExcelUrl);

    var hiddenField = $('<input></input>');
    hiddenField.attr("type", "hidden");
    hiddenField.attr("name", "downloadTokenValue");
    hiddenField.attr("value", token);
    form.append(hiddenField);

    // The form needs to be a part of the document in order to be submitted
    $(document.body).append(form);
    form.submit();

    return false;
}

//********** COMMON FUNCTIONS **********


// generic function for opening a pop up
function openPopUp(url, title, h, w, status, forceNewWindow) {

    var width = screen.width;
    var height = screen.height;

    var left = ((width / 2) - (w / 2));
    var top = ((height / 2) - (h / 2));

    if (forceNewWindow) {
        // opens a new window rather than loading content to existing one
        title += new Date().getTime();
    }

    window.open(url, title, "HEIGHT=" + h + ",WIDTH=" + w
        + ",resizable=yes,scrollbars=yes,status=" + status
        + ",menubar=no, toolbar=no"
        + ",top=" + top + ",left=" + left);
}

/**
 * Show a dialog with user list and optional Force Complete and View Learner buttons.
 */
function showLearnerGroupDialog(ajaxProperties, dialogTitle, allowSearch, allowForceComplete, allowView, allowEmail) {
    var learnerGroupDialog = $('#learnerGroupDialog'),
        learnerGroupList = $('.dialogTable', learnerGroupDialog).empty(),
        // no parameters provided? just work on what we saved
        isRefresh = ajaxProperties == null,
        learners = null,
        learnerCount = null;

    if (isRefresh) {
        // ajax and other properties were saved when the dialog was opened
        ajaxProperties = learnerGroupDialog.data('ajaxProperties');
        allowForceComplete = learnerGroupDialog.data('allowForceComplete');
        allowView = learnerGroupDialog.data('allowView');
        allowEmail = learnerGroupDialog.data('allowEmail');
        allowSearch = $('#learnerGroupSearchRow', learnerGroupDialog).is(':visible');
    } else {
        // add few standard properties to ones provided by method calls
        ajaxProperties = $.extend(true, ajaxProperties, {
            dataType : 'json',
            cache : false,
            async : false,
            data : {
                'pageNumber' 	 : 1,
                'orderAscending' : true
            }
        });

        $('#learnerGroupSearchRow', learnerGroupDialog).css('display', allowSearch ? 'table-row' : 'none');
    }

    var pageNumber = ajaxProperties.data.pageNumber;

    // set values for current variable instances
    ajaxProperties.success = function(response) {
        learners = response.learners;
        learnerCount = response.learnerCount;
    };

    var searchPhrase = allowSearch ?  $('.dialogSearchPhrase', learnerGroupDialog).val().trim() : null;
    ajaxProperties.data.searchPhrase = searchPhrase;

    // make the call
    $.ajax(ajaxProperties);

    // did all users already drift away to an another activity or there was an error?
    // close the dialog and refresh the main screen
    if (!learnerCount && !searchPhrase) {
        if (isRefresh) {
            learnerGroupDialog.modal('hide');
        }
        updateSequenceTab();
        return;
    }

    // did some users already drift away to an another activity?
    // move back until you get a page with any users
    var maxPageNumber = Math.ceil(learnerCount / 10);
    if (maxPageNumber > 0 && pageNumber > maxPageNumber) {
        shiftLearnerGroupList(-1);
        return;
    }

    // hide unnecessary controls
    togglePagingCells(learnerGroupDialog, pageNumber, maxPageNumber);

    $.each(learners, function(learnerIndex, learner) {
        var viewUrl = allowView ? LAMS_URL + 'monitoring/monitoring/getLearnerActivityURL.do?userID='
                + learner.id + '&activityID=' + ajaxProperties.data.activityID + '&lessonID=' + lessonId
                : null,
            learnerRow = $('<tr />').attr({
                'userId'  : learner.id,
                'viewUrl'    : viewUrl
            })
                .addClass('dialogListItem')
                .appendTo(learnerGroupList),
            learnerCell = $('<td />').appendTo(learnerRow),
            portraitDiv = $('<div />').attr({
                'id': 'user-'+learner.id,
            })
                .addClass('roffset5')
                .appendTo(learnerCell);
        addPortrait( portraitDiv, learner.portraitId, learner.id, 'small', true, LAMS_URL );
        $('<span/>').html(getLearnerDisplayName(learner))
            .addClass('portrait-sm-lineheight')
            .appendTo(learnerCell);

        if (allowForceComplete || allowView || allowEmail) {
            learnerRow.click(function(event){
                // select the learner
                var learnerRow = $(this),
                    selectedSiblings = learnerRow.siblings('tr.dialogListItem.dialogListItemSelected');
                // enable buttons
                $('button.learnerGroupDialogSelectableButton', learnerGroupDialog).prop('disabled', false);

                if (allowForceComplete && (event.metaKey || event.ctrlKey)) {
                    var isSelected = learnerRow.hasClass('dialogListItemSelected');
                    if (isSelected) {
                        // do not un-select last learner
                        if (selectedSiblings.length > 0) {
                            learnerRow.removeClass('dialogListItemSelected')
                        }
                    } else {
                        learnerRow.addClass('dialogListItemSelected');
                    }
                    if (selectedSiblings.length + (isSelected ? 0 : 1) > 1) {
                        // disable view button - only one learner can be viewed and multiple are selected
                        $('button#learnerGroupDialogViewButton', learnerGroupDialog).prop('disabled', true);
                    }
                } else {
                    learnerRow.addClass('dialogListItemSelected');
                    // un-select other learners
                    selectedSiblings.removeClass('dialogListItemSelected');
                }
            });
            if (allowView){
                dblTap(learnerRow, function(){
                    // same as clicking View Learner button
                    openPopUp(viewUrl, "LearnActivity", popupHeight, popupWidth, true);
                });
            }
        }
    });

    if (!isRefresh) {
        // show buttons and labels depending on parameters
        $('span#learnerGroupMultiSelectLabel, button#learnerGroupDialogForceCompleteButton, button#learnerGroupDialogForceCompleteAllButton', learnerGroupDialog)
            .css('display', allowForceComplete ? 'inline' : 'none');
        $('button#learnerGroupDialogViewButton', learnerGroupDialog)
            .css('display', allowView ? 'inline' : 'none');
        $('button#learnerGroupDialogEmailButton', learnerGroupDialog)
            .css('display', allowEmail ? 'inline' : 'none');

        $('.modal-title', learnerGroupDialog).text(dialogTitle);
        learnerGroupDialog.data({
            // save properties for refresh
            'ajaxProperties' : ajaxProperties,
            'allowForceComplete' : allowForceComplete,
            'allowView' : allowView,
            'allowEmail' : allowEmail
        })
            .modal('show');
    }
}


/**
 * Formats learner name.
 */
function getLearnerDisplayName(learner, raw) {
    return raw ? learner.lastName + ', ' + learner.firstName + ' (' + learner.login + ')' + (learner.group ? ' - ' + learner.group : '')
        : escapeHtml(learner.lastName) + ', ' + escapeHtml(learner.firstName) + ' (' + escapeHtml(learner.login) + ')'
        + (learner.group ? ' - ' + escapeHtml(learner.group) : '');
}


/**
 * Escapes HTML tags to prevent XSS injection.
 */
function escapeHtml(unsafe) {
    if (unsafe == undefined) {
        return "";
    }

    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


/**
 * Change order of learner sorting in group dialog.
 */
function sortLearnerGroupList() {
    var learnerGroupDialog = $('#learnerGroupDialog'),
        sortIcon = $('td.sortCell span', learnerGroupDialog),
        ajaxProperties = learnerGroupDialog.data('ajaxProperties'),
        // reverse current order after click
        orderAscending = !ajaxProperties.data.orderAscending;

    if (orderAscending) {
        sortIcon.removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
    } else {
        sortIcon.removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
    }

    ajaxProperties.data.orderAscending = orderAscending;
    // refresh the list
    showLearnerGroupDialog();
}

/**
 * Change order of learner sorting in Edit Class dialog.
 */
function sortClassList(role) {
    var classDialog = $('#classDialog'),
        table = $('#class' + role + 'Table', classDialog),
        sortIcon = $('td.sortCell span', table),
        ajaxProperties = classDialog.data(role + 'AjaxProperties'),
        // reverse current order after click
        orderAscending = !ajaxProperties.data.orderAscending;

    if (orderAscending) {
        sortIcon.removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
    } else {
        sortIcon.removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
    }

    ajaxProperties.data.orderAscending = orderAscending;
    // refresh the list
    showClassDialog(role);
}

/**
 * Change page in the group dialog.
 */
function shiftLearnerGroupList(shift) {
    var learnerGroupDialog = $('#learnerGroupDialog'),
        ajaxProperties = learnerGroupDialog.data('ajaxProperties'),
        pageNumber = ajaxProperties.data.pageNumber + shift;
    if (pageNumber < 0) {
        pageNumber = 1;
    }
    ajaxProperties.data.pageNumber = pageNumber;
    // refresh the dialog with new parameters
    showLearnerGroupDialog();
}

/**
 * Change page in the Edit Class dialog.
 */
function shiftClassList(role, shift) {
    var classDialog = $('#classDialog'),
        ajaxProperties = classDialog.data(role + 'AjaxProperties'),
        pageNumber = ajaxProperties.data.pageNumber + shift;
    if (pageNumber < 0) {
        pageNumber = 1;
    }
    ajaxProperties.data.pageNumber = pageNumber;
    // refresh the dialog with new parameters
    showClassDialog(role);
}


/**
 * Hides/shows paging controls
 */
function togglePagingCells(parent, pageNumber, maxPageNumber) {
    if (pageNumber + 10 <= maxPageNumber) {
        $('td.pagePlus10Cell', parent).css('visibility', 'visible');
    } else {
        $('td.pagePlus10Cell', parent).css('visibility', 'hidden');
    }
    if (pageNumber - 10 < 1) {
        $('td.pageMinus10Cell', parent).css('visibility', 'hidden');
    } else {
        $('td.pageMinus10Cell', parent).css('visibility', 'visible');
    }
    if (pageNumber + 1 <= maxPageNumber) {
        $('td.pagePlus1Cell', parent).css('visibility', 'visible');
    } else {
        $('td.pagePlus1Cell', parent).css('visibility', 'hidden');
    }
    if (pageNumber - 1 < 1) {
        $('td.pageMinus1Cell', parent).css('visibility', 'hidden');
    } else {
        $('td.pageMinus1Cell', parent).css('visibility', 'visible');
    }
    if (maxPageNumber < 2) {
        $('td.pageCell', parent).css('visibility', 'hidden');
    } else {
        $('td.pageCell', parent).css('visibility', 'visible').text(pageNumber + ' / ' + maxPageNumber);
    }
}

function showToast(text) {
    let toast = $('#toast-template').clone().attr('id', null).appendTo('#toast-container');
    toast.find('.toast-body', toast).text(text);
    toast = new bootstrap.Toast(toast[0]);
    toast.show();
}

/**
 * Works as dblclick for mobile devices.
 */
function dblTap(elem, dblClickFunction) {
    // double tap detection on mobile devices; it works also for mouse clicks
    // temporarly switched to click as jQuery mobile was removed for bootstrapping
    elem.click(function(event) {
        // was the second click quick enough after the first one?
        var currentTime = new Date().getTime(),
            tapLength = currentTime - lastTapTime;
        lastTapTime = currentTime;

        if (lastTapTarget && lastTapTarget.classList.contains('learner-icon') && tapLength < 10) {
            // after clicking learner icon there is a propagation to activity, which must be ignored
            // we can not stop propagation completetly as force complete stops working
            return;
        }

        // is the second click on the same element as the first one?
        if (event.currentTarget == lastTapTarget) {
            if (tapLength < tapTimeout && tapLength > 0) {
                event.preventDefault();
                dblClickFunction(event);
            }
        }

        lastTapTarget = event.currentTarget;
    });
}

function openEventSource(url, onMessageFunction, skipExisting) {
    if (skipExisting) {
        for (let i = 0; i < eventSources.length; i++) {
            if (eventSources[i].url === url) {
                return eventSources[i];
            }
        }
    }

    const eventSource = new EventSource(url);
    eventSources.push(eventSource);
    eventSource.onmessage = onMessageFunction;
    return eventSource;
}

function clearEventSources() {
    eventSources.forEach(function(eventSource){
        try {
            eventSource.close();
        } catch(e) {
            console.error("Error while closing Event Source", e);
        }
    });
    eventSources = [];
}

function copyLessonUrlToClipboard(){
    var copyText = $('#lessonUrl').text();
    navigator.clipboard.writeText(copyText);

    $('#lessonUrlCopyToClipboardButton').removeClass('btn-primary').addClass('btn-success');
}

function printTable() {
    var title = document.title;
    var divElements = document.getElementById('questions-data').outerHTML;
    var printWindow = window.open("", "_blank", "");
    //open the window
    printWindow.document.open();
    //write the html to the new window, link to css file
    printWindow.document.write('<html><head><title>' + title + '</title>\n');
    printWindow.document.write('<style type="text/css">\n');
    printWindow.document.write('body {font-family: "Helvetica Neue",Helvetica,Arial,sans-serif; -webkit-print-color-adjust: exact;}\n');
    printWindow.document.write('.table > tbody > tr > td.success {background-color: #81c466;}\n');
    printWindow.document.write('table { border: 1px solid #ddd; width: 100%; max-width: 100%; margin-bottom: 20px; background-color: transparent; border-spacing: 0; border-collapse: collapse; }\n');
    printWindow.document.write('* { -webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box; font-size: 16px; }\n');
    printWindow.document.write('.table-striped > tbody > tr:nth-of-type(2n+1) {background-color: #f9f9f9; }\n');
    printWindow.document.write('.table-bordered > tbody > tr > td, .table-bordered > tbody > tr > th, .table-bordered > tfoot > tr > td, .table-bordered > tfoot > tr > th, .table-bordered > thead > tr > td, .table-bordered > thead > tr > th {  border: 1px solid #ddd;  }\n');
    printWindow.document.write('a { color: #337ab7; text-decoration: none; }\n');
    printWindow.document.write('.text-center { text-align: center; }\n');
    printWindow.document.write('</style>\n');
    printWindow.document.write('</head><body>\n');
    printWindow.document.write(divElements);
    printWindow.document.write('\n</body></html>');
    printWindow.document.close();
    printWindow.focus();
    //The Timeout is ONLY to make Safari work, but it still works with FF, IE & Chrome.
    setTimeout(function() {
        printWindow.print();
        printWindow.close();
    }, 100);
}

// Removes html tags from a string
function removeHTMLTags(string) {
    var strTagStrippedText = string.replace(/<\/?[^>]+(>|$)/g, "");
    return strTagStrippedText;
}