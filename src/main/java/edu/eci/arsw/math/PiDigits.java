package edu.eci.arsw.math;


import java.util.ArrayList;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    public static int DigitsPerSum = 8;
    public static double Epsilon = 1e-17;

    private static ArrayList<ThreatPiDigits>threats = new ArrayList<>();

    
    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int numThreads) throws InterruptedException {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        int vari = (start+count)/numThreads;
        for (int i = 0; i < numThreads; i++) {
            //System.out.print(variable+"    ");
            int newStart = start+(vari*i);
            System.out.println(newStart+"    " );
            int newCount = newStart+vari;
            System.out.print(newCount+"    ");
            ThreatPiDigits t = new ThreatPiDigits(newStart,newCount);
            threats.add(t);
        }
        for (ThreatPiDigits t : threats){
            t.start();
        }
        for (ThreatPiDigits t :threats){
            t.join();
        }
        return makeResult(count);

    }

    public static byte[] makeResult(int count){
        byte[] digits = new byte[count];
        int auxy = 0;
        for (ThreatPiDigits t : threats){
            for (byte p :t.getDigits()){
                if (!(auxy >= digits.length)){
                    digits[auxy] = p;
                    System.out.println(p);
                    auxy ++;
                }
            }
        }
        return digits;
    }

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    public static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

}
