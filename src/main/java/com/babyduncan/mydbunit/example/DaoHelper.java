package com.babyduncan.mydbunit.example;

import com.babyduncan.mydbunit.example.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-1 18:30
 */
public final class DaoHelper {

    private static final String driverName = "com.mysql.jdbc.Driver";

    private static final String dbuserName = "root";
    private static final String dbpassword = "mysql";
    private static final String DBname = "test";

    private static final String BookTableName = "book";
    private static final String UserTableName = "user";

    private static String url = "jdbc:mysql://localhost:3307/" + DBname + "?characterEncoding=gbk";

    public static List<Book> getAllBooks() {
        Connection connection = null;
        Statement statement = null;
        List<Book> books = new ArrayList<Book>();

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url, dbuserName, dbpassword);
            statement = connection.createStatement();
            String sql = "select * from " + BookTableName + " limit 20";
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.next()) {
                return new ArrayList<Book>();
            } else {
                while (rs.next()) {
                    Book temp = new Book();
                    temp.setId(rs.getInt("id"));
                    temp.setName(rs.getString("bookname"));
                    temp.setPrice(rs.getInt("price"));
                    books.add(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return books;
    }


    public static boolean updatePrice(int id, int newPrice) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url, dbuserName, dbpassword);
            statement = connection.createStatement();
            String sql = "update " + BookTableName + " set price=" + newPrice + " where id = '" + id + "'";
            int rs = statement.executeUpdate(sql);
            return rs > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean deleteOneBook(int id) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url, dbuserName, dbpassword);
            statement = connection.createStatement();
            String sql = "delete from  " + BookTableName + " where id='" + id + "'";
            int rs = statement.executeUpdate(sql);
            return rs > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean insertOneBook(String bookname, int price) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url, dbuserName, dbpassword);
            statement = connection.createStatement();
            String sql = "insert into " + BookTableName + " (bookname,price) values ('" + bookname + "'," + price + ") ";
            int rs = statement.executeUpdate(sql);
            return rs > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void main(String... args) {
        List<Book> books = DaoHelper.getAllBooks();
        for (Book book : books) {
            System.out.println(book);
        }
    }

}
