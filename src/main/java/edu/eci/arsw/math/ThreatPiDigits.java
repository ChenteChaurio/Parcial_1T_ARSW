package edu.eci.arsw.math;

import static edu.eci.arsw.math.PiDigits.DigitsPerSum;

public class ThreatPiDigits extends Thread{
    private int start;
    private int count;

    private byte[] digits;

    public ThreatPiDigits(int start, int count){
        digits = new byte[count];
        this.start = start;
        this.count = count;
    }

    public byte[] getDigits() {
        return digits;
    }

    @Override
    public void run(){
        double sum = 0;
        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * PiDigits.sum(1, start)
                        - 2 * PiDigits.sum(4, start)
                        - PiDigits.sum(5, start)
                        - PiDigits.sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }

    }
}
