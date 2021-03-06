package com.zipcodewilmington.jdbc.tools.database;

import com.zipcodewilmington.jdbc.tools.database.connection.ResultSetHandler;
import com.zipcodewilmington.jdbc.tools.database.connection.StatementExecutor;
import com.zipcodewilmington.jdbc.tools.testutils.SeedRefresher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.plaf.nimbus.State;
import java.util.Map;

public class DatabaseTableTest {
    private DatabaseTable table;
    private Database database;

    @Before
    public void setup() {
        SeedRefresher.refresh();
        this.database = Database.UAT;
        createPokemonTable(Database.UAT.name());
        this.table = database.getTable("pokemons");
    }

    @Test
    public void limitTest() {
        // Given
        Integer expectedNumberOfRows = 5;

        // When
        ResultSetHandler results = table.limit(expectedNumberOfRows);
        Integer actualNumberOfRows = results.toStack().size();

        // Then
        Assert.assertTrue(actualNumberOfRows <= expectedNumberOfRows);
    }

    @Test
    public void selectTest() {
        // Given
        int expectedNumberOfFields = 2;
        String firstColumn = "name";
        String secondColumn = "secondary_type";
        String columnNames = firstColumn + ", " + secondColumn;

        // When
        ResultSetHandler results = table.select(columnNames);
        Map<String, String> firstRow = results.toStack().pop();
        String firstColumnVal = firstRow.get(firstColumn);
        String secondColumnVal = firstRow.get(secondColumn);
        int actualNumberOfFields = firstRow.size();

        // Then
        Assert.assertEquals(expectedNumberOfFields, actualNumberOfFields);
        Assert.assertNotNull(firstColumnVal);
        Assert.assertNotNull(secondColumnVal);
    }


    @Test
    public void allTest() {
        // Given
        // When
        ResultSetHandler results = table.all();
        Map<String, String> firstRow = results.toStack().pop();
        int actualNumberOfFields = firstRow.size();
        System.out.println(firstRow);
    }

    @Test
    public void toStringTest() {
        System.out.println(table);
    }

    private void createPokemonTable(String databaseName) {
        String sql = "CREATE TABLE IF NOT EXISTS %s.pokemons(id int auto_increment primary key,name text not null,primary_type int not null,secondary_type int null);";
        StatementExecutor executor = new StatementExecutor(database.getConnection());
        executor.execute(sql, databaseName);

    }
}
