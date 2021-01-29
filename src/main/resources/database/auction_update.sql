-- to create
BEGIN
    DBMS_SCHEDULER.CREATE_JOB(
            job_name => 'update_auctions_job',
            job_type => 'STORED_PROCEDURE',
            job_action => 'update_auctions',
            start_date => SYSTIMESTAMP,
            repeat_interval => 'FREQ=MINUTELY;INTERVAL=1;',
            auto_drop => FALSE,
            comments => 'Periodically checks which auctions have to be finished or cancelled');
END;

-- to enable
begin
    DBMS_SCHEDULER.ENABLE('update_auctions_job');
end;

--to drop
BEGIN
    DBMS_SCHEDULER.DROP_JOB(job_name => 'update_auctions_job');
END;

--to disable
begin
    DBMS_SCHEDULER.DISABLE('update_auctions_job');
end;
