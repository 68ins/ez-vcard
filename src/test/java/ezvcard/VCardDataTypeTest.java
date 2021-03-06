package ezvcard;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

public class VCardDataTypeTest {
	@Test
	public void get() {
		assertSame(VCardDataType.TEXT, VCardDataType.get("tExT"));

		VCardDataType test = VCardDataType.get("test");
		VCardDataType test2 = VCardDataType.get("tEsT");
		assertSame(test, test2);
	}

	@Test
	public void find() {
		assertSame(VCardDataType.TEXT, VCardDataType.find("tExT"));

		//find() ignores runtime-defined objects
		VCardDataType.get("test");
		assertNull(VCardDataType.find("test"));
	}

	@Test
	public void all() {
		VCardDataType.get("test"); //all() ignores runtime-defined objects
		Collection<VCardDataType> all = VCardDataType.all();

		assertEquals(15, all.size());
		assertTrue(all.contains(VCardDataType.BINARY));
		assertTrue(all.contains(VCardDataType.BOOLEAN));
		assertTrue(all.contains(VCardDataType.CONTENT_ID));
		assertTrue(all.contains(VCardDataType.DATE));
		assertTrue(all.contains(VCardDataType.DATE_TIME));
		assertTrue(all.contains(VCardDataType.DATE_AND_OR_TIME));
		assertTrue(all.contains(VCardDataType.FLOAT));
		assertTrue(all.contains(VCardDataType.INTEGER));
		assertTrue(all.contains(VCardDataType.LANGUAGE_TAG));
		assertTrue(all.contains(VCardDataType.TEXT));
		assertTrue(all.contains(VCardDataType.TIME));
		assertTrue(all.contains(VCardDataType.TIMESTAMP));
		assertTrue(all.contains(VCardDataType.URI));
		assertTrue(all.contains(VCardDataType.URL));
		assertTrue(all.contains(VCardDataType.UTC_OFFSET));
	}

	@Test
	public void getSupportedVersions() {
		assertArrayEquals(new VCardVersion[] { VCardVersion.V2_1 }, VCardDataType.CONTENT_ID.getSupportedVersions());
		assertArrayEquals(VCardVersion.values(), VCardDataType.TEXT.getSupportedVersions());
		assertArrayEquals(VCardVersion.values(), VCardDataType.get("test").getSupportedVersions());
	}

	@Test
	public void isSupportedBy() {
		assertTrue(VCardDataType.CONTENT_ID.isSupportedBy(VCardVersion.V2_1));
		assertFalse(VCardDataType.CONTENT_ID.isSupportedBy(VCardVersion.V3_0));
		assertFalse(VCardDataType.CONTENT_ID.isSupportedBy(VCardVersion.V4_0));

		for (VCardVersion version : VCardVersion.values()) {
			assertTrue(VCardDataType.TEXT.isSupportedBy(version));
		}

		VCardDataType test = VCardDataType.get("test");
		for (VCardVersion version : VCardVersion.values()) {
			assertTrue(test.isSupportedBy(version));
		}
	}
}
