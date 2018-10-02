var express = require('express');
var app = express();
var fs = require("fs");

app.get('/listUsers', function(req, res){
	fs.readFile(__dirname+"/"+"users.json", 'utf8',
	function(err, data){
		console.log(data);
		res.end(data);
	});
});

app.get('/listAdmissions', function(req, res){
	fs.readFile(__dirname+"/"+"admissions.json", 'utf8',
	function(err, data){
		console.log(data);
		res.header("Access-Control-Allow-Origin", "*");
		res.end(data);
	});
});

app.use(function (req, res, next) {
res.header("Access-Control-Allow-Origin", "*");
res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
next();
});

var server = app.listen(8081, function(){
	var host = server.address().address;
	var port = server.address().port;
	console.log("Example app listening at http://%s:%s",host,port)
});