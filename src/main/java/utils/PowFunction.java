package utils;

public class PowFunction {
    public static double myPow(double x, int n) {
        // handle negative exponents
        long exp = n;            // use long to handle Integer.MIN_VALUE
        if (exp < 0) {
            x = 1 / x;
            exp = -exp;
        }

        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) {        // if exp is odd
                result *= x;
            }
            x *= x;                      // square the base
            exp >>= 1;                   // divide exp by 2
        }
        return result;
    }
}