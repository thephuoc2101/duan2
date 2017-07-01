//dependencies
var express = require('express');
var router = express.Router();

//get models
var users = require('../models/users');
var post = require('../models/post');
var friendslist = require('../models/friendslist');
var bookmarkslist = require('../models/bookmarkslist');

//routes
users.methods(['get','post','put','delete']);
users.register(router,'/users');
post.methods(['get','post','put','delete']);
post.register(router,'/post');
friendslist.methods(['get','post','put','delete']);
friendslist.register(router,'/friendslist');
bookmarkslist.methods(['get','post','put','delete']);
bookmarkslist.register(router,'/bookmarkslist');

//return router
module.exports = router;
