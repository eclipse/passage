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
package org.eclipse.passage.seal.internal.demo;

import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.passage.lic.internal.api.Framework;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings("restriction")
@Component
public final class DemoFrameworkSupplier implements Supplier<Optional<Framework>> {

	@Override
	public Optional<Framework> get() {
		return Optional.of(DemoFramework.demo);
	}

}