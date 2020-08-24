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
package org.eclipse.passage.lbc.internal.equinox;

import java.util.Map;

import org.eclipse.passage.lbc.api.BackendActionExecutor;
import org.eclipse.passage.lbc.api.BackendRequestDispatcher;
import org.eclipse.passage.lbc.base.BaseRequestDispatcher;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(service = BackendRequestDispatcher.class, property = "role" + '=' + "admin")
public class AdminRequestDispatcher extends BaseRequestDispatcher {

	@Activate
	@Override
	public void activate(Map<String, Object> context) {
		super.activate(context);
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	@Override
	public void bindBackendActionExecutor(BackendActionExecutor executor, Map<String, Object> properties) {
		super.bindBackendActionExecutor(executor, properties);
	}

	@Override
	public void unbindBackendActionExecutor(BackendActionExecutor executor, Map<String, Object> properties) {
		super.unbindBackendActionExecutor(executor, properties);
	}

}
