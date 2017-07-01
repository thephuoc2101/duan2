//dependencies
var express = require('express');
var mongoose = require('mongoose');
var bodyParser = require('body-parser');

//connect to mongodb
var uri = "mongodb://cookbookdb:cookbookdb@ds159371.mlab.com:59371/cookbookdb";

mongoose.connect(uri);
mongoose.Promise = global.Promise;
mongoose.connection.on('connected', function(){
  console.log('Mongoose connected to: ' + uri);
});
mongoose.connection.on('error', function(err){
  console.log('Mongoose connection error: '+err);
});
mongoose.connection.on('error', function(){
  console.log('Mongoose disconnected');
});

//express
var app = express();
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

//routes
app.use('/api',require('./routes/api'));

//start server
var server = app.listen(process.env.PORT || 3000, function () {
  var port = server.address().port;
  console.log("App now running on port", port);
 });
