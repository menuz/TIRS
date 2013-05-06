
  CREATE OR REPLACE PROCEDURE "HZGPS_TAXI"."SP_TAXISTATUS_STATISTICS" AS
  vehicle_count NUMBER;
  vehicle_record_count NUMBER;
  vehicle_id VARCHAR2(10);
  CURSOR c1 is (select unique(vehicle_id) from tb_gps_1112);
  
  -- iterate every taxi in dataset, invoke sp_taxistatus_query
  
BEGIN
  DBMS_OUTPUT.ENABLE(1000000);
  vehicle_count := 0;
  vehicle_record_count := 0;
  open c1;
  Loop 
    fetch c1 into vehicle_id;
    select count(vehicle_id) into vehicle_record_count from tb_gps_1112;
    
    sp_taxistatus_query(vehicle_id, vehicle_record_count);
     
    vehicle_count := vehicle_count + 1;
    exit when c1%notfound;
    dbms_output.put_line(vehicle_id);
  End Loop;
  close c1;
    dbms_output.put_line('--------------');
    dbms_output.put_line(vehicle_count);
END SP_TAXISTATUS_STATISTICS;
/
 
