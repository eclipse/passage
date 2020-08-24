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
package org.eclipse.passage.lbc.internal.api;

import java.util.function.Supplier;

import org.eclipse.passage.lic.internal.api.ServiceInvocationResult;

/**
 * @since 1.0
 */
public interface BackendLicenseLock {

	// Returns simply a boolean value of license.taken < license.capacity
	ServiceInvocationResult<Boolean> canTake(Supplier<String> condition);

	// Decreases condition.taken
	ServiceInvocationResult<Boolean> release(Supplier<String> condition);

	// Increases condition.taken
	ServiceInvocationResult<Boolean> take(Supplier<String> condition);

}
