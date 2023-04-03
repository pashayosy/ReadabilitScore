package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        textShow(args[0]);
        File f = new File(args[0]);
        Scanner in = null;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println(e + ": The file not found");
            System.exit(0);
        }
        double ageSum = 0;
        String text = "";
        double wordSumAmount = 0;
        double wordAmount = 0;
        double sentensAmount = 0;
        double characterAmount = 0;
        double automatedReadabilityIndex = 0;
        double fleschIndex = 0;
        double simpleIndex = 0;
        double colemanIndex = 0;
        int counter = 0;
        char ch = 0;

         while (in.hasNext()) {
             text = in.nextLine();
             while (counter < text.length()) {
                 ch = text.charAt(counter);
                 if (Character.toString(ch).matches("[\\.!?]") || counter == text.length() - 1) {
                     sentensAmount++;
                     wordSumAmount += wordAmount + 1;
                     wordAmount = 0;
                 }

                 if (ch == ' ') {
                     if (!Character.toString(text.charAt(counter - 1)).matches("[\\.!?]"))
                         wordAmount++;
                 }

                 if (Character.toString(ch).matches("[^\s]")) {
                     characterAmount++;
                 }
                 counter++;
             }
             counter = 0;
         }

        check(wordSumAmount, sentensAmount, characterAmount);
        automatedReadabilityIndex = 4.71 * (characterAmount / wordSumAmount) + 0.5 * (wordSumAmount / sentensAmount) - 21.43;
        fleschIndex = fleschKincaidReadability(wordSumAmount, sentensAmount, args[0]);
        simpleIndex = simpleMeasureOfGobbledygook(sentensAmount, args[0]);
        colemanIndex = colemanLiauIndex(wordSumAmount, sentensAmount, characterAmount);

        Scanner scanner = new Scanner(System.in);
        String choose;
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        choose = scanner.next();
        System.out.println("\n");

         switch (choose) {
             case "ARI":
                 getAge("Automated Readability Index", automatedReadabilityIndex);
                 break;
             case "FK":
                 getAge("Flesch–Kincaid readability tests", fleschIndex);
                 break;
             case "SMOG":
                 getAge("Simple Measure of Gobbledygook", simpleIndex);
                 break;
             case "CL":
                 getAge("Coleman–Liau index", colemanIndex);
                 break;
             case "all":
                 ageSum += getAge("Automated Readability Index", automatedReadabilityIndex);
                 ageSum += getAge("Flesch–Kincaid readability tests", fleschIndex);
                 ageSum += getAge("Simple Measure of Gobbledygook", simpleIndex);
                 ageSum += getAge("Coleman–Liau index", colemanIndex);
                 ageSum = ageSum / 4;
                 System.out.printf("This text should be understood in average by %.2f-year-olds.",ageSum);
                 break;
         }
    }

    public static void check(double wordSumAmount, double sentensAmount, double characterAmount) {
        System.out.println("Words: " + (int)wordSumAmount);
        System.out.println("Sentences: " + (int)sentensAmount);
        System.out.println("Characters: " + (int)characterAmount);
    }

    public static void textShow(String arg) {
        File f = new File(arg);
        Scanner in = null;
        String text;

        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println(e + ": The file not found");
        }

        System.out.println("The text is:");
        while (in.hasNext()) {
            text = in.nextLine();
            System.out.print(text);
        }
        System.out.println("\n");
    }

    public static int getAge(String algoritm, double averageScore) {
        int age;
        switch ((int) Math.ceil(averageScore)) {
            case 1:
                age = 6;
                break;
            case 2:
                age = 7;
                break;
            case 3:
                age = 8;
                break;
            case 4:
                age = 9;
                break;
            case 5:
                age = 10;
                break;
            case 6:
                age = 11;
                break;
            case 7:
                age = 12;
                break;
            case 8:
                age = 13;
                break;
            case 9:
                age = 14;
                break;
            case 10:
                age = 15;
                break;
            case 11:
                age = 16;
                break;
            case 12:
                age = 17;
                break;
            case 13:
                age = 18;
                break;
            default:
                age = 22;
                break;
        }

        System.out.printf("%s : %.2f (about %d-year-olds).\n", algoritm, averageScore, age);
        return age;
    }

    public static double fleschKincaidReadability(double wordSumAmount, double sentensAmount, String arg) {
        int syllables = 0;
        double score;
        File f = new File(arg);
        Scanner in = null;
        String text;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println(e + ": The file not found");
        }

        while (in.hasNext()) {
            text = in.next();
            syllables += Math.max(1, text.toLowerCase().replaceAll("e$", "")
                                                        .replaceAll("[aeiouy]{2}", "a")
                                                        .replaceAll("[aeiouy]{2}", "a")
                                                        .replaceAll("[^aeiouy]", "").length());
        }
        System.out.println("Syllables: " + syllables);
        score = 0.39 * (wordSumAmount / sentensAmount) + 11.8 * (syllables/wordSumAmount) - 15.59;
        return score;
    }

    public static double simpleMeasureOfGobbledygook(double sentensAmount, String arg) {
        File f = new File(arg);
        double score;
        int syllables;
        int polysyllables = 0;
        Scanner in = null;
        String word;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println(e + ": The file not found");
        }

        while (in.hasNext()) {
            word = in.next();
            syllables = Math.max(1, word.toLowerCase().replaceAll("e$", "")
                    .replaceAll("[aeiouy]{2}", "a")
                    .replaceAll("[aeiouy]{2}", "a")
                    .replaceAll("[^aeiouy]", "").length());

            if (syllables > 2) {
                polysyllables++;
            }
        }
        System.out.println("Polysyllables: " + polysyllables);
        score = 1.043 * Math.sqrt(polysyllables * 30 / sentensAmount) + 3.1291;
        return score;
    }

    public static double colemanLiauIndex(double words, double sentens, double characters) {
        double score;
        double L;
        double S;
        L= characters / words * 100;
        S= sentens / words * 100;
        score = 0.0588 * L - 0.296 * S - 15.8;
        return score;
    }
}
