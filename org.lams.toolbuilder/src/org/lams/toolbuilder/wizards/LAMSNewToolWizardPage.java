package org.lams.toolbuilder.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.lams.toolbuilder.util.LamsToolBuilderLog;
import org.lams.toolbuilder.util.Constants;
/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class LAMSNewToolWizardPage extends WizardNewProjectCreationPage {
	private Text toolSignature;
	private Text vendor;
	private Text compatibility; 
	private Text toolDisplayName;
	private Text toolVersion;
	private Button LAMSButton;
	private Button RAMSButton;
	private Button notVisible;
	private Button isVisible;
	private boolean canContinue= false;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 */
	public LAMSNewToolWizardPage(ISelection selection) {
		super("wizardPage");
		this.canContinue = false;
		setTitle("LAMS Tool Project Wizard: Project Details");
		setDescription("Enter in details to produce a new LAMS tool project.");
		this.selection = selection;
	}

	public boolean canFlipToNextPage()
	{
		return canContinue;
	}
	
	
	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		LamsToolBuilderLog.logInfo("Drawing LAMS Tool Wizard");
		super.createControl(parent);
		Composite control = (Composite)getControl();
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 10;
		control.setLayout(layout);

		// create a group
		Group namesGroup = new Group(control, SWT.NONE);
		namesGroup.setText("Tool Project Meta-Data");
		GridLayout namesLayout = new GridLayout();
		namesLayout.numColumns = 2;
		namesGroup.setLayout(namesLayout);
		namesGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createLabel(namesGroup,	"Tool Signature");
		toolSignature = createText(namesGroup);
		toolSignature.setToolTipText(WizardConstants.TOOL_SIG_TOOL_TIP);
		toolSignature.addModifyListener(new wizardModifyListener());

		
		// put the key field in the group
		createLabel(namesGroup,	"Tool Name");
		toolDisplayName = createText(namesGroup);
		toolDisplayName.setToolTipText(WizardConstants.TOOL_NAME_TOOL_TIP);
		toolDisplayName.addModifyListener(new wizardModifyListener());		
		

		// put the package field in the group
		createLabel(namesGroup,	"Vendor");
		vendor = createText(namesGroup);
		vendor.setToolTipText(WizardConstants.TOOL_VENDOR_TOOL_TIP);
		vendor.addModifyListener(new wizardModifyListener());
		
		createLabel(namesGroup, "Tool Version");
		toolVersion = createText(namesGroup);
		toolVersion.setText(WizardConstants.DEFAULT_TOOL_VERSION);
		toolVersion.setToolTipText(WizardConstants.TOOL_VERSION_TOOL_TIP);
		toolVersion.addModifyListener(new wizardModifyListener());
		
		createLabel(namesGroup,	"Minimum LAMS Version");
		compatibility = createText(namesGroup);
		compatibility.setText(WizardConstants.DEFAULT_SERVER_VERSION);
		compatibility.setToolTipText(WizardConstants.SERVER_VERSION_TOOL_TIP);
		compatibility.addModifyListener(new wizardModifyListener());
		
		// create a group for columns
		Group organisingGroup = new Group(control, SWT.NONE);
		organisingGroup.setText("Tool Options");
		GridLayout organisingLayout = new GridLayout();
		organisingLayout.numColumns = 3;
		organisingGroup.setLayout(organisingLayout);
		organisingGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		// create a group
		Group typesGroup = new Group(organisingGroup, SWT.NONE);
		typesGroup.setText("Tool Type");
		GridLayout typesLayout = new GridLayout();
		typesLayout.numColumns = 1;
		typesGroup.setLayout(typesLayout);
		typesGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		int buttonSpan = 50;
		int buttonIndent = 10;

		GridData gdButton = new GridData();
		gdButton.horizontalSpan = buttonSpan;
		gdButton.horizontalIndent = buttonIndent;

		LAMSButton = createButton(typesGroup, SWT.RADIO, buttonSpan, buttonIndent);
		LAMSButton.setText("LAMS");
		LAMSButton.setSelection(true);
		RAMSButton = createButton(typesGroup, SWT.RADIO, buttonSpan, buttonIndent);
		RAMSButton.setText("RAMS");
		
		// create a group
		Group toolVisibleGroup = new Group(organisingGroup, SWT.NONE);
		toolVisibleGroup.setText("Make Tool Visible");
		GridLayout toolVisibleLayout = new GridLayout();
		toolVisibleLayout.numColumns = 1;
		toolVisibleGroup.setLayout(toolVisibleLayout);
		toolVisibleGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		GridData gdButton2 = new GridData();
		gdButton2.horizontalSpan = buttonSpan;
		gdButton2.horizontalIndent = buttonIndent;
		
		isVisible = createButton(toolVisibleGroup, SWT.RADIO, buttonSpan, buttonIndent);
		isVisible.setText("Yes");
		isVisible.setSelection(true);
		notVisible = createButton(toolVisibleGroup, SWT.RADIO, buttonSpan, buttonIndent);
		notVisible.setText("No");
		initialise();
		dialogChanged();
		setControl(control);
		
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialise() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) 
		{
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				compatibility.setText(container.getFullPath().toString());
			}
		}
		
		compatibility.setText(WizardConstants.DEFAULT_SERVER_VERSION);
		
		//toolDisplayName.setText(WizardConstants.SAMPLE_TOOL_SIGNATURE);
		//vendor.setText(WizardConstants.SAMPLE_VENDOR);
	}



	

	
	
	private Button createButton(Composite container, int style, int span, int indent) {
		Button button = new Button(container, style);
		GridData gd = new GridData();
		gd.horizontalSpan = span;
		gd.horizontalIndent = indent;
		button.setLayoutData(gd);
		return button;
	}

	private Label createLabel(Composite container, String text) {
		Label label = new Label(container, SWT.NONE);
		label.setText(text);
		GridData gd = new GridData();
		gd.horizontalIndent = 30;
		label.setLayoutData(gd);
		return label;
	}

	private Text createText(Composite container) {
		Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		text.setLayoutData(gd);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
			}
		});
		return text;
	}

	

	
	
	public class wizardModifyListener implements ModifyListener
	{
		public void modifyText(ModifyEvent event)
		{
			dialogChanged();
		}
	}
	
	/**
	 * Ensures that all required fields are set.
	 */
	private void dialogChanged() {
		super.setPageComplete(false);
		this.canContinue = false;
		
		
		if(super.getProjectName().trim().length()==0)
		{
			// allow super class to handle this case
			return;
		}
		if (getToolSignature().trim().length()==0)
		{
			updateStatus("Tool signature must be specified.");
			return;
		}
		else
		{
			 if (!matchRegex(WizardConstants.TOOL_SIG_REGEX, getToolSignature()))
			 {
				 setErrorMessage(WizardConstants.TOOL_SIG_ERROR);
				 setPageComplete(false);	
				 return;
			 }
			 else
			 {
				 // check the tool signature does not clash with an existing
				 for (String sig : Constants.getToolSignatures())
				 {
					 if (getToolSignature().trim().equals(sig))
					 {
						 setErrorMessage(WizardConstants.TOOL_SIG_EXISTS_ERROR);
						 setPageComplete(false);
						 return;
					 }
					 
				 }
			 }
		}
		
		if (getToolDisplayName().trim().length() == 0) {
			updateStatus("Tool name must be specified.\n\n" + WizardConstants.TOOL_NAME_TOOL_TIP);
			return;
		}
		else
		{
			if (!matchRegex(WizardConstants.TOOL_NAME_REGEX, getToolDisplayName()))
			{
				setErrorMessage(WizardConstants.TOOL_NAME_ERROR);
				setPageComplete(false);
				return;
			}
		}
		
		if (getCompatibility().trim().length() == 0) {
			updateStatus("Minimum LAMS Server version must be specified");
			return;
		}
		else
		{
			if (!matchRegex(WizardConstants.SERVER_VERSION_REGEX, getCompatibility()))
			{
				setErrorMessage(WizardConstants.SERVER_VERSION_ERROR);
				setPageComplete(false);
				return;
			}
		}
		
		if (getVendor().trim().length() == 0) 
		{
			updateStatus("Vendor details must be specified");
			return;
		}
		else
		{
			if (!matchRegex(WizardConstants.VENDOR_REGEX, getVendor()))
			{
				setErrorMessage(WizardConstants.VENDOR_ERROR);
				setPageComplete(false);
				return;
			}
		}
		
		if (getToolVersion().trim().length() == 0)
		{
			updateStatus("Tool version must be specified");
			return;
		}
		else
		{
			if (!matchRegex(WizardConstants.TOOL_VERSION_REGEX, getToolVersion()))
			{
				setErrorMessage(WizardConstants.TOOL_VERSION_ERROR);
				setPageComplete(false);				
				return;
			}
		}
		
		updateStatus(null);
	}
	
	/**
	 * 
	 * @param regex The regex to test the text	
	 * @param text The text to be tested
	 * @return true if the regex matches the string
	 */
	private boolean matchRegex(String regex, String text)
	{
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text.trim());
		if (!m.matches())
		{
			return false;
		}
		else
		{
			return true;
		}		
	}
	
	private void updateStatus(String message) {
		
		
		if (message==null)
		{
			this.setMessage(WizardConstants.PAGE_COMPLETE_MESSAGE);
			setPageComplete(true);
		}
		else
		{
			this.setMessage(message);
			setPageComplete(false);
		}
	}
	
	// GET METHODS
	public String getToolSignature() {return toolSignature.getText().trim().toLowerCase();}
	public String getVendor() {return vendor.getText().trim();}
	public String getToolDisplayName() {return toolDisplayName.getText().trim();}
	public String getCompatibility() {return compatibility.getText().trim();}
	public boolean getIsLams() {return LAMSButton.getSelection();}
	public boolean getVisible() {return isVisible.getSelection();}
	public String getToolVersion() {return toolVersion.getText().trim();}
}