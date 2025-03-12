//package il.co.ilrd.threadpool;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.concurrent.*;
//
//import static org.junit.Assert.*;
//
//public class ThreadPoolTest {
//    private ThreadPool threadPool;
//
//    @Before
//    public void setUp() {
//        threadPool = new ThreadPool();
//    }
//
//    @After
//    public void tearDown() {
//        threadPool.shutDown();
//        try {
//            threadPool.awaitTermination(10, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            fail("Failed to terminate thread pool in time");
//        }
//    }
//
//    @Test
//    public void testSubmitRunnable() throws ExecutionException, InterruptedException {
//        Future<Void> future = threadPool.submit(() -> {
//            System.out.println("Task executed");
//            return null;
//        });
//        assertNull(future.get());
//        assertTrue(future.isDone());
//    }
//
//    @Test
//    public void testSubmitCallable() throws ExecutionException, InterruptedException {
//        Future<Integer> future = threadPool.submit(() -> 42);
//        assertEquals(Integer.valueOf(42), future.get());
//        assertTrue(future.isDone());
//    }
//
//    @Test
//    public void testSubmitWithPriority() throws ExecutionException, InterruptedException {
//        Future<Integer> lowPriorityTask = threadPool.submit(() -> 1, Priority.LOW);
//        Future<Integer> highPriorityTask = threadPool.submit(() -> 2, Priority.HIGH);
//
//        assertEquals(Integer.valueOf(2), highPriorityTask.get());
//        assertEquals(Integer.valueOf(1), lowPriorityTask.get());
//    }
//
//    @Test
//    public void testSetThreadsNum() {
//        int initialThreads = threadPool.getNumOfThreads();
//        threadPool.setNumOfThreads(initialThreads + 2);
//        assertEquals(initialThreads + 2, threadPool.getNumOfThreads());
//
//        threadPool.setNumOfThreads(initialThreads);
//        assertEquals(initialThreads, threadPool.getNumOfThreads());
//    }
//
//    @Test
//    public void testPauseAndResumeTP() throws ExecutionException, InterruptedException {
//        threadPool.pauseTP();
//
//        Future<Void> future = threadPool.submit(() -> {
//            System.out.println("Paused task executed");
//            return null;
//        });
//
//        Thread.sleep(1000);// Allow time to ensure task doesn't execute during pause
//
//        System.out.println(future.isDone());
//
//        assertFalse(future.isDone());
//
//        threadPool.resumeTP();
//        future.get();
//        assertTrue(future.isDone());
//    }
//
//    @Test(expected = RejectedExecutionException.class)
//    public void testSubmitAfterShutdown() {
//        threadPool.shutDown();
//        threadPool.submit(() -> {
//            System.out.println("Should not execute");
//            return null;
//        });
//    }
//
//    @Test
//    public void testAwaitTermination() throws InterruptedException {
//        threadPool.shutDown();
//        boolean terminated = threadPool.awaitTermination(5, TimeUnit.SECONDS);
//        assertTrue(terminated);
//    }
//
//}

