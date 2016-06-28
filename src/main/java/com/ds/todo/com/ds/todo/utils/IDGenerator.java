package com.ds.todo.com.ds.todo.utils;

import java.util.Random;

/**
 * Created by dsutedja on 6/27/16.
 */
public class IDGenerator {
    private static final Random random = new Random();

    public static int nextID() {
        return random.nextInt(Integer.MAX_VALUE);
    }
}
