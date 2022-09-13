package Assignment1;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.lang.*;
import java.math.BigDecimal;
import java.math.MathContext;

public class AppLogic{
    private static Scanner scan = new Scanner(System.in);
    // A list of all the currencies in the program
    // Each currency is saved as a LinkedHashMap to maintain order of exchange rates
    private static List<LinkedHashMap<String, Double>> currencies = new ArrayList<LinkedHashMap<String, Double>>();
    // A list of the popular currencies. Can be updated by admin
    private static List<String> popCurrencies = new ArrayList<String>(){
        {
            add("USD");
            add("AUD");
            add("SGD");
            add("EUR");
        }
    };

    // Initial choices for admin and users
    public static void selectUserOption(String userType, Scanner scan) {
        if (userType.equals("1")) {
            // run code for user
            String input = UserInterface.getString(Arrays.asList("1", "2", "3"), "User, what would you like to do?\n1: Convert currencies\n2: Display currency table\n3: Summary of 2 currencies", scan);

            switch (input) {
                case "1":
                    // Convert currencies
                    break;
                case "2":
                    AppLogic.displayCurrencyTable();
                    break;
                case "3":
                    // Summary of two currencies
                    break;
            }
        } else if (userType.equals("2")) {
            // run code for admin
            String input = UserInterface.getString(Arrays.asList("1", "2", "3", "4"), "Administrator, what would you like to do?\n1: Convert currencies\n2: Display currency table\n" +
                    "3: Summary of 2 currencies\n4: ", scan);

            switch (input) {
                case "1":
                    // Convert currencies
                    break;
                case "2":
                    UserInterface.adminDisplayTable(scan, popCurrencies, currencies);
                    break;
                case "3":
                    // Summary of two currencies
                    break;
            }
        }
    }

    // Repeatable method, always run at beginning of program, which reads into the currency folder
    // and adds those files into the currency list attribute.
    public static void readCurrencyFiles(){
        try {
            Scanner currencyFiles = new Scanner(new File("./src/main/java/Assignment1/currencies/currencyFiles.txt"));
            while(currencyFiles.hasNextLine()) {
                String line = currencyFiles.nextLine();
                currencies.add(new LinkedHashMap<String, Double>());
            }
            currencyFiles.close();

            // Loading data into the hashmaps
            currencyFiles = new Scanner(new File("./src/main/java/Assignment1/currencies/currencyFiles.txt"));
            for (HashMap<String, Double> map : currencies) {
                String line = currencyFiles.nextLine();
                Scanner temp = new Scanner(new File(line));
                while (temp.hasNextLine()) {
                    String values = temp.nextLine();
                    String[] arr = values.split(", ");
                    map.put(arr[0], Double.parseDouble(arr[1]));
                }
                temp.close();
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found exception.");
        }
    }

    public static void printOptions() {}

    public static void displayCurrencyTable(){
        // Find the most popular currency hashmaps according to the popular currencies set by admin
        List<LinkedHashMap<String, Double>> mostPop = new ArrayList<>();
        for (LinkedHashMap<String, Double> currency: currencies){
            for (String desiredCurrency: popCurrencies) {
                if ((currency.keySet().contains(desiredCurrency))) {
                    mostPop.add(currency);
                }
            }
        }

        // Find the latest exchange rate of those currencies
        HashMap<String, Double> latestRates = new HashMap<>();
        // AND the 2nd last rate
        HashMap<String, Double> prevRates = new HashMap<>();
        for (LinkedHashMap<String, Double> currency: mostPop){
            // get the latest exchange rate
            String lastLine = String.valueOf(currency.entrySet().toArray()[currency.size() -1]);
            Double exRate = Double.parseDouble(lastLine.split("=")[1]);

            String secondLastLine = String.valueOf(currency.entrySet().toArray()[currency.size() -2]);
            Double prevRate = Double.parseDouble(secondLastLine.split("=")[1]);
            String currName = "";
            // get its name
            for (String name: currency.keySet()){
                currName = name;
                break;
            }
            latestRates.put(currName, exRate);
            prevRates.put(currName, prevRate);


        }

        // Print the first line of the table
        String entireLine = "___________________________________________________________________________";
        System.out.println(entireLine);
        System.out.print("| From/to |");
        for (String currName: latestRates.keySet()){
            System.out.print("      " + currName + "      |");
        }
        System.out.println("\n" + entireLine);
        // Print the comparisons
        for (HashMap.Entry<String,Double> currency: latestRates.entrySet()){
            System.out.print("|   " + currency.getKey() + "   |");
            for (HashMap.Entry<String,Double> otherCurrency: latestRates.entrySet()){
                double epsilon = 0.000001d;
                // If we're dealing with the same currency, then we just want to print a dash
                if (Math.abs(currency.getValue() - otherCurrency.getValue()) < epsilon){
                    System.out.print("       -       |");
                    continue;
                }

                // calculate the current exchange rate and the previous exchange rate so you can compare them
                double exRate = calcExchangeRate(currency.getValue(), otherCurrency.getValue());
                double prevExRate = calcExchangeRate(prevRates.get(currency.getKey()), prevRates.get(otherCurrency.getKey()));

                // round to 6s.f
                BigDecimal bd = new BigDecimal(exRate);
                bd = bd.round(new MathContext(6));
                double rounded = bd.doubleValue();
                String ex = Double.toString(rounded);

                // Change the number's length so that table format is preserved
                if (ex.length() > 7){
                    while (ex.length() > 7){
                        ex = ex.substring(0, ex.length() - 1);
                    }
                }

                if (ex.length() < 7){
                    while (ex.length() < 7){
                        ex += "0";
                    }
                }

                if (prevExRate > exRate){
                    System.out.print("  " + ex + " (D)  |");
                }
                if (prevExRate < exRate){
                    System.out.print("  " + ex + " (I)  |");
                }

            }
            System.out.println("\n"+ entireLine);
        }


    }

    // Returns the Names of all our currencies
    public static ArrayList<String> getAllCurrencies(){
        ArrayList<String> allCurrencyNames = new ArrayList<String>();
        for (LinkedHashMap<String, Double> currency: currencies){
            // Loop once in the currency to retrieve the first key of the currency: its name
            for (String name: currency.keySet()){
                allCurrencyNames.add(name);
                break;
            }

        }
        return allCurrencyNames;
    }

    // Method for getting the exchange rate between 2 currencies
    public static double calcExchangeRate(double toUSDCurrencyA, double toUsdCurrencyB){
        return toUSDCurrencyA / toUsdCurrencyB;
    }

    public static List<LinkedHashMap<String, Double>> getCurrencies(){
        return currencies;
    }

}