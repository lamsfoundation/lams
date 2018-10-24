package com.meterware.servletunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2004, Russell Gold
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
import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
class FilterUrlMap {

    private ArrayList _urlPatterns = new ArrayList();
    private ArrayList _filters     = new ArrayList();


    void put( String urlPattern, FilterMetaData metaData ) {
        _urlPatterns.add( UrlPatternMatcher.newPatternMatcher( urlPattern ) );
        _filters.add( metaData );
    }


    FilterMetaData[] getMatchingFilters( String resourceName ) {
        ArrayList matches = new ArrayList();
        for (int i = 0; i < _urlPatterns.size(); i++) {
            UrlPatternMatcher urlPattern = (UrlPatternMatcher) _urlPatterns.get( i );
            if (urlPattern.matchesResourceName( resourceName )) matches.add( _filters.get( i ) );
        }
        return (FilterMetaData[]) matches.toArray( new FilterMetaData[ matches.size() ] );
    }

}


abstract class UrlPatternMatcher {

    static UrlPatternMatcher[] _templates = new UrlPatternMatcher[] { new ExtensionUrlPatternMatcher(), new PathMappingUrlPatternMatcher() };

    static UrlPatternMatcher newPatternMatcher( String pattern ) {
        for (int i = 0; i < _templates.length; i++) {
            UrlPatternMatcher matcher = _templates[i].create( pattern );
            if (matcher != null) return matcher;
        }
        return new ExactUrlPatternMatcher( pattern );
    }

    /**
     * Returns a suitable pattern matcher if this class is compatible with the pattern. Will return null otherwise.
     */
    abstract UrlPatternMatcher create( String pattern );

    /**
     * Returns true if the specified resource matches this pattern.
     */
    abstract boolean matchesResourceName( String resourceName );
}


class ExactUrlPatternMatcher extends UrlPatternMatcher {
    private String _pattern;

    public ExactUrlPatternMatcher( String pattern ) { _pattern = pattern; }

    UrlPatternMatcher create( String pattern ) { return new ExactUrlPatternMatcher( pattern ); }

    boolean matchesResourceName( String resourceName ) { return _pattern.equals( resourceName ); }
}


class ExtensionUrlPatternMatcher extends UrlPatternMatcher {
    private String _suffix;

    ExtensionUrlPatternMatcher() {}

    ExtensionUrlPatternMatcher( String suffix ) { _suffix = suffix; }

    UrlPatternMatcher create( String pattern ) {
        return !pattern.startsWith( "*." ) ? null : new ExtensionUrlPatternMatcher( pattern.substring( 1 ) );
    }

    boolean matchesResourceName( String resourceName ) { return resourceName.endsWith( _suffix ); }
}


class PathMappingUrlPatternMatcher extends UrlPatternMatcher {
    private String _exactPath;
    private String _prefix;

    PathMappingUrlPatternMatcher() {}

    PathMappingUrlPatternMatcher( String exactPath ) {
        _exactPath = exactPath;
        _prefix    = exactPath + '/';
    }

    UrlPatternMatcher create( String pattern ) {
        return !handlesPattern( pattern ) ? null : new PathMappingUrlPatternMatcher( pattern.substring( 0, pattern.length()-2 ) );
    }


    private boolean handlesPattern( String pattern ) {
        return pattern.startsWith( "/" ) && pattern.endsWith( "/*" );
    }


    boolean matchesResourceName( String resourceName ) {
        return resourceName.startsWith( _prefix ) || resourceName.equals( _exactPath );
    }
}
