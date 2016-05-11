package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2008, Russell Gold
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
* documentation files (the "Software"), to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
* to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions
* of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
* THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*******************************************************************************************************************/
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.html.*;

import java.net.URL;
import java.util.*;
import java.io.IOException;

import com.meterware.httpunit.scripting.ScriptableDelegate;
import com.meterware.httpunit.dom.HTMLContainerElement;
import com.meterware.httpunit.dom.HTMLDocumentImpl;
import com.meterware.httpunit.dom.HTMLControl;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @author <a href="mailto:bx@bigfoot.com">Benoit Xhenseval</a>
 **/
public class ParsedHTML {

    final static private HTMLElement[] NO_ELEMENTS = new HTMLElement[0];

    final static private String[] TEXT_ELEMENTS = { "p", "h1", "h2", "h3", "h4", "h5", "h6" };

    private Node         _rootNode;

    private URL          _baseURL;

    private FrameSelector  _frame;

    private String       _baseTarget;

    private String       _characterSet;

    private WebResponse  _response;

    private boolean      _updateElements = true;

    private boolean      _enableNoScriptNodes;

    /** map of element IDs to elements. **/
    private HashMap      _elementsByID = new HashMap();

    /** map of element names to lists of elements. **/
    private HashMap      _elementsByName = new HashMap();

    /** map of DOM elements to HTML elements **/
    private ElementRegistry _registry = new ElementRegistry();

    private ArrayList    _blocksList = new ArrayList();
    private TextBlock[]  _blocks;

    private ArrayList    _tableList = new ArrayList();
    private WebTable[]   _tables;

    private ArrayList    _frameList = new ArrayList();
    private WebFrame[]   _frames;



    ParsedHTML( WebResponse response, FrameSelector frame, URL baseURL, String baseTarget, Node rootNode, String characterSet ) {
        _response     = response;
        _frame        = frame;
        _baseURL      = baseURL;
        _baseTarget   = baseTarget;
        _rootNode     = rootNode;
        _characterSet = characterSet;
    }


    /**
     * Returns the forms found in the page in the order in which they appear.
     *
     * @return an array of objects representing the forms in the page or portion of a page.
     **/
    public WebForm[] getForms() {
        HTMLCollection forms = ((HTMLContainerElement) _rootNode).getForms();
        WebForm[] result = new WebForm[ forms.getLength() ];
        for (int i = 0; i < result.length; i++) {
            result[i] = getWebForm( forms.item( i ) );
        }
        return result;
    }


    private WebForm getWebForm( Node node ) {
        WebForm webForm = (WebForm) _registry.getRegisteredElement( node );
        return webForm != null ? webForm : (WebForm) _registry.registerElement( node, toWebForm( (Element) node ) );
    }


    /**
     * Returns the links found in the page in the order in which they appear.
     **/
    public WebLink[] getLinks() {
        HTMLCollection links = ((HTMLContainerElement) _rootNode).getLinks();
        WebLink[] result = new WebLink[ links.getLength() ];
        for (int i = 0; i < result.length; i++) {
            result[i] = (WebLink) _registry.getRegisteredElement( links.item( i ) );
            if (result[i] == null) {
                result[i] = new WebLink( _response, _baseURL, links.item( i ), _frame, _baseTarget, _characterSet );
                _registry.registerElement( links.item( i ), result[i] );
            }
        }
        return result;
    }


    /**
     * Returns a proxy for each applet found embedded in this page.
     */
    public WebApplet[] getApplets() {
        HTMLCollection applets = ((HTMLContainerElement) _rootNode).getApplets();
        WebApplet[] result = new WebApplet[ applets.getLength() ];
        for (int i = 0; i < result.length; i++) {
            result[i] = (WebApplet) _registry.getRegisteredElement( applets.item( i ) );
            if (result[i] == null) {
                result[i] = new WebApplet( _response, (HTMLAppletElement) applets.item( i ), _baseTarget );
                _registry.registerElement( applets.item( i ), result[i] );
            }
        }
        return result;
    }


    /**
     * Returns the images found in the page in the order in which they appear.
     */
    public WebImage[] getImages() {
        HTMLCollection images = ((HTMLContainerElement) _rootNode).getImages();
        WebImage[] result = new WebImage[ images.getLength() ];
        for (int i = 0; i < result.length; i++) {
            result[i] = (WebImage) _registry.getRegisteredElement( images.item( i ) );
            if (result[i] == null) {
                result[i] = new WebImage( _response, this, _baseURL, (HTMLImageElement) images.item( i ), _frame, _baseTarget, _characterSet );
                _registry.registerElement( images.item( i ), result[i] );
            }
        }
        return result;
    }


