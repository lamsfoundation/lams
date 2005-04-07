/*
 * FileTokenReplacer.java
 *
 * Created on 30 March 2005, 13:06
 */

package org.lamsfoundation.lams.tool.deploy;
        
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
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
    
    public static final Pattern TOKEN_PATTERN = Pattern.compile("\\$\\{\\w+\\}");
    //pattern for ${letters}
    
    protected static String makeToken(final String tokenValue)
    {
        StringBuffer buf =  new StringBuffer(TOKEN_PREFIX);
        buf.append(tokenValue);
        buf.append(TOKEN_SUFFIX);
        return buf.toString();
    }
    
    protected static boolean isValidToken(String token)
    {
        return TOKEN_PATTERN.matcher(token).matches();
    }
    
    
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
                throw new DeployException(key +" does not make a valid token ("+token+")");
            }
            
            fileString = fileString.replaceAll(makeTokenRegex(key), value);
        }
        
        return fileString;
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
        
        try
        {
            return FileUtils.readFileToString(file, "UTF8");
        }
        catch (IOException ioex)
        {
            throw new DeployException("Could not read file", ioex);
        }

    }
}


