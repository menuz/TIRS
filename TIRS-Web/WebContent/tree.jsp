<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.btn {
	display: inline-block;
	padding: 4px 12px;
	margin-bottom: 0;
	font-size: 14px;
	line-height: 20px;
	color: #333333;
	text-align: center;
	text-shadow: 0 1px 1px rgba(255, 255, 255, 0.75);
	vertical-align: middle;
	cursor: pointer;
	background-color: #f5f5f5; Invalid property value.background-image :
	-moz-linear-gradient( top, #ffffff, #e6e6e6);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff),
		to(#e6e6e6) );
	background-image: -webkit-linear-gradient(top, #ffffff, #e6e6e6);
	Invalid property value.background-image : -o-linear-gradient( top,
	#ffffff, #e6e6e6);
	background-image: linear-gradient(to bottom, #ffffff, #e6e6e6);
	background-repeat: repeat-x;
	border: 1px solid #bbbbbb;
	border-color: #e6e6e6 #e6e6e6 #bfbfbf;
	border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
	border-bottom-color: #a2a2a2;
	-webkit-border-radius: 4px; Unknown property name.-moz-border-radius :
	4px;
	border-radius: 4px; Invalid property value.filter : progid :           
	           DXImageTransform.Microsoft.gradient (                  
	    startColorstr =     
	
	      
	         '#ffffffff', endColorstr =                       '#ffe6e6e6',
	GradientType =     
	 
	               0); Invalid property value.filter : progid :
	            
	         DXImageTransform.Microsoft.gradient (            
	          enabled =           
	           false);
	-webkit-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px
		rgba(0, 0, 0, 0.05); Unknown property name.-moz-box-shadow : inset 0
	1px 0 rgba( 255, 255, 255, 0.2), 0 1px 2px rgba( 0, 0, 0, 0.05);
	box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px
		rgba(0, 0, 0, 0.05);
}

.btn:hover {
	color: #333333;
	text-decoration: none;
	background-color: #e6e6e6;
	background-position: 0 -15px;
	-webkit-transition: background-position 0.1s linear; Unknown property
	name.-moz-transition : background-position 0.1s linear; Unknown
	property name.-o-transition : background-position 0.1s linear;
	transition: background-position 0.1s linear;
}
</style>
<script type="text/javascript">
	function query() {
		var v_id = document.getElementById("vehicle_id").value;
		var v_num = document.getElementById("v_num").value;
		var time1 = document.getElementById("time1").value;
		var time2 = document.getElementById("time2").value;
		alert("v_id " + v_id);
		self.parent.frames["map"].query(v_id, v_num, time1, time2);
	}

	function cls() {
		self.parent.frames["map"].clear();
	}
	
	function play() {
		self.parent.frames["map"].play();
		document.getElementById("Pause").value="Pause";
		document.getElementById("Pause").onclick=pause;  
	}
	
	function pause() {
		self.parent.frames["map"].pause();
		document.getElementById("Pause").value="Continue";
		document.getElementById("Pause").onclick=continuePlay;  
	}
	
	function continuePlay() {
	//	alert("continue");
		self.parent.frames["map"].continuePlay();
		document.getElementById("Pause").value="Pause";
		document.getElementById("Pause").onclick=pause;  
	}
	
	function show() {
		self.parent.frames["map"].show();
	}
</script>
<title>实时轨迹信息</title>
</head>
<body>
	<table>
		<tr>
			<td>车辆编号</td>
			<td><select id="vehicle_id">
					<option value="10003">10003</option>
					<option value="10004">10004</option>
					<option value="10005">10005</option>
					<option value="10006">10006</option>
					<option value="10007">10007</option>
					<option value="10008">10008</option>
					<option value="10009">10009</option>
					<option value="10010">10010</option>
					<option value="10011">10011</option>
					<option value="15585">15585</option>
					<option value="16934">16934</option>
			</select></td>
		</tr>

		<tr>
			<td>条数</td>
			<td><select id="v_num">
					<option value="1">1</option>
					<option value="100">100</option>
					<option value="200">200</option>
					<option value="500">500</option>
					<option value="1000">1000</option>
					<option value="9999">9999</option>
			</select></td>
		</tr>

		<tr>
			<td>时间区间</td>
		</tr>
		
		<tr>
			<td><input type="text" id="time1" name="time1" value="00:00:00" size="5"/></td>
			<td><input type="text" id="time2" name="time2" value="06:00:00" size="5"/></td>
		</tr>

		<tr>
			<td width="50%"><input type="button" value="Query"
				onclick="query()" class="btn" /></td>
		</tr>
		<tr>
			<td width="50%"><input type="button" value="Play   " 
				onclick="play()" class="btn" /></td>  
			<td width="50%"><input type="button" id="Pause" name="Pause" value="Pause"
				onclick="pause()" class="btn" /></td>  
		</tr>
		<tr>
			<td width="50%"><input type="button" value="Show "
				onclick="show()" class="btn" /></td>  
			<td width="50%"><input type="button" value="Clear "
				onclick="cls()" class="btn" /></td>
		</tr>
	</table>
</body>

</html>
