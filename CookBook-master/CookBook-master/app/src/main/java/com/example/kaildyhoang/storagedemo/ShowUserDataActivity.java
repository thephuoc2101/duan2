package com.example.kaildyhoang.storagedemo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowUserDataActivity extends AppCompatActivity {

    private TextView mResult;
    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseRef;
//    private FirebaseRecyclerAdapter<ShowDataItemsActivity, ShowDataViewHolder> mFirebaseAdapter;

    private List<ShowUserDataItemsActivity> userList = new ArrayList<>();
    private UserAdapter mUserAdapter;

    public ShowUserDataActivity(){
//        require a empty constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_data);

        mResult = (TextView) findViewById(R.id.textViewDataResult);

        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseRef = firebaseDatabase.getReference('User_Details');\
        databaseRef = FirebaseDatabase.getInstance().getReference();

        mUserAdapter = new UserAdapter(userList);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewShow);
//        recyclerView.setLayoutManager(new LinearLayoutManager(ShowDataActivity.this));
//        Toast.makeText(ShowDataActivity.this,"Wait! Fetching list...", Toast.LENGTH_SHORT).show();


        //Make GET request - View information
        String ip ="192.168.1.19";
        new GetDataTask().execute("http://"+ip+":3000/api/users");
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowDataItemsActivity, ShowDataViewHolder>(ShowDataItemsActivity.class, R.layout.activity_show_data_items,ShowDataViewHolder.class,databaseRef) {
//            @Override
//            protected void populateViewHolder(final ShowDataViewHolder viewHolder, ShowDataItemsActivity model,final int position) {
//                viewHolder.Image_Title(model.getImage_Title());
//                viewHolder.Image_Url(model.getImage_Url());
//
////                Item Click
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDataActivity.this);
//                        builder.setMessage("Do you want to delete this data?").setCancelable(false)
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        int selectedItem = position;
//                                        mFirebaseAdapter.getRef(selectedItem).removeValue();
//                                        mFirebaseAdapter.notifyItemRemoved(selectedItem);
//                                        recyclerView.invalidate();
//                                        onStart();
//                                    }
//                                })
//                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog dialog = builder.create();
//                        dialog.setTitle("Confirm");
//                        dialog.show();
//                    }
//                });
//            }
//        };
//        recyclerView.setAdapter(mFirebaseAdapter);
//    }

    //    View holder for recycler view
//    public static class ShowDataViewHolder extends RecyclerView.ViewHolder{
//        private final TextView image_title;
//        private final ImageView image_url;
//
//        public ShowDataViewHolder(final View itemView) {
//            super(itemView);
//            image_title = (TextView) itemView.findViewById(R.id.fetch_title);
//            image_url = (ImageView) itemView.findViewById(R.id.fetch_image);
//        }
//
//        private void Image_Title(String title){
//            image_title.setText(title);
//        }
//        private void Image_Url(String url){
//            Glide.with(itemView.getContext())
//                    .load(url)
//                    .crossFade()
//                    .placeholder(R.drawable.load_icon)
//                    .thumbnail(0.1f)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(image_url);
//        }
//    }

    //Send GET request (View)
    class GetDataTask extends AsyncTask<String , Void, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ShowUserDataActivity.this);
            progressDialog.setMessage("Loading data ...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return getData(params[0]);
            }catch (IOException e){
                return "Network Error!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //set data response to textView
            try {

                JSONArray jsonArray = (JSONArray) new JSONTokener(result).nextValue();

                for(int i = 0 ;i <= jsonArray.length();i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ShowUserDataItemsActivity user = new ShowUserDataItemsActivity();

                    user.set_idUser(jsonObject.getString("_id"));
                    user.setName(jsonObject.getString("name"));
                    user.setBirthday(jsonObject.getString("birthday"));
                    user.setEmail(jsonObject.getString("email"));
                    user.setUserName(jsonObject.getString("userName"));
                    user.setPassword(jsonObject.getString("passWord"));
                    user.setAddress(jsonObject.getString("address"));
                    user.setDescription(jsonObject.getString("description"));
                    user.setImage_Url(jsonObject.getString("avatar"));
                    user.setGender(jsonObject.getString("gender"));
                    user.setCreateDay(jsonObject.getString("createDay"));
                    user.setCountOfPosts(Integer.parseInt(jsonObject.getString("countOfPosts")));
                    user.setCountOfFriends(Integer.parseInt(jsonObject.getString("countOfFriends")));
                    user.setCountOfBookmarks(Integer.parseInt(jsonObject.getString("countOfBookmarks")));
//                    user.setIdFriendsList(jsonObject.getString("idFriendsList"));
//                    user.setIdBookmarksList(jsonObject.getString("idBookmarksList"));

                    userList.add(user);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowUserDataActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mUserAdapter);
//                    if(jsonObject.has("avatar")){
////                        Toast.makeText(getApplicationContext(),"avatar: " + jsonObject.getString("avatar"), Toast.LENGTH_SHORT).show();
//                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
//            catch (ParseException e) {
//                e.printStackTrace();
//            }
//            mResult.setText(result.toString());
            if (progressDialog != null){
                progressDialog.dismiss();
            }
        }

        private String getData(String urlPath) throws IOException {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = null;

            try {
                //Initialize and config request, then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
            return result.toString();
        }
    };//End GetDataTask
}
