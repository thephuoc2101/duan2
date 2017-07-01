//dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;
var Schema = mongoose.Schema
,ObjectId = Schema.ObjectId;
//Schema
var friendsListSchema = new mongoose.Schema({
  _idFriendsList: {
        type: ObjectId,
        index: true
  },
  friendsList:[ //danh sách bạn bè
		{
			_idUser: String,
			name: String,
			avatar: String //láy id hình, lưu vào
		}
	]
});
//return models
module.exports = restful.model('friendslist',friendsListSchema);
