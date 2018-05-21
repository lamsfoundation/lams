/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.util.ILoadedMessageSourceService;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * This class forms the basic implementation of an output definition and output value factory, which is the class in a
 * tool that creates the output definition and output values for a tool. Each tool that has outputs should create its
 * own factory class that extends this class and uses the methods implemented in this class to create the actual
 * ToolOutputDefinition and ToolOutput objects.
 *
 * The implemented factory (in the tool) needs to supply either (a) its own messageService bean (a Spring bean normally
 * defined in the tool's applicationContext file in the toolMessageService property or (b) the name of its I18N language
 * package/filename AND the the loadedMessageSourceService (which is defined as a Spring bean in the core context). One
 * of the two options (but not both) is required so that the getDescription() method can get the internationalised
 * description of the output definition from the tool's internationalisation files. If neither the messageService or the
 * I18N name is not supplied then the name if the output definition will appear in the description field.
 *
 * The implemented factory should implement public SortedMap<String, ToolOutputDefinition>
 * getToolOutputDefinitions(Object toolContentObject) and this method should call the buildBlahDefinition() calls to
 * build the definitions.
 *
 * It should also implement two methods similar to SortedMap<String, ToolOutput> getToolOutput(List<String> names,
 * various tool objects) and ToolOutput getToolOutput(String name, various tool objects) to get the actual outputs. The
 * "various tool objects" will vary from tool to tool - some tools may wish to pass in the raw session and user ids,
 * others may pass in specific tool objects. As these inputs will vary greatly from tool to tool, no abstract method has
 * been included in this parent class. Putting these calls in the factory allows all the "work" of the output generation
 * to be grouped together and it also allows the getToolOutput() logic to get to getDescription() logic in the factory.
 *
 * Example definitions for tool factories: <bean id="mcOuputFactory"
 * class="org.lamsfoundation.lams.tool.mc.service.MCOutputFactory"> <property name="loadedMessageSourceService"><ref
 * bean="loadedMessageSourceService"/></property>
 * <property name="languageFilename"><value>org.lamsfoundation.lams.tool.mc.ApplicationResources</value></property>
 * </bean>
 *
 * <bean id="forumOuputDefinitionFactory" class="org.lamsfoundation.lams.tool.forum.service.ForumOutputFactory">
 * <property name="toolMessageService"><ref bean="forumMessageService"/></property> </bean>
 *
 */
public abstract class OutputFactory {

    protected Logger log = Logger.getLogger(OutputFactory.class);

    private MessageService toolMessageService;
    private ILoadedMessageSourceService loadedMessageSourceService;
    private String languageFilename;
    private MessageSource msgSource = null; // derived from toolMessageService, loadedMessageSourceService,
    // languageFilename
    protected final String KEY_PREFIX = "output.desc.";
    protected final String CONDITION_NAME_SEPARATOR = "#";

    /**
     * Create a map of the tool output definitions, suitable for returning from the method getToolOutputDefinitions() in
     * the ToolContentManager interface. The class for the toolContentObject will be unique to each tool e.g. for
     * Multiple Choice tool it will be the McContent object.
     *
     * If the toolContentObject should not be null - if the toolContentId supplied in the call to
     * getToolOutputDefinitions(Long toolContentId) does not match to any existing content, the content should be the
     * tool's default tool content.
     *
     * @param toolContentObject
     * @return SortedMap of ToolOutputDefinitions where the key is the name from each definition
     * @throws ToolException
     */
    public abstract SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException;

    /**
     * Tool specific toolMessageService, such as the forumMessageService in the Forum tool
     */
    private MessageService getToolMessageService() {
	return toolMessageService;
    }

    public void setToolMessageService(MessageService toolMessageService) {
	this.toolMessageService = toolMessageService;
    }

    /**
     * Set the common loadedMessageSourceService, based on the bean defined in the common Spring context. If
     * toolMessageService is not set, then the languageFilename and loadedMessageSourceService should be set.
     */
    private ILoadedMessageSourceService getLoadedMessageSourceService() {
	return loadedMessageSourceService;
    }

    public void setLoadedMessageSourceService(ILoadedMessageSourceService loadedMessageSourceService) {
	this.loadedMessageSourceService = loadedMessageSourceService;
    }

    /**
     * Set the filename/path for the tool's I18N files. If toolMessageService is not set, then the languageFilename and
     * loadedMessageSourceService should be set.
     */
    public String getLanguageFilename() {
	return languageFilename;
    }

    /** Get the filename and path for the tool's I18N files. */
    public void setLanguageFilename(String languageFilename) {
	this.languageFilename = languageFilename;
    }

    /**
     * Get the I18N description for this key. If the tool has supplied a messageService, then this is used to look up
     * the key and hence get the text. Otherwise if the tool has supplied a I18N languageFilename then it is accessed
     * via the shared toolActMessageService. If neither are supplied or the key is not found, then any "." in the name
     * are converted to space and this is used as the return value.
     *
     * This is normally used to get the description for a definition, in whic case the key should be in the format
     * output.desc.[definition name], key = definition name and addPrefix = true. For example a definition name of
     * "learner.mark" becomes output.desc.learner.mark.
     *
     * If you want to use this to get an arbitrary string from the I18N files, then set addPrefix = false and the
     * output.desc will not be added to the beginning.
     */
    protected String getI18NText(String key, boolean addPrefix) {
	String translatedText = null;

	MessageSource tmpMsgSource = getMsgSource();
	if (tmpMsgSource != null) {
	    if (addPrefix) {
		key = KEY_PREFIX + key;
	    }
	    Locale locale = LocaleContextHolder.getLocale();
	    try {
		translatedText = tmpMsgSource.getMessage(key, null, locale);
	    } catch (NoSuchMessageException e) {
//		log.warn("Unable to internationalise the text for key " + key
//			+ " as no matching key found in the msgSource");
	    }
	} else {
//	    log.warn("Unable to internationalise the text for key " + key
//		    + " as no matching key found in the msgSource. The tool's OutputDefinition factory needs to set either (a) messageSource or (b) loadedMessageSourceService and languageFilename.");
	}

	if (translatedText == null || translatedText.length() == 0) {
	    translatedText = key.replace('.', ' ');
	}

	return translatedText;
    }

    /**
     * Get the MsgSource, combining getToolMessageService() and getLoadedMessageSourceService(). Caches the result so it
     * only needs to be calculated once (most tools will require more than one call to this code!
     */
    private MessageSource getMsgSource() {
	if (msgSource == null) {
	    if (getToolMessageService() != null) {
		msgSource = getToolMessageService().getMessageSource();
	    }
	    if (msgSource == null && getLoadedMessageSourceService() != null && getLanguageFilename() != null) {
		msgSource = getLoadedMessageSourceService().getMessageService(getLanguageFilename());
	    }
	}
	return msgSource;
    }

    /**
     * Generic method for building a tool output definition. It will get the definition's description from the I18N file
     * using the getDescription() method. Only use if the other buildBlahDefinitions do not suit your needs.
     */
    protected ToolOutputDefinition buildDefinition(String definitionName, OutputType type, Object startValue,
	    Object endValue, Object complexValue, Boolean showConditionNameOnly, Class valueClass) {
	ToolOutputDefinition definition = new ToolOutputDefinition();
	definition.setName(definitionName);
	definition.setDescription(getI18NText(definitionName, true));
	definition.setType(type);
	definition.setStartValue(startValue);
	definition.setEndValue(endValue);
	definition.setComplexDefinition(complexValue);
	definition.setShowConditionNameOnly(showConditionNameOnly);
	definition.setValueClass(valueClass);
	return definition;
    }

    /**
     * Wrapper method for build definition to set the isDefaultGradebookMark flag
     */
    protected ToolOutputDefinition buildDefinition(String definitionName, OutputType type, Object startValue,
	    Object endValue, Object complexValue, Boolean showConditionNameOnly, Boolean isDefaultGradebookMark,
	    Class valueClass) {
	ToolOutputDefinition definition = this.buildDefinition(definitionName, type, startValue, endValue, complexValue,
		showConditionNameOnly, valueClass);
	definition.setIsDefaultGradebookMark(isDefaultGradebookMark);
	return definition;
    }

    /**
     * Build a tool definition designed for a range of integer values. It will get the definition's description from the
     * I18N file using the getDescription() method and set the type to OUTPUT_LONG.
     */
    protected ToolOutputDefinition buildRangeDefinition(String definitionName, Long startValue, Long endValue) {
	return buildDefinition(definitionName, OutputType.OUTPUT_LONG, startValue, endValue, null, Boolean.FALSE,
		Long.class);
    }

    /**
     * Build a tool definition designed for a range of string values. It will get the definition's description from the
     * I18N file using the getDescription() method and set the type to OUTPUT_LONG.
     */
    protected ToolOutputDefinition buildRangeDefinition(String definitionName, String startValue, String endValue) {
	return buildDefinition(definitionName, OutputType.OUTPUT_STRING, startValue, endValue, null, Boolean.FALSE,
		String.class);
    }

    /**
     * Wrapper for buildRangeDefinition so you can set isDefaultGradebookMark
     */
    protected ToolOutputDefinition buildRangeDefinition(String definitionName, Long startValue, Long endValue,
	    Boolean isDefaultGradebookMark) {
	return buildDefinition(definitionName, OutputType.OUTPUT_LONG, startValue, endValue, null, Boolean.FALSE,
		isDefaultGradebookMark, String.class);
    }

    /**
     * Wrapper for buildRangeDefinition so you can set isDefaultGradebookMark
     */
    protected ToolOutputDefinition buildRangeDefinition(String definitionName, String startValue, String endValue,
	    Boolean isDefaultGradebookMark) {
	return buildDefinition(definitionName, OutputType.OUTPUT_STRING, startValue, endValue, null, Boolean.FALSE,
		isDefaultGradebookMark, String.class);
    }

    /**
     * Build a tool definition designed for a single double value, which is likely to be a statistic number of questions
     * answered It will get the definition's description from the I18N file using the getDescription() method and set
     * the type to OUTPUT_LONG.
     */
    protected ToolOutputDefinition buildLongOutputDefinition(String definitionName) {
	return buildDefinition(definitionName, OutputType.OUTPUT_LONG, null, null, null, Boolean.FALSE, Long.class);
    }

    /**
     * Build a tool definition designed for a single double value, which is likely to be a statistic such as average
     * number of posts. It will get the definition's description from the I18N file using the getDescription() method
     * and set the type to OUTPUT_DOUBLE.
     */
    protected ToolOutputDefinition buildDoubleOutputDefinition(String definitionName) {
	return buildDefinition(definitionName, OutputType.OUTPUT_DOUBLE, null, null, null, Boolean.FALSE, Double.class);
    }

    /**
     * Build a tool definition designed for a single boolean value, which is likely to be a test such as user has
     * answered all questions correctly. It will get the definition's description from the I18N file using the
     * getDescription() method and set the type to OUTPUT_BOOLEAN. A Boolean tool definition should have default
     * condition name for the true and false conditions. The code will automatically look for two strings in the I18N
     * file output.desc.<description>.true and output.desc.<description>.false
     */
    protected ToolOutputDefinition buildBooleanOutputDefinition(String definitionName) {
	ToolOutputDefinition definition = buildDefinition(definitionName, OutputType.OUTPUT_BOOLEAN, null, null, null,
		Boolean.FALSE, Boolean.class);

	List<BranchCondition> conditions = new ArrayList<BranchCondition>();
	conditions.add(new BranchCondition(null, null, new Integer(1), definitionName,
		getI18NText(definitionName + ".true", true), OutputType.OUTPUT_BOOLEAN.toString(), null, null,
		Boolean.TRUE.toString()));

	conditions.add(new BranchCondition(null, null, new Integer(2), definitionName,
		getI18NText(definitionName + ".false", true), OutputType.OUTPUT_BOOLEAN.toString(), null, null,
		Boolean.FALSE.toString()));

	definition.setConditions(conditions);

	return definition;
    }

    /**
     * Build a tool definition designed for a set of boolean conditions. It will get the definition's description from
     * the I18N file using the getDescription() method and set the type to OUTPUT_SET_BOOLEAN. The tool's factory should
     * then set up a series of conditions, each of type OUTPUT_BOOLEAN. Sets showConditionNameOnly to true so that the
     * user in authoring doesn't see the internal definitions, just the condition name.
     */
    protected ToolOutputDefinition buildBooleanSetOutputDefinition(String definitionName) {
	ToolOutputDefinition definition = buildDefinition(definitionName, OutputType.OUTPUT_SET_BOOLEAN, null, null,
		null, Boolean.TRUE, (new HashSet<Boolean>()).getClass());
	List<BranchCondition> defaultConditions = new ArrayList<BranchCondition>();
	definition.setConditions(defaultConditions);
	return definition;
    }

    /**
     * Build a tool definition designed for a single String value. It will get the definition's description from the
     * I18N file using the getDescription() method and set the type to OUTPUT_STRING.
     */
    protected ToolOutputDefinition buildStringOutputDefinition(String definitionName) {
	return buildDefinition(definitionName, OutputType.OUTPUT_STRING, null, null, null, Boolean.FALSE, String.class);
    }

    /**
     * Build a tool definition for a complex value output. It will get the definition's description from the I18N file
     * using the getDescription() method and set the type to OUTPUT_COMPLEX.
     */
    protected ToolOutputDefinition buildComplexOutputDefinition(String definitionName, Class valueClass) {
	return buildDefinition(definitionName, OutputType.OUTPUT_COMPLEX, null, null, null, Boolean.TRUE, valueClass);
    }

    /**
     * Build a condition name based on a definition name. For user customised conditions, the conditions name MUST start
     * with the definition name for UI to be able to match conditions to definition in the authoring interface, but
     * then each condition name needs to be unique, hence "uniquePart".
     *
     * @param definitionName:
     *            Must not be null
     * @param uniquePart:
     *            May be null if the condition names are to be the same as the definition name.
     * @return combined string
     */
    protected String buildConditionName(String definitionName, String uniquePart) {
	return uniquePart != null && uniquePart.length() > 0 ? definitionName + CONDITION_NAME_SEPARATOR + uniquePart
		: definitionName;
    }

    /**
     * Given a condition name built with buildConditionName, split is back into its definition name and unique part.
     *
     * @param conditionName:
     *            Must not be null
     * @return String[definition name, unique part]
     */
    protected String[] splitConditionName(String conditionName) {
	int index = conditionName.indexOf(CONDITION_NAME_SEPARATOR);
	if (index > -1) {
	    if (index + 1 < conditionName.length()) {
		return new String[] { conditionName.substring(0, index), conditionName.substring(index + 1) };
	    } else {
		return new String[] { conditionName.substring(0, index), "" };
	    }
	} else {
	    return new String[] { conditionName, "" };
	}
    }

    /**
     * If a tool supports data flow, it should override this method and return all the classes it supports, otherwise
     * non matching inputs will be filtered off in Authoring. IMPORTANT: For compatibility, NULL means that all
     * definitions are accepted! If the return value is not NULL, the definitions should be limited only to the matching
     * ones.
     *
     * @param definitionType
     * @return
     */
    public Class[] getSupportedDefinitionClasses(int definitionType) {
	return null;
    }
}
