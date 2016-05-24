package api.contracts.dto;

import api.business.entities.LineItem;

public class LineItemDto {
    public String title;
    public double price;
    public int quantity;
    public int id;

    public LineItemDto(LineItem lineItem){
        this.title = lineItem.getTitle();
        this.price = lineItem.getPrice()/100d;
        this.quantity = lineItem.getQuantity();
        this.id = lineItem.getId();
    }
}
