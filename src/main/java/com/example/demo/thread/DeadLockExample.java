package com.example.demo.thread;

public class DeadLockExample {
    static class Interviewer {
        public synchronized void conductInterview(Candidate candidate) {
            System.out.println("Interviewer: Waiting for candidate to arrive...");
            try {
                Thread.sleep(1000); // Simulating some processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Interviewer: Starting the interview.");
            candidate.interview();
        }

        public synchronized void thankYou() {
            System.out.println("Interviewer: Thank you for the interview.");
        }
    }

    static class Candidate {
        public synchronized void attendInterview(Interviewer interviewer) {
            System.out.println("Candidate: Waiting for the interview to start...");
            try {
                Thread.sleep(1000); // Simulating some processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Candidate: Interview has started.");
            interviewer.thankYou();
        }

        public synchronized void interview() {
            System.out.println("Candidate: Conducting the interview.");
        }
    }

    public static void main(String[] args) {
        Interviewer interviewer = new Interviewer();
        Candidate candidate = new Candidate();

        Thread thread1 = new Thread(() -> interviewer.conductInterview(candidate));
        Thread thread2 = new Thread(() -> candidate.attendInterview(interviewer));

        thread1.start();
        thread2.start();

        // Wait for threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main: Execution completed.");
    }
}
