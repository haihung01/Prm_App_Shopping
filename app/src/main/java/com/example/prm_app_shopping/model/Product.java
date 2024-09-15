package com.example.prm_app_shopping.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("product_name")
    private String name;

//    @SerializedName("list_price")
    private String image;

    @SerializedName("title")
    private String status;

    @SerializedName("list_price")
    private double price;

    @SerializedName("product_id")
    private int id;

    private double discount;
    private int model_year;
    private int stock;
    private Category category;

    public Product(String name, String image, String status, double price, double discount, int stock, int id, int model_year) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.id = id;
        this.model_year= model_year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int model_year) {
        this.id = id;
    }
    public int getModel_year() {
        return model_year;
    }

    public void setModel_year(int model_year) {
        this.model_year = model_year;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", model_year=" + model_year +
                ", stock=" + stock +
                ", id=" + id +
                ", category=" + category +
                '}';
    }
}