    /**
     * Returns the top-level block elements found in the page in the order in which they appear.
     */
    public TextBlock[] getTextBlocks() {
        if (_blocks == null) {
            loadElements();
            _blocks = (TextBlock[]) _blocksList.toArray( new TextBlock[ _blocksList.size() ] );
        }
        return _blocks;
    }


    /**
     * Returns the first text block found in the page which matches the specified predicate and value.
     */
    public TextBlock getFirstMatchingTextBlock( HTMLElementPredicate predicate, Object criteria ) {
        TextBlock[] blocks = getTextBlocks();
        for (int i = 0; i < blocks.length; i++) {
            if (predicate.matchesCriteria( blocks[i], criteria )) return blocks[i];
        }
        return null;
    }


    public TextBlock getNextTextBlock( TextBlock block ) {
        int index = _blocksList.indexOf( block );
        if (index < 0 || index == _blocksList.size() - 1) return null;
        return (TextBlock) _blocksList.get( index+1 );
    }


    /**
     * Returns the top-level tables found in the page in the order in which they appear.
     **/
    public WebTable[] getTables() {
        if (_tables == null) {
            loadElements();
            _tables = (WebTable[]) _tableList.toArray( new WebTable[ _tableList.size() ] );
        }
        return _tables;
    }


    /**
     * Returns the HTMLElement with the specified ID.
     */
    public HTMLElement getElementWithID( String id ) {
        return (HTMLElement) getElementWithID( id, HTMLElement.class );
    }


    /**
     * Returns the HTML elements with the specified name.
     */
    public HTMLElement[] getElementsWithName( String name ) {
        loadElements();
        ArrayList elements = (ArrayList) _elementsByName.get( name );
        return elements == null ? NO_ELEMENTS : (HTMLElement[]) elements.toArray( new HTMLElement[ elements.size() ] );
    }


    /**
     * Returns the HTML elements with an attribute with the specified name and value.
     * @param name - the name of the attribute to check
     * @param value - the value of the attribute to check
     */
    public HTMLElement[] getElementsWithAttribute( String name, String value ) {
        loadElements();
        ArrayList elements = new ArrayList();
        for (Iterator i = _registry.iterator(); i.hasNext();) {
            HTMLElement element = (HTMLElement) i.next();
            String aValue=element.getAttribute( name );
            if (value.equals(aValue )) {
               //System.err.println(element.getTagName()+"("+name+")="+aValue);
             	 elements.add( element );
            }	
        }
        return (HTMLElement[]) elements.toArray( new HTMLElement[ elements.size() ] );
    }


    /**
     * Returns a list of HTML element names contained in this HTML section.
     */
    public String[] getElementNames() {
        loadElements();
        return (String[]) _elementsByName.keySet().toArray( new String[ _elementsByName.size() ] );
    }


    /**
     * get the elements with the given tagname
     * @param dom - the node to start from
     * @param name - the name of the attribute to look for
     * @return
     */
    HTMLElement[] getElementsByTagName( Node dom, String name ) {
        loadElements();
        if (dom instanceof Element) {
            return getElementsFromList( ((Element) dom).getElementsByTagName( name ) );
        } else {
            return getElementsFromList( ((Document) dom).getElementsByTagName( name ) );
        }
    }


    private HTMLElement[] getElementsFromList( NodeList nl ) {
        HTMLElement[] elements = new HTMLElement[ nl.getLength() ];
        for (int i = 0; i < elements.length; i++) {
            Node node = nl.item(i);
            elements[i] = (HTMLElement) _registry.getRegisteredElement( node );
            if (elements[i] == null) {
                elements[i] = toDefaultElement( (Element) node );
                _registry.registerElement( node, elements[i] );
            }
        }
        return elements;
    }


    /**
     * Returns the form found in the page with the specified ID.
     **/
    public WebForm getFormWithID( String id ) {
        return (WebForm) getElementWithID( id, WebForm.class );
    }


    /**
     * Returns the link found in the page with the specified ID.
     **/
    public WebLink getLinkWithID( String id ) {
        return (WebLink) getElementWithID( id, WebLink.class );

    }


    private Object getElementWithID( String id, final Class klass ) {
        loadElements();
        return whenCast( _elementsByID.get( id ), klass );
    }


    private Object whenCast( Object o, Class klass ) {
        return klass.isInstance( o ) ? o : null;
    }


