package com.poli.actipuls;

import java.util.Random;

/**
 * This class is used for testing purposes in lack of an Arduino present
 */
public class MockPulseGenerator {

    public static int generatePulse(){
        Random rand = new Random();
        // maximum 3 beats per second
        return rand.nextInt(3);
    }
}
