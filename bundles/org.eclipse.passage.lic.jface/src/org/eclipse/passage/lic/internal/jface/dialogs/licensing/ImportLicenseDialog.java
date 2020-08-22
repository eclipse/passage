/*******************************************************************************
 * Copyright (c) 2020 ArSysOp
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package org.eclipse.passage.lic.internal.jface.dialogs.licensing;

import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.passage.lic.internal.api.ServiceInvocationResult;
import org.eclipse.passage.lic.internal.api.conditions.Condition;
import org.eclipse.passage.lic.internal.api.conditions.ConditionPack;
import org.eclipse.passage.lic.internal.api.conditions.ValidityPeriod;
import org.eclipse.passage.lic.internal.api.conditions.mining.LicenseReadingService;
import org.eclipse.passage.lic.internal.api.diagnostic.Diagnostic;
import org.eclipse.passage.lic.internal.base.conditions.BaseValidityPeriodClosed;
import org.eclipse.passage.lic.internal.base.diagnostic.DiagnosticExplained;
import org.eclipse.passage.lic.internal.equinox.EquinoxPassageLicensingToolBox;
import org.eclipse.passage.lic.internal.jface.i18n.ImportLicenseDialogMessages;
import org.eclipse.passage.lic.jface.resource.LicensingImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public final class ImportLicenseDialog extends NotificationDialog {

	private final DateTimeFormatter dates = DateTimeFormatter.ofPattern("dd-MM-yyyy"); //$NON-NLS-1$

	public ImportLicenseDialog(Shell shell) {
		super(shell);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Import License"); //$NON-NLS-1$
		shell.setImage(LicensingImages.getImage(LicensingImages.IMG_IMPORT));
		shell.setSize(850, 300);
	}

	@Override
	protected void buildUI(Composite parent) {
		buildSelector(parent);
		buildViewer(parent);
	}

	@Override
	protected String defaultMessage() {
		return ImportLicenseDialogMessages.ImportLicenseDialog_prelude;
	}

	private void buildSelector(Composite parent) {
		Composite composite = row(parent, 3);
		new Label(composite, SWT.NONE).setText(ImportLicenseDialogMessages.ImportLicenseDialog_path_label);
		Text path = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		Button browse = new Button(composite, SWT.PUSH);
		browse.setText("B&rowse..."); //$NON-NLS-1$
		browse.addListener(SWT.Selection, e -> browseAndLoad(path));
		setButtonLayoutData(browse);
	}

	private Composite row(Composite parent, int columns) {
		Composite row = new Composite(parent, SWT.NONE);
		row.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		row.setLayout(new GridLayout(columns, false));
		return row;
	}

	private void buildViewer(Composite parent) {
		viewer = new HereTable<Condition>(parent, Condition.class) //
				.withColumn(ImportLicenseDialogMessages.ImportLicenseDialog_column_feature, 300, this::feature) //
				.withColumn(ImportLicenseDialogMessages.ImportLicenseDialog_column_period, 300, this::period) //
				.withColumn(ImportLicenseDialogMessages.ImportLicenseDialog_column_evaluation, 220, this::evaluation) //
				.viewer();
	}

	private String feature(Condition condition) {
		return String.format("%s version %s (%s)", //$NON-NLS-1$
				condition.feature(), //
				condition.versionMatch().version(), //
				condition.versionMatch().rule().identifier());
	}

	private String period(Condition condition) {
		ValidityPeriod period = condition.validityPeriod();
		if (!BaseValidityPeriodClosed.class.isInstance(period)) { // to be eliminated #566015
			return "unknown"; //$NON-NLS-1$
		}
		BaseValidityPeriodClosed closed = (BaseValidityPeriodClosed) condition.validityPeriod();
		return String.format("%s - %s", dates.format(closed.from()), dates.format(closed.to())); //$NON-NLS-1$
	}

	private String evaluation(Condition condition) {
		return String.format("%s (%s)", //$NON-NLS-1$
				condition.evaluationInstructions().expression(), //
				condition.evaluationInstructions().type().identifier());
	}

	private void browseAndLoad(Text path) {
		browse(path).ifPresent(this::loadLicense);
	}

	private Optional<String> browse(Text path) {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN | SWT.SHEET);
		dialog.setText(ImportLicenseDialogMessages.ImportLicenseDialog_browse_dialog_title);
		dialog.setFilterPath(path.getText().trim());
		dialog.setFilterExtensions(new String[] { "*.licen", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
		Optional<String> file = Optional.ofNullable(dialog.open());
		file.ifPresent(path::setText);
		return file;
	}

	private void loadLicense(String file) {
		ServiceInvocationResult<LicenseReadingService> reader = new EquinoxPassageLicensingToolBox()
				.licenseReadingService();
		if (!reader.data().isPresent()) {
			reportError(reader.diagnostic());
			return;
		}
		ServiceInvocationResult<Collection<ConditionPack>> packs = reader.data().get().read(Paths.get(file));
		if (!packs.data().isPresent()) {
			reportError(packs.diagnostic());
			return;
		}
		List<Condition> conditions = packs.data().get().stream()//
				.flatMap(pack -> pack.conditions().stream())//
				.collect(Collectors.toList());
		System.out.println(conditions.size());
		viewer.setInput(conditions);
	}

	private void reportError(Diagnostic diagnostic) {
		setErrorMessage(new DiagnosticExplained(diagnostic).get());
	}

	@Override
	protected void initButtons() {
		// do nothing
	}

	@Override
	protected void inplaceData() {
		// do nothing
	}

	@Override
	protected void updateButtonsEnablement() {
		// do nothing
	}

}