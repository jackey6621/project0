package org.example;

import io.javalin.Javalin;

import java.sql.Connection;


public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        Javalin app = Javalin.create().start(9000);


        DbFunctions db=new DbFunctions();
        Connection conn=db.connect_to_db("garage","postgres","admin");
        //db.delete_table(conn, "owner");
        db.createTableItem(conn,"cars");
        db.createTableUser(conn, "owner");

        //db.insert_row_item(conn,"cars","bmw");
        //db.insert_row_user(conn, "owner", "jack", 1);
        //db.insert_row_user(conn, "owner", "john", 3);

        app.get("selectallitem", ctx->{
            //db.read_data(conn, "cars");
            String table_name = ctx.queryParam("table");
            ctx.contentType("application/json");
            ctx.result(db.read_data_item(conn, table_name));
        });

        app.get("selectallitembyuserid", ctx->{
            //db.read_data(conn, "cars");
            String name = ctx.queryParam("name");
            int id = db.search_by_name(conn, "owner", name);

            ctx.contentType("application/json");
            ctx.result(db.search_by_id(conn, "cars", id));
        });

        app.post("insertitem", ctx->{
            //db.read_data(conn, "cars");
            String brand = ctx.queryParam("brand");
            db.insert_row_item(conn,"cars",brand);
            ctx.result("Inserted: "+ brand);
        });

        app.post("insertuser", ctx->{
            //db.read_data(conn, "cars");
            String name = ctx.queryParam("name");
            int car_id = Integer.valueOf(ctx.queryParam("id"));
            db.insert_row_user(conn,"owner",name, car_id);
            ctx.result("Inserted: "+ name + "," + car_id);
        });

        app.post("update", ctx->{
            //db.read_data(conn, "cars");
            String oldName = ctx.queryParam("oldname");
            String newName = ctx.queryParam("newname");
            db.update_name(conn,"owner",oldName, newName);
        });

        app.delete("deleteitem", ctx->{
            int id = Integer.valueOf(ctx.queryParam("id"));
            db.delete_row_by_id(conn,"cars",id);
        });

        app.delete("deleteowner", ctx->{
            int id = Integer.valueOf(ctx.queryParam("id"));
            db.delete_row_by_id(conn,"owner",id);
        });



        //app.delete("")
        //db.insert_row(conn,"employee","Rajat","India");
        //db.update_name(conn,"employee","Rahul","Raj");
        //db.search_by_name(conn,"employee","abhishek");
        //db.delete_row_by_name(conn,"employee","abhishek");
        //db.delete_row_by_id(conn,"employee",4);
        //db.read_data(conn,"employee");
        //db.delete_table(conn,"employee");
    }
}