    /**
     * Returns the first link found in the page matching the specified criteria.
     **/
    public WebForm getFirstMatchingForm( HTMLElementPredicate predicate, Object criteria ) {
        WebForm[] forms = getForms();
        for (int i = 0; i < forms.length; i++) {
            if (predicate.matchesCriteria( forms[i], criteria )) return forms[i];
        }
        return null;
    }


    /**
     * Returns all links found in the page matching the specified criteria.
     **/
    public WebForm[] getMatchingForms( HTMLElementPredicate predicate, Object criteria ) {
        ArrayList matches = new ArrayList();
        WebForm[] forms = getForms();
        for (int i = 0; i < forms.length; i++) {
            if (predicate.matchesCriteria( forms[i], criteria )) matches.add( forms[i] );
        }
        return (WebForm[]) matches.toArray( new WebForm[ matches.size() ] );
    }


    /**
     * Returns the form found in the page with the specified name.
      **/
    public WebForm getFormWithName( String name ) {
        return getFirstMatchingForm( WebForm.MATCH_NAME, name );
    }


    /**
     * interpret the given script element
     * @param element
     */
    void interpretScriptElement( Element element ) {
    	  // proposed patch 1152036
    	  // not enabled by wf@bitplan.com since it would brake
    	  // com.meterware.httpunit.javascript.NekoEnhancedScriptingTest - testNoScriptSections
    		//if (!HttpUnitOptions.isScriptingEnabled()) {
    		// 	return;
    		//}
        String script = getScript( element );
        if (script != null) {
            try {
                _updateElements = false;
                String language = NodeUtils.getNodeAttribute( element, "language", null );
                if (!getResponse().getScriptingHandler().supportsScriptLanguage( language )) 
                	_enableNoScriptNodes = true;
                getResponse().getScriptingHandler().runScript( language, script );
            } finally {
                clearCaches();
            }
        }
    }


    /**
     * get the script for the given node
     * @param scriptNode
     * @return the script
     */
    private String getScript( Node scriptNode ) {
        String scriptLocation = NodeUtils.getNodeAttribute( scriptNode, "src", null );
        if (scriptLocation == null) {
            return NodeUtils.asText( scriptNode.getChildNodes() );
        } else {
            try {
                return getIncludedScript( scriptLocation );
            } catch (IOException e) {
                throw new RuntimeException( "Error loading included script: " + e );
            }
        }
    }


    /**
     * Returns the contents of an included script, given its src attribute.
     * @param srcAttribute the location of the script.
     * @return the contents of the script.
     * @throws java.io.IOException if there is a problem retrieving the script
     */
    String getIncludedScript( String srcAttribute ) throws IOException {
        WebRequest req = new GetMethodWebRequest( getBaseURL(), srcAttribute );
        WebWindow window = getResponse().getWindow();
        if (window == null) throw new IllegalStateException( "Unable to retrieve script included by this response, since it was loaded by getResource(). Use getResponse() instead.");
        return window.getResource( req ).getText();
    }


    /**
     * If noscript node content is enabled, returns null - otherwise returns a concealing element.
     */
    private HTMLElement toNoscriptElement( Element element ) {
    	HTMLElement result=null;
    	if (!_enableNoScriptNodes)
        result=new NoScriptElement( element );
    	return result;
    }


    static class HtmlElementRecorder {

        protected void recordHtmlElement( NodeUtils.PreOrderTraversal pot, Node node, HTMLElement htmlElement ) {
            if (htmlElement != null) {
                addToMaps( pot, node, htmlElement );
                addToLists( pot, htmlElement );
            }
        }

        protected void addToLists( NodeUtils.PreOrderTraversal pot, HTMLElement htmlElement ) {
            for (Iterator i = pot.getContexts(); i.hasNext();) {
                Object o = i.next();
                if (o instanceof ParsedHTML) ((ParsedHTML) o).addToList( htmlElement );
            }
        }

        protected void addToMaps( NodeUtils.PreOrderTraversal pot, Node node, HTMLElement htmlElement ) {
            for (Iterator i = pot.getContexts(); i.hasNext();) {
                Object o = i.next();
                if (o instanceof ParsedHTML) ((ParsedHTML) o).addToMaps( node, htmlElement );
            }
        }

    }


    abstract static class HTMLElementFactory extends HtmlElementRecorder {
        abstract HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element );

        void recordElement( NodeUtils.PreOrderTraversal pot, Element element, ParsedHTML parsedHTML ) {
            HTMLElement htmlElement = toHTMLElement( pot, parsedHTML, element );
            recordHtmlElement( pot, element, htmlElement );
        }

