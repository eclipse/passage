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
package org.eclipse.passage.lic.api.tests.fakes;

import java.io.InputStream;

import org.eclipse.passage.lic.internal.api.LicensedProduct;
import org.eclipse.passage.lic.internal.api.LicensingException;
import org.eclipse.passage.lic.internal.api.io.KeyKeeper;

@SuppressWarnings("restriction")
public final class FakeKeyKeeper implements KeyKeeper, LicensedProduct {

	@Override
	public LicensedProduct id() {
		return this;
	}

	@Override
	public String identifier() {
		return "fake-product-keeping-keys"; //$NON-NLS-1$
	}

	@Override
	public String version() {
		return "0.0.1"; //$NON-NLS-1$
	}

	@Override
	public InputStream productPublicKey() throws LicensingException {
		throw new UnsupportedOperationException();
	}

}
