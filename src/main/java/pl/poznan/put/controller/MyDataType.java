package pl.poznan.put.controller;

public class MyDataType {
    String auction_name;
    String item_name;
    String end_date;
    String price;
    String seller;
    String category;
    String details;

    public MyDataType(String auction_name, String item_name, String date, String price, String seller, String category, String details) {
        this.auction_name = auction_name;
        this.item_name = item_name;
        this.end_date = date;
        this.price = price;
        this.seller = seller;
        this.category = category;
        this.details = details;
    }

    public String getAuction_name() {
        return auction_name;
    }

    public void setAuction_name(String auction_name) {
        this.auction_name = auction_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
