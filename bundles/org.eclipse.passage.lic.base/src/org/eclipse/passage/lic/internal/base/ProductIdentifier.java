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
package org.eclipse.passage.lic.internal.base;

import java.util.Map;
import java.util.function.Function;

public final class ProductIdentifier extends StringNamedData {

	public ProductIdentifier(String value) {
		super(value);
	}

	public ProductIdentifier(Map<String, Object> values) {
		super(values);
	}

	public ProductIdentifier(Function<String, String> retriever) {
		super(retriever);
	}

	@Override
	public String key() {
		return "licensing.product.identifier"; //$NON-NLS-1$
	}

}