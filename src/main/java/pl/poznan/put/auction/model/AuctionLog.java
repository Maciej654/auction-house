package pl.poznan.put.auction.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AuctionLog {
    private Date   date;
    private String description;
}
