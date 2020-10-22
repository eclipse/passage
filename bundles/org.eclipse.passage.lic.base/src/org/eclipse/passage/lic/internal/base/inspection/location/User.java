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

import org.eclipse.passage.lic.internal.base.inspection.BaseEnvironmentProperty;

public abstract class User extends BaseEnvironmentProperty {

	protected User(String name) {
		super("user", name); //$NON-NLS-1$
	}

	public static final class Timezone extends User {

		public Timezone() {
			super("timezone"); //$NON-NLS-1$
		}

	}

	public static final class Language extends User {

		public Language() {
			super("language"); //$NON-NLS-1$
		}

	}

	public static final class Country extends User {

		public Country() {
			super("country"); //$NON-NLS-1$
		}

	}

}
