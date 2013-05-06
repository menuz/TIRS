<%@ page language="java" pageEncoding="GBK"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link rel='stylesheet' type='text/css' href='css/main.css'>
<script
	src="https://maps.googleapis.com/maps/api/js?v=AIzaSyAaayFfpVa4X7gsYqu_MCQLDKmdXLoJ5D8&sensor=true"></script>
<script src="js/jquery-1.8.3.js"></script>

<script type="text/javascript">
var map;
var poly;
var markers = new Array();
var count;
var intervalId;

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

var gpses;

function clear() {
	if(poly != null) {
		poly.setMap(null);
	}
	if(markers != null) {
		for(var i=0; i<markers.length; i++) {
			markers[i].setMap(null);
		}
	}
	window.clearInterval(intervalId);
	count=0;
}  

 function play() {
	if(gpses == null) {
		alert("Query first");
	} else {
		clear();
		var polyOptions = {
					strokeColor : '#000000',
					strokeOpacity : 1.0,
					strokeWeight : 3
				}
		poly = new google.maps.Polyline(polyOptions);
		poly.setMap(map);
		path = poly.getPath();
		markers = new Array();
		
		count=0;
		intervalId = window.setInterval("drawTrace()",1000);
	}
}  
 
function pause() {
	if(gpses == null || gpses.length == 0) {
		alert("Query first");
		if(count == 0) {
			alert("Play first");
			window.clearInterval(intervalId);
		}
	}
}

function continuePlay() {
	if(gpses == null || gpses.length == 0) {
		alert("Query first");
		if(count == 0) {
			alert("Play first");
			intervalId = window.setInterval("drawTrace()",1000);
		}
	}
	
}

function show() {
	clear();
	var polyOptions = {
				strokeColor : '#000000',
				strokeOpacity : 1.0,
				strokeWeight : 3
			}
	poly = new google.maps.Polyline(polyOptions);
	poly.setMap(map);
	path = poly.getPath();
	markers = new Array();
	for (var i=0;i<gpses.length;i++) {
		gps = gpses[i];
		addGPS(gps, path);
	}
}

function drawTrace() {
	if(count < gpses.length) {
		gps = gpses[count];
		addGPS(gps, path);
		count++;
	} else {
		count = 0;
		window.clearInterval(intervalId);
	}
}

// js single thread make ui pause too 
/* function pause(millisecondi)
{
     var now = new Date();
     var exitTime = now.getTime() + millisecondi;
     while(true)
     {
         now = new Date();
         if(now.getTime() > exitTime) return;
     }
} */

function addGPS(gps, path) {
	if(map != null) {
		// add gps to Poly Path 
		var p;
		if(map.mapTypeId == google.maps.MapTypeId.ROADMAP) {
			p = new google.maps.LatLng(gps.lati_correct, gps.longi_correct);
		} else {
			p = new google.maps.LatLng(gps.lati, gps.longi);
		}
		path.push(p);
		
		var marker;
		if(gps.state == 1) {
			marker = new google.maps.Marker({
				position : p,
				title : '#' + path.getLength(),
				map: map,
				icon: 'image/red.png'
			});
		} else {
			marker = new google.maps.Marker({
				position : p,
				title : '#' + path.getLength(),
				map: map,
				icon: 'image/green.png'
			});
		}
		
		markers.push(marker);
	}
}
	 
function query(vehicleId, vNum, time1, time2){
	alert("map query " + vehicleId);
	jQuery.ajax({
	url: "PublicXMLFeed",
		data:{vehicle_id:vehicleId, v_num: vNum, time1: time1, time2: time2},
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
       		
       		clear();
       		
       			var mapType = map.getMapTypeId();
       			var vehicles = xmldoc.getElementsByTagName("gps");
       			
       			var polyOptions = {
       					strokeColor : '#000000',
       					strokeOpacity : 1.0,
       					strokeWeight : 3
       				}
       			poly = new google.maps.Polyline(polyOptions);
       			poly.setMap(map);
       			var path = poly.getPath();
       			
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
           			
           			addGPS(gps, path);
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