-- job nie zadziała na serwerze pp przez brak uprawnien, skrypt trzeba puścić na localhost(albo innym serwerze na którym ma się uprawnienia)
create or replace procedure update_auction is
begin
    insert into items_in_shopping_carts
    select a.auction_id, logs.actor
    from auctions a
             join
         (
             SELECT auction,
                    actor,
                    TO_NUMBER(REGEXP_SUBSTR(action_description, '[0-9.]+')) as price
             FROM auctions_logs
             where action_description like 'Auction bid to%'
         ) logs on a.auction_id = logs.auction
             and logs.price = a.price
    where a.end_date < sysdate
      and a.status = 'BIDDING';


    update auctions
    set status = 'FINISHED'
    where end_date < sysdate
      and status = 'CREATED';

    update auctions
    set status = 'IN_SHOPPING_CART'
    where end_date < sysdate
      and status = 'BIDDING';

    commit;
end update_auction;

BEGIN
    DBMS_SCHEDULER.CREATE_JOB(
            job_name => 'update_auctions_job',
            job_type => 'STORED_PROCEDURE',
            job_action => 'update_auction',
            start_date => SYSTIMESTAMP,
            repeat_interval => 'FREQ=HOURLY;INTERVAL=1;',
            auto_drop => FALSE,
            comments => 'My new job');
END;

exec DBMS_SCHEDULER.enable('update_auctions_job');


--to drop
BEGIN
    dbms_scheduler.drop_job(job_name => 'update_auctions_job');
END;

--to disable
exec DBMS_SCHEDULER.disable('update_auctions_job');
