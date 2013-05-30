
<%@ page language="java" pageEncoding="GBK"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link rel='stylesheet' type='text/css' href='css/main.css'>
<script type="text/javascript"
      src="http://maps.googleapis.com/maps/api/js?key=AIzaSyCwLdQoVkV7CttqAoidnBTeIlJf8cfdC6o&sensor=false">
</script>

<script src="js/jquery-1.8.3.js"></script>

<script type="text/javascript">
	var map;
	var directionsService;
	var directionsDisplay;

	function initialize() {
		var mapOptions = {
			center : new google.maps.LatLng(30.0, 120.0),
			zoom : 10,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById("map-canvas"),
				mapOptions);
		
		//初始化右键菜单，在初始化地图中一并初始化了。
		var ContextMenuControlDiv = document.createElement('DIV');
		var ContextMenuControl = new createContextMenu(ContextMenuControlDiv, map);

		ContextMenuControlDiv.index = 1;
		/*增加层的方式*/
		map.controls[google.maps.ControlPosition.TOP_LEFT].push(ContextMenuControlDiv);
	}
	
	function clearTrajectory() {
		if (poly != null) {
			poly.setMap(null);
		}
		if (markers != null) {
			for ( var i = 0; i < markers.length; i++) {
				markers[i].setMap(null);
			}
		}
		window.clearInterval(intervalId);
		count = 0;
	}
	
	function clearFindPassenger() {
		directionsService = null;
		if(directionsDisplay != null){
			directionsDisplay.setMap(null);
			directionsDisplay = null;
		}
	}
	
	function clearRouteSchedule() {
		directionsService = null;
		if(directionsDisplay != null){
			directionsDisplay.setMap(null);
			directionsDisplay = null;
		}
	}
	
	function clear() {
		clearTrajectory();
		clearFindPassenger();
		clearRouteSchedule();
	}
	
	
	/* ---------------------------------- trajectory --------------------------------------- */
	var gpses;
	var poly;
	var markers = new Array();
	var count;
	var intervalId;
	
	 function GPS(message_id, vehicle_id, vehicle_num, longi, lati,
			longi_correct, lati_correct, px, py, speed, direction, state,
			carState, speed_time, db_time, note) {
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

	function play() {
		if (gpses == null) {
			alert("Query first");
		} else {
			clearTrajectory();
			var polyOptions = {
				strokeColor : '#000000',
				strokeOpacity : 1.0,
				strokeWeight : 3
			}
			poly = new google.maps.Polyline(polyOptions);
			poly.setMap(map);
			path = poly.getPath();
			markers = new Array();

			count = 0;
			intervalId = window.setInterval("drawTrace()", 1000);
		}
	}

	function pause() {
		if (gpses == null || gpses.length == 0) {
			alert("Query first");
			if (count == 0) {
				alert("Play first");
				window.clearInterval(intervalId);
			}
		}
	}

	function continuePlay() {
		if (gpses == null || gpses.length == 0) {
			alert("Query first");
			if (count == 0) {
				alert("Play first");
				intervalId = window.setInterval("drawTrace()", 1000);
			}
		}
	}

	function show() {
		clearTrajectory();
		var polyOptions = {
			strokeColor : '#000000',
			strokeOpacity : 1.0,
			strokeWeight : 3
		}
		poly = new google.maps.Polyline(polyOptions);
		poly.setMap(map);
		path = poly.getPath();
		markers = new Array();
		for ( var i = 0; i < gpses.length; i++) {
			gps = gpses[i];
			addGPS(gps, path);
		}
	}

	function drawTrace() {
		if (count < gpses.length) {
			gps = gpses[count];
			addGPS(gps, path);
			count++;
		} else {
			count = 0;
			window.clearInterval(intervalId);
		}
	}

	function addGPS(gps, path) {
		if (map != null) {
			// add gps to Poly Path 
			var p;
			//alert("1");
			if (map.mapTypeId == google.maps.MapTypeId.ROADMAP) {
				p = new google.maps.LatLng(gps.lati_correct, gps.longi_correct);
				//alert("2");
			} else {
				//alert("3");
				p = new google.maps.LatLng(gps.lati, gps.longi);
			}
			
			if(path != null)
				path.push(p);

			var marker;
			if (gps.state == 1) {
				marker = new google.maps.Marker({
					position : p,
					title : '#',
					map : map,
					icon : 'image/red.png'
				});
			} else {
				marker = new google.maps.Marker({
					position : p,
					title : '#',
					map : map,
					icon : 'image/green.png'
				});
			}

			markers.push(marker);
		}
	}
	
	function query2(content) {
		alert("map query " + content);
		jQuery
				.ajax({
					url : "PublicXMLFeed",
					data : {
						command : "line",
						content : content
					},
					complete : function(xhr, textStatus) {
						if (xhr.readyState == 4) {
							if (xhr.status == 200 || xhr.status == 304) {
								var xmlText = xhr.responseText;
								if (window.ActiveXObject) {
									xmldoc = new ActiveXObject(
											"Microsoft.XMLDOM");
									xmldoc.async = "false";
									xmldoc.loadXML(xmlText);
								} else {
									parser = new DOMParser();
									xmldoc = parser.parseFromString(xmlText,
											"text/xml");
								}

								 clear();

								var mapType = map.getMapTypeId();
								var vehicles = xmldoc
										.getElementsByTagName("point");

								/* var polyOptions = {
									strokeColor : '#000000',
									strokeOpacity : 1.0,
									strokeWeight : 3
								}
								poly = new google.maps.Polyline(polyOptions);
								poly.setMap(map);
								var path = poly.getPath();*/

								gpses = new Array(); 
								
								for ( var i = 0; i < vehicles.length; i++) {
									
									longi = vehicles[i].getAttribute("longitude");
									lati = vehicles[i].getAttribute("latitude");
									longi_correct = vehicles[i].getAttribute("longitude_correct");
									lati_correct = vehicles[i].getAttribute("latitude_correct");

									var gps = new GPS(null, null,
											null, longi, lati,
											longi_correct, lati_correct, null,
											null, null, null, 1,
											null, null, null, null);

									addGPS(gps, null);
									gpses.push(gps);
								}
							}
						}
					}
				});
	}
	

	function query(vehicleId, vNum, time1, time2) {
		alert("map query " + vehicleId);
		jQuery
				.ajax({
					url : "PublicXMLFeed",
					data : {
						command : "trajectory",
						vehicle_id : vehicleId,
						v_num : vNum,
						time1 : time1,
						time2 : time2
					},
					complete : function(xhr, textStatus) {
						if (xhr.readyState == 4) {
							if (xhr.status == 200 || xhr.status == 304) {
								var xmlText = xhr.responseText;
								if (window.ActiveXObject) {
									xmldoc = new ActiveXObject(
											"Microsoft.XMLDOM");
									xmldoc.async = "false";
									xmldoc.loadXML(xmlText);
								} else {
									parser = new DOMParser();
									xmldoc = parser.parseFromString(xmlText,
											"text/xml");
								}

								clear();

								var mapType = map.getMapTypeId();
								var vehicles = xmldoc
										.getElementsByTagName("gps");

								var polyOptions = {
									strokeColor : '#000000',
									strokeOpacity : 1.0,
									strokeWeight : 3
								}
								poly = new google.maps.Polyline(polyOptions);
								poly.setMap(map);
								var path = poly.getPath();

								gpses = new Array();

								for ( var i = 0; i < vehicles.length; i++) {
									message_id = vehicles[i].getAttribute(
											"message_id").toString();
									vehicle_id = vehicles[i]
											.getAttribute("vehicle_id");
									vehicle_num = vehicles[i]
											.getAttribute("vehicle_num");

									longi = vehicles[i].getAttribute("longi");
									lati = vehicles[i].getAttribute("lati");

									longi_correct = vehicles[i]
											.getAttribute("longi_correct");
									lati_correct = vehicles[i]
											.getAttribute("lati_correct");

									px = vehicles[i].getAttribute("px");
									py = vehicles[i].getAttribute("py");
									speed = vehicles[i].getAttribute("speed");
									direction = vehicles[i]
											.getAttribute("direction");
									state = vehicles[i].getAttribute("state")
											.toString();
									carState = vehicles[i]
											.getAttribute("carState");
									speed_time = vehicles[i]
											.getAttribute("speed_time");
									db_time = vehicles[i]
											.getAttribute("db_time");
									note = vehicles[i].getAttribute("note");

									var gps = new GPS(message_id, vehicle_id,
											vehicle_num, longi, lati,
											longi_correct, lati_correct, px,
											py, speed, direction, state,
											carState, speed_time, db_time, note);

									addGPS(gps, path);
									gpses.push(gps);
								}
							}
						}
					}
				});
	}
	
	/* ----------------------------------------- findpassenger -------------------------------------*/
	function fp_query(lati, longi, time) {
		jQuery
		.ajax({
			url : "PublicXMLFeed",
			data : {
				command : "findpassenger", 
				lati : lati,
				longi : longi,
				time : time + ""
			},
			complete : function(xhr, textStatus) {
				if (xhr.readyState == 4) {
					if (xhr.status == 200 || xhr.status == 304) {
						var xmlText = xhr.responseText;
						if (window.ActiveXObject) {
							xmldoc = new ActiveXObject(
									"Microsoft.XMLDOM");
							xmldoc.async = "false";
							xmldoc.loadXML(xmlText);
						} else {
							parser = new DOMParser();
							xmldoc = parser.parseFromString(xmlText,
									"text/xml");
						}
						ps_route(xmldoc);
					}
				}
			}
		});
	}
	
	function ps_route(xmldoc) {
		clear();
		var startpoint = xmldoc
		.getElementsByTagName("startpoint");
		var waypoints = xmldoc
		.getElementsByTagName("waypoint");
		var endpoint = xmldoc
		.getElementsByTagName("endpoint");
		
		directionsService = new google.maps.DirectionsService();
		directionsDisplay = new google.maps.DirectionsRenderer();
		directionsDisplay.setMap(map);

		var ori = new google.maps.LatLng(
				startpoint[0].getAttribute("lati"), 
				startpoint[0].getAttribute("longi"));
		var dest = new google.maps.LatLng(
				endpoint[0].getAttribute("lati"), 
				endpoint[0].getAttribute("longi"));

		var request = {
		            origin: ori,
		            destination: dest,
		            travelMode: google.maps.TravelMode['DRIVING']
		};

		for ( var i = 0; i < waypoints.length; i++) {
			lati = waypoints[i].getAttribute("lati").toString();
			longi = waypoints[i].getAttribute("longi").toString();
			waypoint = new google.maps.LatLng(30.2905, 120.1670);
			request.waypoints.push({
	            location: waypoint,
	            stopover: true
	          });
		}
		
		directionsService.route(request, function(response, status) {
	          if (status == google.maps.DirectionsStatus.OK) {
	            	directionsDisplay.setDirections(response);
	          } else { 
	           	    alert("Directions Request Failed, "+status);
	          }
		});
	}
	
	function fp_draw() {
		clear();
		directionsService = new google.maps.DirectionsService();
		directionsDisplay = new google.maps.DirectionsRenderer();
		directionsDisplay.setMap(map);

		var ori = new google.maps.LatLng(30.2251, 120.0414);
		var dest = new google.maps.LatLng(30.2905, 120.1670);

		var request = {
		            origin: ori,
		            destination: dest,
		            travelMode: google.maps.TravelMode['DRIVING']
		};

		var waypoint1 = new google.maps.LatLng(30.2905, 120.1670);
		var waypoint2 = new google.maps.LatLng(30.2905, 120.1670);
		var waypoint3 = new google.maps.LatLng(30.2905, 120.1670);

		request.waypoints = [{location: waypoint1, stopover: true},
		                               {location: waypoint2, stopover: true},
		                               {location: waypoint3, stopover: true}];

		directionsService.route(request, function(response, status) {
		          if (status == google.maps.DirectionsStatus.OK) {
		            directionsDisplay.setDirections(response);
		            routes = response.routes;
		            gpses = routes[0].overview_path;
		          } else { 
		            alert("Directions Request Failed, "+status);
		          }
		});
	}
	
/* ------------------------------------------------- route schedule --------------------*/
var $O,$M,$L;
(function(undefined){
	O = function (id) {
		return "string" == typeof id ? document.getElementById(id):id;
	};
	M = {
			mark:function(map,latLng,title){
				if(title)
				return new google.maps.Marker({
					icon: this.icon,
					position: latLng,
					map: map,
					title:title
				});
				else 
				return new google.maps.Marker({
					//icon: this.icon,
					position: latLng,
					map: map
				});
			}
		}
	
	//event 事件
	L = {
		listen:null,
		add:function(dom,event,fn){
			return google.maps.event.addDomListener(dom, event, fn);
		}
	}
	
	$M = M;
	$L = L;
	$O = O;
})();

var start;
var startMarker = null;
var end;
var endMarker = null;
var cur;

function setStartPoint() {
	start = cur;
	if(startMarker != null) startMarker.setMap(null);
	startMarker = $M.mark(map, cur, "Start");
	
	self.parent.frames["tree"].setStartPoint(cur);
}

function setEndPoint() {
	end = cur;
	if(endMarker != null) endMarker.setMap(null);
	endMarker = $M.mark(map, cur, "End");
	
	self.parent.frames["tree"].setEndPoint(cur);
}

/*创建右键菜单*/
function createContextMenu(controlUI,map) {
	contextmenu = document.createElement("div");
	contextmenu.style.display = "none";
	contextmenu.style.background = "#ffffff";
	contextmenu.style.border = "1px solid #8888FF";
	contextmenu.innerHTML = 
	  "<a href='javascript:setStartPoint()'><div class='context'> 设为起始点 </div></a>"
	+ "<a href='javascript:setEndPoint()'><div class='context'> 设为终点 </div></a>";
	controlUI.appendChild(contextmenu);
	/*给整个地图增加右键事件监听*/
	$L.add(map, 'rightclick', function (event) {
		cur = event.latLng;
		// 起始 方法详细内容
		/* $O("info").innerText = event.latLng.lat()+"\n"+event.latLng.lng();
		var ss = "\n\n";
		for(var e in event.pixel)
			ss = ss+ e+":"+event.pixel[e]+"\n";
		$O("info").innerText = $O("info").innerText+ ss; */
		//结束 方法详细内容

		contextmenu.style.position="relative";
		contextmenu.style.left=(event.pixel.x-80)+"px";	//平移显示以对应右键点击坐标
		contextmenu.style.top=event.pixel.y+"px";
		contextmenu.style.display = "block";
	});
	/*点击菜单层中的某一个菜单项，就隐藏菜单*/
	$L.add(controlUI, 'click', function () {
		contextmenu.style.display = "none";
	});
	
	$L.add(map, 'click', function () {
		contextmenu.style.display = "none";
	});
	$L.add(map, 'drag', function () {
		contextmenu.style.display = "none";
	});
}

function rs_query(lati1, longi1, lati2, longi2, time) {
	clear();
	directionsService = new google.maps.DirectionsService();
	directionsDisplay = new google.maps.DirectionsRenderer();
	directionsDisplay.setMap(map);

	var ori = new google.maps.LatLng(lati1, longi1);
	var dest = new google.maps.LatLng(lati2, longi2);

	var request = {
	            origin: ori,
	            destination: dest,
	            travelMode: google.maps.TravelMode['DRIVING']
	};

	directionsService.route(request, function(response, status) {
	          if (status == google.maps.DirectionsStatus.OK) {
	              directionsDisplay.setDirections(response);
	              
	              routes = response.routes;
	              alert("route size = " + routes.length);
	              
	              gpsList = routes[0].overview_path;
	              alert("size = " + gpsList.length);
	              
	              for(var i in gpsList) {
	            	  $M.mark(map, gpsList[i], gpsList[i].lat()+","+gpsList[i].lng());
	              }
	              
	          } else { 
	              alert("Directions Request Failed, "+status);
	          }
	});
}

/*---------------------------------- extract arc detail ---------------------------*/
var idx = 0;
var arcList;
var markerList = new Array();
var arcIntervalId;
var pathList = new Array();

function Arc(id, start_node_id, end_node_id, lati1, longi1, lati2, longi2) {
	this.id = id;
	this.start_node_id = start_node_id;
	this.end_node_id = end_node_id;
	this.lati1 = lati1;
	this.longi1 = longi1;
	this.lati2 = lati2;
	this.longi2 = longi2;
}

function clearArc() {
	for(var i in markerList) {
		markerList[i].setMap(null);
	}
}

function getArcDetail() {
		clearArc();	
		arc = arcList[idx];
		arc_route(arc.id, arc.lati1, arc.longi1, arc.lati2, arc.longi2, 0);
		idx++;
		if(idx >= arcList.length) {
			idx = 0;
			window.clearInterval(arcIntervalId);
		}
}

function getNodeDetail() {
	clearArc();	
	arc = arcList[idx];
	arc_node(arc.id, arc.start_node_id, arc.end_node_id, arc.lati1, arc.longi1, arc.lati2, arc.longi2);
	idx++;
	if(idx >= arcList.length) {
		idx = 0;
		window.clearInterval(arcIntervalId);
	}
}

function arc_arc_play() {
	idx = 0;
	arcIntervalId = window.setInterval("getArcDetail()", 5000);
}

function arc_node_play() {
	idx = 0;
	arcIntervalId = window.setInterval("getNodeDetail()", 1000);
}

function setWindow(lati, longi, content) {
/* 	alert("content" + content);
 */	infowindow = new google.maps.InfoWindow();
	infowindow.setContent(content);
	g = new google.maps.LatLng(lati, longi);
	marker = new google.maps.Marker({
		position : g,
		title : '#',
		map : map,
		icon : 'image/red.png'
	});
	infowindow.open(map, marker);
}

function arc_node(id, start_node_id, end_node_id, lati1, longi1, lati2, longi2) {
	 var infowindow1 = new google.maps.InfoWindow();
     infowindow1.setContent(id + '  ' + start_node_id);
     var infowindow2 = new google.maps.InfoWindow();
     infowindow2.setContent(id+'   ' + end_node_id);
     
     markerList = new Array();
     g1 = new google.maps.LatLng(lati1, longi1);
     g2 = new google.maps.LatLng(lati2, longi2);
     marker1 = $M.mark(map, g1, '');
     marker2 = $M.mark(map, g2, '');
     markerList.push(marker1);
     markerList.push(2);
     
     infowindow1.open(map,marker1);
     infowindow2.open(map,marker2);
}

function arc_route(id, lati1, longi1, lati2, longi2, time) {
	clear();
	directionsService = new google.maps.DirectionsService();
	directionsDisplay = new google.maps.DirectionsRenderer();
	directionsDisplay.setMap(map);

	var ori = new google.maps.LatLng(lati1, longi1);
	var dest = new google.maps.LatLng(lati2, longi2);

	var request = {
	            origin: ori,
	            destination: dest,
	            travelMode: google.maps.TravelMode['WALKING']
	};

	directionsService.route(request, function(response, status) {
	          if (status == google.maps.DirectionsStatus.OK) {
	              directionsDisplay.setDirections(response);
	              routes = response.routes;
	              gpsList = routes[0].overview_path;
	              
	              var infowindow = new google.maps.InfoWindow();
	              infowindow.setContent(id+'');
	              
	              markerList = new Array();
	              
	              var latStr = '';
	              var lonStr = '';
	              
	              for(var i in gpsList) {
	            	  marker = $M.mark(map, gpsList[i], gpsList[i].lat()+","+gpsList[i].lng());
	            	  markerList.push(marker);
	            	  
	            	  latStr = latStr + gpsList[i].lat() + ',';
	            	  lonStr = lonStr + gpsList[i].lng() +',';
	              }
	              infowindow.open(map,marker);
	              
	              arc_push(id, latStr, lonStr);
	          } else { 
	              alert("Directions Request Failed, "+status);
	          }
	});
}

function arc_push(arcid, latStr, lonStr) {
	jQuery.ajax({
		url : "PublicXMLFeed",
		data : {
			command : "arcpush",
			arcid : arcid,
			latstr : latStr,
			lonstr : lonStr
		},
		complete : function(xhr, textStatus) {
			if (xhr.readyState == 4) {
				if (xhr.status == 200 || xhr.status == 304) {
					
				}
			}
		}
	});
}

/**
 * show all arc
 */
function arc_query() {
	jQuery.ajax({
				url : "PublicXMLFeed",
				data : {
					command : "arc"
				},
				complete : function(xhr, textStatus) {
					if (xhr.readyState == 4) {
						if (xhr.status == 200 || xhr.status == 304) {
							var xmlText = xhr.responseText;
							if (window.ActiveXObject) {
								xmldoc = new ActiveXObject(
										"Microsoft.XMLDOM");
								xmldoc.async = "false";
								xmldoc.loadXML(xmlText);
							} else {
								parser = new DOMParser();
								xmldoc = parser.parseFromString(xmlText,
										"text/xml");
							}

							var mapType = map.getMapTypeId();
							pointList = xmldoc
									.getElementsByTagName("point");
							
							arcList = new Array();
							
							
							for ( var i = 0; i < pointList.length; i++) {
								id = pointList[i].getAttribute("id");
								start_node_id = pointList[i].getAttribute("start_node_id");
								end_node_id = pointList[i].getAttribute("end_node_id");
								lati1 = pointList[i].getAttribute("lati1");
								longi1 = pointList[i].getAttribute("longi1");
								lati2 = pointList[i].getAttribute("lati2");
								longi2 = pointList[i].getAttribute("longi2");

								var arc = new Arc(id, start_node_id, end_node_id, lati1, longi1, lati2, longi2);
								arcList.push(arc);
								
								var polyOptions = {
										strokeColor : '#000000',
										strokeOpacity : 1.0,
										strokeWeight : 3
									}
								poly = new google.maps.Polyline(polyOptions);
								poly.setMap(map);
								var path = poly.getPath();
								
								p1 = new google.maps.LatLng(lati1, longi1);
								p2 = new google.maps.LatLng(lati2, longi2);
								path.push(p1);
								path.push(p2);
								pathList.push(path);
								
							}
						}
					}
				}
			});
}


/*-------------------------------------------------- cluster ----------------------------------------------*/ 
 function cluster() {
		jQuery
		.ajax({
			url : "PublicXMLFeed",
			data : {
				command : "cluster"
			},
			complete : function(xhr, textStatus) {
				if (xhr.readyState == 4) {
					if (xhr.status == 200 || xhr.status == 304) {
						var xmlText = xhr.responseText;
						if (window.ActiveXObject) {
							xmldoc = new ActiveXObject(
									"Microsoft.XMLDOM");
							xmldoc.async = "false";
							xmldoc.loadXML(xmlText);
						} else {
							parser = new DOMParser();
							xmldoc = parser.parseFromString(xmlText,
									"text/xml");
						}

						var mapType = map.getMapTypeId();
						var gpsList = xmldoc
								.getElementsByTagName("gps");
						var clusterList = xmldoc
								.getElementsByTagName("cluster");

						var polyOptions = {
							strokeColor : '#000000',
							strokeOpacity : 1.0,
							strokeWeight : 3
						}
						poly = new google.maps.Polyline(polyOptions);
						poly.setMap(map);
						var path = poly.getPath();

						gpses = new Array();

						for ( var i = 0; i < gpsList.length; i++) {
							id = gpsList[i].getAttribute("id");							
							longi = gpsList[i].getAttribute("longi");
							lati = gpsList[i].getAttribute("lati");
							clusterindex = gpsList[i].getAttribute("clusterindex");	
							
							p = new google.maps.LatLng(lati, longi);
							
							marker = new google.maps.Marker({
								position : p,
								title : '#',
								map : map,
								icon : 'image/green.png'
							});
							marker.setMap(map);
						}
						
						for ( var i = 0; i < clusterList.length; i++) {
							id = clusterList[i].getAttribute("id");							
							longi = clusterList[i].getAttribute("longi");
							lati = clusterList[i].getAttribute("lati");
							clusternum = clusterList[i].getAttribute("clusternum");	

							p = new google.maps.LatLng(lati, longi);
							
							marker = new google.maps.Marker({
								position : p,
								title : '#',
								map : map,
								icon : 'image/orange.png'
							});
							marker.setMap(map);
						}
					}
				}
			}
		});
	}
	
 /*-------------------------------------------------- box ----------------------------------------------*/ 

	
	function box(boxid) {
		jQuery
		.ajax({
			url : "PublicXMLFeed",
			data : {
				command : "line",
				content : "box+"+boxid
			},
			complete : function(xhr, textStatus) {
				if (xhr.readyState == 4) {
					if (xhr.status == 200 || xhr.status == 304) {
						var xmlText = xhr.responseText;
						if (window.ActiveXObject) {
							xmldoc = new ActiveXObject(
									"Microsoft.XMLDOM");
							xmldoc.async = "false";
							xmldoc.loadXML(xmlText);
						} else {
							parser = new DOMParser();
							xmldoc = parser.parseFromString(xmlText,
									"text/xml");
						}

						var mapType = map.getMapTypeId();
						var pointlist = xmldoc
								.getElementsByTagName("point");
						var nodelist = xmldoc
								.getElementsByTagName("node");

						gpses = new Array();
						

						var polyOptions = {
								strokeColor : '#000000',
								strokeOpacity : 1.0,
								strokeWeight : 3
							}
						
						// arc cross box
						for ( var i = 0; i < pointlist.length; i=i+2) {
							id =pointlist[i].getAttribute("id");
							lati_correct1 = pointlist[i].getAttribute("latitude_correct");							
							longi_correct1 = pointlist[i].getAttribute("longitude_correct");
							
							setWindow(lati_correct1, longi_correct1, 'Arc' + id);
							
							lati_correct2 = pointlist[i+1].getAttribute("latitude_correct");							
							longi_correct2 = pointlist[i+1].getAttribute("longitude_correct");
							
							p1 = new google.maps.LatLng(lati_correct1, longi_correct1);
							p2 = new google.maps.LatLng(lati_correct2, longi_correct2);
							
							marker1 = new google.maps.Marker({
								position : p1,
								title : '#',
								map : map,
								icon : 'image/red.png'
							});
							marker1.setMap(map);
							
							marker2 = new google.maps.Marker({
								position : p2,
								title : '#',
								map : map,
								icon : 'image/red.png'
							});
							marker2.setMap(map);
							
							var polyOptions = {
									strokeColor : '#000000',
									strokeOpacity : 1.0,
									strokeWeight : 3
								}
							
							poly = new google.maps.Polyline(polyOptions);
							poly.setMap(map);
							var path = poly.getPath();
						    path.push(p1);
							path.push(p2);
						} 
						
						poly = new google.maps.Polyline(polyOptions);
						poly.setMap(map);
						var path = poly.getPath();
						
						poly_correct = new google.maps.Polyline(polyOptions);
						poly_correct.setMap(map);
						var path_correct = poly_correct.getPath();
						
						// node green node is real gps red node is correct gpss
						for ( var i = 0; i < nodelist.length; i++) {
							lati = nodelist[i].getAttribute("latitude");							
							longi = nodelist[i].getAttribute("longitude");
							
							lati_correct = nodelist[i].getAttribute("latitude_correct");							
							longi_correct = nodelist[i].getAttribute("longitude_correct");
							
							id = nodelist[i].getAttribute("id");
							
							p = new google.maps.LatLng(lati, longi);
							
							p_correct = new google.maps.LatLng(lati_correct, longi_correct);
							
							/* marker = new google.maps.Marker({
								position : p,
								title : '#',
								map : map,
								icon : 'image/green.png'
							});
							marker.setMap(map); */
							
							if(i==0) {
								setWindow(lati_correct, longi_correct, 'Box ' + id);
							}
							
							 marker_correct = new google.maps.Marker({
								position : p_correct,
								title : '#',
								map : map,
								icon : 'image/red.png'
							});
							marker_correct.setMap(map); 
							
							//path.push(p);
							 path_correct.push(p_correct); 
						}
					}
				}
			}
		});
	}
 
	/*-------------------------------------------------- arc ----------------------------------------------*/ 

function arcdetail(arcid) {
		jQuery
		.ajax({
			url : "PublicXMLFeed",
			data : {
				command : "line",
				content : "arcdetail+"+arcid
			},
			complete : function(xhr, textStatus) {
				if (xhr.readyState == 4) {
					if (xhr.status == 200 || xhr.status == 304) {
						var xmlText = xhr.responseText;
						if (window.ActiveXObject) {
							xmldoc = new ActiveXObject(
									"Microsoft.XMLDOM");
							xmldoc.async = "false";
							xmldoc.loadXML(xmlText);
						} else {
							parser = new DOMParser();
							xmldoc = parser.parseFromString(xmlText,
									"text/xml");
						}

						var mapType = map.getMapTypeId();
						var pointlist = xmldoc
								.getElementsByTagName("point");

						gpses = new Array();


						/* var polyOptions = {
								strokeColor : '#000000',
								strokeOpacity : 1.0,
								strokeWeight : 3
							}
						
						poly = new google.maps.Polyline(polyOptions);
						poly.setMap(map);
						var path = poly.getPath();
						for ( var i = 0; i < pointlist.length; i=i+1) {
							lati = pointlist[i].getAttribute("latitude");							
							longi = pointlist[i].getAttribute("longitude");
							
							p = new google.maps.LatLng(lati, longi);
							
							marker = new google.maps.Marker({
								position : p,
								title : '#',
								map : map,
								icon : 'image/green.png'
							});
							marker.setMap(map);
							
							path.push(p);
						} */
						
						
						var polyOptions = {
								strokeColor : '#000000',
								strokeOpacity : 1.0,
								strokeWeight : 3
							}
						
						poly = new google.maps.Polyline(polyOptions);
						poly.setMap(map);
						var path = poly.getPath();
						for ( var i = 0; i < pointlist.length; i=i+1) {
							id = pointlist[i].getAttribute("id");
							lati = pointlist[i].getAttribute("latitude_correct");							
							longi = pointlist[i].getAttribute("longitude_correct");
							
							if(i==0) {
								setWindow(lati, longi, 'Arc ' + id);
							}
							
							p = new google.maps.LatLng(lati, longi);
							
							marker = new google.maps.Marker({
								position : p,
								title : '#',
								map : map,
								icon : 'image/red.png'
							});
							marker.setMap(map);
							
							path.push(p);
						}
					}
				}
			}
		});
	}
</script>

</head>

<body onload="initialize()">
	<div id="map-canvas" />
</body>

</html>