package gdbm;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GDBMTest {
    public static void main(String[] args) throws SQLException {
        GDBM gdbm = new GDBM("TestGDBM", "jdbc:mysql://localhost:3306", "root", "12345");
        List<Map.Entry<DataTypes, String>> values = new ArrayList<>();
        values.add(new AbstractMap.SimpleEntry<>(DataTypes.INT, "id"));
        values.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "name"));
        values.add(new AbstractMap.SimpleEntry<>(DataTypes.VARCHAR, "email"));
        values.add(new AbstractMap.SimpleEntry<>(DataTypes.INT, "age"));

        List<String> varCharsLengths = new ArrayList<>();
        varCharsLengths.add("100");
        varCharsLengths.add("100");

        gdbm.createTable("TableTest", values, varCharsLengths);

        ArrayList<String> data = new ArrayList<>();
        data.add("545");
        data.add("'hjlhl'");
        data.add("'hjlkhlj@kjfdb'");
        data.add("724");
        gdbm.insertRecord("TableTest", data);

        List<List<String>> result = gdbm.getRecords("TableTest");
        for(List<String> col : result){
            System.out.println(col + " ");
        }
        System.out.println();

//        gdbm.deleteRecords("TableTest");

        result = gdbm.getRecords("TableTest");
        for(List<String> col : result){
            System.out.println(col + " ");
        }
        System.out.println();

        gdbm.updateRecord("TableTest", "id", "545", "5");

        result = gdbm.getRecords("TableTest");
        for(List<String> col : result){
            System.out.println(col + " ");
        }
        System.out.println();
    }
}
