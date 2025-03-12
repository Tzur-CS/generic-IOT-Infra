package gdbm;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBSetup {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/";
        String dbName = "TestDB";
        String username = "root";
        String password = "12345";

        try {
            // Initialize GDBM and create database
            GDBM gdbm = new GDBM(dbName, jdbcURL, username, password);

            gdbm.dropTable("ContactInfo");
            gdbm.dropTable("Products");
            gdbm.dropTable("Company");


            // Create Company table
            List<Map.Entry<DataTypes, String>> companyColumns = new ArrayList<>();
            List<String> companyLengths = new ArrayList<>();
            companyColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "CompanyID"));
            companyLengths.add("16");
            companyColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "CompanyName"));
            companyLengths.add("32");
            companyColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "CompanyAddress"));
            companyLengths.add("64");
            companyColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "SubscriptionPlan"));
            companyLengths.add("16");

            gdbm.createTable("Company", companyColumns, "CompanyID", companyLengths);


            // Create ContantInfo table
            List<Map.Entry<DataTypes, String>> contantInfoColumns = new ArrayList<>();
            List<String> contantInfoLengths = new ArrayList<>();
            contantInfoColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "CompanyID"));
            contantInfoLengths.add("16");
            contantInfoColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "Name"));
            contantInfoLengths.add("30");
            contantInfoColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "Email"));
            contantInfoLengths.add("64");
            contantInfoColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "PhoneNumber"));
            contantInfoLengths.add("16");
            contantInfoColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "AgentID"));
            contantInfoLengths.add("16");

            gdbm.createTable(
                    "ContactInfo",
                    contantInfoColumns,
                    contantInfoLengths,
                    "AgentID",
                    "CompanyID",
                    "Company",
                    "CompanyID"
            );

            // Create Products table
            List<Map.Entry<DataTypes, String>> productsColumns = new ArrayList<>();
            List<String> productsLengths = new ArrayList<>();
            productsColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "CompanyID"));
            productsLengths.add("16");
            productsColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "ProductName"));
            productsLengths.add("32");
            productsColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "ProductID"));
            productsLengths.add("16");
            productsColumns.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "ProductDescription"));
            productsLengths.add("64");

            gdbm.createTable(
                    "Products",
                    productsColumns,
                    productsLengths,
                    "ProductID",
                    "CompanyID",
                    "Company",
                    "CompanyID"
            );

            System.out.println("Database setup completed!");

            // Insert 5 companies
            insertCompanies(gdbm);

            // Insert 10 products
            insertProducts(gdbm);

            // Insert 7 agents, 3 from the same company
            insertAgents(gdbm);

            System.out.println("Data inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertCompanies(GDBM gdbm) throws SQLException {
        for (int i = 1; i <= 5; i++) {
            List<String> companyData = new ArrayList<>();
            companyData.add("'C00" + i + "'"); // CompanyID
            companyData.add("'Company " + i + "'"); // CompanyName
            companyData.add("'Address " + i + "'"); // Name
            companyData.add("'Plan" + (i % 3 + 1) + "'"); // SubscriptionPlan
            gdbm.insertRecord("Company", companyData);
        }
        System.out.println("5 Companies inserted.");
    }

    private static void insertProducts(GDBM gdbm) throws SQLException {
        for (int i = 1; i <= 10; i++) {
            List<String> productData = new ArrayList<>();
            productData.add("'C00" + (i % 5 + 1) + "'"); // CompanyID (rotate between 5 companies)
            productData.add("'Product " + i + "'"); // ProductName
            productData.add("'P00" + i + "'"); // ProductID
            productData.add("'ProductDescription " + i + "'"); // ProductDescription
            gdbm.insertRecord("Products", productData);
        }
        System.out.println("10 Products inserted.");
    }

    private static void insertAgents(GDBM gdbm) throws SQLException {
        for (int i = 1; i <= 7; i++) {
            List<String> agentData = new ArrayList<>();
            agentData.add("'C00" + ((i <= 3) ? 1 : (i % 5 + 1)) + "'"); // CompanyID (first 3 agents belong to Company 1)
            agentData.add("'Agent " + i + "'"); // Name
            agentData.add("'agent" + i + "@example.com'"); // Email
            agentData.add("'123-456-78" + i + "'"); // PhoneNumber
            agentData.add("'A00" + i + "'"); // AgentID
            gdbm.insertRecord("ContactInfo", agentData);
        }
        System.out.println("7 Agents inserted.");
    }
}
