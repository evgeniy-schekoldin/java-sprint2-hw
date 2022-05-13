package main.tests.service;

import main.java.service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

}