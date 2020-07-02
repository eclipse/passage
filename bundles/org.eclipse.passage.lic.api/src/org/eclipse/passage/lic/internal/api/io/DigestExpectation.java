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
package org.eclipse.passage.lic.internal.api.io;

public interface DigestExpectation {

	/**
	 * @return is there is any expectation at all
	 */
	boolean expected();

	/**
	 * <p>
	 * In the case we actually expect input digest to be verified, here actual
	 * digest expectation is supplied.
	 * </p>
	 * <p>
	 * Is not designed to be called if there are no expectations at all. Thus, is
	 * not designed to ever return {@code null}.
	 * </p>
	 */
	byte[] value();

	public static final class None implements DigestExpectation {

		@Override
		public boolean expected() {
			return false;
		}

		@Override
		public byte[] value() {
			throw new UnsupportedOperationException();
		}

	}
}
