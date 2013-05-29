<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'pathtest.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<style type="text/css">
#dituContent {
	margin: 2px;
	background-color: #E5E3DF;
	border: 1px solid #999999;
	height: 90%;
	overflow: hidden;
	position: relative;
	width: 99.2%;
}
</style>
<%-- 导入jQuery --%>
<script src="js/jquery-1.7.2.min.js"></script>

<%-- 调用google map 接口 --%>
<script type="text/javascript"
			src="http://maps.google.com/maps/api/js?sensor=false"> </script>
<script type="text/javascript">
	var polyline;
	var map;
function initialize() {	
		//一个点
	   var latlng = new google.maps.LatLng(30.245695043444545, 120.08074284878535);
		//地图选项
	   var myOptions = {
	      zoom: 14,
	      center: latlng,
	      mapTypeId: google.maps.MapTypeId.ROADMAP    
	   };
		//地图对象
	   map = new google.maps.Map(document.getElementById("dituContent"),myOptions);
	}
	
	window.onload=function(){
		initialize();
	}


</script>
	</head>

	<body>
		<div id="ditu">
			<div id="dituContent"></div>
			<div>
				方向id<input type="text" id="dirid">
				<input type="button" value="查看路线" onclick="getpath(document.getElementById('dirid').value)">
			</div>
		</div>
		

		<script type="text/javascript">
  		var routePath = new Array(); //点集
  		var markersArray = new Array();//图标集
  		var lineArrow = null;
  		
  		lineArrow = {
    			  path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW
    		};
  		
  			function getpath(dirid){
  				jQuery.ajax({
  					url: "PublicXMLFeed",
  					data:{command:"pathtest",dirid:dirid},
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
  				       		var path = xmldoc.getElementsByTagName("path"); //获得多个path
  				       		if(path.length != 0 ){
  				       			for(var i=0;i<path.length;i++){
  				       				if (dirid==path[i].getAttribute("dirtag")) {
  				       					//获得一个方向上的点
  				       					var points_ = path[i].childNodes;
	  				       				for(var j=0;j<points_.length;j++){
	  				  		 				var lat = points_[j].getAttribute("latitude");
	  				  		 				var lng = points_[j].getAttribute("longitude");
	  				  		 				var point = new google.maps.LatLng(lat,lng);
	  				  		 				routePath.push(point);
	  				  		 				
		  				  		 			markersArray.push(new google.maps.Marker({
		  										position: point,
		  										icon: {
		  											path: google.maps.SymbolPath.CIRCLE,
		  											scale: 2
		  										}
		  					       			}));
		  				  		 			
	  				  		 			}
									}
  				       			}  		
  				       		if (markersArray && path) {
  				      		for (i in markersArray) {
  				      			markersArray[i].setMap(map);
  				          	}
	  				      	}else{
	  				      		alert("站点未正确标注！~请重选线路");
	  				      	}
  				       		polyline = new google.maps.Polyline({
			        			path: routePath,
			        			strokeColor: "#00008B",
			         			strokeOpacity: 1,
			         			strokeWeight: 2
			        		});
  				       	
	  				      	if(polyline){
	  				      	polyline.setPath(routePath);
	  				     	 polyline.setMap(map);
	  				      		//alert(path);
	  				      	}else{
	  				      		alert("线路未正确标注，请重选线路~！");
	  				      	}
	  				      	//map.panTo(centerPoint);
								
  				       			}
  				     		}
  				   		}
  					}
  					});
  				}
  		
  		</script>
	</body>
</html>