        protected boolean isRecognized( ClientProperties properties ) { return true; }
        protected boolean addToContext() { return false; }

        final protected ParsedHTML getParsedHTML( NodeUtils.PreOrderTraversal pot ) {
            return (ParsedHTML) getClosestContext( pot, ParsedHTML.class );
        }

        final protected Object getClosestContext( NodeUtils.PreOrderTraversal pot, Class aClass ) {
            return pot.getClosestContext( aClass );
        }

        protected ParsedHTML getRootContext( NodeUtils.PreOrderTraversal pot ) {
            return (ParsedHTML) pot.getRootContext();
        }
    }


    static class DefaultElementFactory extends HTMLElementFactory {

        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
        	  // [ 1531005 ] getElementsWithAttribute **FIX**
            //if (element.getAttribute( "id" ).equals( "" )) { 
            //	return null;
            //}	
            return parsedHTML.toDefaultElement( element );
        }

        protected void addToLists( NodeUtils.PreOrderTraversal pot, HTMLElement htmlElement ) {}
    }


    private HTMLElement toDefaultElement( Element element ) {
        return new HTMLElementBase( element ) {
            public ScriptableDelegate newScriptable() { return new HTMLElementScriptable( this ); }
            public ScriptableDelegate getParentDelegate() { return getResponse().getDocumentScriptable(); }
        };
    }


    static class WebFormFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toWebForm( element );
        }
    }


    static class WebLinkFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toLinkAnchor( element );
        }
    }


    static class TextBlockFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toTextBlock( element );
        }


        protected boolean addToContext() {
            return true;
        }


        protected void addToLists( NodeUtils.PreOrderTraversal pot, HTMLElement htmlElement ) {
            for (Iterator i = pot.getContexts(); i.hasNext();) {
                Object o = i.next();
                if (!(o instanceof ParsedHTML)) continue;
                ((ParsedHTML) o).addToList( htmlElement );
                break;
            }
        }

    }


    static class ScriptFactory extends HTMLElementFactory {

        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return null;
        }

        void recordElement( NodeUtils.PreOrderTraversal pot, Element element, ParsedHTML parsedHTML ) {
            parsedHTML.interpretScriptElement( element );
        }
    }


    static class NoScriptFactory extends HTMLElementFactory {

        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toNoscriptElement( element );
        }

        protected boolean addToContext() {
            return true;
        }
    }


    static class WebFrameFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toWebFrame( element );
        }
    }


    static class WebIFrameFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toWebIFrame( element );
        }


        protected boolean isRecognized( ClientProperties properties ) {
            return properties.isIframeSupported();
        }


        protected boolean addToContext() {
            return true;
        }
    }


    static class WebImageFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toWebImage( element );
        }
    }


    static class WebAppletFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toWebApplet( element );
        }
        protected boolean addToContext() { return true; }
    }


    static class WebTableFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toWebTable( element );
        }
        protected boolean addToContext() { return true; }

        protected void addToLists( NodeUtils.PreOrderTraversal pot, HTMLElement htmlElement ) {
            for (Iterator i = pot.getContexts(); i.hasNext();) {
                Object o = i.next();
                if (o instanceof ParsedHTML) ((ParsedHTML) o).addToList( htmlElement );
                if (o instanceof TableCell) break;
            }
        }
    }


    static class TableRowFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            WebTable wt = getWebTable( pot );
            if (wt == null) return null;
            return wt.newTableRow( (HTMLTableRowElement) element );
        }
        private WebTable getWebTable( NodeUtils.PreOrderTraversal pot ) {
            return (WebTable) getClosestContext( pot, WebTable.class );
        }
        protected boolean addToContext() { return true; }
        protected void addToLists( NodeUtils.PreOrderTraversal pot, HTMLElement htmlElement ) {
            getWebTable( pot ).addRow( (TableRow) htmlElement );
        }
    }


    static class TableCellFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            TableRow tr = getTableRow( pot );
            if (tr == null) return null;
            return tr.newTableCell( (HTMLTableCellElement) element );
        }
        private TableRow getTableRow( NodeUtils.PreOrderTraversal pot ) {
            return (TableRow) getClosestContext( pot, TableRow.class );
        }
        protected boolean addToContext() { return true; }
        protected void addToLists( NodeUtils.PreOrderTraversal pot, HTMLElement htmlElement ) {
            getTableRow( pot ).addTableCell( (TableCell) htmlElement );
        }
    }

    static class FormControlFactory extends HTMLElementFactory {

        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            HTMLFormElement form = ((HTMLControl) element).getForm();
            if (form == null) {
                return newControlWithoutForm( parsedHTML, element );
            } else {
                return parsedHTML.getWebForm( form ).newFormControl( element );
            }
        }

        private HTMLElement newControlWithoutForm( ParsedHTML parsedHTML, Element element ) {
            if ((element.getNodeName().equalsIgnoreCase( "button" ) || element.getNodeName().equalsIgnoreCase( "input" )) &&
                isValidNonFormButtonType( NodeUtils.getNodeAttribute( element, "type" ) )) {
                return parsedHTML.toButtonWithoutForm( element );
            } else {
                return null;
            }
        }


        private boolean isValidNonFormButtonType( String buttonType ) {
            return buttonType.equals( "" ) || buttonType.equalsIgnoreCase( "button" );
        }
    }


    static class WebListFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            return parsedHTML.toOrderedList( element );
        }

        protected boolean addToContext() { return true; }

        protected void addToLists( NodeUtils.PreOrderTraversal pot, HTMLElement htmlElement ) {
            TextBlock textBlock = getTextBlock( pot );
            if (textBlock != null) textBlock.addList( (WebList) htmlElement );
        }

        private TextBlock getTextBlock( NodeUtils.PreOrderTraversal pot ) {
            return (TextBlock) getClosestContext( pot, TextBlock.class );
        }
    }


    static class ListItemFactory extends HTMLElementFactory {
        HTMLElement toHTMLElement( NodeUtils.PreOrderTraversal pot, ParsedHTML parsedHTML, Element element ) {
            WebList webList = getWebList( pot );
            if (webList == null) return null;
            return webList.addNewItem( element );
        }

        private WebList getWebList( NodeUtils.PreOrderTraversal pot ) {
            return (WebList) getClosestContext( pot, WebList.class );
        }

        protected boolean addToContext() { return true; }

        protected void addToLists( NodeUtils.PreOrderTraversal pot, HTMLElement htmlElement ) {
        }
    }


    private static HashMap _htmlFactoryClasses = new HashMap();
    private static HTMLElementFactory _defaultFactory = new DefaultElementFactory();

    static {
        _htmlFactoryClasses.put( "a",        new WebLinkFactory() );
        _htmlFactoryClasses.put( "area",     new WebLinkFactory() );
        _htmlFactoryClasses.put( "form",     new WebFormFactory() );
        _htmlFactoryClasses.put( "img",      new WebImageFactory() );
        _htmlFactoryClasses.put( "applet",   new WebAppletFactory() );
        _htmlFactoryClasses.put( "table",    new WebTableFactory() );
        _htmlFactoryClasses.put( "tr",       new TableRowFactory() );
        _htmlFactoryClasses.put( "td",       new TableCellFactory() );
        _htmlFactoryClasses.put( "th",       new TableCellFactory() );
        _htmlFactoryClasses.put( "frame",    new WebFrameFactory() );
        _htmlFactoryClasses.put( "iframe",   new WebIFrameFactory() );
        _htmlFactoryClasses.put( "script",   new ScriptFactory() );
        _htmlFactoryClasses.put( "noscript", new NoScriptFactory() );
        _htmlFactoryClasses.put( "ol",       new WebListFactory() );
        _htmlFactoryClasses.put( "ul",       new WebListFactory() );
        _htmlFactoryClasses.put( "li",       new ListItemFactory() );

        for (int i = 0; i < TEXT_ELEMENTS.length; i++) {
            _htmlFactoryClasses.put( TEXT_ELEMENTS[i], new TextBlockFactory() );
        }

        for (Iterator i = Arrays.asList( FormControl.getControlElementTags() ).iterator(); i.hasNext();) {
            _htmlFactoryClasses.put( i.next(), new FormControlFactory() );
        }
    }

    private static HTMLElementFactory getHTMLElementFactory( String tagName ) {
        final HTMLElementFactory factory = (HTMLElementFactory) _htmlFactoryClasses.get( tagName );
        return factory != null ? factory : _defaultFactory;
    }


    private void loadElements() {
        if (!_updateElements) return;

        NodeUtils.NodeAction action = new NodeUtils.NodeAction() {
            public boolean processElement( NodeUtils.PreOrderTraversal pot, Element element ) {
                HTMLElementFactory factory = getHTMLElementFactory( element.getNodeName().toLowerCase() );
                if (factory == null || !factory.isRecognized( getClientProperties() )) return true;
                if (pot.getClosestContext( ContentConcealer.class ) != null) return true;

                if (!_registry.hasNode( element )) factory.recordElement( pot, element, ParsedHTML.this );
                if (factory.addToContext()) pot.pushContext( _registry.getRegisteredElement( element ) );

                return true;
            }
            public void processTextNode( NodeUtils.PreOrderTraversal pot, Node textNode ) {
                if (textNode.getNodeValue().trim().length() == 0) return;

                Node parent = textNode.getParentNode();
                if (!parent.getNodeName().equalsIgnoreCase( "body" )) return;
                if (pot.getClosestContext( ContentConcealer.class ) != null) return;
                new HtmlElementRecorder().recordHtmlElement( pot, textNode, newTextBlock( textNode ) );
            }
        };
        NodeUtils.PreOrderTraversal nt = new NodeUtils.PreOrderTraversal( getRootNode() );
        nt.pushBaseContext( this );
        nt.perform( action );

        _updateElements = false;
    }


    private ClientProperties getClientProperties() {
        WebWindow window = _response.getWindow();
        return window == null ? ClientProperties.getDefaultProperties() : window.getClient().getClientProperties();
    }


    private Button toButtonWithoutForm( Element element ) {
        return new Button( _response, (HTMLControl) element );
    }


    private WebForm toWebForm( Element element ) {
        return new WebForm( _response, _baseURL, element, _frame, _baseTarget, _characterSet, _registry );
    }


    private WebFrame toWebFrame( Element element ) {
        return new WebFrame( _response, _baseURL, element, _frame );
    }


    private WebFrame toWebIFrame( Element element ) {
        return new WebIFrame( _baseURL, element, _frame );
    }


    /**
     * convert the given child to a link anchor
     * @param child
     * @return
     */
    private WebLink toLinkAnchor( Element child ) {
        return (!isWebLink( child )) ? null : new WebLink( _response, _baseURL, child, _frame, _baseTarget, _characterSet );
    }


    /**
     * check whether the given node is a Web link by checking that 
     * the node is of type "A"
     * @param node - the node to check
     * @return whether the given node represents a web link
     */
    public static boolean isWebLink( Node node ) {
    	/*
       * Bug report [ 1156972 ] isWebLink doesn't recognize all anchor tags
       * by fregienj claims this be changed to check whether the case-insensitive node name is "A"
       * -> should be isAnchor method and getAnchor
       */
    	boolean result=false;
      String tagName = ((Element) node).getTagName();    	
      if (!tagName.equalsIgnoreCase( "area" ) && !tagName.equalsIgnoreCase( "a")) {
      } else {
      	// pre 1.7 code - still active
      	result=(node.getAttributes().getNamedItem( "href" ) != null);
      	// proposed patch - not activated
      	// result=true;
      }
      return result;
    }


    private WebImage toWebImage( Element child ) {
        return new WebImage( _response, this, _baseURL, (HTMLImageElement) child, _frame, _baseTarget, _characterSet );
    }


    private WebApplet toWebApplet( Element element ) {
        return new WebApplet( _response, (HTMLAppletElement) element, _baseTarget );
    }


    private WebTable toWebTable( Element element ) {
        return new WebTable( _response, _frame, element, _baseURL, _baseTarget, _characterSet );
    }


    private TextBlock toTextBlock( Element element ) {
        return new TextBlock( _response, _frame, _baseURL, _baseTarget, element, _characterSet );
    }


    private TextBlock newTextBlock( Node textNode ) {
        return new TextBlock( _response, _frame, _baseURL, _baseTarget, textNode, _characterSet );
    }


    private WebList toOrderedList( Element element ) {
        return new WebList( _response, _frame, _baseURL, _baseTarget, element, _characterSet );
    }


    private void addToMaps( Node node, HTMLElement htmlElement ) {
        _registry.registerElement( node, htmlElement );
        if (htmlElement.getID() != null) _elementsByID.put( htmlElement.getID(), htmlElement );
        if (htmlElement.getName() != null) addNamedElement( htmlElement.getName(), htmlElement );
    }


    private void addNamedElement( String name, HTMLElement htmlElement ) {
        List list = (List) _elementsByName.get( name );
        if (list == null) _elementsByName.put( name, list = new ArrayList() );
        list.add( htmlElement );
    }


    private void addToList( HTMLElement htmlElement ) {
        ArrayList list = getListForElement( htmlElement );
        if (list != null) list.add( htmlElement );
    }


    private ArrayList getListForElement( HTMLElement element ) {
        if (element instanceof WebTable) return _tableList;
        if (element instanceof WebFrame) return _frameList;
        if (element instanceof BlockElement) return _blocksList;
        return null;
    }


    /**
     * Returns the first link which contains the specified text.
     **/
    public WebLink getLinkWith( String text ) {
        return getFirstMatchingLink( WebLink.MATCH_CONTAINED_TEXT, text );
    }


    /**
     * Returns the link which contains the first image with the specified text as its 'alt' attribute.
     **/
    public WebLink getLinkWithImageText( String text ) {
        WebImage image = getImageWithAltText( text );
        return image == null ? null : image.getLink();
    }


    /**
     * Returns the link found in the page with the specified name.
     **/
    public WebLink getLinkWithName( String name ) {
        return getFirstMatchingLink( WebLink.MATCH_NAME, name );
    }


    /**
     * Returns the first link found in the page matching the specified criteria.
     **/
    public WebLink getFirstMatchingLink( HTMLElementPredicate predicate, Object criteria ) {
        WebLink[] links = getLinks();
        for (int i = 0; i < links.length; i++) {
            if (predicate.matchesCriteria( links[i], criteria )) return links[i];
        }
        return null;
    }


    /**
     * Returns all links found in the page matching the specified criteria.
     **/
    public WebLink[] getMatchingLinks( HTMLElementPredicate predicate, Object criteria ) {
        ArrayList matches = new ArrayList();
        WebLink[] links = getLinks();
        for (int i = 0; i < links.length; i++) {
            if (predicate.matchesCriteria( links[i], criteria )) matches.add( links[i] );
        }
        return (WebLink[]) matches.toArray( new WebLink[ matches.size() ] );
    }


    /**
     * Returns the image found in the page with the specified name.
     **/
    public WebImage getImageWithName( String name ) {
        WebImage[] images = getImages();
        for (int i = 0; i < images.length; i++) {
            if (HttpUnitUtils.matches( name, images[i].getName() )) return images[i];
        }
        return null;
    }


    /**
     * Returns the first image found in the page with the specified src attribute.
     **/
    public WebImage getImageWithSource( String source ) {
        WebImage[] images = getImages();
        for (int i = 0; i < images.length; i++) {
            if (HttpUnitUtils.matches( source, images[i].getSource() )) return images[i];
        }
        return null;
    }


    /**
     * Returns the first image found in the page with the specified alt attribute.
     **/
    public WebImage getImageWithAltText( String altText ) {
        WebImage[] images = getImages();
        for (int i = 0; i < images.length; i++) {
            if (HttpUnitUtils.matches( altText, images[i].getAltText() )) return images[i];
        }
        return null;
    }


    /**
     * Returns the first table in the response which matches the specified predicate and value.
     * Will recurse into any nested tables, as needed.
     * @return the selected table, or null if none is found
     **/
    public WebTable getFirstMatchingTable( HTMLElementPredicate predicate, Object criteria ) {
        return getTableSatisfyingPredicate( getTables(), predicate, criteria );
    }

     /**
      * Returns the tables in the response which match the specified predicate and value.
      * Will recurse into any nested tables, as needed.
      * @return the selected tables, or null if none are found
      **/
     public WebTable[] getMatchingTables( HTMLElementPredicate predicate, Object criteria ) {
         return getTablesSatisfyingPredicate( getTables(), predicate, criteria );
     }


    /**
     * Returns the first table in the response which has the specified text as the full text of
     * its first non-blank row and non-blank column. Will recurse into any nested tables, as needed.
     * @return the selected table, or null if none is found
     **/
    public WebTable getTableStartingWith( String text ) {
        return getFirstMatchingTable( WebTable.MATCH_FIRST_NONBLANK_CELL, text );
    }


    /**
     * Returns the first table in the response which has the specified text as a prefix of the text
     * in its first non-blank row and non-blank column. Will recurse into any nested tables, as needed.
     * @return the selected table, or null if none is found
     **/
    public WebTable getTableStartingWithPrefix( String text ) {
        return getFirstMatchingTable( WebTable.MATCH_FIRST_NONBLANK_CELL_PREFIX, text );
    }


    /**
     * Returns the first table in the response which has the specified text as its summary attribute.
     * Will recurse into any nested tables, as needed.
     * @return the selected table, or null if none is found
     **/
    public WebTable getTableWithSummary( String summary ) {
        return getFirstMatchingTable( WebTable.MATCH_SUMMARY, summary );
    }


    /**
     * Returns the first table in the response which has the specified text as its ID attribute.
     * Will recurse into any nested tables, as needed.
     * @return the selected table, or null if none is found
     **/
    public WebTable getTableWithID( String ID ) {
        return getFirstMatchingTable( WebTable.MATCH_ID, ID );
    }


    /**
     * Returns a copy of the domain object model associated with this page.
     **/
    public Node getDOM() {
        return getRootNode().cloneNode( /* deep */ true );
    }

