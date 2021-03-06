-- Generated by Oracle SQL Developer Data Modeler 19.4.0.350.1424
--   at:        2020-12-16 22:49:09 CET
--   site:      Oracle Database 12c
--   options:      Oracle Database 12c


CREATE TABLE ads
(
    auction INTEGER      NOT NULL,
    slogan  VARCHAR2(64) NOT NULL
);

ALTER TABLE ads
    ADD CONSTRAINT ad_pk PRIMARY KEY (auction);

CREATE SEQUENCE auctions_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE auctions
(
    auction_id       INTEGER          NOT NULL,
    auction_name     VARCHAR2(128)    NOT NULL,
    item_name        VARCHAR2(64)     NOT NULL,
    item_description VARCHAR2(1024)   NOT NULL,
    creation_date    DATE             NOT NULL,
    end_date         DATE             NOT NULL,
    price            DOUBLE PRECISION NOT NULL,
    seller           VARCHAR2(64)     NOT NULL,
    status           VARCHAR2(64)     NOT NULL,
    discriminator    VARCHAR2(64)     NOT NULL
);

CREATE INDEX auctions_seller_idx ON
    auctions (
              seller
              ASC);

CREATE INDEX auctions_status_idx ON
    auctions (
              status
              ASC);

CREATE INDEX auctions_creation_date_idx ON
    auctions (
              creation_date
              DESC);

ALTER TABLE auctions
    ADD CONSTRAINT auctions_pk PRIMARY KEY (auction_id);

ALTER TABLE auctions
    ADD CONSTRAINT auctions__un UNIQUE (auction_name,
                                        item_name,
                                        seller);

CREATE TABLE auctions_logs
(
    auction            INTEGER       NOT NULL,
    timestamp          DATE          NOT NULL,
    action_description VARCHAR2(128) NOT NULL,
    actor              VARCHAR2(64)
);

CREATE INDEX auctions_histories_actor_idx ON
    auctions_logs (
                   actor
                   ASC);

ALTER TABLE auctions_logs
    ADD CONSTRAINT auctions_histories_pk PRIMARY KEY (timestamp,
                                                      auction);

CREATE TABLE books
(
    auction_id INTEGER      NOT NULL,
    author     VARCHAR2(64) NOT NULL,
    cover      VARCHAR2(64) NOT NULL,
    genre      VARCHAR2(64) NOT NULL
);

CREATE INDEX books_author_idx ON
    books (
           author
           ASC);

CREATE INDEX books_cover_idx ON
    books (
           cover
           ASC);

CREATE INDEX books_genre_idx ON
    books (
           genre
           ASC);

ALTER TABLE books
    ADD CONSTRAINT books_pk PRIMARY KEY (auction_id);

CREATE TABLE cars
(
    auction_id   INTEGER      NOT NULL,
    make         VARCHAR2(64) NOT NULL,
    model        VARCHAR2(64) NOT NULL,
    mileage      INTEGER      NOT NULL,
    transmission VARCHAR2(64) NOT NULL,
    engine       VARCHAR2(64) NOT NULL,
    fuel         VARCHAR2(64) NOT NULL,
    condition    VARCHAR2(64) NOT NULL
);

CREATE INDEX cars_make_idx ON
    cars (
          make
          ASC);

CREATE INDEX cars_model_idx ON
    cars (
          model
          ASC);

CREATE INDEX cars_mileage_idx ON
    cars (
          mileage
          ASC);

CREATE INDEX cars_transmission_idx ON
    cars (
          transmission
          ASC);

CREATE INDEX cars_engine_idx ON
    cars (
          engine
          ASC);

CREATE INDEX cars_fuel_idx ON
    cars (
          fuel
          ASC);

CREATE INDEX cars_condition_idx ON
    cars (
          condition
          ASC);

ALTER TABLE cars
    ADD CONSTRAINT cars_pk PRIMARY KEY (auction_id);

CREATE TABLE defaults
(
    auction_id INTEGER      NOT NULL,
    category   VARCHAR2(64) NOT NULL
);

CREATE INDEX defaults_category_idx ON
    defaults (
              category
              ASC);

ALTER TABLE defaults
    ADD CONSTRAINT defaults_pk PRIMARY KEY (auction_id);

CREATE TABLE delivery_preferences
(
    user_ref VARCHAR2(64)  NOT NULL,
    address  VARCHAR2(128) NOT NULL
);

