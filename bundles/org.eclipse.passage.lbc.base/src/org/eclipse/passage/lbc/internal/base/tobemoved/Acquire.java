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
package org.eclipse.passage.lbc.internal.base.tobemoved;

import org.eclipse.passage.lbc.internal.api.tobemoved.FloatingResponse;
import org.eclipse.passage.lbc.internal.api.tobemoved.RawRequest;
import org.eclipse.passage.lbc.internal.base.tobemoved.acquire.Acquisition;
import org.eclipse.passage.lic.internal.api.LicensingException;

final class Acquire extends ChoreDraft {

	Acquire(RawRequest data) {
		super(data);
	}

	@Override
	protected FloatingResponse withProductUser(ProductUserRequest request) throws LicensingException {
		FeatureRequest feature = new FeatureRequest(request);
		if (!feature.feature().isPresent()) {
			return new Failure.BadRequestNoFeature();
		}
		return new Acquisition(feature).get();
	}

}
