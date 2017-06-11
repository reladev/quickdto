import static org.junit.Assert.fail;

import com.github.quickdto.testharness.SimpleDto;
import org.junit.Test;


public class BasicTypesDtoTest {

	@Test
	public void testDirty() throws Exception {

		// Normal
		SimpleDto.class.getDeclaredMethod("getNormal");
		SimpleDto.class.getDeclaredMethod("setNormal", int.class);

		// ReadOnly
		SimpleDto.class.getDeclaredMethod("getReadOnly");
		try {
			SimpleDto.class.getDeclaredMethod("setReadOnly", int.class);
			fail();
		} catch (NoSuchMethodException ignored) {
		}

		// ReadOnlyWithSetter
		SimpleDto.class.getDeclaredMethod("getReadOnlyWithSetter");
		SimpleDto.class.getDeclaredMethod("setReadOnlyWithSetter", int.class);

		// WriteOnly
		SimpleDto.class.getDeclaredMethod("getWriteOnly");
		SimpleDto.class.getDeclaredMethod("setWriteOnly", int.class);

	}
}
