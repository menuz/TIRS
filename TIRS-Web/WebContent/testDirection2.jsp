<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>Google Maps JavaScript API v3 Example: Directions Travel Modes</title>
<style type="text/css">
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

#map_canvas {
  height: 100%;
}

@media print {
  html, body {
    height: auto;
  }

  #map_canvas {
    height: 650px;
  }
}
</style>
    <script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
    <script>
      var directionDisplay;
      var directionsService = new google.maps.DirectionsService();
      var map;
      
     /*  var ori = new google.maps.LatLng(30.228, 120.035);
      var dest = new google.maps.LatLng(30.2928, 120.1618);
      
      var waypoint1 = new google.maps.LatLng(30.2648, 120.0877);
      var waypoint2 = new google.maps.LatLng(30.2780, 120.0962);
      var waypoint3 = new google.maps.LatLng(30.2728, 120.1079); */
      
      var ori = new google.maps.LatLng(30.2251, 120.0414);
      var dest = new google.maps.LatLng(30.2905, 120.1670);
      
      var waypoint1 = new google.maps.LatLng(30.2624, 120.0916);
      var waypoint2 = new google.maps.LatLng(30.2708, 120.1015);
      var waypoint3 = new google.maps.LatLng(30.2707, 120.1128);

     function initialize() {
        directionsDisplay = new google.maps.DirectionsRenderer();
        var mapOptions = {
          zoom: 14,
          // ROADMAP
          mapTypeId: google.maps.MapTypeId.SATELLITE,
          center: ori
        }
        map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
      // If there are any parameters at eh end of the URL, they will be in  location.search
      var lat,lng,zoom,type;
      // looking something like  "?marker=3"
 
      // skip the first character, we are not interested in the "?"
      var query = location.search.substring(1);
 
      // split the rest at each "&" character to give a list of  "argname=value"  pairs
      var pairs = query.split("&");
      for (var i=0; i<pairs.length; i++) {
        // break each pair at the first "=" to obtain the argname and value
	  var pos = pairs[i].indexOf("=");
	  var argname = pairs[i].substring(0,pos).toLowerCase();
	  var value = pairs[i].substring(pos+1).toLowerCase();
 
        // process each possible argname  -  use unescape() if theres any chance of spaces
        if (argname == "mode"){
          elem = document.getElementById('mode');
	  numEntries = elem.length
          for (var e = 0; e<numEntries; e++){
            if (value.toUpperCase() == elem[e].value)
               elem.selectedIndex = e;
          }
        }
        if (argname == "waypt"){
          if (value.toUpperCase() == 'Y') 
            document.getElementById('waypoint').checked = true;
          else
            document.getElementById('waypoint').checked = false;
        }
        if (argname == "lat") {lat = parseFloat(value);}
        if (argname == "lng") {lng = parseFloat(value);}
        if (argname == "zoom") {zoom = parseInt(value);}
        if (argname == "type") {
// from the v3 documentation 8/24/2010
// HYBRID This map type displays a transparent layer of major streets on satellite images. 
// ROADMAP This map type displays a normal street map. 
// SATELLITE This map type displays satellite images. 
// TERRAIN This map type displays maps with physical features such as terrain and vegetation. 
          if (value == "m") {maptype = google.maps.MapTypeId.ROADMAP;}
          if (value == "k") {maptype = google.maps.MapTypeId.SATELLITE;}
          if (value == "h") {maptype = google.maps.MapTypeId.HYBRID;}
          if (value == "t") {maptype = google.maps.MapTypeId.TERRAIN;}

        }
      }
      directionsDisplay.setMap(map);
      calcRoute();
     }

      function calcRoute() {
        var selectedMode = document.getElementById('mode').value;
        var request = {
            origin: ori,
            destination: dest,
            // Note that Javascript allows us to access the constant
            // using square brackets and a string value as its
            // "property."
            travelMode: google.maps.TravelMode[selectedMode]
        };
        if (document.getElementById('waypoint').checked)
          request.waypoints = [{location: waypoint1, stopover: true},
                               {location: waypoint2, stopover: true},
                               {location: waypoint3, stopover: true}];

          directionsService.route(request, function(response, status) {
          if (status == google.maps.DirectionsStatus.OK) {
        	  alert("OK");
        	 /*  gpses = reponse.overview_path();
        	  alert("size " + gpses.length); */
            directionsDisplay.setDirections(response);
        	//alert("dddd")
        	routes = response.routes;
            gpses = routes[0].overview_path;
            //alert("godd");
            //alert("size of gpses = " +  gpses.length);
            //alert("gaga");
          } else { 
            alert("Directions Request Failed, "+status);
          }
        });
      }
    </script>
  </head>
  <body onload="initialize()">
    <div>
    <b>Mode of Travel: </b>
    <select id="mode" onchange="calcRoute();">
      <option value="DRIVING">Driving</option>
      <option value="WALKING">Walking</option>
      <option value="BICYCLING">Bicycling</option>
      <option value="TRANSIT">Transit</option>
    </select>
    use waypoint <input id="waypoint" type="checkbox" checked="checked" onchange="calcRoute();" onclick="calcRoute();">
    </div>
    <div id="map_canvas" style="top:30px;"></div>
<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "UA-162157-1";
urchinTracker();
</script>





</body>
</html>