
package com.example.demo.thread;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@Data
@AllArgsConstructor
class Employee {
    private int id;
    private String name;
    private double salary;


    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}

class EmployeeProcessor implements Callable<List<Employee>> {
    private List<Employee> employees;

    public EmployeeProcessor(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public List<Employee> call() throws Exception {
        List<Employee> filteredEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getSalary() > 80000) {
                filteredEmployees.add(employee);
            }
        }
        return filteredEmployees;
    }
}

public class EmployeeThreadExample {
    public static void main(String[] args) {
        // Generate a list of 1000 random employees
        List<Employee> employees = generateRandomEmployees(1000);

        // Create an ExecutorService with 5 threads
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // Divide the employee list into 5 parts
        int partitionSize = employees.size() / 5;
        List<Callable<List<Employee>>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int startIndex = i * partitionSize;
            int endIndex = (i == 4) ? employees.size() : (i + 1) * partitionSize;
            List<Employee> subList = employees.subList(startIndex, endIndex);
            EmployeeProcessor processor = new EmployeeProcessor(subList);
            tasks.add(processor);
        }

        try {
            // Execute the tasks and retrieve the results
            List<Future<List<Employee>>> results = executorService.invokeAll(tasks);

            // Collect and print the filtered employees
            List<Employee> filteredEmployees = new ArrayList<>();
            for (Future<List<Employee>> result : results) {
                filteredEmployees.addAll(result.get());
            }
            for (Employee employee : filteredEmployees) {
                System.out.println(employee);
            }

            // Shutdown the executor service
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> generateRandomEmployees(int count) {
        List<Employee> employees = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int id = i + 1;
            String name = "Employee" + id;
            double salary = random.nextDouble() * 100000;
            employees.add(new Employee(id, name, salary));
        }
        return employees;
    }


}

