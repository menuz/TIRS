
  CREATE OR REPLACE PROCEDURE "HZGPS_TAXI"."SP_GPSDISTANCE_CAL" 
( lat IN NUMBER
, lon IN NUMBER
) AS
  dis number;
  t_lati number;
  t_longi number;
  t_message_id varchar2(12);
  t_vehicle_id varchar2(10);
  t_speed number(5,2);
  t_speed_time date;
  satisfy_count number;
  
  cursor c1 is (select message_id, vehicle_id, lati, longi, speed, speed_time from tb_gps_1112 where state = 0 and
    to_char(speed_time, 'hh24:mi:ss') between '12:00:00' and '14:00:00' and rownum < 10000);
BEGIN
  dis := 0;
  satisfy_count := 0;
  
  open c1;
  loop 
    fetch c1 into t_message_id, t_vehicle_id, t_lati, t_longi, t_speed, t_speed_time;
    
      select sdo_geom.sdo_distance(
            sdo_geometry(2001,8307,sdo_point_type(lat,      lon,null),null,null),
            sdo_geometry(2001,8307,sdo_point_type(t_lati, t_longi,null),null,null),
            0.05) as dist into dis
      from dual;
      
      if dis < 20 and t_speed < 1 then
        insert into tb_dis_temp(message_id, vehicle_id, lati, longi, dis, speed, speed_time) 
          values (t_message_id, t_vehicle_id, t_lati, t_longi, dis, t_speed, t_speed_time);
        satisfy_count := satisfy_count + 1;
      end if;

    exit when c1 % notfound;
  end loop;
  close c1;
  
  dbms_output.put_line('--------------');
  dbms_output.put_line(satisfy_count);
 
END SP_GPSDISTANCE_CAL;
/
 