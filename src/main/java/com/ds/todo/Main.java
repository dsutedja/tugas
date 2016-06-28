package com.ds.todo;

import com.ds.todo.controllers.AuthenticationController;
import com.ds.todo.controllers.TaskController;

/**
 * Created by dsutedja on 6/20/16.
 */
public class Main {
    public static void main(String[] argv) {
        new TaskController();
        new AuthenticationController();
    }
}
