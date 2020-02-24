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
package org.eclipse.passage.loc.internal.workbench;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.Test;

public class ClassifierMetadataTest {

	private final EClass type = EcoreFactory.eINSTANCE.createEClass();
	private final EStructuralFeature id = EcoreFactory.eINSTANCE.createEAttribute();
	private final EStructuralFeature name = EcoreFactory.eINSTANCE.createEReference();

	@Test(expected = NullPointerException.class)
	public void testNullType() {
		new ClassifierMetadata(null, id, name);
	}

	@Test(expected = NullPointerException.class)
	public void testNullId() {
		new ClassifierMetadata(type, null, name);
	}

	@Test(expected = NullPointerException.class)
	public void testNullName() {
		new ClassifierMetadata(type, id, null);
	}

	@Test
	public void testPositive() {
		ClassifierMetadata metadata = new ClassifierMetadata(type, id, name);
		assertEquals(type, metadata.eClass());
		assertEquals(id, metadata.identification());
		assertEquals(name, metadata.naming());
	}
}