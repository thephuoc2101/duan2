//dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;
var Schema = mongoose.Schema
,ObjectId = Schema.ObjectId;
//Schema
var postSchema = new Schema({
  //post collection
 	 _idPost: {
         type: ObjectId,
         index: true
   },
 	 postTittle: String,
 	 content: String,
 	 countOfLikes: Number, //số lượng like của bài đăng
 	 likeBy: [ //danh sách người like
 		 {
 		 _Id: String,
 		 name: String
 	 	}
 	 ],
 	 postTime: { //thời gian đăng bài
  		type: Date,
  		default: Date.now //mặc định currenttime
  	},
 	illustrationPicture: String,//ảnh minh họa - láy id hình, lưu vào
 	description: String,	//mô tả ngắn về món ăn
 	eatPeopleCount: Number, //số lượng người ăn
 	cookTimeLimit: Number, //nấu trong bao lâu
 	ingredient:[ //nguyên liệu
 		{
 			content: String,
      howMany: Number
 		}
 	],
 	direction: [ //các bước thực hiện
 		{
 			ordinalNumber: Number, //stt
 			content: String,
 			illustrationPicture: String,//ảnh minh họa - láy id hình, lưu vào
 		}
 	],
 	publicPost: Boolean	//bài đăng có public ko?
});
//return models
module.exports = restful.model('post',postSchema);
