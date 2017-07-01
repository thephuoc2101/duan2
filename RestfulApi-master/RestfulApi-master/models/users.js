//dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;
var Schema = mongoose.Schema
,ObjectId = Schema.ObjectId;
//Schema
var usersSchema = new Schema({
	_idUser: {
        type: ObjectId,
        index: true
  },
	name: String,
	birthday: Date,
	email: String,
	userName: String,
	passWord: String,
	address: String,
	description: String,
	avatar :	String, //láy id hình, lưu vào
	gender: String, //giới tính
	createDay: {
		type: Date,
		default: Date.now
	},
	countOfPosts: Number, //số lượng bài đăng
	countOfFriends: Number, //số lượng bạn
	countOfBookmarks: Number, //số lượng lưu lại công thức
	//link to friends collection
	idFriendsList: String,//liên kết thông qua id
	//link to bookmark collection
	idBookmarksList: String//liên kết thông qua id
});
//return models
module.exports = restful.model('users',usersSchema);
