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
package org.eclipse.passage.lic.internal.base.inspection.location;

import org.eclipse.passage.lic.internal.api.LicensingException;
import org.eclipse.passage.lic.internal.api.conditions.EvaluationType;
import org.eclipse.passage.lic.internal.api.inspection.EnvironmentProperty;
import org.eclipse.passage.lic.internal.api.inspection.RuntimeEnvironment;

public final class LocationEnvironment implements RuntimeEnvironment {

	private final EvaluationType location = new EvaluationType.Of("location"); //$NON-NLS-1$

	@Override
	public EvaluationType id() {
		return location;
	}

	@Override
	public String state() throws LicensingException {
		StringBuilder out = new StringBuilder();
		System.getProperties().forEach((k, v) -> out//
				.append(k)//
				.append(" = ") //$NON-NLS-1$
				.append(v)//
				.append("\r\n")); //$NON-NLS-1$
		return out.toString();
	}

	@Override
	public boolean isAssuptionTrue(EnvironmentProperty property, String expected) throws LicensingException {
		String regexp = expected.toLowerCase().replaceAll("\\*", ".*"); //$NON-NLS-1$//$NON-NLS-2$
		// FIXME implement
//				user.timezone=Europe/Moscow
//				user.language=ru
//				user.country=RU
//				osgi.nl=ru_RU
//				osgi.nl.user =ru_RU
		return false;
	}

}
