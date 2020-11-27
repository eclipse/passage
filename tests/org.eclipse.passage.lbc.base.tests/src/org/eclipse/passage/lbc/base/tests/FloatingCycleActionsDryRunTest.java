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
package org.eclipse.passage.lbc.base.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.passage.lbc.internal.api.FloatingResponse;
import org.eclipse.passage.lbc.internal.api.RawRequest;
import org.eclipse.passage.lbc.internal.base.BaseFlotingRequestHandled;
import org.eclipse.passage.lbc.internal.base.EagerFloatingState;
import org.eclipse.passage.lbc.internal.base.Failure;
import org.eclipse.passage.lic.floating.model.api.GrantAcqisition;
import org.eclipse.passage.lic.floating.model.meta.FloatingFactory;
import org.eclipse.passage.lic.floating.model.meta.FloatingPackage;
import org.eclipse.passage.lic.internal.api.LicensedProduct;
import org.eclipse.passage.lic.internal.api.LicensingException;
import org.eclipse.passage.lic.internal.api.conditions.ConditionAction;
import org.eclipse.passage.lic.internal.base.BaseLicensedProduct;
import org.eclipse.passage.lic.internal.base.FeatureIdentifier;
import org.eclipse.passage.lic.internal.base.ProductIdentifier;
import org.eclipse.passage.lic.internal.base.ProductVersion;
import org.eclipse.passage.lic.internal.emf.EObjectToBytes;
import org.eclipse.passage.lic.internal.net.LicenseUser;
import org.junit.Test;

public final class FloatingCycleActionsDryRunTest {

	private final LicensedProduct product = new BaseLicensedProduct("proj", "1.2.3"); //$NON-NLS-1$ //$NON-NLS-2$
	private final String feature = "erutaef"; //$NON-NLS-1$
	private final String user = "resu"; //$NON-NLS-1$

	@Test
	public void mineNothing() throws LicensingException, IOException {
		FloatingResponse response = new BaseFlotingRequestHandled(request(new ConditionAction.Mine())).get();
		assertFalse(response.failed());
		assertTrue(new License(response).get().getLicenseGrants().isEmpty());
	}

	@Test
	public void acquireNothing() throws LicensingException, IOException {
		FloatingResponse response = new BaseFlotingRequestHandled(request(new ConditionAction.Acquire())).get();
		assertTrue(response.failed());
		assertEquals(new Failure.NoGrantsAvailable(product, feature).error().code(), response.error().code());
	}

	@Test
	public void releaseInVain() throws LicensingException, IOException {
		GrantAcqisition acqisition = acquisition();
		FloatingResponse response = new BaseFlotingRequestHandled(//
				request(new ConditionAction.Release(), Optional.of(acqisition))).get();
		assertTrue(response.failed());
		assertEquals(new Failure.NotReleased(product, acqisition).error().code(), response.error().code());
	}

	private RawRequest request(ConditionAction action) throws LicensingException {
		return request(action, Optional.empty());
	}

	private RawRequest request(ConditionAction action, Optional<EObject> payload) throws LicensingException {
		RequestConstructed construct = new RequestConstructed()//
				.withAction(action)//
				.withParameters(Arrays.asList(//
						new ProductIdentifier(product), //
						new ProductVersion(product), //
						new LicenseUser(user), new FeatureIdentifier(feature)))
				.withState(new EagerFloatingState());//
		if (payload.isPresent()) {
			construct.withContent(raw(payload.get()));
		}
		return construct.get();
	}

	private byte[] raw(EObject obj) throws LicensingException {
		return new EObjectToBytes(obj)//
				.get(Collections.singletonMap(FloatingPackage.eNS_URI, FloatingPackage.eINSTANCE));
	}

	private GrantAcqisition acquisition() {
		GrantAcqisition acqisition = FloatingFactory.eINSTANCE.createGrantAcqisition();
		acqisition.setIdentifier("fake-acquisition-id"); //$NON-NLS-1$
		acqisition.setFeature(feature);
		acqisition.setGrant("fake-grant-id"); //$NON-NLS-1$
		acqisition.setCreated(new Date());
		acqisition.setUser(user);
		return acqisition;
	}

}