CREATE INDEX delivery_preferences_user_idx ON
    delivery_preferences (
                          user_ref
                          ASC);

ALTER TABLE delivery_preferences
    ADD CONSTRAINT delivery_preferences_pk PRIMARY KEY (address,
                                                        user_ref);

CREATE TABLE followers
(
    follower VARCHAR2(64) NOT NULL,
    followee VARCHAR2(64) NOT NULL
);

CREATE INDEX followers_idx ON
    followers (
               follower
               ASC);

ALTER TABLE followers
    ADD CONSTRAINT followers_pk PRIMARY KEY (follower,
                                             followee);

CREATE TABLE items_in_shopping_carts
(
    auction INTEGER      NOT NULL,
    buyer   VARCHAR2(64) NOT NULL
);

CREATE INDEX shopping_carts_buyer_idx ON
    items_in_shopping_carts (
                             buyer
                             ASC);

ALTER TABLE items_in_shopping_carts
    ADD CONSTRAINT shopping_carts_pk PRIMARY KEY (buyer,
                                                  auction);

CREATE TABLE personal_ads
(
    auction   INTEGER      NOT NULL,
    recipient VARCHAR2(64) NOT NULL
);

CREATE INDEX personal_ads_recipient_idx ON
    personal_ads (
                  recipient
                  ASC);

ALTER TABLE personal_ads
    ADD CONSTRAINT personal_ads_pk PRIMARY KEY (recipient,
                                                auction);

CREATE TABLE phones
(
    auction_id       INTEGER      NOT NULL,
    producer         VARCHAR2(64) NOT NULL,
    screen_size      VARCHAR2(64) NOT NULL,
    battery          VARCHAR2(64) NOT NULL,
    processor        VARCHAR2(64) NOT NULL,
    ram              VARCHAR2(64) NOT NULL,
    operating_system VARCHAR2(64) NOT NULL
);

CREATE INDEX phones_producer_idx ON
    phones (
            producer
            ASC);

CREATE INDEX phones_screen_size_idx ON
    phones (
            screen_size
            ASC);

CREATE INDEX phones_battery_idx ON
    phones (
            battery
            ASC);

CREATE INDEX phones_processor_idx ON
    phones (
            processor
            ASC);

CREATE INDEX phones_ram_idx ON
    phones (
            ram
            ASC);

CREATE INDEX phones_os_idx ON
    phones (
            operating_system
            ASC);

ALTER TABLE phones
    ADD CONSTRAINT phones_pk PRIMARY KEY (auction_id);

CREATE TABLE pictures
(
    auction INTEGER       NOT NULL,
    name    VARCHAR2(256) NOT NULL,
    image   BLOB
);

ALTER TABLE pictures
    ADD CONSTRAINT pictures_pk PRIMARY KEY (name,
                                            auction);

CREATE TABLE ratings
(
    auction   INTEGER      NOT NULL,
    reviewer  VARCHAR2(64) NOT NULL,
    rating    INTEGER      NOT NULL,
    text_desc VARCHAR2(512)
);

ALTER TABLE ratings
    ADD CONSTRAINT ratings_pk PRIMARY KEY (reviewer,
                                           auction);

