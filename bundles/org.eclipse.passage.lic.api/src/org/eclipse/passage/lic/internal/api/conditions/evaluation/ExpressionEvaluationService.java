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
package org.eclipse.passage.lic.internal.api.conditions.evaluation;

import org.eclipse.passage.lic.api.registry.Service;

/**
 * Any implementation must follow the contract defined in
 * {@code ExpressionEvaluationServiceContractTest}
 */
public interface ExpressionEvaluationService extends Service<ExpressionProtocol> {

	/**
	 * Assess the {@code expression} tokens with the given {@code assessor}.
	 * 
	 * @throws ExpressionEvaluationException in case of infrastructure denial or
	 *                                       assessment failure
	 */
	void evaluate(ParsedExpression expression, ExpressionTokenAssessmentService assessor)
			throws ExpressionEvaluationException;

}