//---------------------------------- Object methods --------------------------------


    public String toString() {
        return _baseURL.toExternalForm() + System.getProperty( "line.separator" ) +
               _rootNode;
    }


//---------------------------------- package members --------------------------------


    /**
     * Specifies the root node for this HTML fragment.  It will be either an HTMLDocument or an HTMLElement
     * representing a page fragment.
     */
    void setRootNode( Node rootNode ) {
        if (_rootNode != null && rootNode != _rootNode )
            throw new IllegalStateException( "The root node has already been defined as " + _rootNode + " and cannot be redefined as " + rootNode );
        _rootNode = rootNode;
        if (rootNode instanceof HTMLDocumentImpl) ((HTMLDocumentImpl) rootNode).setIFramesEnabled( getClientProperties().isIframeSupported());
        clearCaches();
    }


    private void clearCaches() {
        _tables = null;
        _frames = null;
        _blocks = null;
        _updateElements = true;
    }


    /**
     * Returns the base URL for this HTML segment.
     **/
    URL getBaseURL() {
        return _baseURL;
    }


    WebResponse getResponse() {
        return _response;
    }


    /**
     * Returns the domain object model associated with this page, to be used internally.
     **/
    Node getOriginalDOM() {
        return getRootNode();
    }


    HTMLElement getElement( Node node ) {
        return (HTMLElement) _registry.getRegisteredElement( node );
    }


    /**
     * Returns the frames found in the page in the order in which they appear.
     **/
    public WebFrame[] getFrames() {
        if (_frames == null) {
            loadElements();
            _frames = (WebFrame[]) _frameList.toArray( new WebFrame[ _frameList.size() ] );
        }
        return _frames;
    }


