/*******************************************************************************
 * Copyright (c) 2018, 2020 ArSysOp
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
package org.eclipse.passage.lic.equinox.requirements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.passage.lic.api.LicensingConfiguration;
import org.eclipse.passage.lic.api.inspector.FeatureCase;
import org.eclipse.passage.lic.api.inspector.FeatureInspector;
import org.eclipse.passage.lic.api.requirements.LicensingRequirement;
import org.eclipse.passage.lic.base.LicensingNamespaces;
import org.eclipse.passage.lic.base.requirements.LicensingRequirements;
import org.eclipse.passage.lic.equinox.ApplicationConfigurations;
import org.eclipse.passage.lic.equinox.LicensingEquinox;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleWiring;

public class EquinoxRequirements {

	public static Iterable<BundleRequirement> extractLicensingManagementRequirements(Bundle bundle) {
		BundleWiring wiring = bundle.adapt(BundleWiring.class);
		if (wiring != null) {
			return wiring.getRequirements(LicensingNamespaces.CAPABILITY_LICENSING_MANAGEMENT);
		}
		return Collections.emptyList();
	}

	public static Iterable<BundleCapability> extractLicensingFeatures(Bundle bundle) {
		BundleWiring wiring = bundle.adapt(BundleWiring.class);
		if (wiring != null) {
			return wiring.getCapabilities(LicensingNamespaces.CAPABILITY_LICENSING_FEATURE);
		}
		return Collections.emptyList();
	}

	public static Iterable<LicensingRequirement> getFeatureRequirements(String... featureIds) {
		FeatureInspector featureInspector = LicensingEquinox.getFeatureInspector();
		if (featureInspector == null) {
			LicensingConfiguration configuration = ApplicationConfigurations.getLicensingConfiguration();
			if (featureIds.length == 0) {
				String id = configuration.getProductIdentifier();
				return Collections.singletonList(LicensingRequirements.createConfigurationError(id, configuration));
			}
			List<LicensingRequirement> errors = new ArrayList<>();
			for (String id : featureIds) {
				errors.add(LicensingRequirements.createConfigurationError(id, configuration));
			}
			return errors;
		}
		try(FeatureCase inspection = featureInspector.inspectFeatures(featureIds)) {
			return inspection.getRequirements();
		}
	}
	
}
