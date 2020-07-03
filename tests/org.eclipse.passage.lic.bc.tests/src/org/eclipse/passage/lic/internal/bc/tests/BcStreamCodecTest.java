package org.eclipse.passage.lic.internal.bc.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.passage.lic.internal.api.LicensedProduct;
import org.eclipse.passage.lic.internal.api.LicensingException;
import org.eclipse.passage.lic.internal.base.BaseLicensedProduct;
import org.eclipse.passage.lic.internal.base.io.PassageFileExtension;
import org.eclipse.passage.lic.internal.bc.BcStreamCodec;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("restriction")
abstract class BcStreamCodecTest {

	@Rule
	public final TemporaryFolder root = new TemporaryFolder();

	protected final void assertFileExists(Path file) {
		assertTrue(Files.exists(file));
		assertTrue(Files.isRegularFile(file));
	}

	protected final PairInfo<Path> pair(String user, String pass) throws IOException {
		return pair((pub, secret) -> new PairKeys(pub, secret), user, pass);
	}

	protected final <I> PairInfo<I> pair(ThrowingCtor<I> ctor, String user, String pass) throws IOException {
		Path pub = new TmpFile(root).keyFile(new PassageFileExtension.PublicKey());
		Path secret = new TmpFile(root).keyFile(new PassageFileExtension.PrivateKey());
		BcStreamCodec codec = new BcStreamCodec(this::product);
		try {
			codec.createKeyPair(pub, secret, user, pass);
		} catch (LicensingException e) {
			fail("PGP key pair generation on valid data is not supposed to fail"); //$NON-NLS-1$
		}
		return ctor.create(pub, secret);
	}

	protected final InputStream anInput() {
		return new ByteArrayInputStream(new byte[0]);
	}

	protected final OutputStream anOutput() {
		return new ByteArrayOutputStream();
	}

	protected final LicensedProduct product() {
		return new BaseLicensedProduct("bc-stream-codec-test-product", "2.4.21"); //$NON-NLS-1$//$NON-NLS-2$
	}
}
