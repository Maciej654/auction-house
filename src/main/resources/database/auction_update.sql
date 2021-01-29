-- to create
BEGIN
    DBMS_SCHEDULER.CREATE_JOB(
            job_name => 'update_auctions_job',
            job_type => 'STORED_PROCEDURE',
            job_action => 'update_auctions',
            start_date => SYSTIMESTAMP,
            repeat_interval => 'FREQ=HOURLY;INTERVAL=1;',
            auto_drop => FALSE,
            comments => 'My new job');
END;

-- to enable
exec DBMS_SCHEDULER.enable('update_auctions_job');

--to drop
BEGIN
    DBMS_SCHEDULER.DROP_JOB(job_name => 'update_auctions_job');
END;

--to disable
exec DBMS_SCHEDULER.disable('update_auctions_job');
