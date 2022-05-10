package edu.uoc.lti.jwt;

import lombok.Getter;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author xaracil@uoc.edu
 */
public class AlgorithmFactory {
	private final RSAPublicKey publicKey;
	@Getter
	private final RSAPrivateKey privateKey;

	public AlgorithmFactory(String publicKey, String privateKey) {
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			byte[] encodedPb = Base64.getDecoder().decode(publicKey);
			X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
			this.publicKey = (RSAPublicKey) kf.generatePublic(keySpecPb);

			DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKey));

			DerValue[] seq = derReader.getSequence(0);

			if (seq.length < 9) {
				throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
			}

			// skip version seq[0];
			BigInteger modulus = seq[1].getBigInteger();
			BigInteger publicExp = seq[2].getBigInteger();
			BigInteger privateExp = seq[3].getBigInteger();
			BigInteger prime1 = seq[4].getBigInteger();
			BigInteger prime2 = seq[5].getBigInteger();
			BigInteger exp1 = seq[6].getBigInteger();
			BigInteger exp2 = seq[7].getBigInteger();
			BigInteger crtCoef = seq[8].getBigInteger();

			RSAPrivateCrtKeySpec keySpecPv = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);

			this.privateKey = (RSAPrivateKey) kf.generatePrivate(keySpecPv);

		} catch (GeneralSecurityException | IOException e) {
			throw new BadToolProviderConfigurationException(e);
		}
	}
}
