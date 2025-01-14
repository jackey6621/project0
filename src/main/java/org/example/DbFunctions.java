package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbFunctions {
    public Connection connect_to_db(String dbname,String user,String pass){
        Connection conn=null;
        try{
            Class.forName("org.postgresql.Driver");
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,user,pass);
            if(conn!=null){
                System.out.println("Connection Established");
            }
            else{
                System.out.println("Connection Failed");
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }
    public void createTableUser(Connection conn, String table_name){
        Statement statement;
        try{
            String query="create table "+table_name+"(id SERIAL,name varchar(200),vehicle integer,primary key(id),foreign key(vehicle) references cars(id) on delete set null);";
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void createTableItem(Connection conn, String table_name){
        Statement statement;
        try{
            String query="create table "+table_name+"(id SERIAL,brand varchar(200),primary key(id));";
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }catch (Exception e){
            System.out.println(e);
        }
    }

//    public void insert_row(Connection conn,String table_name,String name, String address){
//        Statement statement;
//        try {
//            String query=String.format("insert into %s(name,address) values('%s','%s');",table_name,name,address);
//            statement=conn.createStatement();
//            statement.executeUpdate(query);
//            System.out.println("Row Inserted");
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }

    public void insert_row_item(Connection conn,String table_name, String brand){
        Statement statement;
        try {
            String query=String.format("insert into %s(brand) values('%s');",table_name,brand);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Inserted");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void insert_row_user(Connection conn,String table_name, String name, int car_id){
        Statement statement;
        try {
            String query=String.format("insert into %s(name, vehicle) values('%s','%d');",table_name,name,car_id);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Inserted");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public String read_data_item(Connection conn, String table_name){
        Statement statement;
        ResultSet rs=null;
        StringBuilder response = new StringBuilder();
        try {
            String query=String.format("select * from %s",table_name);
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
            response.append("[");
            while (rs.next()) {
                int carId = rs.getInt("id");
                String name = rs.getString("brand");
                response.append("{")
                        .append("\"id\": ").append(carId).append(", ")
                        .append("\"brand\": \"").append(name).append("\"")
                        .append("}, ");
            }
            // Remove the last comma and space
            if (response.length() > 1) {
                response.setLength(response.length() - 2);
            }
            response.append("]");
//            while(rs.next()){
//                System.out.print(rs.getString("id")+" ");
//                System.out.print(rs.getString("brand")+" ");
//                //System.out.println(rs.getString("Address")+" ");
//            }

        }
        catch (Exception e){
            System.out.println(e);
        }
        return response.toString();
    }
    public void update_name(Connection conn,String table_name, String old_name,String new_name){
        Statement statement;
        try {
            String query=String.format("update %s set name='%s' where name='%s'",table_name,new_name,old_name);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Updated");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public int search_by_name(Connection conn, String table_name,String name){
        Statement statement;
        String response = "";
        ResultSet rs=null;
        try {
            String query=String.format("select * from %s where name= '%s'",table_name,name);
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
            while (rs.next()){
                System.out.print(rs.getString("id")+" ");
                System.out.print(rs.getString("name")+" ");
                System.out.println(rs.getString("vehicle"));
                response = rs.getString("vehicle");

            }
        }catch (Exception e){
            System.out.println(e);
        }
        return Integer.valueOf(response);
    }
    public String search_by_id(Connection conn, String table_name,int id){
        Statement statement;
        ResultSet rs=null;
        StringBuilder response = new StringBuilder();
        try {
            String query=String.format("select * from %s where id= %s",table_name,id);
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
            response.append("[");
            while (rs.next()) {
                int carId = rs.getInt("id");
                String name = rs.getString("brand");
                response.append("{")
                        .append("\"id\": ").append(carId).append(", ")
                        .append("\"brand\": \"").append(name).append("\"")
                        .append("}, ");

            }
            // Remove the last comma and space
            if (response.length() > 1) {
                response.setLength(response.length() - 2);
            }
            response.append("]");
//            while (rs.next()){
//                System.out.print(rs.getString("empid")+" ");
//                System.out.print(rs.getString("name")+" ");
//                System.out.println(rs.getString("address"));
//
//            }
        }catch (Exception e){
            System.out.println(e);
        }
        return response.toString();
    }

    public void delete_row_by_name(Connection conn,String table_name, String name){
        Statement statement;
        try{
            String query=String.format("delete from %s where name='%s'",table_name,name);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void delete_row_by_id(Connection conn,String table_name, int id){
        Statement statement;
        try{
            String query=String.format("delete from %s where id= %s",table_name,id);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void delete_table(Connection conn, String table_name){
        Statement statement;
        try {
            String query= String.format("drop table %s",table_name);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
