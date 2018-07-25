package com.meterware.httpunit.dom;
/********************************************************************************************************************
 * $Header$
 *
 * Copyright (c) 2007-2008, Russell Gold
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
import com.meterware.httpunit.javascript.ScriptingEngineImpl;
import com.meterware.httpunit.scripting.ScriptingEngine;
import com.meterware.httpunit.scripting.ScriptableDelegate;

import org.mozilla.javascript.Scriptable;

/**
 * @author <a href="mailto:mailto:russgold@httpunit.org">Russell Gold</a>
 */
public abstract class AbstractDomComponent extends ScriptingEngineImpl implements Scriptable {

    private static int _anonymousFunctionNum;

    public String getClassName() {
        return getClass().getName();
    }


    public ScriptingEngine newScriptingEngine( ScriptableDelegate child ) {
        throw new UnsupportedOperationException( );
    }


    public void clearCaches() {}


    public boolean has( String name, Scriptable start ) {
        return super.has( name, start ) || ScriptingSupport.hasNamedProperty( this, getJavaPropertyName( name ), start );
    }


    public Object get( String propertyName, Scriptable scriptable ) {
        Object result = super.get( propertyName, scriptable );
        if (result != NOT_FOUND) return result;

        return ScriptingSupport.getNamedProperty( this, getJavaPropertyName( propertyName ), scriptable );
    }


    protected String getJavaPropertyName( String propertyName ) {
        return propertyName;
    }


    public void put( String propertyName, Scriptable initialObject, Object value ) {
        super.put( propertyName, initialObject, value );
        ScriptingSupport.setNamedProperty( this, getJavaPropertyName( propertyName ), value );
    }


    protected static String createAnonymousFunctionName() {
        return "anon_" + (++_anonymousFunctionNum);
    }
}
