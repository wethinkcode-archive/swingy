package com.swingy.utils.database;

import com.swingy.model.character.Hero;
import com.swingy.model.character.CharacterType;
import com.swingy.utils.factory.HeroFactory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO debug
import static com.swingy.utils.Colors.*;
import static com.swingy.utils.Log.log;

//TODO convert to wrapper
public class DatabaseWrapper {
    private static DatabaseWrapper instance;
    private static Connection conn = Conn.getConnection();
    private DatabaseWrapper() {}

    public static DatabaseWrapper getInstance() {
        if (instance == null) {
            instance = new DatabaseWrapper();
        }
        return instance;
    }

    public void setupDatabase() {
//        if (!dbExists("Swingy.db")) {
//            System.out.println("creating database");
//            createDatabase();
//            createTable();
//        } else {
//            System.out.println("database exists");
//
//        }
        createTable();
    }

    private void createTable() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS heroes (\nheroID INTEGER PRIMARY KEY," +
                "\nheroName TEXT NOT NULL UNIQUE ,\nheroClass TEXT NOT NULL ,\nheroLevel INTEGER NOT NULL ," +
                "\nheroExperience INTEGER NOT NULL ,\nheroHP INTEGER NOT NULL ,\nheroAttack INTEGER NOT NULL ," +
                "\nheroDefense INTEGER NOT NULL \n );");

        try {
            //create table
            Statement stmt = conn.createStatement();
//            TODO check to see if table was created
            stmt.executeUpdate(sql);
            System.out.println("table added");
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\nError: cannot create table");
            System.exit(-1);
        }
    }

    private void createDatabase() {
        try {
            if (conn != null){
                DatabaseMetaData dbMeta = conn.getMetaData();
                System.out.println("meta is " + dbMeta);
                System.out.println("A new database has been created: Swingy.db");
            }
        } catch(SQLException e){
            System.out.println(e.getMessage() + "\nError: cannot create database");
            System.exit(0);
        }
    }

    private boolean dbExists(String dbName) {
        File dbFile = new File (dbName);
        return dbFile.exists();
    }

    public boolean heroExists(String name) throws SQLException {
        boolean exists = false;
        String sql = "SELECT * FROM heroes WHERE heroName='" + name + "'";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        if (resultSet.next()) {
            exists = true;
        }
        return exists;
    }

    public void insertHero(Hero newHero) throws SQLException {
        String sql = "INSERT INTO heroes (" +
                "heroName, heroClass, " +
                "heroLevel, heroExperience, " +
                "heroHP, heroAttack, heroDefense) " +
                "VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newHero.getName());
        pstmt.setString(2, newHero.getType());
        pstmt.setInt(3, newHero.getLevel());
        pstmt.setInt(4, newHero.getExperience());
        pstmt.setInt(5, newHero.getHitPoints());
        pstmt.setInt(6, newHero.getAttack());
        pstmt.setInt(7, newHero.getDefense());

        pstmt.execute();
//        TODO return true or false
        //        if (pstmt.execute()) {
//            System.out.println("(Remove) Failed for create Hero");
//        } else {
//            System.out.println("(Remove) Hero Created");
//        }
    }

    public int numberOfHeroes() throws SQLException {
        int rowCount = 0;
        String sql = "SELECT * FROM heroes";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next())
            rowCount++;
        return rowCount;
    }

    public List<Hero> retrieveAllHeroes() throws SQLException {
        String sql = "SELECT * FROM heroes";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        return (parseResult(resultSet));
    }

//    TODO remove
//    public ArrayList<ArrayList<Object>> parseResultset(ResultSet resultSet) throws SQLException {
//        ArrayList<ArrayList<Object>> resultList = new ArrayList<>();
//        while (resultSet.next()) {
//            ArrayList<Object> row =  new ArrayList<>();
//            row.add(resultSet.getString("heroName"));
//            row.add(resultSet.getString("heroClass"));
//            row.add(resultSet.getString("heroAttack"));
//            row.add(resultSet.getString("heroDefense"));
//            row.add(resultSet.getString("heroExperience"));
//            row.add(resultSet.getString("heroHP"));
//            row.add(resultSet.getString("heroLevel"));
//            resultList.add(row);
//        }
//        return resultList;
//    }

//    TODO use prepared statement
    public Hero retrieveHeroData(String name) throws SQLException {
        String sql = String.format("SELECT * FROM heroes WHERE heroName='%s'", name);
        Statement stmt = conn.createStatement();
        ResultSet resultSet =  stmt.executeQuery(sql);
        Hero hero = parseResult(resultSet).get(0);
        return hero;
    }

//    TODO handle resultset validation
    private List<Hero> parseResult(ResultSet rs) throws SQLException {
        List<Hero> list = new ArrayList<>();
        CharacterType type = null;
        while (rs.next()) {
            String heroType = rs.getString("heroClass").toUpperCase();
            switch (heroType) {
                case "DEADPOOL":
                    type = CharacterType.DEADPOOL;
                    break;
                case "THOR":
                    type = CharacterType.THOR;
                    break;
                case "WOLVERINE":
                    type = CharacterType.WOLVERINE;
                    break;
            }
            assert type != null;
            Hero hero = HeroFactory.newHero(rs.getString("heroName"), type);
            hero.setId(rs.getInt("heroID"));
            hero.setAttack(rs.getInt("heroAttack"));
            hero.setDefense(rs.getInt("heroDefense"));
            hero.setExperience(rs.getInt("heroExperience"));
            hero.setHitPoints(rs.getInt("heroHP"));
            hero.setLevel(rs.getInt("heroLevel"));
            list.add(hero);
        }
        return list;
    }

//    TODO use prepared statement
    public void updateHero(Hero hero) throws SQLException {
//        String sql = "UPDATE heroes SET heroAttack=, heroDefense, "
        String sql = String.format("UPDATE heroes " +
                "SET heroAttack='%s', " +
                "heroDefense='%s', " +
                "heroExperience='%s', " +
                "heroHP='%s', " +
                "heroLevel='%s' WHERE heroName='%s'", hero.getAttack(), hero.getDefense(), hero.getExperience(), hero.getHitPoints(), hero.getLevel(), hero.getName());
        Statement stmt = conn.createStatement();
        ResultSet resultSet =  stmt.executeQuery(sql);
    }

    public List<Hero> retrieveDatabase() {
        return new ArrayList<Hero>();
    }
}
