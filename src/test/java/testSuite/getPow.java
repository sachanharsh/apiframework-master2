package testSuite;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utils.PowFunction;

public class getPow {

    @DataProvider(name = "powerData")
    public Object[][] powerData() {
        return new Object[][] {
            // base, exponent, expected, delta for floating-point
            { 2.0, 10, 1024.0, 1e-9 },
            { 2.0, -2, 0.25, 1e-9 },
            { 3.0, 0, 1.0, 1e-9 },
            { 0.0, 5, 0.0, 1e-9 },
            { 5.0, 1, 5.0, 1e-9 },
            { -2.0, 3, -8.0, 1e-9 },
            { 2.0, Integer.MIN_VALUE, 0.0, 1e-9 }  // tests our long-cast logic
        };
    }

    @Test(dataProvider = "powerData")
    public void testMyPow(double x, int n, double expected, double delta) {
        double actual = PowFunction.myPow(x, n);
        assertEquals(actual, expected, String.format("myPow(%f, %d) should be %f but was %f", x, n, expected, actual));
    }
}
