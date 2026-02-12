package project1placeholder;

import java.util.Scanner;
import java.io.File;
import java.io.*;
import java.io.PrintWriter;

public class GradeCalculator {

	public static void main(String[] args) throws IOException { 
	
		// initialize variables 
		
		String courseName = "";
		String category1 = "";
		String category2 = "";
		String category3 = "";
		String firstName = "";
		String lastName = "";
		String baseGrade = "";
		String adjustedGrade = "";
		
		int categoryAmount = 0;
		int category1Weight = 0;
		int category2Weight = 0;
		int category3Weight = 0;
		
		double category1Average = 0.0;
		double category2Average = 0.0;
		double category3Average = 0.0;
		
		boolean defaultConfig = false;
		boolean adjustedGrading = false;
		
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("========================================");
		System.out.println("CMSC203 Project 1 - Grade Calculator");
		System.out.println("========================================");
		System.out.println("Loading configuration from gradeconfig.txt ...");
		
		File configFile = new File("gradeconfig.txt");
        boolean configValidity = false;
        
        // check for config and read it
        
        if (configFile.exists()) {
            Scanner configScanner = new Scanner(configFile);
            if (configScanner.hasNextLine()) {
                courseName = configScanner.nextLine();
            }
            if (configScanner.hasNextInt()) {
                categoryAmount = configScanner.nextInt();
                int totalWeight = 0;

                if (categoryAmount >= 1 && configScanner.hasNext()) {
                    category1 = configScanner.next();
                    category1Weight = configScanner.nextInt();
                    totalWeight += category1Weight;
                }

                if (categoryAmount >= 2 && configScanner.hasNext()) {
                    category2 = configScanner.next();
                    category2Weight = configScanner.nextInt();
                    totalWeight += category2Weight;
                }
 
                if (categoryAmount >= 3 && configScanner.hasNext()) {
                    category3 = configScanner.next();
                    category3Weight = configScanner.nextInt();
                    totalWeight += category3Weight;
                }

                // validate total weighting for config
                
                if (totalWeight == 100) {
                    configValidity = true;
                    System.out.println("Configuration loaded successfully.");
                } else {
                    System.out.println("Error: Weights do not add up to 100.");
                }
            }
            configScanner.close();
        } else { 
            System.out.println("Configuration file not found.");
        }
        
        // use default if no config/invalid config
        
        if (!configValidity) {
            System.out.println("Using default configuration: Projects 40, Quizzes 30, Exams 30");
            defaultConfig = true;
            courseName = "CMSC203";
            categoryAmount = 3;
            category1 = "Projects"; 
            category1Weight = 40;
            category2 = "Quizzes";  
            category2Weight = 30;
            category3 = "Exams";    
            category3Weight = 30;
        }
        
        // validate existence of input
        
        File inputFile = new File("grades_input.txt");
        
        if (!inputFile.exists()) {
            System.out.println("Error: grades_input.txt not found.");
            System.exit(0);
        }
        
        // write info
        
        Scanner fileScanner = new Scanner(inputFile);
        firstName = fileScanner.nextLine();
        lastName = fileScanner.nextLine();
        
        for (int i = 0; i < categoryAmount; i++) {
            if (fileScanner.hasNext()) {
                String currentCategory = fileScanner.next();
                int numScores = fileScanner.nextInt();
                double sum = 0;
                
                // summate scores 
                
                for (int j = 0; j < numScores; j++) {
                    sum += fileScanner.nextDouble();
                }
                
                // find average for each category and validate results

                double calculatedAverage = 0;
                if (numScores > 0) {
                    calculatedAverage = sum / numScores;
                } else {
                    calculatedAverage = 0;
                }
                
                // assign average to each category

                if (currentCategory.equalsIgnoreCase(category1)) {
                    category1Average = calculatedAverage;
                } else if (currentCategory.equalsIgnoreCase(category2)) {
                    category2Average = calculatedAverage;
                } else if (currentCategory.equalsIgnoreCase(category3)) {
                    category3Average = calculatedAverage;
                }
            }
        }
        
        fileScanner.close();
        
        // receive user input
        
        String userInput = "";
        boolean validChoice = false;
        
        // validate user input and change grading system based on input
        
        while (!validChoice) {
            System.out.print("Apply +/- grading? (Y/N): ");
            userInput = keyboard.next();
            if (userInput.equalsIgnoreCase("Y")) {
                adjustedGrading = true;
                validChoice = true;
            } else if (userInput.equalsIgnoreCase("N")) {
                adjustedGrading = false;
                validChoice = true;
            }
        }
        
        double overallAverage = (category1Average * category1Weight / 100.0) + (category2Average * category2Weight / 100.0) + (category3Average * category3Weight / 100.0);
        
        // determine grade
        
        if (overallAverage >= 90) baseGrade = "A";
        else if (overallAverage >= 80) baseGrade = "B";
        else if (overallAverage >= 70) baseGrade = "C";
        else if (overallAverage >= 60) baseGrade = "D";
        else baseGrade = "F";
        
        adjustedGrade = baseGrade;
        
        // determine and apply grading adjustment
        
        if (adjustedGrading && !baseGrade.equals("F")) {
            double decimal = overallAverage - (int)overallAverage;
            if (decimal >= 0.75) adjustedGrade += "+";
            else if (decimal <= 0.25) adjustedGrade += "-";
        }
            
        // display results to user
        
        String report = "\nCourse: " + courseName + "\n" +
                "Student: " + firstName + " " + lastName + "\n" +
                category1 + ": Avg " + String.format("%.2f", category1Average) + " (Weight " + category1Weight + "%)\n" +
                category2 + ": Avg " + String.format("%.2f", category2Average) + " (Weight " + category2Weight + "%)\n" +
                category3 + ": Avg " + String.format("%.2f", category3Average) + " (Weight " + category3Weight + "%)\n" +
                "Overall Numeric Average: " + String.format("%.2f", overallAverage) + "\n" +
                "Final Letter Grade: " + adjustedGrade + "\n" +
                "Default Configuration Used: " + (defaultConfig ? "Yes" : "No");

        System.out.println(report);
        
        // write results to text file and exit
        
        PrintWriter writer = new PrintWriter("grades_report.txt");
        writer.println(report);
        writer.close();
        
        System.out.println("\nSummary written to grades_report.txt\r\n"
        		+ "Program complete. Goodbye!\r\n"
        		+ "");
        
		System.exit(0);
	}
}

/*
 * Class: CMSC203 
 * Instructor: Ahmed Tarek
 * Description: Grade calculator that determines a student's grade based on 
 * their performance in each category and its weight. 
 * Due: 02/11/2026
 * Platform/compiler: Eclipse 
 * I pledge that I have completed the programming assignment 
 * independently. I have not copied the code from a student or any source. I have not given my code to any student.
 * Print your Name here: ARHUM ASIM
*/
