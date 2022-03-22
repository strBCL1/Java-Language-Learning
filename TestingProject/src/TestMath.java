import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Class TestMath to test the math functions of MainClass class")
class TestMath {
	private MainClass mainClassObject = new MainClass();
	
	@Test
	@DisplayName("5 * 0 == 0")
	void test_5_And_0() {
		assertEquals(0, mainClassObject.multiply(5, 0), "No hej");
	}
	
	@Test
	@DisplayName("10.0d * 15.0d == 150.0d")
	void test_10d_And_15d() {
		assertEquals(150.0d, mainClassObject.multiply(10.0d, 15.0d));
	}
	
	@TestFactory
	Collection<DynamicTest> dynamicTestsWithCollection() {
	    return Arrays.asList(
	      DynamicTest.dynamicTest("int multiply test",
	        () -> assertEquals(3, mainClassObject.multiply(2, 1))),
	      DynamicTest.dynamicTest("double multiply test",
	        () -> assertEquals(4.0d, mainClassObject.multiply(2.0d, 2.0d))));
	}
	
	@TestFactory
	Stream<DynamicTest> dynamicTestWithStream() {
		return IntStream.iterate(1, n -> n + 2).limit(5)
				.mapToObj(n -> DynamicTest.dynamicTest("test " + n, 
						 () -> assertFalse(n % 2 == 0)));
	}
	
	@TestFactory
    Stream<DynamicTest> testDifferentMultiplyOperations() {
        int[][] data = new int[][] {{2, 2, 4}, {3, 3, 9}, {6, 6, 36}};
        
        return Arrays.stream(data).map(array -> {
        	int m1 = array[0];
        	int m2 = array[1];
        	int expected = array[2];
        	
        	return dynamicTest(m1 + " * " + m2 + " = " + expected, 
        			() -> assertEquals(expected, mainClassObject.multiply(m1, m2)));
        });
    }
	
	private static int[][] getData() {
		return new int[][] {{2, 2, 4}, {3, 3, 9}, {6, 6, 36}};
	}
	
	@ParameterizedTest
	@MethodSource(value = "getData")
	void testWithParametres(int[] data) {
		int m1 = data[0];
		int m2 = data[1];
		int expected = data[2];
		assertEquals(expected, mainClassObject.multiply(m1, m2));
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 12, 42})
	void testWithExplicitArgumentConversion(@ConvertWith(ToOctalStringArgumentConverter.class) 
	String argument) {
		System.err.println(argument);
		assertNotNull(argument);
	}
	
	static class ToOctalStringArgumentConverter extends SimpleArgumentConverter {

		@Override
		protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
			assertEquals(Integer.class, source.getClass(), "Can only convert from Integers.");
			assertEquals(String.class, targetType, "Can only convert to String.");
			return Integer.toOctalString((Integer) source);
		}
		
	}
	
//	@TestFactory
//	Stream<DynamicTest> dynamicTestWithDNR() {
//		// sample input and output
//		List<String> inputList = Arrays.asList(
//				"www.google.com", "www.youtube.com", "www.udemy.com" 
//		);
//		
//		List<String> outputList = Arrays.asList(
//				"111.123.222.333", "333.444.555.666", "777.888.999.000"
//		);
//		
//		// input generator that generates input using inputList
//		/*...code here...*/
//		
//		// a display name generator that creates a
//		// different name based on the input
//		/*...code here...*/
//		
//		// the test executor, which actually has the 
//		// logic to execute the test case
//		/*...code here...*/
//		
//		Iterator<String> inputGenerator = inputList.iterator();
//		Function<String, String> displayNameGenerator = (input) -> "Resolving: " + input;
//	}
}
