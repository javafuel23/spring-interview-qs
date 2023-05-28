package com.example.demo.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockResolvedExample {
    static class Interviewer {
        private Lock lock = new ReentrantLock();

        public void conductInterview(Candidate candidate) throws InterruptedException {
            System.out.println("Interviewer: Waiting for candidate to arrive...");
            while (!lock.tryLock()) {
                Thread.sleep(1000); // Give up the lock temporarily
            }
            try {
                System.out.println("Interviewer: Starting the interview.");
                candidate.interview();
            } finally {
                lock.unlock();
            }
        }

        public void thankYou() {
            System.out.println("Interviewer: Thank you for the interview.");
        }
    }

    static class Candidate {
        private Lock lock = new ReentrantLock();

        public void attendInterview(Interviewer interviewer) throws InterruptedException {
            System.out.println("Candidate: Waiting for the interview to start...");
            while (!lock.tryLock()) {
                Thread.sleep(1000); // Give up the lock temporarily
            }
            try {
                System.out.println("Candidate: Interview has started.");
                interviewer.thankYou();
            } finally {
                lock.unlock();
            }
        }

        public void interview() {
            System.out.println("Candidate: Conducting the interview.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Interviewer interviewer = new Interviewer();
        Candidate candidate = new Candidate();

        Thread thread1 = new Thread(() -> {
            try {
                interviewer.conductInterview(candidate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                candidate.attendInterview(interviewer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Main: Execution completed.");
    }
}