////
////import org.junit.Assert;
////import org.junit.Before;
////import org.junit.Test;
////
////import java.util.ArrayList;
////import java.util.concurrent.*;
////
////import static java.lang.Thread.sleep;
////import static org.junit.jupiter.api.Assertions.*;
////
////public class ThreadPoolTest {
////    private ThreadPool tp;
////
////    @Before
////    public void initTP(){
////        tp = new ThreadPool(10);
////    }
////
////    @Test
////    public void submitTest() {
////        System.out.println("SubmitTest test!");
////        for(int i = 0; i < 10; ++i){
////            RunTask t = new RunTask();
////            t.addN(i);
////            tp.submit(t, Priority.DEFAULT);
////        }
////    }
////
////    @Test
////    public void testTaskCancellation() throws Exception {
////        Callable<Integer> longRunningTask = () -> {
////            Thread.sleep(5_000);
////            System.out.println("executed");
////            return 17;
////        };
////
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        tp.submit(longRunningTask);
////        Future<Integer> future = tp.submit(longRunningTask, Priority.LOW);
////
////        assertNotNull(future);
////        assertFalse(future.isCancelled());
////        Thread.sleep(1_000);
////        boolean canceled = future.cancel(false);
////
////        assertTrue(canceled);
////        assertTrue(future.isCancelled());
////    }
////
////    @Test
////    public void futureTest() throws InterruptedException, ExecutionException {
////        ArrayList<Future<Integer>> futureContainer = new ArrayList<>();
////
////        for(int i = 0; i < 20; ++i){
////            RunTaskCall t = new RunTaskCall();
////            t.addN(i);
////            if(i == 13 || i == 16){
////                tp.pauseTP();
////            }
////
////            futureContainer.add(tp.submit(t, Priority.DEFAULT));
////            if(i == 13 || i == 16){
////                futureContainer.get(i).cancel(true);
////                sleep(2000);
////                assertTrue(futureContainer.get(i).isCancelled());
////                tp.resumeTP();
////            }
////
////            if(i == 15){
////                assertFalse(futureContainer.get(15).isDone());
////                sleep(1000);
////
////                assertTrue(futureContainer.get(0).isDone());
////                assertTrue(futureContainer.get(5).isDone());
////            }
////        }
////        sleep(1000);
////        for(int i = 0; i < 20; ++i){
////            if(i == 13 || i == 16){
////                ++i;
////            }
////            assertEquals(i, futureContainer.get(i).get());
////        }
////    }
////
////    /*    @Test
////        public void futureGetTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
////            ArrayList<Future<Integer>> futureContainer = new ArrayList<>();
////            int numOfEx = 0;
////
////            for(int i = 0; i < 20; ++i){
////                RunTaskCall t = new RunTaskCall();
////                t.addN(i);
////
////                if(i == 10){
////                    futureContainer.add(tp.submit(new Callable<Integer>() {
////                        private int i = 10;
////
////                        public void addN(int n){
////                            i += n;
////                        }
////
////                        @Override
////                        public Integer call() throws Exception {
////                            sleep(15000);
////                            return i;
////                        }
////                    }, Priority.DEFAULT));
////                }
////                else {
////                    futureContainer.add(tp.submit(t, Priority.DEFAULT));
////                }
////            }
////
////            sleep(5000);
////            for(int i = 0; i < 20; ++i){
////                try {
////                    assertEquals(i, futureContainer.get(i).get(3, TimeUnit.SECONDS));
////                } catch (TimeoutException e){
////                    //e.printStackTrace();
////                    System.out.println("Timeout in get worked");
////                    ++numOfEx;
////                }
////            }
////            System.out.println(numOfEx);
////        }
////    */
////    @Test
////    public void futureGetTimeoutTest2(){
////        ArrayList<Future<Integer>> futureContainer = new ArrayList<>();
////
////        for(int i = 0; i < 21; ++i){
////            RunTaskCall t = new RunTaskCall();
////            t.addN(i);
////
////            if(i % 10 == 0 && i > 0){
////                RunTaskCall t2 = new RunTaskCall(){
////                    @Override
////                    public Integer call() throws Exception {
////                        sleep(15000);
////                        return super.i;
////                    }
////                };
////                t2.addN(i);
////                futureContainer.add(tp.submit(t2, Priority.DEFAULT));
////            }
////            else {
////                futureContainer.add(tp.submit(t, Priority.DEFAULT));
////            }
////        }
////
////        for(int i = 0; i < 21; ++i){
////            try {
////                assertEquals(i, futureContainer.get(i).get(5, TimeUnit.SECONDS));
////            } catch (TimeoutException | InterruptedException | ExecutionException e){
////                System.out.println("Timeout in get worked");
////            }
////        }
////    }
////
////    @Test
////    public void SetMoreThreads(){
////        System.out.println("SetMoreThreads test!");
////        assertEquals(10, tp.getNumOfThreads());
////        for(int i = 0; i < 11; ++i){
////            RunTask t = new RunTask();
////            t.addN(i);
////            tp.submit(t, Priority.DEFAULT);
////            if(i == 7){
////                tp.setThreadsNum(12);
////            }
////        }
////        assertEquals(12, tp.getNumOfThreads());
////    }
////
////    @Test
////    public void SetLessThreads(){
////        System.out.println("SetLessThreads test!");
////        assertEquals(10, tp.getNumOfThreads());
////        for(int i = 0; i < 11; ++i){
////            RunTask t = new RunTask();
////            t.addN(i);
////            tp.submit(t, Priority.DEFAULT);
////            if(i == 7){
////                tp.setThreadsNum(6);
////            }
////        }
////        assertEquals(6, tp.getNumOfThreads());
////    }
////
////    @Test
////    public void PauseResumeTest(){
////        //Need to look for 3 elements after pause
////        System.out.println("Pause Resume test!");
////        for(int i = 0; i < 11; ++i){
////            RunTask t = new RunTask();
////            t.addN(i);
////            tp.submit(t, Priority.DEFAULT);
////            if(i == 7){
////                tp.pauseTP();
////            }
////        }
////        tp.resumeTP();
////    }
////
////    @Test
////    public void ShutdownTest(){
////        System.out.println("Shutdown test!");
////        for(int i = 0; i < 11; ++i){
////            RunTask t = new RunTask();
////            t.addN(i);
////            tp.submit(t, Priority.DEFAULT);
////        }
////
////        tp.shutDown();
////        RunTask t = new RunTask();
////        t.addN(11);
////        assertThrows(RejectedExecutionException.class ,()->{tp.submit(t, Priority.DEFAULT);});
////    }
////
////
////    @Test
////    public void AwaitTerminationTimeoutTest() throws InterruptedException {
////        System.out.println("AwaitTermination test!");
////        for(int i = 0; i < 11; ++i){
////            RunTask t = new RunTask();
////            t.addN(i);
////            tp.submit(t, Priority.DEFAULT);
////        }
////
////        RunTaskCall t2 = new RunTaskCall(){
////            @Override
////            public Integer call() throws Exception {
////                System.out.println("hey");
////                sleep(1500000);
////                System.out.println("awken");
////                return super.i;
////            }
////        };
////        t2.addN(11);
////        tp.submit(t2, Priority.LOW);
////        tp.shutDown();
////
////        sleep(100);
////
////
////        try {
////            assertFalse(tp.awaitTermination(1, TimeUnit.NANOSECONDS));
////        } catch (InterruptedException e){
////            System.out.println("Timeout in get worked");
////        }
////
////
////        assertEquals(10, tp.getNumOfThreads());
////
////
////    }
////
////    private static class RunTask implements Runnable{
////        private int i = 0;
////
////        public void addN(int n){
////            i += n;
////        }
////
////        @Override
////        public void run() {
////            System.out.println("Task number: " + i);
////        }
////    }
////
////    private static class RunTaskCall implements Callable<Integer> {
////        private int i = 0;
////
////        public void addN(int n){
////            i += n;
////        }
////
////        @Override
////        public Integer call() throws Exception {
////            sleep(1000);
////            return i;
////        }
////    }
////}
//










