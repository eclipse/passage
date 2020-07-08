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

import java.util.Objects;

import org.eclipse.passage.lic.internal.api.conditions.evaluation.ExpressionParsingException;
import org.eclipse.passage.lic.internal.api.conditions.evaluation.ExpressionPasringService;
import org.eclipse.passage.lic.internal.api.conditions.evaluation.ExpressionProtocol;
import org.eclipse.passage.lic.internal.api.conditions.evaluation.ParsedExpression;
import org.eclipse.passage.lic.internal.api.registry.Registry;

/**
 * <p>
 * Turns the given raw {@code expression} string into a workable
 * {@linkplain ParsedExpression} instance. Is aware of top-level expression
 * syntax (<protocol><separator><content>):
 * </p>
 * <ul>
 * <li>cuts the protocol part, uses default is there is no such thing</li>
 * <li>guided by the protocol - detects the parsing service</li>
 * <li>uses the service to parse content of the expression</li>
 * </ul>
 */
@SuppressWarnings("restriction")
final class FormalizedExpression {

	private final String raw;
	private final String separator;
	private final Registry<ExpressionProtocol, ExpressionPasringService> parsers;

	FormalizedExpression(String raw, String separator, Registry<ExpressionProtocol, ExpressionPasringService> parsers) {
		Objects.requireNonNull(raw, "RetrievedExpression::raw"); //$NON-NLS-1$
		Objects.requireNonNull(separator, "RetrievedExpression::separator"); //$NON-NLS-1$
		Objects.requireNonNull(parsers, "RetrievedExpression::parsers"); //$NON-NLS-1$
		this.raw = raw;
		this.separator = separator;
		this.parsers = parsers;
	}

	FormalizedExpression(String raw, Registry<ExpressionProtocol, ExpressionPasringService> parsers) {
		this(raw, "!!", parsers); //$NON-NLS-1$
	}

	ParsedExpression get() throws ExpressionParsingException {
		return service().parsed(content());
	}

	private ExpressionPasringService service() {
		ExpressionProtocol protocol = protocol();
		return parsers.hasService(protocol) //
				? parsers.service(protocol) //
				: parsers.service(new ExpressionProtocol.Default()); // guaranteed by Framework contract
	}

	private ExpressionProtocol protocol() {
		int index = raw.indexOf(separator);
		return index <= 0 //
				? new ExpressionProtocol.Default() //
				: new ExpressionProtocol.Of(raw.substring(0, index));
	}

	private String content() {
		int index = raw.indexOf(separator);
		return index <= 0 //
				? raw //
				: raw.substring(index + separator.length());
	}

}
