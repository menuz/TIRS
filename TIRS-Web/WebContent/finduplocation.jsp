<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">

table, td, th
  {
  border:1px solid white;
  }

th
  {
  background-color:white;
  color:white;
  }

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

<script src="js/date.format.js"></script>

<script type="text/javascript">
	var $O;
	(function(undefined){
		O = function (id) {
			return "string" == typeof id ? document.getElementById(id):id;
		};
		$O = O;
	})();

	function fu_query() {
		var now = new Date();
		var time = dateFormat(now, "yyyy-mm-dd HH:MM:ss");
		var longitude = document.getElementById("longitude").value;
		var latitude = document.getElementById("latitude").value;
		
		self.parent.frames["map"].fu_query(latitude, longitude, time);
	}
	
	/*function fp_draw() {
		self.parent.frames["map"].fp_draw();
	}*/
	
	function setStartPoint(cur) {
		$O("latitude").value = cur.lat();
		$O("longitude").value = cur.lng();
	}
</script>
<title>寻找上客点</title>
</head>
<body>
	寻找上客点</br></br>
	
	<table>
		<tr>
			乘客当前位置
		</tr>
		
		<tr>
			<td>经度</td>
			<td><input type="text" id="longitude" name="longitude" value="120.0" size="10"></td>
		</tr>
		
		<tr>
			<td>纬度</td>
			<td><input type="text" id="latitude" name="latitude" value="30.0" size="10"></td>
		</tr>
	</table>
	
	<!-- <table>
		<tr>
			乘客希望上车的就近街道点（可选）
		</tr>
		
		<tr>
			<td>经度</td>
			<td><input type="text" id="longitude2" name="longitude2" value="0" size="10"></td>
		</tr>
		
		<tr>
			<td>纬度</td>
			<td><input type="text" id="latitude2" name="latitude2" value="0" size="10"></td>
		</tr>
	</table>
	 -->
	
	
	<input type="button" value="手气不错" onclick="fu_query()" class="btn" />
	<!-- <input type="button" value="Test" onclick="fp_draw()" class="btn" /> -->
</body>

</html>
