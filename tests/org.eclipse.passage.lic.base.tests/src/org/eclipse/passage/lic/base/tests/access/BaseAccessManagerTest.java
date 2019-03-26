/*******************************************************************************
 * Copyright (c) 2018-2019 ArSysOp
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package org.eclipse.passage.lic.base.tests.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.passage.lic.base.LicensingConfigurations;
import org.eclipse.passage.lic.base.LicensingProperties;
import org.eclipse.passage.lic.base.LicensingVersions;
import org.eclipse.passage.lic.base.SystemReporter;
import org.eclipse.passage.lic.base.access.BaseAccessManager;
import org.eclipse.passage.lic.base.conditions.BaseLicensingCondition;
import org.eclipse.passage.lic.base.conditions.LicensingConditions;
import org.eclipse.passage.lic.base.requirements.LicensingRequirements;
import org.eclipse.passage.lic.base.tests.LicensningBaseTests;
import org.eclipse.passage.lic.runtime.LicensingConfiguration;
import org.eclipse.passage.lic.runtime.LicensingReporter;
import org.eclipse.passage.lic.runtime.LicensingResult;
import org.eclipse.passage.lic.runtime.access.FeaturePermission;
import org.eclipse.passage.lic.runtime.requirements.LicensingRequirement;
import org.eclipse.passage.lic.runtime.requirements.RequirementResolver;
import org.eclipse.passage.lic.runtime.restrictions.RestrictionVerdict;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseAccessManagerTest {

	private static final String PRODUCT_ID = "product.id"; //$NON-NLS-1$
	private static final String PRODUCT_VERSION = "0.1.0"; //$NON-NLS-1$
	private static final String FEATURE_ID = "feature.id"; //$NON-NLS-1$
	private static final String FEATURE_VERSION = "0.1.0"; //$NON-NLS-1$

	private List<LicensingResult> sent = new ArrayList<>();
	private List<LicensingResult> posted = new ArrayList<>();
	private List<LicensingResult> logged = new ArrayList<>();

	private LicensingConfiguration conf = LicensingConfigurations.create(PRODUCT_ID, PRODUCT_VERSION);

	private LicensingReporter reporter = new LicensingReporter() {

		@Override
		public void sendResult(LicensingResult result) {
			sent.add(result);
		}

		@Override
		public void postResult(LicensingResult result) {
			posted.add(result);
		}

		@Override
		public void logResult(LicensingResult result) {
			logged.add(result);
		}
	};

	private BaseAccessManager manager = new BaseAccessManager() {
	};

	@Before
	public void setUp() {
		manager.bindLicensingReporter(reporter);
	}

	@After
	public void tearDown() {
		sent.clear();
		posted.clear();
		logged.clear();
		manager.unbindLicensingReporter(reporter);
	}

	@Test
	public void testLicensingReporter() {
		assertEquals(reporter, manager.getLicensingReporter());
		manager.unbindLicensingReporter(SystemReporter.INSTANCE);
		assertEquals(reporter, manager.getLicensingReporter());
		manager.bindLicensingReporter(SystemReporter.INSTANCE);
		assertEquals(SystemReporter.INSTANCE, manager.getLicensingReporter());
		manager.unbindLicensingReporter(SystemReporter.INSTANCE);
		assertEquals(SystemReporter.INSTANCE, manager.getLicensingReporter());
	}

	@Test
	public void testResolveRequirementsNullConfiguration() {
		List<LicensingRequirement> resolved = new ArrayList<>();
		manager.resolveRequirements(null).forEach(resolved::add);
		assertEquals(1, resolved.size());
		LicensingRequirement req0 = resolved.get(0);
		assertEquals(LicensingConfigurations.INVALID.getProductIdentifier(), req0.getFeatureIdentifier());
		assertEquals(LicensingProperties.LICENSING_FEATURE_NAME_DEFAULT, req0.getFeatureName());
		assertEquals(LicensingProperties.LICENSING_FEATURE_PROVIDER_DEFAULT, req0.getFeatureProvider());
		assertEquals(LicensingConfigurations.INVALID.getProductVersion(), req0.getFeatureVersion());
		assertEquals(manager.getClass().getName(), req0.getRequirementSource());
		assertEquals(LicensingProperties.LICENSING_RESTRICTION_LEVEL_ERROR, req0.getRestrictionLevel());

		int errors = 0;
		int events = 1;
		checkMaps(errors, events);

	}

	@Test
	public void testResolveRequirementsInvalidConfiguration() {
		List<LicensingRequirement> resolved = new ArrayList<>();
		manager.resolveRequirements(LicensingConfigurations.INVALID).forEach(resolved::add);
		assertEquals(1, resolved.size());
		LicensingRequirement req0 = resolved.get(0);
		assertEquals(LicensingConfigurations.INVALID.getProductIdentifier(), req0.getFeatureIdentifier());
		assertEquals(LicensingProperties.LICENSING_FEATURE_NAME_DEFAULT, req0.getFeatureName());
		assertEquals(LicensingProperties.LICENSING_FEATURE_PROVIDER_DEFAULT, req0.getFeatureProvider());
		assertEquals(LicensingConfigurations.INVALID.getProductVersion(), req0.getFeatureVersion());
		assertEquals(manager.getClass().getName(), req0.getRequirementSource());
		assertEquals(LicensingProperties.LICENSING_RESTRICTION_LEVEL_ERROR, req0.getRestrictionLevel());

		int errors = 0;
		int events = 1;
		checkMaps(errors, events);

	}

	@Test
	public void testResolveRequirementsPositive() {
		String source = getClass().getName();
		String featureId = "some.feature.id"; //$NON-NLS-1$
		LicensingRequirement error = LicensingRequirements.createConfigurationError(featureId, source);
		RequirementResolver resolver = LicensningBaseTests.createRequirementResolver(Collections.singleton(error));
		manager.bindRequirementResolver(resolver);
		String productId = "some.product.id"; //$NON-NLS-1$
		String productVersion = "X3"; //$NON-NLS-1$

		List<LicensingRequirement> resolved = new ArrayList<>();
		manager.resolveRequirements(LicensingConfigurations.create(productId, productVersion)).forEach(resolved::add);

		assertEquals(1, resolved.size());
		LicensingRequirement req0 = resolved.get(0);
		assertEquals(featureId, req0.getFeatureIdentifier());
		assertEquals(LicensingProperties.LICENSING_FEATURE_NAME_DEFAULT, req0.getFeatureName());
		assertEquals(LicensingProperties.LICENSING_FEATURE_PROVIDER_DEFAULT, req0.getFeatureProvider());
		assertEquals(LicensingVersions.VERSION_DEFAULT, req0.getFeatureVersion());
		assertEquals(getClass().getName(), req0.getRequirementSource());
		assertEquals(LicensingProperties.LICENSING_RESTRICTION_LEVEL_ERROR, req0.getRestrictionLevel());

		int errors = 0;
		int events = 1;
		checkMaps(errors, events);
	}

	@Test
	public void testResolveRequirementsUnbind() {
		String source = getClass().getName();
		String featureId = "some.feature.id"; //$NON-NLS-1$
		LicensingRequirement error = LicensingRequirements.createConfigurationError(featureId, source);
		RequirementResolver resolver = LicensningBaseTests.createRequirementResolver(Collections.singleton(error));
		manager.bindRequirementResolver(resolver);
		String productId = "some.product.id"; //$NON-NLS-1$
		String productVersion = "X3"; //$NON-NLS-1$

		List<LicensingRequirement> resolved = new ArrayList<>();
		manager.unbindRequirementResolver(resolver);
		manager.resolveRequirements(LicensingConfigurations.create(productId, productVersion)).forEach(resolved::add);

		assertEquals(1, resolved.size());
		LicensingRequirement req0 = resolved.get(0);
		assertEquals(productId, req0.getFeatureIdentifier());
		assertEquals(LicensingProperties.LICENSING_FEATURE_NAME_DEFAULT, req0.getFeatureName());
		assertEquals(LicensingProperties.LICENSING_FEATURE_PROVIDER_DEFAULT, req0.getFeatureProvider());
		assertEquals(LicensingVersions.VERSION_DEFAULT, req0.getFeatureVersion());
		assertEquals(manager.getClass().getName(), req0.getRequirementSource());
		assertEquals(LicensingProperties.LICENSING_RESTRICTION_LEVEL_ERROR, req0.getRestrictionLevel());

		int errors = 0;
		int events = 1;
		checkMaps(errors, events);
	}

	@Test
	public void testExecuteAccessRestrictionsNegative() {
		manager.executeAccessRestrictions(null);
		int errors = 1;
		int events = 6;
		checkMaps(errors, events);
	}

	@Test
	public void testEvaluateConditionsNegative() {
		int logSize = 0;
		int eventSize = 0;
		checkMaps(logSize, eventSize);

		Iterable<FeaturePermission> permissions = Collections.emptyList();
		permissions = manager.evaluateConditions(null, null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		permissions = manager.evaluateConditions(new ArrayList<>(), null);
		assertFalse(permissions.iterator().hasNext());
		eventSize++;
		checkMaps(logSize, eventSize);

		permissions = manager.evaluateConditions(Collections.singleton(null), null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);
	}

	@Test
	public void testEvaluateConditionDates() {
		int logSize = 0;
		int eventSize = 0;
		Iterable<FeaturePermission> permissions = Collections.emptyList();
		permissions = manager.evaluateConditions(Collections.singleton(createCondition(null, null)), null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		permissions = manager.evaluateConditions(Collections.singleton(createCondition(new Date(), null)), null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		permissions = manager.evaluateConditions(Collections.singleton(createCondition(null, new Date())), null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		Date before = new Date(System.currentTimeMillis() - 100500);
		Date after = new Date(System.currentTimeMillis() + 100500);
		permissions = manager.evaluateConditions(Collections.singleton(createCondition(after, after)), null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		permissions = manager.evaluateConditions(Collections.singleton(createCondition(before, before)), null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		permissions = manager.evaluateConditions(Collections.singleton(createCondition(after, before)), null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		permissions = manager.evaluateConditions(Collections.singleton(createCondition(before, after)), null);
		assertFalse(permissions.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);
	}

	protected BaseLicensingCondition createCondition(Date from, Date until) {
		return LicensingConditions.create(FEATURE_ID, FEATURE_VERSION, null, from, until, null, null);
	}

	@Test
	public void testExaminePermissionsNegative() {
		int logSize = 0;
		int eventSize = 0;
		Iterable<RestrictionVerdict> verdicts = Collections.emptyList();
		verdicts = manager.examinePermissons(null, null, null);
		assertFalse(verdicts.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		verdicts = manager.examinePermissons(null, null, conf);
		assertFalse(verdicts.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		verdicts = manager.examinePermissons(new ArrayList<>(), null, conf);
		assertFalse(verdicts.iterator().hasNext());
		checkMaps(++logSize, ++eventSize);

		verdicts = manager.examinePermissons(Collections.singleton(null), null, conf);
		assertFalse(verdicts.iterator().hasNext());
		logSize++;
		checkMaps(++logSize, ++eventSize);
	}

	protected void checkMaps(int logSize, int eventSize) {
		assertEquals(logSize, logged.size());
		assertEquals(eventSize, posted.size());
	}

}
