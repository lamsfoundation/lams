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

/* $Id$ */
package org.eucm.lams.tool.eadventure.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.dto.ExpressionInfo;
import org.eucm.lams.tool.eadventure.model.EadventureCondition;
import org.eucm.lams.tool.eadventure.model.EadventureExpression;
import org.eucm.lams.tool.eadventure.model.EadventureParam;
import org.eucm.lams.tool.eadventure.util.EadventureConditionComparator;
import org.eucm.lams.tool.eadventure.util.EadventureExpressionComparator;
import org.eucm.lams.tool.eadventure.web.form.EadventureConditionForm;
import org.eucm.lams.tool.eadventure.web.form.EadventureExpressionForm;
import org.eucm.lams.tool.eadventure.web.form.EadventureForm;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Auxiliary action in author mode. It contains operations with EadventureCondition. The rest of operations are located
 * in
 * <code>AuthoringAction</code> action.
 *
 * @author Andrey Balan
 * @author Angel del Blanco
 * @see org.eucm.lams.tool.eadventure.web.action.AuthoringAction
 */
public class AuthoringEadventureConditionAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();

	if (param.equals("showConditions")) {
	    return showConditions(mapping, form, request, response);
	}
	if (param.equals("newConditionInit")) {
	    return newConditionInit(mapping, form, request, response);
	}
	if (param.equals("editCondition")) {
	    return editCondition(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateCondition")) {
	    return saveOrUpdateCondition(mapping, form, request, response);
	}
	if (param.equals("removeCondition")) {
	    return removeCondition(mapping, form, request, response);
	}
	if (param.equals("newExpressionInit")) {
	    return newExpressionInit(mapping, form, request, response);
	}
	if (param.equals("prepareOperatorAndSecondVar")) {
	    return prepareOperatorAndSecondVar(mapping, form, request, response);
	}
	if (param.equals("editExpression")) {
	    return editExpression(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateExpression")) {
	    return saveOrUpdateExpression(mapping, form, request, response);
	}
	if (param.equals("removeExpression")) {
	    return removeExpression(mapping, form, request, response);
	}
	if (param.equals("upCondition")) {
	    return upCondition(mapping, form, request, response);
	}
	if (param.equals("downCondition")) {
	    return downCondition(mapping, form, request, response);
	}
	if (param.equals("upExpression")) {
	    return upExpression(mapping, form, request, response);
	}
	if (param.equals("downExpression")) {
	    return downExpression(mapping, form, request, response);
	}

	if (param.equals("checkCondition")) {
	    return checkCondition(mapping, form, request, response);
	}

	if (param.equals("prepareCheckCondition")) {
	    return prepareCheckCondition(mapping, form, request, response);
	}

	return mapping.findForward(EadventureConstants.ERROR);
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */

    private ActionForward showConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	EadventureForm existForm = (EadventureForm) sessionMap.get(EadventureConstants.ATTR_RESOURCE_FORM);

	EadventureForm eadForm = (EadventureForm) form;
	try {
	    PropertyUtils.copyProperties(eadForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	if (mode.isAuthor()) {
	    return mapping.findForward(EadventureConstants.SUCCESS);
	} else {
	    return mapping.findForward(EadventureConstants.DEFINE_LATER);
	}
    }

    private ActionForward cleanConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	//TODO ver lo de recargar la página
	//sessionMap.put(EadventureConstants., value)
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Display empty page for new eadventure condition.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newConditionInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	// re-init expressionList
	sessionMap.put(EadventureConstants.ATTR_EXPRESSION_LIST, null);

	// check if some edventure game has been added
	// it is not necessary, anulo el boton con javascriptç
	//TODO se puede quitar
	/*
	 * ActionErrors errors = new ActionErrors();
	 * String hasFile = (String)sessionMap.get(EadventureConstants.ATTR_HAS_FILE);
	 * if (!hasFile.equalsIgnoreCase("true"))
	 * errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	 * EadventureConstants.ERROR_NOT_EAD_ADD));
	 * 
	 * if (!errors.isEmpty()) {
	 * this.addErrors(request, errors);
	 * sessionMap.put(EadventureConstants.ATTR_ERROR_IN_CONDITION, "true");
	 * return mapping.findForward("conditions");
	 * }
	 */

	((EadventureConditionForm) form).setSessionMapID(sessionMapID);
	//Create expressionList and it into sessionMap, delete if already exists
	SortedSet expressionList = getEadventureExpressionList(sessionMap);
	//Add to the session that the condition is not being edited. In case of deleting an expression of this conditions, this expression must be deleted from the
	//conditions too.
	sessionMap.put(EadventureConstants.ATTR_EDIT_CONDITION, "false");
	//TODO se puede quitarjunto con el de arriba
//	sessionMap.put(EadventureConstants.ATTR_ERROR_IN_CONDITION, "false");
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Display edit page for existed taskList item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	// re-init expressionList
	sessionMap.put(EadventureConstants.ATTR_EXPRESSION_LIST, null);

	int position = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_CONDITION_POSITION), -1);
	EadventureCondition condition = null;
	if (position != -1) {
	    SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	    List<EadventureCondition> rList = new ArrayList<EadventureCondition>(conditionList);
	    condition = rList.get(position);
	    if (condition != null) {
		((EadventureConditionForm) form).setName(condition.getName());
		//TODO va duplicado, creo que mejor solo por atributo
		((EadventureConditionForm) form).setExpressionList(condition.getEadListExpression());
		((EadventureConditionForm) form).setPosition(String.valueOf(condition.getSequenceId()));
		SortedSet expressionList = getEadventureExpressionList(sessionMap);
		expressionList.addAll(condition.getEadListExpression());
	    }
	}
	//Add to the session that the condition is being edited. In case of deleting an expression of this conditions, this expression must be deleted from the
	//conditions too.
	sessionMap.put(EadventureConstants.ATTR_EDIT_CONDITION, "true");
	//TODO comprobar el caso null
	return condition == null ? null : mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * This method will get necessary information from taskList item form and save or update into
     * <code>HttpSession</code> TaskListItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward saveOrUpdateCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	EadventureConditionForm conditionForm = (EadventureConditionForm) form;
	ActionErrors errors = validateEadventureCondition(conditionForm, request);

	if (!errors.isEmpty()) {

	    // expressionList.clear();
	    this.addErrors(request, errors);
	    return mapping.findForward("addcondition");
	}

	try {
	    extractFormToEadventureCondition(request, conditionForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(EadventureConstants.ERROR_MSG_UPLOAD_FAILED, e.getMessage()));
	    if (!errors.isEmpty()) {
		//SortedSet expressionList = getEadventureExpressionList(sessionMap);
		//	expressionList.clear();
		this.addErrors(request, errors);
		return mapping.findForward("addcondition");
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, conditionForm.getSessionMapID());
	SortedSet expressionList = getEadventureExpressionList(sessionMap);
	expressionList.clear();
	// return null to close this window
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Remove eadventureCondition from HttpSession list and update page display. As authoring rule, all persist only
     * happen
     * when user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	// re-init expressionList
	sessionMap.put(EadventureConstants.ATTR_EXPRESSION_LIST, null);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_CONDITION_POSITION),
		-1);
	if (sequenceId != -1) {
	    SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	    List<EadventureCondition> rList = new ArrayList<EadventureCondition>(conditionList);
	    EadventureCondition condition = rList.remove(sequenceId);
	    for (EadventureCondition otherCondition : conditionList) {
		if (otherCondition.getSequenceId() > sequenceId) {
		    otherCondition.setSequenceId(otherCondition.getSequenceId() - 1);
		}
	    }
	    conditionList.clear();
	    conditionList.addAll(rList);
	    // add to delList
	    List delList = getDeletedEadventureConditionList(sessionMap);
	    delList.add(condition);
	}
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Move up current condition.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward upCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchCondition(mapping, request, true);
    }

    /**
     * Move down current Condition.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward downCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchCondition(mapping, request, false);
    }

    /**
     * Move up current Expression.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward upExpression(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	//TODO quitar el ultimo parametro
	return switchExpression(mapping, request, true, 0);
    }

    /**
     * Move down current Expression.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward downExpression(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	//TODO quitar el ultimo parametro
	return switchExpression(mapping, request, false, 0);
    }

    /**
     * Display empty page for new eadventure expression.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newExpressionInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	((EadventureExpressionForm) form).setSessionMapID(sessionMapID);
	populateFormWithPossibleVariables(form, request);

	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Prepare the operator and the second var of the expression taking into account the type of the first var
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward prepareOperatorAndSecondVar(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	EadventureExpressionForm expForm = ((EadventureExpressionForm) form);
	expForm.setSessionMapID(sessionMapID);
	// conserve the all possible varsOp1
	populateFormWithPossibleVariables(form, request);
	// fill the form with the selected value
	expForm.setSelectedVarOp1(WebUtil.readStrParam(request, EadventureConstants.PARAM_VAR_NAME));
	// now fill the other list of elements taking into account the value selected in the authoringView for the firstOp
	List listParams = this.getEadventureParamList(sessionMap);
	String name = WebUtil.readStrParam(request, EadventureConstants.PARAM_VAR_NAME);
	int selectedVarType = prepareOpAndVarFromType(form, name, listParams);
	// get the position
	String position = request.getParameter(EadventureConstants.PARAM_EXPRESSION_POSITION);
	((EadventureExpressionForm) form).setPosition(position);
	((EadventureExpressionForm) form).setNextOp("and");
	sessionMap.put(EadventureConstants.ATTR_EXPR_SELECTED_TYPE, selectedVarType);

	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Prepare the operator and the second var of the expression taking into account the type of the first var
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editExpression(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	int selectedVarType = -1;
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	populateFormWithPossibleVariables(form, request);

	int position = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_EXPRESSION_POSITION), -1);
	EadventureExpression expression = null;
	if (position != -1) {
	    // List<EadventureExpression> expressionList = this.getListFromSession(sessionMap, EadventureConstants.ATTR_EXPRESSION_LIST);
	    SortedSet<EadventureExpression> expressionList = getEadventureExpressionList(sessionMap);
	    List<EadventureExpression> rList = new ArrayList<EadventureExpression>(expressionList);
	    ((EadventureExpressionForm) form).setPosition(String.valueOf(position));
	    List<EadventureParam> listParams = this.getEadventureParamList(sessionMap);
	    expression = rList.get(position);
	    if (expression != null) {
		//TODO repasar si merece la pena hacerlo asi o meter algo mas de info sobre los parametros en el form!!!!
		selectedVarType = prepareOpAndVarFromType(form, expression.getFirstOp().getName(), listParams);
		((EadventureExpressionForm) form).setSelectedVarOp1(String.valueOf(expression.getFirstOp().getName()));
		((EadventureExpressionForm) form).setSelectedOperator(expression.getExpresionOp());
		if (expression.getValueIntroduced() == null) {
		    ((EadventureExpressionForm) form)
			    .setSelectedVarOp2(String.valueOf(expression.getVarIntroduced().getName()));
		    ((EadventureExpressionForm) form).setIntroducedValue(null);
		    ((EadventureExpressionForm) form).setSecondVarSelected(true);

		} else {
		    ((EadventureExpressionForm) form).setIntroducedValue(expression.getValueIntroduced());
		    ((EadventureExpressionForm) form).setSelectedVarOp2(null);
		    ((EadventureExpressionForm) form).setSecondVarSelected(false);
		}
	    }
	}
	((EadventureExpressionForm) form).setNextOp("and");

	sessionMap.put(EadventureConstants.ATTR_EXPR_SELECTED_TYPE, selectedVarType);

	return mapping.findForward(EadventureConstants.SUCCESS);

    }

    /**
     * This method will get necessary information from taskList item form and save or update into
     * <code>HttpSession</code> TaskListItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward saveOrUpdateExpression(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	EadventureExpressionForm expressionForm = (EadventureExpressionForm) form;
	ActionErrors errors = validateEadventureExpression(expressionForm, request);

	if (!errors.isEmpty()) {
	    populateFormWithPossibleVariables(form, request);
	    if (errors.get("no-op").hasNext()) {
		//TODO nuevo metodo, no tiene sentido pasar todo esto!!!!
		List listParams = this.getEadventureParamList(sessionMap);
		String name = expressionForm.getSelectedVarOp1();
		int selectedVarType = prepareOpAndVarFromType(expressionForm, name, listParams);
		expressionForm.setNextOp("and");
	    }

	    this.addErrors(request, errors);
	    return mapping.findForward("addexpression");
	}
	try {
	    extractFormToEadventureExpression(request, expressionForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(EadventureConstants.ERROR_MSG_NO_EXPRESSIONS, e.getMessage()));
	    if (!errors.isEmpty()) {
		populateFormWithPossibleVariables(form, request);
		this.addErrors(request, errors);
		return mapping.findForward("addexpression");
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, expressionForm.getSessionMapID());
	// return null to close this window
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Remove eadventureExpression from HttpSession list and update page display. As authoring rule, all persist only
     * happen
     * when user submit whole page. So this remove is just impact HttpSession values.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeExpression(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int position = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_EXPRESSION_POSITION), -1);
	if (position != -1) {
	    SortedSet<EadventureExpression> expressionList = getEadventureExpressionList(sessionMap);
	    List<EadventureExpression> rList = new ArrayList<EadventureExpression>(expressionList);
	    EadventureExpression expression = rList.remove(position);
	    for (EadventureExpression otherExpression : expressionList) {
		if (otherExpression.getSequenceId() > position) {
		    otherExpression.setSequenceId(otherExpression.getSequenceId() - 1);
		}
	    }
	    // actualize the expressionList in sessionMap
	    expressionList.clear();
	    expressionList.addAll(rList);
	    // add to delList
	    List delList = getDeletedEadventureExpressionList(sessionMap);
	    delList.add(expression);
	    // TODO preguntar si esto es necesario, quizás no interesa si no lo damos a salvar!!!!!!!!!!!!!!!!!!!
	    //check if we need delete this expression from a condition in conditions list
	    /*
	     * boolean isConditionEdit =
	     * Boolean.getBoolean((String)sessionMap.get(EadventureConstants.ATTR_EDIT_CONDITION));
	     * if (isConditionEdit){
	     * // get the condition in condition list
	     * SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	     * List<EadventureCondition> cList = new ArrayList<EadventureCondition>(conditionList);
	     * int positionCondition = Integer.parseInt(((EadventureConditionForm)form).getPosition());
	     * EadventureCondition condition = cList.get(positionCondition);
	     * if (condition != null) {
	     * condition.setEadListExpression(expressionList);
	     * ((EadventureConditionForm)form).setExpressionList(expressionList);
	     * //TODO borrar si esta bien lo anterior
	     * /*Set<EadventureExpression> expressionFromConditionList = condition.getEadListExpression();
	     * List<EadventureExpression> ecList = new ArrayList<EadventureExpression>(expressionFromConditionList);
	     * ecList.remove(expression);
	     * //actualize the expression list in the condition in the conditionsList in the sessionMap
	     * expressionFromConditionList.clear();
	     * expressionFromConditionList.addAll(ecList);
	     * ////TODO quitar, creo que no hace falta actualize the expressionList in conditionForm
	     * 
	     * }
	     * }
	     */
	}

	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Prepare the operator and the second var of the expression taking into account the type of the first var
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward prepareCheckCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	SortedSet<EadventureExpression> expressionList = getEadventureExpressionList(sessionMap);
	List<EadventureExpression> exprList = new ArrayList(expressionList);
	java.util.Iterator<EadventureExpression> it = exprList.iterator();
	// prepare the data structures to send to jsp
	ArrayList<ExpressionInfo> expressionsInfo = new ArrayList<ExpressionInfo>();
	while (it.hasNext()) {
	    EadventureExpression expr = it.next();
	    ExpressionInfo exprInfo = null;
	    // identify the variable
	    String name = expr.getFirstOp().getName();
	    String type = expr.getFirstOp().getType();
	    String operator = expr.getExpresionOp();
	    String nextOp = expr.getNextOp();
	    exprInfo = new ExpressionInfo(name, type, operator, nextOp);
	    if (expr.getValueIntroduced() != null) {
		exprInfo.setValueInExpression(expr.getValueIntroduced());
	    } else {
		exprInfo.setNameSecondOp(expr.getVarIntroduced().getName());
	    }

	    expressionsInfo.add(exprInfo);

	}

	sessionMap.put(EadventureConstants.ATTR_EXPRS_INFO, expressionsInfo);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Prepare the operator and the second var of the expression taking into account the type of the first var
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward checkCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************
    private ActionForward switchCondition(ActionMapping mapping, HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_CONDITION_POSITION),
		-1);
	if (sequenceId != -1) {
	    SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	    List<EadventureCondition> rList = new ArrayList<EadventureCondition>(conditionList);
	    // get current and the target condition, and switch their sequnece
	    EadventureCondition condition = rList.get(sequenceId);
	    EadventureCondition repCondition;
	    if (up) {
		repCondition = rList.get(--sequenceId);
	    } else {
		repCondition = rList.get(++sequenceId);
	    }
	    int upSeqId = repCondition.getSequenceId();
	    repCondition.setSequenceId(condition.getSequenceId());
	    condition.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    conditionList.clear();
	    conditionList.addAll(rList);
	}

	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    private ActionForward switchExpression(ActionMapping mapping, HttpServletRequest request, boolean up,
	    int conditionPosition) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_EXPRESSION_POSITION),
		-1);
	if (sequenceId != -1) {
	    SortedSet<EadventureExpression> expressionList = getEadventureExpressionList(sessionMap);
	    List<EadventureExpression> rList = new ArrayList<EadventureExpression>(expressionList);
	    // get current and the target item, and switch their sequnece
	    EadventureExpression expression = rList.get(sequenceId);
	    EadventureExpression repExpression;
	    if (up) {
		repExpression = rList.get(--sequenceId);
	    } else {
		repExpression = rList.get(++sequenceId);
	    }
	    int upSeqId = repExpression.getSequenceId();
	    repExpression.setSequenceId(expression.getSequenceId());
	    expression.setSequenceId(upSeqId);
	    // put back list, it will be sorted again
	    expressionList.clear();
	    expressionList.addAll(rList);

	    // TODO comprobar si esto es necesario, quizás no interesa si no le doy a salvar!!!!!!!!!!!!!!!!!!!!!!!!!!
	    //check if we need delete this expression from a condition in conditions list
	    /*
	     * boolean isConditionEdit =
	     * Boolean.getBoolean((String)sessionMap.get(EadventureConstants.ATTR_EDIT_CONDITION));
	     * if (isConditionEdit){
	     * // get the condition in condition list
	     * SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	     * List<EadventureCondition> cList = new ArrayList<EadventureCondition>(conditionList);
	     * EadventureCondition condition = cList.get(conditionPosition);
	     * if (condition != null) {
	     * condition.setEadListExpression(expressionList);
	     * //TODO borrar si sobra!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
	     * /*Set<EadventureExpression> expressionFromConditionList = condition.getEadListExpression();
	     * List<EadventureExpression> ecList = new ArrayList<EadventureExpression>(expressionFromConditionList);
	     * ecList.remove(expression);
	     * //actualize the expression list in the condition in the conditionsList in the sessionMap
	     * expressionFromConditionList.clear();
	     * expressionFromConditionList.addAll(ecList);
	     * 
	     * }
	     * }
	     */
	}

	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    // returns the int value to save in sessionMap to know in jsp which type is the selected variable
    private int prepareOpAndVarFromType(ActionForm form, String varName, List<EadventureParam> listParams) {

	int returnType = -1;
	// search the type of the param
	java.util.Iterator<EadventureParam> it = listParams.iterator();
	EadventureParam param = null;
	String type = null;
	while (it.hasNext()) {
	    param = it.next();
	    if (param.getName().equals(varName)) {
		type = param.getType();
	    }
	}

	String[] operators = null;
	if (type.equals("integer")) {
	    // Initialize the operator .
	    returnType = 0;
	    operators = EadventureConstants.IntergerOp;
	    ((EadventureExpressionForm) form).setIntroducedValue("0");
	} else if (type.equals("string")) {
	    returnType = 1;
	    operators = EadventureConstants.StringOp;

	} else if (type.equals("boolean")) {
	    returnType = 2;
	    operators = EadventureConstants.BooleanOp;
	}
	((EadventureExpressionForm) form).setPossibleOperator(operators);

	// prepare the second var taking into account the type of first param
	String[] params = prepareListOfParams(listParams, type, varName);
	((EadventureExpressionForm) form).setPossibleVarsOp2(params);

	return returnType;
    }

    /**
     * Return the LabelValueBean list of all the params filter by type.
     * If type is "none", this method takes all the params.
     *
     * @param list
     * @param type
     * @return
     */
    private String[] prepareListOfParams(List<EadventureParam> list, String type, String name) {
	// Initialise the LabelValueBeans in the possibleOptions array.
	//TODO como para el caso de un tipo específico no sabemos cual va a ser el tamaño del array, mejor usa run arrayListparcial y luego
	// pasarlo a array normal

	ArrayList<String> paramAux = new ArrayList<String>();
	for (EadventureParam param : list) {
	    //if type is none means that we want to add all params
	    if (type.equals("none") || (type.equals(param.getType()) && !name.equals(param.getName()))) {
		paramAux.add(param.getName());
	    }
	}

	return paramAux.toArray(new String[paramAux.size()]);
    }

    private String[] prepareListOfParams(List<EadventureParam> list) {
	return prepareListOfParams(list, "none", "");
    }

    /**
     * Return TaskListService bean.
     */
    /*
     * private ITaskListService getTaskListService() {
     * WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
     * .getServletContext());
     * return (ITaskListService) wac.getBean(TaskListConstants.RESOURCE_SERVICE);
     * }
     * 
     * /**
     * List save current taskList items.
     *
     * @param request
     * 
     * @return
     */
    /*
     * private SortedSet<TaskListCondition> getTaskListConditionList(SessionMap sessionMap) {
     * SortedSet<TaskListCondition> list = (SortedSet<TaskListCondition>) sessionMap
     * .get(TaskListConstants.ATTR_CONDITION_LIST);
     * if (list == null) {
     * list = new TreeSet<TaskListCondition>(new TaskListConditionComparator());
     * sessionMap.put(TaskListConstants.ATTR_CONDITION_LIST, list);
     * }
     * return list;
     * }
     * 
     * 
     * 
     * /**
     * List save deleted taskList items, which could be persisted or non-persisted items.
     *
     * @param request
     * 
     * @return
     */
    private List getDeletedEadventureConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, EadventureConstants.ATTR_DELETED_CONDITION_LIST);
    }

    /**
     * List save deleted taskList items, which could be persisted or non-persisted items.
     * 
     * @param request
     * @return
     */
    private List getDeletedEadventureExpressionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, EadventureConstants.ATTR_DELETED_EXPRESSION_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     *
     * @param request
     * @param name
     * @return
     */
    private List getListFromSession(SessionMap sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * This method will populate eadventure expression vars information to its form for edit use (for first param).
     *
     * @param sequenceId
     * @param condition
     * @param form
     * @param request
     */
    private void populateFormWithPossibleVariables(ActionForm form, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	EadventureExpressionForm eadForm = ((EadventureExpressionForm) form);
	List<EadventureParam> paramsList = getEadventureParamList(sessionMap);

	// Initialise the options in the possibleOptions array.
	String[] params = prepareListOfParams(paramsList);
	eadForm.setPossibleVarsOp1(params);

    }

    /**
     * List save current eadventure params.
     *
     * @param sessionMap
     * @return
     */

    private List getEadventureParamList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, EadventureConstants.ATTR_PARAMS_LIST);
    }

    /**
     * List save current eadventure conditions.
     *
     * @param request
     * @return
     */
    private SortedSet<EadventureCondition> getEadventureConditionList(SessionMap sessionMap) {
	SortedSet<EadventureCondition> list = (SortedSet<EadventureCondition>) sessionMap
		.get(EadventureConstants.ATTR_CONDITION_LIST);
	if (list == null) {
	    list = new TreeSet<EadventureCondition>(new EadventureConditionComparator());
	    sessionMap.put(EadventureConstants.ATTR_CONDITION_LIST, list);
	}
	return list;
    }

    private SortedSet<EadventureExpression> getEadventureExpressionList(SessionMap sessionMap) {
	SortedSet<EadventureExpression> list = (SortedSet<EadventureExpression>) sessionMap
		.get(EadventureConstants.ATTR_EXPRESSION_LIST);
	if (list == null) {
	    list = new TreeSet<EadventureExpression>(new EadventureExpressionComparator());
	    sessionMap.put(EadventureConstants.ATTR_EXPRESSION_LIST, list);
	}
	return list;
    }

    /**
     * Extract form content to eadventureCondition.
     *
     * @param request
     * @param form
     * @throws TaskListException
     */
    private void extractFormToEadventureCondition(HttpServletRequest request, EadventureConditionForm form)
	    throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from request form to a old or new TaskListItem instance. It
	 * gets all info EXCEPT TaskListItem.createDate and TaskListItem.createBy, which need be set when persisting
	 * this taskList item.
	 */

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	int position = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_CONDITION_POSITION), -1);
	EadventureCondition condition = null;

	if (position == -1) { // add
	    condition = new EadventureCondition();
	    int maxSeq = 0;
	    if (conditionList != null && conditionList.size() > 0) {
		EadventureCondition last = conditionList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    condition.setSequenceId(maxSeq);
	    conditionList.add(condition);
	} else { // edit
	    List<EadventureCondition> rList = new ArrayList<EadventureCondition>(conditionList);
	    condition = rList.get(position);
	}

	condition.setName(form.getName());

	SortedSet<EadventureExpression> expressionList = getEadventureExpressionList(sessionMap);
	// set the condition as parent of all expressions
	List<EadventureExpression> eList = new ArrayList<EadventureExpression>(expressionList);
	java.util.Iterator<EadventureExpression> it = eList.iterator();
	while (it.hasNext()) {
	    EadventureExpression eadExpr = it.next();
	    eadExpr.setCondition(condition);
	}
	condition.setEadListExpression(expressionList);
	// re-init expressionList
	sessionMap.put(EadventureConstants.ATTR_EXPRESSION_LIST, null);

    }

    /**
     * Extract form content to eadventureExpression.
     *
     * @param request
     * @param form
     * @throws TaskListException
     */
    private void extractFormToEadventureExpression(HttpServletRequest request, EadventureExpressionForm form)
	    throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from request form to a old or new TaskListItem instance. It
	 * gets all info EXCEPT TaskListItem.createDate and TaskListItem.createBy, which need be set when persisting
	 * this taskList item.
	 */

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<EadventureExpression> expressionList = getEadventureExpressionList(sessionMap);
	//int position = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_EXPRESSION_POSITION), -1);
	int position = NumberUtils.stringToInt(form.getPosition());
	EadventureExpression expression = null;

	if (position == -1) { // add
	    expression = new EadventureExpression();
	    int maxSeq = 0;
	    if (expression != null && expressionList.size() > 0) {
		EadventureExpression last = expressionList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    expression.setSequenceId(maxSeq);
	    expressionList.add(expression);
	} else { // edit
	    List<EadventureExpression> rList = new ArrayList<EadventureExpression>(expressionList);
	    expression = rList.get(position);
	}

	List<EadventureParam> paramList = this.getEadventureParamList(sessionMap);
	expression.setFirstOp(getParam(form.getSelectedVarOp1(), paramList));
	expression.setExpresionOp(form.getSelectedOperator());

	if (form.isSecondVarSelected()) {
	    expression.setVarIntroduced(getParam(form.getSelectedVarOp2(), paramList));
	    expression.setValueIntroduced(null);
	} else {
	    expression.setValueIntroduced(form.getIntroducedValue());
	    expression.setVarIntroduced(null);
	}
	expression.setNextOp(form.getNextOp());

    }

    private EadventureParam getParam(String name, List<EadventureParam> list) {

	java.util.Iterator<EadventureParam> it = list.iterator();
	while (it.hasNext()) {
	    EadventureParam param = it.next();
	    if (param.getName().equals(name)) {
		return param;
	    }
	}
	return null;
    }

    /**
     * Validate EadventureCondition
     *
     * @param conditionForm
     * @return
     */
    private ActionErrors validateEadventureCondition(EadventureConditionForm conditionForm,
	    HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();
	String sessionMapID = conditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	String formConditionName = conditionForm.getName();
	if (StringUtils.isBlank(formConditionName)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(EadventureConstants.ERROR_NAME_CONDITION_BLANK));
	} else if (StringUtils.contains(formConditionName, '#')) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(EadventureConstants.ERROR_MSG_NAME_CONTAINS_WRONG_SYMBOL));
	} else {

	    String formConditionSequenceId = conditionForm.getPosition();
	    SortedSet<EadventureCondition> conditionList = getEadventureConditionList(sessionMap);
	    for (EadventureCondition condition : conditionList) {
		if (formConditionName.equals(condition.getName())
			&& !formConditionSequenceId.equals((new Integer(condition.getSequenceId())).toString())) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(EadventureConstants.ERROR_MSG_NAME_DUPLICATED));
		    break;
		}
	    }
	}

	// should be created at least one expression
	//Set expressionList = conditionForm.getExpressionList();
	Set expressionList = getEadventureExpressionList(sessionMap);
	if (expressionList == null || expressionList.size() == 0) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(EadventureConstants.ERROR_MSG_NO_EXPRESSIONS));
	}

	return errors;
    }

    /**
     * Validate EadventureCondition
     *
     * @param conditionForm
     * @return
     */
    private ActionErrors validateEadventureExpression(EadventureExpressionForm expressionForm,
	    HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();

	/*
	 * String formEaxpressionName = expressionForm.getName();
	 * if (StringUtils.isBlank(formEaxpressionName)) {
	 * errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(EadventureConstants.ERROR_NAME_CONDITION_BLANK));
	 * } else if (StringUtils.contains(formEaxpressionName, '#')) {
	 * errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	 * EadventureConstants.ERROR_MSG_NAME_CONTAINS_WRONG_SYMBOL));
	 * } else {
	 */

//	StringUtils.isNumeric(arg0)
//	StringUtils.
	//TODO revisar, muchas de estas cosas no se permiten en la vista, por lo que no hace falta comprbarlas aqui
	String formExpressionSequenceId = expressionForm.getPosition();

	String sessionMapID = expressionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	String firstVar = expressionForm.getSelectedVarOp1();
	List<EadventureParam> paramList = this.getEadventureParamList(sessionMap);
	String type = null;
	for (EadventureParam param : paramList) {
	    if (param.getName().equals(firstVar)) {
		type = param.getType();
	    }
	}

	if (firstVar.equals("no-option")) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(EadventureConstants.ERROR__EXPRESSION_NO_FIRST_VAR));
	} else {

	    if (expressionForm.getSelectedVarOp2() != null && !expressionForm.getSelectedVarOp2().equals("")) {
		//validate selected var
		String secondVar = expressionForm.getSelectedVarOp2();
		if (firstVar.equals(secondVar)) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(EadventureConstants.ERROR_SAME_VARS_NAME));
		} else {
		    String type2 = null;
		    for (EadventureParam param : paramList) {
			if (param.getName().equals(firstVar)) {
			    type2 = param.getType();
			}
		    }
		    if (type != null) {
			if (!type2.equals(type)) {
			    errors.add(ActionMessages.GLOBAL_MESSAGE,
				    new ActionMessage(EadventureConstants.ERROR_NOT_EQ_VARS_TYPE));
			}
		    }
		}

	    } else if (expressionForm.getIntroducedValue() != null
		    && !expressionForm.getIntroducedValue().endsWith("")) {
		//Validate introduced value

		String introducedValue = expressionForm.getIntroducedValue();
		if (StringUtils.isBlank(introducedValue)) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(EadventureConstants.ERROR_NAME_CONDITION_BLANK));
		} else if (StringUtils.contains(introducedValue, '#')) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(EadventureConstants.ERROR_MSG_NAME_CONTAINS_WRONG_SYMBOL));
		} else {
		    if (type != null) {
			if (type.equals("string")) {
			    if (StringUtils.isBlank(introducedValue)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(EadventureConstants.ERROR_NAME_VALUE_BLANK));
			    } else if (!StringUtils.isAlpha(introducedValue)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(EadventureConstants.ERROR_VALUE_NOT_ALPHA));
			    }

			} else if (type.equals("integer")) {
			    if (StringUtils.isBlank(introducedValue)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(EadventureConstants.ERROR_NAME_VALUE_BLANK));
			    } else if (!StringUtils.isNumeric(introducedValue)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(EadventureConstants.ERROR_VALUE_NOT_NUMERIC));
			    }
			} else if (type.equals("boolean")) {
			    if (StringUtils.isBlank(introducedValue)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(EadventureConstants.ERROR_NAME_VALUE_BLANK));
			    } else if (!introducedValue.equalsIgnoreCase("true")
				    || !introducedValue.equalsIgnoreCase("false")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(EadventureConstants.ERROR_VALUE_NOT_BOOLEAN));

			    }
			}
		    } else {
			//TODO caso en que el type no es ninguno de los anteriores
		    }
		}
	    }

	    if (expressionForm.getSelectedOperator() != null
		    && expressionForm.getSelectedOperator().equals("no-option")) {
		errors.add("no-op", new ActionMessage(EadventureConstants.ERROR__EXPRESSION_NO_OPERATOR));
	    } else if (expressionForm.isSecondVarSelected() && expressionForm.getSelectedVarOp2().equals("no-option")) {
		errors.add("no-op", new ActionMessage(EadventureConstants.ERROR__EXPRESSION_NO_SECOND_VAR));
	    }
	}

	return errors;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     *
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

    /*
     * private ActionMessages validate(TaskListForm taskListForm, ActionMapping mapping, HttpServletRequest request) {
     * ActionMessages errors = new ActionMessages();
     * // if (StringUtils.isBlank(taskListForm.getTaskList().getTitle())) {
     * // ActionMessage error = new ActionMessage("error.resource.item.title.blank");
     * // errors.add(ActionMessages.GLOBAL_MESSAGE, error);
     * // }
     * 
     * // define it later mode(TEACHER) skip below validation.
     * String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
     * if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
     * return errors;
     * }
     * 
     * // Some other validation outside basic Tab.
     * 
     * return errors;
     * }
     */

}
