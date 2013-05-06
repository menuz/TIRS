
  CREATE OR REPLACE PROCEDURE "HZGPS_TAXI"."SP_TAXISTATUS_QUERY" 
( in_vehicle_id IN VARCHAR2, in_vehicle_record_count in NUMBER
) AS
  time_interval integer;
  record_count number;
  vehicle_exception_time number;
  vehicle_exception_count number;
  vehicle_running_time number;
  vehicle_running_count number;
  vehicle_occupied_time number;
  vehicle_cruise_time number; 
  vehicle_remain_time number;
  vehicle_statuschange_count number;
  t_speed_time_1 date;
  t_speed_time_2 date;
  t_state_1 INTEGER;
  t_state_2 INTEGER;

  cursor c1 IS select now.db_time, now.state, lst.db_time, lst.state from 
  (select message_id,lag(message_id, 1,0) over (order by message_id) as last_message_id from 
  (select * from tb_gps_1112 where vehicle_id = in_vehicle_id and rownum < in_vehicle_record_count)) conn, tb_gps_1112  now, tb_gps_1112 lst 
  where conn.message_id = now.message_id and conn.last_message_id = lst.message_id;
  
  -- used for statistics running time, occupied time and cruise time of a taxi 
  
BEGIN
  time_interval := 0;
  record_count := 0;
  vehicle_exception_time := 0;
  vehicle_exception_count := 0;
  vehicle_running_time := 0;
  vehicle_running_count :=0;
  vehicle_occupied_time := 0;
  vehicle_remain_time := 0;
  vehicle_cruise_time := 0;
  vehicle_statuschange_count := 0;
  t_speed_time_1 := null;
  t_speed_time_2 := null;  
  t_state_1 := 0;
  t_state_2 := 1;
  
  open c1;
  Loop 
    fetch c1 into t_speed_time_1, t_state_1, t_speed_time_2, t_state_2;
      time_interval := abs(datediff('ss', t_speed_time_2, t_speed_time_1));
      record_count := record_count + 1;
      dbms_output.put_line(time_interval);  
      
      if time_interval < 3600*4 then
        vehicle_running_time := vehicle_running_time + time_interval;
        vehicle_running_count := vehicle_running_count + 1;
        if t_state_1 = t_state_2 then 
          if t_state_1 = 1 then
            vehicle_occupied_time := vehicle_occupied_time + time_interval;
          ELSIF t_state_1 = 0 then
            vehicle_cruise_time := vehicle_cruise_time + time_interval;
          end if;
        elsif t_state_1 != t_state_2 then
          vehicle_statuschange_count := vehicle_statuschange_count + 1;
          vehicle_remain_time := vehicle_remain_time + time_interval;
        end if;
      elsif time_interval >= 3600*4 then 
        vehicle_exception_time := vehicle_exception_time + time_interval;
        vehicle_exception_count := vehicle_exception_count + 1;
      end if;
      
    exit when c1%notfound;
    
  End Loop;
  close c1;
  
  vehicle_running_time := vehicle_running_time / 60;
  vehicle_occupied_time := vehicle_occupied_time / 60;
  vehicle_cruise_time := vehicle_cruise_time / 60;
  vehicle_remain_time := vehicle_remain_time / 60;
  
  dbms_output.put_line('vehicle_id                  (    )  '|| in_vehicle_id );
  dbms_output.put_line('record_count                (    )  '|| record_count);
  dbms_output.put_line('vehicle_exception_time      (    )  '|| vehicle_exception_time);
  dbms_output.put_line('vehicle_exception_count     (    )  '|| vehicle_exception_count);
  dbms_output.put_line('vehicle_running_time        (1440)  '|| vehicle_running_time);
  dbms_output.put_line('vehicle_running_count       (    )  '|| vehicle_running_count);
  dbms_output.put_line('vehicle_exception_time      (    )  '|| vehicle_exception_time);
  dbms_output.put_line('vehicle_occupied_time       (    )  '|| vehicle_occupied_time);
  dbms_output.put_line('vehicle_cruise_time         (    )  '|| vehicle_cruise_time);
  dbms_output.put_line('vehicle_remain_time         (    )  '|| vehicle_remain_time);
  dbms_output.put_line('vehicle_statuschange_count  (    )  '|| vehicle_statuschange_count);
    
END SP_TAXISTATUS_QUERY;
/
 