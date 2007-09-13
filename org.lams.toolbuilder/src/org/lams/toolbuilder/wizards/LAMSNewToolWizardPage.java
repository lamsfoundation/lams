package org.lams.toolbuilder.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.lams.toolbuilder.util.LamsToolBuilderLog;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;


/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class LAMSNewToolWizardPage extends WizardNewProjectCreationPage {
	private Text toolName;
	private Text toolSignature;
	private Text vendor;
	private Text compatibility;
	private Text toolDisplayName;
	private Button LAMSButton;
	private Button RAMSButton;
	private Button notVisible;
	private Button isVisible;
	

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public LAMSNewToolWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("LAMS Tool Project Wizard");
		setDescription("Enter in details to produce a new LAMS tool project.");
		this.selection = selection;
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

		
		// put the key field in the group
		createLabel(namesGroup,	"Tool Display Name");
		toolDisplayName = createText(namesGroup);
		//toolDisplayName.setText(WizardConstants.SAMPLE_TOOL_SIGNATURE);		
		

		// put the package field in the group
		createLabel(namesGroup,	"Vendor");
		vendor = createText(namesGroup);
		//vendor.setText(WizardConstants.SAMPLE_VENDOR);
		
		createLabel(namesGroup,	"Minimum Server Version");
		compatibility = createText(namesGroup);
		compatibility.setText("2.0");

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
		
		
		/*
		for (int i=0; i<implRadios.length; i++) {
			implRadios[i] = createButton(implsGroup, SWT.RADIO, buttonSpan, buttonIndent);
			implRadios[i].setText(validImplTags[i]);
			if (i == 0) {
				implRadios[i].setSelection(true);
			}
			implRadios[i].addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						checkTesting();
					}
				}
			);
		}

		// create a group
		Group optionsGroup = new Group(organisingGroup, SWT.NONE);
		optionsGroup.setText(Messages.getString("SakaiNewWizardPage.options")); //$NON-NLS-1$
		GridLayout optionsLayout = new GridLayout();
		optionsLayout.numColumns = 1;
		optionsGroup.setLayout(implsLayout);
		optionsGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		checkTesting = new Button(optionsGroup, SWT.CHECK);
		checkTesting.setLayoutData(gdButton);
		checkTesting.setText(Messages.getString("SakaiNewWizardPage.testing")); //$NON-NLS-1$
		checkTesting.setToolTipText(Messages.getString("SakaiNewWizardPage.testing.tooltip")); //$NON-NLS-1$
		checkTesting.setSelection(true);
		*/
		
		initialise();
		setControl(control);
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		Composite container = new Composite(parent, SWT.NULL);
		
		
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		layout.makeColumnsEqualWidth = true;
		
		Label label = new Label(container, SWT.NULL);
		label.setText("&Tool Name:");
		toolName = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		toolName.setLayoutData(gd);
		toolName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(container, SWT.NULL);
		label.setText("&Vendor:");
		vendor = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		vendor.setLayoutData(gd);
		vendor.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(container, SWT.NULL);
		label.setText("&Tool Signature:");
		compatibility = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		compatibility.setLayoutData(gd);
		compatibility.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		
		
		
		label = new Label(container, SWT.NULL);
		label.setText("&File name:");
		
		toolDisplayName = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		toolDisplayName.setLayoutData(gd);
		toolDisplayName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		
		label = new Label(container, SWT.NULL);
		label.setText("&Project Type:");
		
		LAMSButton = new Button(container, SWT.RADIO);
		LAMSButton.setText("LAMS");
		/*button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});*/
		
		/*
		RAMSButton = new Button(container, SWT.RADIO);
		RAMSButton.setText("RAMS");
		/*button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});*/
		
		
		/*Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});*/
		
		
		
		//toolDisplayName();
		//dialogChanged();
		//setControl(container);
		
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
		
		compatibility.setText("2.0");
		//toolName.setText(WizardConstants.SAMPLE_TOOL_NAME);
		//toolDisplayName.setText(WizardConstants.SAMPLE_TOOL_SIGNATURE);
		//vendor.setText(WizardConstants.SAMPLE_VENDOR);
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	/*private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}*/

	/**
	 * Ensures that all required fields are set.
	 */
	private void dialogChanged() {
		//IResource container = ResourcesPlugin.getWorkspace().getRoot()
		//		.findMember(new Path(getContainerName()));
		

		if (getToolName().length() == 0) {
			updateStatus("Tool Name must be specified");
			return;
		}
		if (getToolDisplayName().length() == 0) {
			updateStatus("Tool Signature must be specified");
			return;
		}
		if (getCompatibility().length() == 0) {
			updateStatus("Content Compatibility must be specified");
			return;
		}
		
		
		/*if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		*/
		
		/*
		 * String fileName = getFileName();
		 * if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("mpe") == false) {
				updateStatus("File extension must be \"mpe\"");
				return;
			}
		}*/
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
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

	
	public String getToolSignature() {return toolSignature.getText().trim().toLowerCase();}
	public String getToolName() {return toolName.getText().trim();}
	public String getVendor() {return vendor.getText().trim();}
	public String getToolDisplayName() {return toolDisplayName.getText().trim();}
	public String getCompatibility() {return compatibility.getText().trim();}
	public boolean getIsLams() {return LAMSButton.getSelection();}
	public boolean getVisible() {return isVisible.getSelection();}

}