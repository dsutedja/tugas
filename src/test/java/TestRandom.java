import org.junit.Test;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by dsutedja on 6/27/16.
 */
public class TestRandom {
    @Test
    public void testRandom() {
        HashSet<Integer> generated = new HashSet<>();
        int n = Integer.MAX_VALUE;

        Random random = new Random();
        int numTest = 100;
        int totalCollisionCount = 0;
        for (int i=0; i<numTest; i++) {
            System.out.println("Test #" + i);
            int collisionCount = 0;
            for (int x = 0; x < 1000000; x++) {
                int next = random.nextInt(n);
                if (generated.contains(next)) {
                    collisionCount++;
                } else {
                    generated.add(next);
                }
            }
            totalCollisionCount += collisionCount;
            System.out.println("For 1 millions random, found " +  collisionCount + " collisions");
        }

        System.out.println(numTest + " run with 1 mil each, average collision is: " + (totalCollisionCount / numTest));
    }
}
