CREATE OR REPLACE PACKAGE Subprograms IS

    FUNCTION calculateRating(p_reviewee VARCHAR) RETURN number ;

    PROCEDURE delayAuctionsEnd(p_auction_name VARCHAR, p_seller VARCHAR, p_item_name VARCHAR);

END Subprograms;

CREATE OR REPLACE PACKAGE BODY Subprograms IS
    FUNCTION calculateRating(p_reviewee VARCHAR) RETURN number is
        reviewee_rating number;
    begin
        select avg(r.rating)
        into reviewee_rating
        from ratings r
                 join auctions a on r.auction = a.auction_id
        where a.seller = p_reviewee;
        return reviewee_rating;
    end calculateRating;
    PROCEDURE delayAuctionsEnd(p_auction_name VARCHAR, p_seller VARCHAR, p_item_name VARCHAR) IS
    BEGIN
        update auctions
        set end_date = end_date + interval '1' day
        where auction_name = p_auction_name
          and seller = p_seller
          and item_name = p_item_name;
    END DELAYAUCTIONSEND;

END Subprograms;