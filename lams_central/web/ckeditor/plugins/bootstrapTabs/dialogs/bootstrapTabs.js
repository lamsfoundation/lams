// Our dialog definition.
CKEDITOR.dialog.add( 'bootstrapTabsDialog', function( editor ) {
  return {
    // Basic properties of the dialog window: title, minimum size.

    // dialog window title
    title: editor.lang.bootstrapTabs.dialogTitle,
    // dialog window size for .cke_dialog_contents_body
    minWidth: 310,
    minHeight: 280,

    // Dialog window content definition.
    // An array of objects that defines tabs.
    // Each object that defines a tab has an array of elements (e.g., form fields).
    contents: [
      {
        // Definition of the Basic Settings dialog tab.
        id: 'tab-basic',
        label: editor.lang.bootstrapTabs.tabBasicLabel,

        // The tab content.
        elements: [
          // Dialog window UI element: HTML code field.
          // http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.ui.dialog.html.html
          {
            type: 'html',
            // HTML code to be shown inside the field.
            // http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.ui.dialog.html.html#constructor
            html: editor.lang.bootstrapTabs.infoHtml
          },
          {
            // Text input field for the number of tabs.
            // Possible valid values:
            // hbox, vbox, labeled, button, checkbox, file, fileButton, html, radio, selectElement, textInput, textarea
            type: 'text',
            id: 'tab-set-title',
            label: editor.lang.bootstrapTabs.tabSetTitleLabel,
            // Validation for empty values.
            validate: CKEDITOR.dialog.validate.notEmpty( editor.lang.bootstrapTabs.invalidTabSetTitle ),
            setup: function( element ) {
              var tabsElement = element,
                  tabSetTitle = element.data( 'tab-set-title' );
              this.setValue( tabSetTitle );
            }
          },
          {
            // Select input field for the number of tabs.
            // Possible valid values:
            // hbox, vbox, labeled, button, checkbox, file, fileButton, html, radio, selectElement, textInput, textarea
            type: 'select',
            id: 'number-of-tabs',
            label: editor.lang.bootstrapTabs.numberOfTabsLabel,
            "default": 4,
            items: [ ['2'], ['3'], ['4'], ['5'], ['6'], ['7'], ['8'], ['9'] ],
            // Validation for empty values.
            validate: CKEDITOR.dialog.validate.notEmpty( editor.lang.bootstrapTabs.invalidNumberOfTabs ),
            setup: function( element ) {
              var tabsElement = element,
                  oldNumberOfTabs = tabsElement.find( '.nav.nav-tabs li a.tab-link' ).count();
              this.setValue( oldNumberOfTabs );
            }
          },
          {
            type: 'html',
            // HTML code to be shown inside the field.
            // http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.ui.dialog.html.html#constructor
            html: '<hr style="display: block;position: relative;padding: 0;margin: 8px auto;width: 100%;max-height: 0;font-size: 1px;line-height: 0;clear: both;border: none;border-top: 1px solid #aaaaaa;border-bottom: 1px solid #ffffff;"/>'
          },
          {
            type: "select",
            id: "tab-to-remove",
            label: editor.lang.bootstrapTabs.removeTabLabel,
            "default": editor.lang.bootstrapTabs.removeTabDefault,
            items: [ [ editor.lang.bootstrapTabs.removeTabDefault ] ],
            setup: function(element) {
              // clear the array of options (because repeated dialog calls will accumulate options).
              this.clear().add( editor.lang.bootstrapTabs.removeTabDefault );
              // iterate over the tabs, adding each tab's link text as an option for select field
              var tabElements = element.findOne( '.nav.nav-tabs').find('li');
              for (i = 0; i < tabElements.count(); i ++) {
                this.add( tabElements.getItem(i).findOne('a.tab-link').getText() );
              }
            }
          }
        ]
      }
    ], // end contents
    onShow: function() {
      // The code that will be executed when a dialog window is loaded.
      // To get to the element that is selected by the user (either highlighted or just having the caret inside),
      // we need to use the editor.getSelection method.
      var selection = editor.getSelection();
      // We will also use the selection.getStartElement method to get the element in which the selection starts,
      // and assign it to the element variable.
      var element = selection.getStartElement();

      if ( element ) {
        ascendant = element.getAscendant( function(element) {
          // Suddenly, getAscendant traverses up all CKEDITOR.dom.element instances to CKEDITOR.dom.document
          // Return false since this indicates we have not found a tab set as an ascendant.
          if (element instanceof CKEDITOR.dom.document) {
            return false;
          }
          return element.hasClass('bootstrap-tabs');
        });
      }

      // If there is asendant (the selection is part of a tab set)
      if ( ascendant ) {
        // We are not inserting a new tab set.
        this.insertMode = false;
        // Set the element to be passed to setup functions.
        element = ascendant;
      } else {
        // We are inserting a new tab set.
        this.insertMode = true;
      }
      // element is always a CKEDITOR.dom.element ?
      this.element = element;


      // The onShow function will finish with a call to the setupContent method that will
      // invoke the setup functions for the element. Each parameter that will be passed
      // on to the setupContent function will also be passed on to the setup functions.
      if ( !this.insertMode )
        this.setupContent( element );
    }, // end onShow
    // This method is invoked once a user clicks the OK button, confirming the dialog.
    onOk: function() {
      // The context of this function is the dialog object itself.
      // http://docs.ckeditor.com/#!/api/CKEDITOR.dialog
      var dialog = this;

      // Dialog input data.
      var numberOfTabs = dialog.getValueOf( 'tab-basic', 'number-of-tabs' ),
          tabSetTitle  = dialog.getValueOf( 'tab-basic', 'tab-set-title'  ),
          tabToRemove  = dialog.getValueOf( 'tab-basic', 'tab-to-remove'  );

      if (!this.insertMode) { // If we're editing an existing tabsElement ...

        // The tabsElement is the element that was found by the context menu via onShow.
        var tabsElement       = this.element,
            oldNumberOfTabs   = tabsElement.find( '.nav.nav-tabs li a.tab-link' ).count(), // actual number of tabs that exist
            // This is how to get the old title that is stored on containing div.
            // oldTabSetTitle is not currently used, but we may want to update all references to the
            // tabSetTitle in future revisions of this plugin via oldTabSetTitle.
            oldTabSetTitle    = tabsElement.data( 'tab-set-title' );

        var navTabsElement    = tabsElement.findOne( 'ul.nav.nav-tabs' ),
            tabContentElement = tabsElement.findOne( 'div.tab-content' );

        var tabElements      = navTabsElement.find('li'),
            tabPanelElements = tabContentElement.find( 'div.tab-pane' );

        for (i = 0; i < tabElements.count(); i ++) {

          // tabToRemove always holds a value, even if only the default value.
          if ( tabToRemove === tabElements.getItem(i).findOne('a.tab-link').getText() ) {
            // Remove the tab that was specified.
            // Only remove the tab and its content if we konw that the number of tabs
            // is the same as the number of tab contents
            // This is a simple precaution to removing content from a tab element that has become "corrupt".
            if ( tabElements.count() === tabPanelElements.count() ) {
              tabElements
                .getItem(i).remove();
              tabPanelElements
                .getItem(i).remove();
            }
          }
        }

        if ( numberOfTabs > oldNumberOfTabs ) { // If the user has increased the number of tabs,

          // Find the active tab and tab panel, then remove the active class.
          navTabsElement.findOne('.active').removeClass('active');
          tabContentElement.findOne('.active').removeClass('active');

          for ( var i = oldNumberOfTabs + 1; i <= numberOfTabs; i++ ) {  // create and append the difference in tabs.
            appendTabToElement(editor, dialog, tabsElement, numberOfTabs, i);
          }
        }

      } else { // this.insertMode (we're inserting a new set of tabs).

        // Define various html and elements of the bootstrap tabs skeleton markup (http://getbootstrap.com/javascript/#tabs-examples).
        var tabsHtml          = '<div class="bootstrap-tabs"><ul class="nav nav-tabs" role="tablist"><!-- add tabs here --></ul><div class="tab-content"><!-- add tab panels here --></div></div>',
            tabsElement       = CKEDITOR.dom.element.createFromHtml( tabsHtml );

        // Create and append numberOfTabs tabs to the tabsElement, complete with default content and tab names.
        for ( var i = 1; i <= numberOfTabs; i++ ) {
          appendTabToElement(editor, dialog, tabsElement, numberOfTabs, i);
        };

        // Finally, insert the tabsElement into the editor at the caret position.
        editor.insertElement( tabsElement );

      } // end for ( var i = 1; i <= numberOfTabs; i++ )

      // Set the (new) title on the tabsElement containing div.
      tabsElement.data( 'tab-set-title', tabSetTitle);

    } // end onOK
  }; // return dialog definition object
}); // CKEDITOR.dialog.add( 'bootstrapTabsDialog', function( editor ) {


