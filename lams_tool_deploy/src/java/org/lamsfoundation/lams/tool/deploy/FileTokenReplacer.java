/*
 * FileTokenReplacer.java
 *
 * Created on 30 March 2005, 13:06
 */

package org.lamsfoundation.lams.tool.deploy;
        
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
/**
 * Parsers a file and does token replacement
 * @author chris
 */
public class FileTokenReplacer
{
    protected File file;
    protected Map replacementMap;
    
    public static final String TOKEN_PREFIX = "${";
    public static final String TOKEN_SUFFIX = "}";
    public static final String TOKEN_REGEX_PREFIX = "\\$\\{";
    public static final String TOKEN_REGEX_SUFFIX = "\\}";
    
    public static final Pattern TOKEN_PATTERN = Pattern.compile("^\\$\\{[A-Za-z0-9]+\\}$");
    //pattern for ${letters}
    
    
    
    
    /** Creates a new instance of FileTokenReplacer */
    public FileTokenReplacer(final File file, final Map replacementMap)
    {
        if (file == null)
        {
            throw new IllegalArgumentException("File is null");
        }
        else if (replacementMap == null)
        {
            throw new IllegalArgumentException("Replacement map is null");
        }
        else if (!file.exists())
        {
            throw new IllegalArgumentException("File does not exist");
        }
        
        this.file = file;
        this.replacementMap = replacementMap;
    }
    
    public String replace() throws DeployException
    {
        String fileString = readFile();
        Set keys = replacementMap.keySet();
        Iterator keyIter = keys.iterator();
        while (keyIter.hasNext())
        {
            String key =  (String) keyIter.next();
            String value = (String) replacementMap.get(key);
            String token = makeToken(key);
            if (!isValidToken(token))
            {
                throw new DeployException(key +" does not make a valid token");
            }
            
            fileString = fileString.replaceAll(makeTokenRegex(key), value);
        }
        
        return fileString;
    }
    
    protected String makeToken(final String tokenValue)
    {
        StringBuffer buf =  new StringBuffer(TOKEN_PREFIX);
        buf.append(tokenValue);
        buf.append(TOKEN_SUFFIX);
        return buf.toString();
    }
    
    protected boolean isValidToken(String token)
    {
        return TOKEN_PATTERN.matcher(token).matches();
    }
    
    protected String makeTokenRegex(String tokenValue) throws DeployException
    {
        StringBuffer buf = new StringBuffer(TOKEN_REGEX_PREFIX);
        buf.append(tokenValue);
        buf.append(TOKEN_REGEX_SUFFIX);
        String regex = buf.toString();
        try
        {
            Pattern.compile(regex);
        }
        catch (PatternSyntaxException psynex)
        {
            throw new DeployException(tokenValue+" does not make a valid regex", psynex);
        }
        return regex;
    }
    
    protected String readFile() throws DeployException
    {
        StringBuffer buf = new StringBuffer();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null)
            {
                buf.append(line);
            }
        }
        catch (IOException ioex)
        {
            throw new DeployException("Could not read file", ioex);
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ioex)
                {
                    //not much we can do here
                }
            }
        }
        return buf.toString();
    }
}


