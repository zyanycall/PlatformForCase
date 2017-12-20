<%@ page language="java" contentType="text/html; UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="js/jquery-3.1.0.min.js"></script>
<script src="js/d3.js"></script>
</head>
<style>  
.node circle {  
  fill: #fff;  
  stroke: steelblue;  
  stroke-width: 1.5px;  
}  
  
.node {  
  font: 12px sans-serif;  
}  
  
.link {  
  fill: none;  
  stroke: #ccc;  
  stroke-width: 1.5px;  
}
</style>  
<body>
<script>
var width = 500,height = 500;  

var tree = d3.layout.tree().size([width, height-200]).separation(function(a, b) { return (a.parent == b.parent ? 1 : 2) / a.depth; });  

var diagonal = d3.svg.diagonal()
.projection(function(d) { return [d.y, d.x]; });  
  
var svg = d3.select("body").append("svg")
.attr("width", width)
.attr("height", height)
.append("g")
.attr("transform", "translate(40,0)");
</script>
</body>
</html>