CREATE TABLE users
(
    email      VARCHAR2(64) NOT NULL,
    first_name VARCHAR2(64) NOT NULL,
    last_name  VARCHAR2(64) NOT NULL,
    hash       VARCHAR2(64) NOT NULL,
    birth_date DATE         NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT users_pk PRIMARY KEY (email);

CREATE TABLE watch_lists
(
    auction  INTEGER      NOT NULL,
    follower VARCHAR2(64) NOT NULL,
    name     VARCHAR2(64) NOT NULL
);

CREATE INDEX watch_lists_follower_idx ON
    watch_lists (
                 follower
                 ASC);

ALTER TABLE watch_lists
    ADD CONSTRAINT watch_lists_pk PRIMARY KEY (name,
                                               follower,
                                               auction);

ALTER TABLE ads
    ADD CONSTRAINT ad_auction_fk FOREIGN KEY (auction)
        REFERENCES auctions (auction_id);

ALTER TABLE auctions
    ADD CONSTRAINT auctions_fk FOREIGN KEY (seller)
        REFERENCES users (email);

ALTER TABLE auctions_logs
    ADD CONSTRAINT auctions_histories_actor_fk FOREIGN KEY (actor)
        REFERENCES users (email);

ALTER TABLE auctions_logs
    ADD CONSTRAINT auctions_histories_auction_fk FOREIGN KEY (auction)
        REFERENCES auctions (auction_id);

ALTER TABLE books
    ADD CONSTRAINT books_fk FOREIGN KEY (auction_id)
        REFERENCES auctions (auction_id);

ALTER TABLE cars
    ADD CONSTRAINT cars_fk FOREIGN KEY (auction_id)
        REFERENCES auctions (auction_id);

ALTER TABLE defaults
    ADD CONSTRAINT defaults_fk FOREIGN KEY (auction_id)
        REFERENCES auctions (auction_id);

ALTER TABLE delivery_preferences
    ADD CONSTRAINT delivery_preferences_fk FOREIGN KEY (user_ref)
        REFERENCES users (email);

ALTER TABLE followers
    ADD CONSTRAINT followees_fk FOREIGN KEY (followee)
        REFERENCES users (email);

ALTER TABLE followers
    ADD CONSTRAINT followers_fk FOREIGN KEY (follower)
        REFERENCES users (email);

ALTER TABLE personal_ads
    ADD CONSTRAINT personal_ads_ad_fk FOREIGN KEY (auction)
        REFERENCES ads (auction);

ALTER TABLE personal_ads
    ADD CONSTRAINT personal_ads_recipient_fk FOREIGN KEY (recipient)
        REFERENCES users (email);

ALTER TABLE phones
    ADD CONSTRAINT phones_fk FOREIGN KEY (auction_id)
        REFERENCES auctions (auction_id);

ALTER TABLE pictures
    ADD CONSTRAINT pictures_fk FOREIGN KEY (auction)
        REFERENCES auctions (auction_id);

ALTER TABLE ratings
    ADD CONSTRAINT ratings_fk FOREIGN KEY (reviewer,
                                           auction)
        REFERENCES items_in_shopping_carts (buyer,
                                            auction);

ALTER TABLE items_in_shopping_carts
    ADD CONSTRAINT shopping_carts_auction_fk FOREIGN KEY (auction)
        REFERENCES auctions (auction_id);

ALTER TABLE items_in_shopping_carts
    ADD CONSTRAINT shopping_carts_buyer_fk FOREIGN KEY (buyer)
        REFERENCES users (email);

ALTER TABLE watch_lists
    ADD CONSTRAINT watch_lists_auction_fk FOREIGN KEY (auction)
        REFERENCES auctions (auction_id);

ALTER TABLE watch_lists
    ADD CONSTRAINT watch_lists_follower_fk FOREIGN KEY (follower)
        REFERENCES users (email);

CREATE OR REPLACE PACKAGE subprograms IS

    FUNCTION calculateRating(p_reviewee VARCHAR) RETURN DOUBLE PRECISION;

    PROCEDURE delay_auction_end(p_id INTEGER);

END subprograms;

CREATE OR REPLACE PACKAGE BODY subprograms IS

    FUNCTION calculateRating(p_reviewee VARCHAR) RETURN DOUBLE PRECISION is
        reviewee_rating number;
    begin
        select avg(r.rating)
        into reviewee_rating
        from ratings r
                 join auctions a on r.auction = a.auction_id
        where a.seller = p_reviewee;
        return reviewee_rating;
    end calculateRating;

    PROCEDURE delay_auction_end(p_id integer) IS
    BEGIN
        update auctions
        set end_date = end_date + interval '1' day
        where auction_id = p_id;
    END delay_auction_end;

END subprograms;

create or replace trigger FINISHED_AUCTION_TRIGGER
    before update of STATUS
    on AUCTIONS
    for each row
    when ( NEW.STATUS = 'FINISHED' )
begin
    insert into ITEMS_IN_SHOPPING_CARTS
    values (:NEW.AUCTION_ID, (
        select ACTOR
        from AUCTIONS_LOGS
        where ACTION_DESCRIPTION like 'Auction bid to %'
          and AUCTION = :NEW.AUCTION_ID
          and TO_NUMBER(REGEXP_SUBSTR(ACTION_DESCRIPTION, '[0-9.]+')) = :NEW.PRICE
    ));
    :NEW.STATUS := 'IN_SHOPPING_CART';
end;

create or replace procedure update_auctions is
begin
    update auctions
    set status = 'CANCELLED'
    where end_date < sysdate
      and status = 'CREATED';

    update auctions
    set status = 'FINISHED'
    where end_date < sysdate
      and status = 'BIDDING';

    commit;
end update_auctions;
