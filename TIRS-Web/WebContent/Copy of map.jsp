<%@ page language="java" pageEncoding="GBK"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

<%-- <jsp:include page="header.jsp"></jsp:include>   --%>

<link rel='stylesheet' type='text/css' href='css/main.css'>
<script
	src="https://maps.googleapis.com/maps/api/js?v=AIzaSyAaayFfpVa4X7gsYqu_MCQLDKmdXLoJ5D8&sensor=true"></script>
<script src="js/jquery-1.8.3.js"></script>

<script type="text/javascript">
var map;
var poly;
var path;
var gpses;

function GPS(message_id, vehicle_id, vehicle_num, longi, lati,longi_correct, lati_correct,
			px, py, speed, direction, state, carState, speed_time, db_time, note) {
	 this.message_id = message_id;
	 this.vehicle_id = vehicle_id;
	 this.vehicle_num = vehicle_num;
	 
	 this.longi = longi;
	 this.lati = lati;
	 
	 this.longi_correct = longi_correct;
	 this.lati_correct = lati_correct;
	 
	 this.px = px;
	 this.py = py;
	 this.speed = speed;
	 this.direction = direction;
	 this.state = state;
	 this.carState = carState;
	 this.speed_time = speed_time;
	 this.db_time = db_time;
	 this.note = note;
}

function clear() {
	if(poly != null)
		poly.setMap(null);
} 

function replay() {
	if(gpses == null) {
		alert("Query first");
	} else {
		poly = new google.maps.Polyline(polyOptions);
		poly.setMap(map);
		path = poly.getPath();
			
		for (var i = 0; i < gpses.length; i++) {
			GPS gps = gpses[i];
			addGPSToPath(gps.lati, gps.longi);
			setTimeout(function(){}, 2*1000);
		}
	}
}

function initialize() {
	var mapOptions = {
		center : new google.maps.LatLng(30.0, 120.0),
		zoom : 10,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map-canvas"),
			mapOptions);
}

google.maps.event.addDomListener(window, 'load', initialize);


function addGPSToPath(lat, lon) {
	var p = new google.maps.LatLng(lat, lon);
	path.push(p);
	var marker = new google.maps.Marker({
		position : p,
		title : '#' + path.getLength(),
		map : map
	});
}
	
function query(vehicleId, vNum){
	alert("map query " + vehicleId);
	jQuery.ajax({
	url: "PublicXMLFeed",
		data:{vehicle_id:vehicleId, v_num: vNum},
	complete: function (xhr,textStatus){
		if (xhr.readyState == 4) {
     		if(xhr.status == 200||xhr.status == 304) {
    	 	var xmlText=xhr.responseText;
       		if (window.ActiveXObject) {  
       			xmldoc=new ActiveXObject("Microsoft.XMLDOM");  
       			xmldoc.async="false";  
       			xmldoc.loadXML(xmlText);
       	    } else {  
	       	        parser=new DOMParser();  
	       	        xmldoc=parser.parseFromString(xmlText,"text/xml");  
       	    }
       			var mapType = map.getMapTypeId();
       			var vehicles = xmldoc.getElementsByTagName("gps");
       			
       			var polyOptions = {
       					strokeColor : '#000000',
       					strokeOpacity : 1.0,
       					strokeWeight : 3
       				}
       			poly = new google.maps.Polyline(polyOptions);
       			poly.setMap(map);
       			path = poly.getPath();
       			
       			gpses = new Array();
       			
       			for (var i=0;i<vehicles.length;i++)
       			{
       				message_id=vehicles[i].getAttribute("message_id").toString();
           			vehicle_id=vehicles[i].getAttribute("vehicle_id");
           			vehicle_num=vehicles[i].getAttribute("vehicle_num");
           			
           			longi=vehicles[i].getAttribute("longi");
           			lati=vehicles[i].getAttribute("lati");
           			
           			longi_correct=vehicles[i].getAttribute("longi_correct");
           			lati_correct=vehicles[i].getAttribute("lati_correct");
           			
           			px=vehicles[i].getAttribute("px");
           			py=vehicles[i].getAttribute("py");
           			speed=vehicles[i].getAttribute("speed");
           			direction=vehicles[i].getAttribute("direction");
           			state=vehicles[i].getAttribute("state").toString();
           			carState=vehicles[i].getAttribute("carState");
           			speed_time=vehicles[i].getAttribute("speed_time");
           			db_time=vehicles[i].getAttribute("db_time");
           			note=vehicles[i].getAttribute("note");
           			
           			var gps = new GPS(message_id, vehicle_id, vehicle_num, longi, lati, longi_correct, lati_correct,
           					px, py, speed, direction, state, carState, speed_time, db_time, note);
           			
           			addGPSToPath(gps.lati, gps.longi);
           			gpses.push(gps);
       			}
     		}
   		}
	}
	});
}

</script>

</head>

<body>
	<div id="map-canvas" />
</body>

</html>