function appendTabToElement(editor, dialog, tabsElement, numberOfTabs, i) {

  // Get the new tab-set-title from the dialog input.
  var tabSetTitle       = dialog.getValueOf( 'tab-basic', 'tab-set-title' );

  // Text for the tab name and dynamically assigned attributes for the tab and tab-panels
  var tabName = 'Tab ' + i,
      // prepend the tab tabSetTitle for tab id uniqueness and replace non-alpha-numeric and whitespace with a dash
      // 'TeSt Title StRing !! .* !@# $$ %^&  () __-- += with non-alpha-num'.replace(/(\W+|\s+|_|-+)/g, '-').replace(/-+/g, '-')
      tabIdentifier = (tabSetTitle + ' ' + tabName).replace(/(\W+|\s+|_|-+)/g, '-').replace(/-+/g, '-').toLowerCase();

  // This div template contains the content that a user will edit.
  // Without this extra div inide the tab-pane, editing the tab contents produces unexpected results.
  var tabPanelContentHtml = '<div class="tab-pane-content">' + tabName + ' Content</div>';

  // Template html for a tab and tabPanel (http://getbootstrap.com/javascript/#tabs-examples).
  var tabHtml             = '<li role="presentation"><a class="tab-link" href="#' + tabIdentifier + '" aria-controls="' + tabIdentifier + '" role="tab" data-toggle="tab">' + tabName + ' Name</a></li>';
  var tabPanelHtml        = '<div role="tabpanel" class="tab-pane" id="' + tabIdentifier + '">' + tabPanelContentHtml + '</div>';

  var tabElement      = new CKEDITOR.dom.element.createFromHtml( tabHtml ),
      tabPanelElement = new CKEDITOR.dom.element.createFromHtml( tabPanelHtml );

  // add the active class to the last tab of the element
  if (i == numberOfTabs) {
    tabElement.addClass( 'active' );
    tabPanelElement.addClass( 'active' );
  }

  // From the tabsElement, find the appropriate place to insert a new tab and its tab content.
  var navTabsElement    = tabsElement.findOne( 'ul.nav.nav-tabs' ),
      tabContentElement = tabsElement.findOne( 'div.tab-content' );

  // Append number-of-tabs nav-tabs (the clickable tabs in Bootstrap tabs)
  navTabsElement.append( tabElement );
  // Append number-of-tabs tab-panels (the area where content is displayed in Bootstrap tabs)
  tabContentElement.append( tabPanelElement );
}

