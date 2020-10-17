/**
 * <ul>
 * General license cycle errors
 * <li>1xx - errors begotten by framework/services configuration or behavior, be
 * wrong product-under-license passage-equipment. Configuration part of these
 * errors are to be fixed on a product-under-licensing development side.
 * <ul>
 * <li>100 - no framework found. Critical.</li>
 * <li>101 - service cannot operate due to the insufficient configuration or
 * severe construction error</li>
 * <li>102 - no service of demanded type. Usually means error in the Framework
 * configuration. Thus, severe.</li>
 * <li>103 - service failed on a morsel. In most cases bearable.</li>
 * <li>104 - service failed because of infrastructure denial. Service cannot
 * finish operation because of IO error, connection troubles or other outer
 * obstacle.</li>
 * <li>110 - several implementations of Framework has been found. Critical.</li>
 * <li>151 - no requirements defined for a feature on in product as a whole. In
 * the worst case it is a sign of an error in licensing demands
 * declaration.</li>
 * </ul>
 * </li>
 * <li>2xx - reserved</li>
 * <li>3xx - reserved</li>
 * <li>4xx - errors begotten by user data (license internals, credentials,
 * settings, etc) - the things a user of a product-under-licensing can change.
 * <ul>
 * <li></li>
 * <li>401 - invalid license: a license cannot be applied as contains corrupted
 * data or data that are not readable with the active access cycle
 * configuration</li>
 * <li>402 - license is not started: start of a license validity period lies in
 * the future</li>
 * <li>403 - license expired: end of a license validity period lies in the
 * past</li>
 * <li>404 - license does not match: a condition specified is not met</li>
 * <li>405 - insufficient license coverage. Analytical error, reports that among
 * requested features there are some that do not have proper license
 * permissions.</li>
 * </ul>
 * </li>
 * </ul>
 */
package org.eclipse.passage.lic.internal.base.diagnostic.code;