package threadpool;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.util.concurrent.*;
//import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolTest {
    private ThreadPool tp;

    // public static void main(String[] args) {
    //     System.out.println("khg");
    // }

    @Before
    public void initTP() {
        tp = new ThreadPool(10);
    }

    @Test
    public void submitTest() {
        System.out.println("SubmitTest test!");
        for (int i = 0; i < 10; ++i) {
            RunTask t = new RunTask();
            t.addN(i);
            tp.submit(t, Priority.DEFAULT);
        }
    }

    @Test
    public void testTaskCancellation() throws Exception {
        Callable<Integer> longRunningTask = () -> {
            Thread.sleep(5_000);
            System.out.println("executed");
            return 17;
        };
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        tp.submit(longRunningTask);
        Future<Integer> future = tp.submit(longRunningTask, Priority.LOW);
        assertNotNull(future);
        assertFalse(future.isCancelled());
        Thread.sleep(1_000);
        boolean canceled = future.cancel(false);

        assertTrue(canceled);
        assertTrue(future.isCancelled());
    }

    @Test
    public void futureTest() throws InterruptedException, ExecutionException {
        ArrayList<Future<Integer>> futureContainer = new ArrayList<>();

        for (int i = 0; i < 20; ++i) {
            RunTaskCall t = new RunTaskCall();
            t.addN(i);
            if (i == 13 || i == 16) {
                tp.pauseTP();
            }

            futureContainer.add(tp.submit(t, Priority.DEFAULT));
            if (i == 13 || i == 16) {
                futureContainer.get(i).cancel(true);
                sleep(2000);
                assertTrue(futureContainer.get(i).isCancelled());
                tp.resumeTP();
            }

            if (i == 15) {
                //assertFalse(futureContainer.get(15).isDone());
                sleep(3000);
                assertTrue(futureContainer.get(0).isDone());
                assertTrue(futureContainer.get(5).isDone());
            }
        }
        sleep(1000);
        for (int i = 0; i < 20; ++i) {
            if (i == 13 || i == 16) {
                ++i;
            }
            assertEquals(i, futureContainer.get(i).get());
        }
    }

    /*    @Test
        public void futureGetTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
            ArrayList<Future<Integer>> futureContainer = new ArrayList<>();
            int numOfEx = 0;
    
            for(int i = 0; i < 20; ++i){
                RunTaskCall t = new RunTaskCall();
                t.addN(i);
    
                if(i == 10){
                    futureContainer.add(tp.submit(new Callable<Integer>() {
                        private int i = 10;
    
                        public void addN(int n){
                            i += n;
                        }
    
                        @Override
                        public Integer call() throws Exception {
                            sleep(15000);
                            return i;
                        }
                    }, Priority.DEFAULT));
                }
                else {
                    futureContainer.add(tp.submit(t, Priority.DEFAULT));
                }
            }
    
            sleep(5000);
            for(int i = 0; i < 20; ++i){
                try {
                    assertEquals(i, futureContainer.get(i).get(3, TimeUnit.SECONDS));
                } catch (TimeoutException e){
                    //e.printStackTrace();
                    System.out.println("Timeout in get worked");
                    ++numOfEx;
                }
            }
            System.out.println(numOfEx);
        }
    */
    @Test
    public void futureGetTimeoutTest2() {
        ArrayList<Future<Integer>> futureContainer = new ArrayList<>();

        for (int i = 0; i < 21; ++i) {
            RunTaskCall t = new RunTaskCall();
            t.addN(i);

            if (i % 10 == 0 && i > 0) {
                RunTaskCall t2 = new RunTaskCall() {
                    @Override
                    public Integer call() throws Exception {
                        sleep(15000);
                        return super.i;
                    }
                };
                t2.addN(i);
                futureContainer.add(tp.submit(t2, Priority.DEFAULT));
            } else {
                futureContainer.add(tp.submit(t, Priority.DEFAULT));
            }
        }

        for (int i = 0; i < 21; ++i) {
            try {
                assertEquals(i, futureContainer.get(i).get(5, TimeUnit.SECONDS));
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                System.out.println("Timeout in get worked");
            }
        }
    }

    @Test
    public void SetMoreThreads() throws InterruptedException {
        System.out.println("SetMoreThreads test!");
        assertEquals(10, tp.getNumOfThreads());
        for (int i = 0; i < 11; ++i) {
            RunTask t = new RunTask();
            t.addN(i);
            tp.submit(t, Priority.DEFAULT);
            if (i == 7) {
                tp.setNumOfThreads(12);
            }
        }
        assertEquals(12, tp.getNumOfThreads());
    }

    @Test
    public void SetLessThreads() throws InterruptedException {
        System.out.println("SetLessThreads test!");
        assertEquals(10, tp.getNumOfThreads());
        for (int i = 0; i < 11; ++i) {
            RunTask t = new RunTask();
            t.addN(i);
            tp.submit(t, Priority.DEFAULT);
            if (i == 7) {
                tp.setNumOfThreads(6);
            }
        }

        assertEquals(6, tp.getNumOfThreads());
    }

    @Test
    public void PauseResumeTest() {
        //Need to look for 3 elements after pause
        System.out.println("Pause Resume test!");
        for (int i = 0; i < 11; ++i) {
            RunTask t = new RunTask();
            t.addN(i);
            tp.submit(t, Priority.DEFAULT);
            if (i == 7) {
                tp.pauseTP();
            }
        }
        tp.resumeTP();
    }

    @Test
    public void ShutdownTest() throws InterruptedException {
        System.out.println("Shutdown test!");
        for (int i = 0; i < 11; ++i) {
            RunTask t = new RunTask();
            t.addN(i);
            tp.submit(t, Priority.DEFAULT);
        }

        tp.shutDown();
        try {
            RunTask t = new RunTask();
            t.addN(11);
            tp.submit(t, Priority.DEFAULT);
        } catch (RejectedExecutionException e) {
            assertInstanceOf(RejectedExecutionException.class, e);
        }
    }

    @Test
    public void AwaitTerminationTest() throws InterruptedException {
        System.out.println("AwaitTermination test!");
        for (int i = 0; i < 11; ++i) {
            RunTask t = new RunTask();
            t.addN(i);
            tp.submit(t, Priority.DEFAULT);
        }

        RunTaskCall t2 = new RunTaskCall() {
            @Override
            public Integer call() throws Exception {
                sleep(4000);
                return super.i;
            }
        };
        t2.addN(11);
        tp.submit(t2, Priority.DEFAULT);

        tp.shutDown();
        tp.awaitTermination();

        assertEquals(0, tp.getNumOfThreads()); // it fine it not working
    }

    @Test
    public void AwaitTerminationTimeoutTest() throws InterruptedException {
        System.out.println("AwaitTermination test!");
        for (int i = 0; i < 11; ++i) {
            RunTask t = new RunTask();
            t.addN(i);
            tp.submit(t, Priority.DEFAULT);
        }

        RunTaskCall t2 = new RunTaskCall() {
            @Override
            public Integer call() throws Exception {
                sleep(1500000);
                return super.i;
            }
        };
        t2.addN(11);
        tp.submit(t2, Priority.DEFAULT);
        tp.shutDown();

        System.out.println("Before termineion");
      //  assertFalse(tp.awaitTermination(1000));

    //    assertEquals(10, tp.getNumOfThreads());
    }

    private static class RunTask implements Runnable {
        private int i = 0;

        public void addN(int n) {
            i += n;
        }

        @Override
        public void run() {
            System.out.println("Task number: " + i);
        }
    }

    private static class RunTaskCall implements Callable<Integer> {
        private int i = 0;

        public void addN(int n) {
            i += n;
        }

        @Override
        public Integer call() throws Exception {
            sleep(1000);
            return i;
        }
    }
}

