package ezvcard.util;

import java.net.URI;
import java.util.Arrays;

import ezvcard.Messages;
import ezvcard.util.org.apache.commons.codec.binary.Base64;

/*
 Copyright (c) 2012-2015, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * <p>
 * Represents a URI for encoding binary data.
 * </p>
 * <p>
 * Example: {@code data:image/jpeg;base64,[base64 string]}
 * </p>
 * @author Michael Angstadt
 */
public final class DataUri {
	private final byte[] data;
	private final String contentType;

	/**
	 * Creates a data URI.
	 * @param contentType the content type (e.g. "image/jpeg")
	 * @param data the binary data
	 */
	public DataUri(String contentType, byte[] data) {
		this.contentType = contentType;
		this.data = data;
	}

	/**
	 * Parses a data URI string.
	 * @param uri the URI string (e.g. "data:image/jpeg;base64,[base64 string]")
	 * @throws IllegalArgumentException if the string is not a valid data URI
	 */
	public static DataUri parse(String uri) {
		//URI format: data:CONTENT/TYPE;base64,DATA

		String scheme = "data:";
		if (uri.length() < scheme.length() || !uri.substring(0, scheme.length()).equalsIgnoreCase(scheme)) {
			//not a data URI
			throw Messages.INSTANCE.getIllegalArgumentException(18, scheme);
		}

		int semiColon = uri.indexOf(';');
		if (semiColon < 0) {
			throw Messages.INSTANCE.getIllegalArgumentException(19);
		}

		int comma = uri.indexOf(',', semiColon + 1);
		if (comma < 0) {
			throw Messages.INSTANCE.getIllegalArgumentException(19);
		}

		String contentType = uri.substring(5, semiColon);

		String encoding = uri.substring(semiColon + 1, comma);
		if (!"base64".equalsIgnoreCase(encoding)) {
			throw Messages.INSTANCE.getIllegalArgumentException(20, encoding);
		}

		byte[] data = Base64.decodeBase64(uri.substring(comma + 1));

		return new DataUri(contentType, data);
	}

	/**
	 * Gets the binary data.
	 * @return the binary data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Sets the content type.
	 * @return the content type (e.g. "image/jpeg")
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Creates a {@link URI} object from this data URI.
	 * @return the {@link URI} object
	 */
	public URI toUri() {
		return URI.create(toString());
	}

	@Override
	public String toString() {
		return "data:" + contentType + ";base64," + Base64.encodeBase64String(data);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.toLowerCase().hashCode());
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DataUri other = (DataUri) obj;
		if (contentType == null) {
			if (other.contentType != null) return false;
		} else if (!contentType.equalsIgnoreCase(other.contentType)) return false;
		if (!Arrays.equals(data, other.data)) return false;
		return true;
	}
}
