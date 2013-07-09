package com.babyduncan.mydbunit.example.model;

/**
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-5 12:11
 */
public class Book {

    int id;
    String name;
    int price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
