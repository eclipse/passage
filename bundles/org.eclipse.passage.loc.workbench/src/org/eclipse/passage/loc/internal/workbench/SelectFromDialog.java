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
package org.eclipse.passage.loc.internal.workbench;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.passage.loc.internal.workbench.i18n.WorkbenchMessages;
import org.eclipse.passage.loc.jface.dialogs.Appearance;
import org.eclipse.passage.loc.jface.dialogs.FilteredSelectionDialog;
import org.eclipse.passage.loc.jface.dialogs.LabelSearchFilter;
import org.eclipse.swt.widgets.Shell;

/**
 * Selects the classifier from the given input with
 * {@link FilteredSelectionDialog}
 * 
 * @since 0.6
 *
 * @param <C> classifier to be selected see {@link EClass#getName()}
 */
public final class SelectFromDialog<C> implements Function<Iterable<C>, Optional<C>> {

	private final Supplier<Shell> shellSupplier;
	private final Appearance appearance;
	private final Collection<C> initial;

	/**
	 * 
	 * @param shell      the supplier of {@link Shell} to use for
	 *                   {@link FilteredSelectionDialog}, must not be
	 *                   <code>null</code>
	 * @param appearance the title, image and {@link LabelProvider} to use for
	 *                   {@link FilteredSelectionDialog}, must not be
	 *                   <code>null</code>
	 */
	public SelectFromDialog(Supplier<Shell> supplier, Appearance appearance) {
		this(supplier, appearance, Collections.emptyList());
	}

	/**
	 * 
	 * @param shell      the supplier of {@link Shell} to use for
	 *                   {@link FilteredSelectionDialog}, must not be
	 *                   <code>null</code>
	 * @param appearance the title, image and {@link LabelProvider} to use for
	 *                   {@link FilteredSelectionDialog}, must not be
	 *                   <code>null</code>
	 * @param initial    the collection of objects to be an initial selection for
	 *                   {@link FilteredSelectionDialog}, must not be
	 *                   <code>null</code>
	 */
	public SelectFromDialog(Supplier<Shell> supplier, Appearance appearance, Collection<C> initial) {
		Objects.requireNonNull(supplier, WorkbenchMessages.SelectFromDialog_e_null_shell);
		Objects.requireNonNull(appearance, WorkbenchMessages.SelectFromDialog_e_null_appearance);
		Objects.requireNonNull(initial, WorkbenchMessages.SelectFromDialog_e_null_initial);
		this.shellSupplier = supplier;
		this.appearance = appearance;
		this.initial = new ArrayList<C>(initial);
	}

	/**
	 * Returns the selected object or {@link Optional#empty()}
	 */
	@Override
	public Optional<C> apply(Iterable<C> input) {
		FilteredSelectionDialog<C> dialog = new FilteredSelectionDialog<C>(shellSupplier.get(), false,
				new LabelSearchFilter());
		dialog.setTitle(appearance.title());
		dialog.setImage(appearance.image().get());
		dialog.setLabelProvider(appearance.labelProvider());
		dialog.setInitialSelection(initial);
		dialog.setInput(input);
		if (dialog.open() == Dialog.OK) {
			return dialog.getFirstResult();
		}
		return Optional.empty();
	}

}
