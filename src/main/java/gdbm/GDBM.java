package gdbm;

import java.sql.*;
import java.util.*;

public class GDBM {
    Connection connection = null;

    public GDBM(String DBName, String jdbcURL, String username, String password) throws SQLException {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + DBName;
        String useDatabaseSQL = "USE " + DBName;
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(createDatabaseSQL);
                stmt.executeUpdate(useDatabaseSQL);
            }
        } catch (SQLException e) {
            close();
            throw new SQLException(e);
        }
    }

    public void createTable(String name, List<Map.Entry<DataTypes, String>> values, List<String> varCharsLengths, String primaryKey, String foreignKey, String referencedTable, String referencedAttribute) throws SQLException {
        StringBuilder createTableSQL = new StringBuilder();
        Iterator<String> iter = varCharsLengths.iterator();

        // Start building the CREATE TABLE statement
        createTableSQL.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
        for (Map.Entry<DataTypes, String> col : values) {
            createTableSQL.append(col.getValue()).append(" ").append(col.getKey().name());
            if (col.getKey() == DataTypes.VARCHAR) {
                createTableSQL.append("(").append(iter.next()).append(")");
            }

            // Add PRIMARY KEY constraint if this column matches the primary key
            if (Objects.equals(primaryKey, col.getValue())) {
                createTableSQL.append(" PRIMARY KEY");
            }

            createTableSQL.append(", ");
        }

        // Add the FOREIGN KEY constraint
        if (foreignKey != null && referencedTable != null && referencedAttribute != null) {
            createTableSQL.append("FOREIGN KEY (").append(foreignKey).append(") ")
                    .append("REFERENCES ").append(referencedTable).append("(").append(referencedAttribute).append("), ");
        }

        // Remove trailing comma and space, and close the statement
        createTableSQL = new StringBuilder(createTableSQL.substring(0, createTableSQL.length() - 2));
        createTableSQL.append(")");
        System.out.println(createTableSQL);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL.toString());
        }
    }

    public void createTable(String name, List<Map.Entry<DataTypes, String>> values, List<String> varCharsLengths, String foreignKey, String referencedTable, String referencedAttribute) throws SQLException {
        createTable(name, values, varCharsLengths, null, foreignKey, referencedTable, referencedAttribute);
    }

    public void createTable(String name, List<Map.Entry<DataTypes, String>> values, String primaryKey, List<String> varCharsLengths) throws SQLException {
        createTable(name, values, varCharsLengths, primaryKey, null, null, null);
    }

    public void createTable(String name, List<Map.Entry<DataTypes, String>> values, List<String> varCharsLengths) throws SQLException {
        createTable(name, values, varCharsLengths, null, null, null, null);
    }

    public void insertRecord(String table, List<String> data) throws SQLException {
        StringBuilder insertValuesSQL = new StringBuilder();
        insertValuesSQL.append("INSERT INTO ").append(table).append(" VALUES (");
        for (String att : data) {
            insertValuesSQL.append(att).append(", ");
        }

        insertValuesSQL = new StringBuilder(insertValuesSQL.substring(0, insertValuesSQL.length() - 2));
        insertValuesSQL.append(")");

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertValuesSQL.toString());
            System.out.println(insertValuesSQL);
        }
    }

    public List<List<String>> getRecords(String table) throws SQLException {
        StringBuilder fetchRecordsSQL = new StringBuilder();
        fetchRecordsSQL.append("SELECT * ").append(" FROM ").append(table);

        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(fetchRecordsSQL.toString());
            return fetchingRecords(resultSet);
        }
    }

    public List<List<String>> getRecords(String table, String attribute) throws SQLException {
        StringBuilder fetchRecordsSQL = new StringBuilder();
        fetchRecordsSQL.append("SELECT ").append(attribute).append(" FROM ").append(table);

        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(fetchRecordsSQL.toString());
            return fetchingRecords(resultSet);
        }
    }

    public List<List<String>> getRecords(String table, String attribute, String val) throws SQLException {
        StringBuilder fetchRecordsSQL = new StringBuilder();
        fetchRecordsSQL.append("SELECT * ").append(" FROM ").append(table);
        fetchRecordsSQL.append(" WHERE ").append(attribute).append(" = ").append(val);

        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(fetchRecordsSQL.toString());
            return fetchingRecords(resultSet);
        }
    }

    public void deleteRecords(String table) throws SQLException {
        StringBuilder deleteRecordsSQL = new StringBuilder();
        deleteRecordsSQL.append("DELETE ").append(" FROM ").append(table);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteRecordsSQL.toString());
        }
    }

    public void deleteRecords(String table, String attribute, String val) throws SQLException {
        StringBuilder deleteRecordsSQL = new StringBuilder();
        deleteRecordsSQL.append("DELETE ").append(" FROM ").append(table);
        deleteRecordsSQL.append(" WHERE ").append(attribute).append(" = ").append(val);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteRecordsSQL.toString());
        }
    }

    public void dropTable(String table) throws SQLException {
        StringBuilder dropSQL = new StringBuilder();
        dropSQL.append("DROP TABLE IF EXISTS ").append(table);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(dropSQL.toString());
        }
    }

    public void updateRecord(String table, String attribute, String val, String newVal) throws SQLException {
        StringBuilder fetchRecordsSQL = new StringBuilder();
        fetchRecordsSQL.append("UPDATE ").append(table).append(" SET ");
        fetchRecordsSQL.append(attribute).append(" = ").append(newVal);
        fetchRecordsSQL.append(" WHERE ").append(attribute).append(" = ").append(val);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(fetchRecordsSQL.toString());
            System.out.println(fetchRecordsSQL);
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

    private List<List<String>> fetchingRecords(ResultSet resultSet) throws SQLException {
        List<List<String>> records = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            List<String> row = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnValue = resultSet.getString(i);
                row.add(columnValue);
            }
            records.add(row);
        }
        return records;
    }

}