//---------------------------------- private members --------------------------------


    Node getRootNode() {
        if (_rootNode == null) throw new IllegalStateException( "The root node has not been specified" );
        return _rootNode;
    }


    /**
     * Returns the table with the specified text in its summary attribute.
     **/
    private WebTable getTableSatisfyingPredicate( WebTable[] tables, HTMLElementPredicate predicate, Object value ) {
        for (int i = 0; i < tables.length; i++) {
            if (predicate.matchesCriteria( tables[i], value )) {
                return tables[i];
            } else {
                for (int j = 0; j < tables[i].getRowCount(); j++) {
                    for (int k = 0; k < tables[i].getColumnCount(); k++) {
                        TableCell cell = tables[i].getTableCell(j,k);
                        if (cell != null) {
                            WebTable[] innerTables = cell.getTables();
                            if (innerTables.length != 0) {
                                WebTable result = getTableSatisfyingPredicate( innerTables, predicate, value );
                                if (result != null) return result;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


     /**
      * Returns the tables which match the specified criteria.
      **/
     private WebTable[] getTablesSatisfyingPredicate(WebTable[] tables, HTMLElementPredicate predicate, Object value) {
         ArrayList matches = new ArrayList();
         for (int i = 0; i < tables.length; i++) {
             if (predicate.matchesCriteria(tables[i], value)) {
                 matches.add(tables[i]);
             }
             for (int j = 0; j < tables[i].getRowCount(); j++) {
                 for (int k = 0; k < tables[i].getColumnCount(); k++) {
                     TableCell cell = tables[i].getTableCell(j, k);
                     if (cell != null) {
                         WebTable[] innerTables = cell.getTables();
                         if (innerTables.length != 0) {
                             WebTable[] result = getTablesSatisfyingPredicate(innerTables, predicate, value);
                             if (result != null && result.length > 0) {
                                 for (int l = 0; l < result.length; l++) {
                                     matches.add(result[l]);
                                 }
                             }
                         }
                     }
                 }
             }
         }
         if(matches.size() > 0) {
             return (WebTable[]) matches.toArray( new WebTable[ matches.size() ] );
         } else {
             return null;
         }
     }


    class WebIFrame extends WebFrame implements ContentConcealer {

        public WebIFrame( URL baseURL, Node frameNode, FrameSelector parentFrame ) {
            super( _response, baseURL, frameNode, parentFrame );
        }
    }


    /**
     * NoScript Element
     */
    class NoScriptElement extends HTMLElementBase implements ContentConcealer {

        public NoScriptElement( Node node ) {
            super( node );
        }


        public ScriptableDelegate newScriptable() {
            return null;
        }


        public ScriptableDelegate getParentDelegate() {
            return null;
        }
    }

}
