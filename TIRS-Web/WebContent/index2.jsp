<%@ page language="java" pageEncoding="GBK"%>  

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>Complex polylines</title>

<jsp:include page="header.jsp"></jsp:include>  
	
<script>
	var poly;
	var map;
	var path;
	
	function addGPSToPath(lat, lon) {
		var p = new google.maps.LatLng(lat, lon);
		path.push(p);
		var marker = new google.maps.Marker({
			position : p,
			title : '#' + path.getLength(),
			map : map
		});
	}

	function initialize() {
		var chicago = new google.maps.LatLng(41.879535, -87.624333);
		var mapOptions = {
			zoom : 7,
			center : chicago,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};

		map = new google.maps.Map(document.getElementById('map-canvas'),
				mapOptions);

		var polyOptions = {
			strokeColor : '#000000',
			strokeOpacity : 1.0,
			strokeWeight : 3
		}
		poly = new google.maps.Polyline(polyOptions);
		poly.setMap(map);
		
		path = poly.getPath();
		
		var lat = 41.00;
		var lon = -87.00;
		var i;
		for(i=0; i<10; i++) {
			lat += 0.001;
			var j;
			for(j=0; j<100; j++) {
				lon -= 0.001;
				addGPSToPath(lat, lon);
			}
		}
	}

	google.maps.event.addDomListener(window, 'load', initialize);
</script>
</head>



<body>
	<div id="map-canvas"></div>
</body>
</html>