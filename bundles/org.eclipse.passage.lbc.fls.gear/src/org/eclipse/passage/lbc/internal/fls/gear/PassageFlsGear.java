/*******************************************************************************
 * Copyright (c) 2021 ArSysOp
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
package org.eclipse.passage.lbc.internal.fls.gear;

import java.nio.file.Path;
import java.util.function.Supplier;

import org.eclipse.passage.lbc.internal.base.api.FlsGear;
import org.eclipse.passage.lic.api.io.Hashes;
import org.eclipse.passage.lic.api.io.HashesRegistry;
import org.eclipse.passage.lic.api.io.KeyKeeper;
import org.eclipse.passage.lic.api.io.StreamCodec;
import org.eclipse.passage.lic.api.registry.Registry;
import org.eclipse.passage.lic.api.registry.StringServiceId;
import org.eclipse.passage.lic.base.io.MD5Hashes;
import org.eclipse.passage.lic.base.io.PathKeyKeeper;
import org.eclipse.passage.lic.base.registry.ReadOnlyRegistry;
import org.eclipse.passage.lic.bc.BcStreamCodec;
import org.eclipse.passage.lic.internal.api.LicensedProduct;
import org.eclipse.passage.lic.internal.api.conditions.mining.ConditionTransport;
import org.eclipse.passage.lic.internal.api.conditions.mining.ContentType;
import org.eclipse.passage.lic.internal.licenses.model.toberemoved.XmiConditionTransport;

@SuppressWarnings("restriction")
final class PassageFlsGear implements FlsGear {

	private final Registry<StringServiceId, Hashes> hashes;

	static final PassageFlsGear gear = new PassageFlsGear();

	PassageFlsGear() {
		this.hashes = new ReadOnlyRegistry<>(//
				new MD5Hashes()//
		);
	}

	@Override
	public HashesRegistry hashes() {
		return () -> hashes;
	}

	@Override
	public KeyKeeper keyKeper(LicensedProduct product, Supplier<Path> residense) {
		return new PathKeyKeeper(product, residense);
	}

	@Override
	public StreamCodec codec(LicensedProduct product) {
		return new BcStreamCodec(() -> product);
	}

	@Override
	public ConditionTransport transport(ContentType contentType) {
		return new XmiConditionTransport();
	}

}
