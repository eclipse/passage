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
package org.eclipse.passage.lic.internal.base.conditions.evaluation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.passage.lic.internal.api.conditions.evaluation.ExpressionParsingException;
import org.eclipse.passage.lic.internal.api.conditions.evaluation.ExpressionParsingService;
import org.eclipse.passage.lic.internal.api.conditions.evaluation.ExpressionProtocol;
import org.eclipse.passage.lic.internal.api.conditions.evaluation.ParsedExpression;
import org.eclipse.passage.lic.internal.base.i18n.ConditionsEvaluationMessages;

/**
 * 
 */
@SuppressWarnings("restriction")
public final class AndsProtocolExpressionParseService implements ExpressionParsingService {

	private final ExpressionProtocol protocol = new ExpressionProtocol.Ands();
	private final String separator = ";"; //$NON-NLS-1$
	private final String mediator = "="; //$NON-NLS-1$

	@Override
	public ExpressionProtocol id() {
		return protocol;
	}

	/**
	 * Expect the incoming {@code expression} to be a semicolon-separated
	 * {@code key=value} pairs meaning {@code AND}-ed equality checks.
	 */
	@Override
	public ParsedExpression parsed(String expression) throws ExpressionParsingException {
		Objects.requireNonNull(expression);
		Map<String, String> couples = new HashMap<>();
		for (String segment : expression.split(separator)) {
			addCouple(segment, couples);
		}
		if (couples.isEmpty()) {
			throw new ExpressionParsingException(String.format(//
					ConditionsEvaluationMessages.getString("AndsProtocolExpressionParseService.no_checks"), //$NON-NLS-1$
					expression));
		}
		return new SimpleMapExpression(protocol, couples);
	}

	private void addCouple(String segment, Map<String, String> couples) throws ExpressionParsingException {
		String[] couple = segment.split(mediator);
		if (coupleIsInvalid(couple)) {
			throw new ExpressionParsingException(String.format(//
					ConditionsEvaluationMessages.getString("AndsProtocolExpressionParseService.invalid_format"), segment)); //$NON-NLS-1$
		}
		couples.put(couple[0].trim(), couple[1].trim());
	}

	private boolean coupleIsInvalid(String[] couple) {
		return (couple.length != 2) || //
				couple[0].trim().isEmpty() || //
				couple[1].trim().isEmpty();
	}

}
