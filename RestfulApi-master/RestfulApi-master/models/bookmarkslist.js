//dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;
var Schema = mongoose.Schema
,ObjectId = Schema.ObjectId;
//Schema
var bookmarksListSchema = new mongoose.Schema({
  _idBookmarksList: {
        type: ObjectId,
        index: true
  },
  bookmarksList:[ //danh sách công thức đã lưu
		{
		 _idPost: String,//Lưu lại _idPost
		 name: String,
		 time: Date
	 }
 ]
});
//return models
module.exports = restful.model('bookmarkslist',bookmarksListSchema);
