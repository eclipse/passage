/*******************************************************************************
 * Copyright (c) 2020, 2021 ArSysOp
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
package org.eclipse.passage.lic.api.registry;

import java.util.Collection;

/**
 * 
 * @param <S> sub type of {@linkplain Service}
 * @since 1.1
 */
public interface Registry<I extends ServiceId, S extends Service<I>> {

	boolean hasService(I id);

	S service(I id);

	Collection<S> services();

}