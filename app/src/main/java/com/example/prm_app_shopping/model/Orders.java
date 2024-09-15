package com.example.prm_app_shopping.model;

import java.util.Date;
import java.util.List;

public class Orders {
    private String id;
    private String coustomerID;
    private List<Cart> odersItem;
    private int orderStatus;
    private Date orderDate;
    private Date requiredDate;
    private Date shipDate;


    public Orders() {
    }

    public Orders(String id, String coustomerID, List<Cart> odersItem, int orderStatus, Date orderDate, Date requiredDate, Date shipDate) {
        this.id = id;
        this.coustomerID = coustomerID;
        this.odersItem = odersItem;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shipDate = shipDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoustomerID() {
        return coustomerID;
    }

    public void setCoustomerID(String coustomerID) {
        this.coustomerID = coustomerID;
    }

    public List<Cart> getOdersItem() {
        return odersItem;
    }

    public void setOdersItem(List<Cart> odersItem) {
        this.odersItem = odersItem;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    @Override
    public String toString() {
        return "orders{" +
                "id='" + id + '\'' +
                ", coustomerID='" + coustomerID + '\'' +
                ", odersItem=" + odersItem +
                ", orderStatus=" + orderStatus +
                ", orderDate=" + orderDate +
                ", requiredDate=" + requiredDate +
                ", shipDate=" + shipDate +
                '}';
    }
}
