/*
 * FileTokenReplacerTest.java
 * JUnit based test
 *
 * Created on 30 March 2005, 14:50
 */

package org.lamsfoundation.lams.tool.deploy;

import junit.framework.*;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import org.lamsfoundation.lams.tool.deploy.*;


/**
 *
 * @author chris
 */
public class FileTokenReplacerTest extends TestCase
{
    private File testFile = null;
    
    public static final String TEST_TOKEN = "test";
    public static final String TOKEN_BAD_CHAR = "test.test";
    public static final String TOKEN_BAD_REGEX = "test]test";
    public static final String TEST_VALUE = "value";
    
    public FileTokenReplacerTest(String testName)
    {
        super(testName);
        
    }
    
    protected void setUp() throws java.lang.Exception
    {
        testFile = new File("test/file/tokenreplacer.test");
    }
    
    protected void tearDown() throws java.lang.Exception
    {
    }
    
    public void testBadCharToken() throws Exception
    {
        HashMap replacementMap = new HashMap();
        replacementMap.put(TOKEN_BAD_CHAR, TEST_VALUE);
        try
        {
            FileTokenReplacer replacer = new FileTokenReplacer(testFile, replacementMap);
        }
        catch (Exception ex)
        {
            if (!(ex instanceof DeployException))
            {
                fail();
            }
            System.out.println(ex);
        }
    }
    
    public void testBadRegexToken() throws Exception
    {
        HashMap replacementMap = new HashMap();
        replacementMap.put(TOKEN_BAD_REGEX, TEST_VALUE);
        try
        {
            FileTokenReplacer replacer = new FileTokenReplacer(testFile, replacementMap);
        }
        catch (Exception ex)
        {
            if (!(ex instanceof DeployException))
            {
                fail();
            }
            else if ((ex.getCause() == null) || (!(ex.getCause() instanceof PatternSyntaxException)))
            {
                fail();
            }
            System.out.println(ex);
        }
    }
    
    public void testNoFile() throws Exception
    {
        HashMap replacementMap = new HashMap();
        replacementMap.put(TEST_TOKEN, TEST_VALUE);
        File noFile = new File("quux/foo/bar.txt");
        try
        {
            FileTokenReplacer replacer = new FileTokenReplacer(noFile, replacementMap);
            fail();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
    
    public void testReplace() throws Exception
    {
        HashMap replacementMap = new HashMap();
        replacementMap.put(TEST_TOKEN, TEST_VALUE);
        FileTokenReplacer replacer = new FileTokenReplacer(testFile, replacementMap);
        String output = replacer.replace();
        assertNotNull(output);
        assertTrue(output.indexOf(TEST_TOKEN) == -1);
        System.out.println(output);
    }
    
    
    
    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(FileTokenReplacerTest.class);
        
        return suite;
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    
}
