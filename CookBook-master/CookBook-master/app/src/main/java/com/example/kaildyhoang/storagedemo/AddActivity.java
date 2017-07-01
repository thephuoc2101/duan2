package com.example.kaildyhoang.storagedemo;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.client.Firebase;
import com.google.firebase.storage.UploadTask;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

    public class AddActivity extends AppCompatActivity  {

    private EditText _etxtName, _etxtUserName, _etxtPassword, _etxtBirthDay, _etxtEmail, _etxtAddress, _etxtDescription;
    private Button _btnDoAdd;
    private RadioGroup _rdoGrGender;
    private RadioButton _rdoBtnMale, _rdoBtnFemale, _rdoBtnOthers;
    private ImageView _imgVGetImage;
    private String _imgUrlReturn = null; //image url be returned after uploaded onto firebase
    private String _genderReturn = null;
    private RadioButton radioButton;
    private DateFormat dateFormat;
    private Calendar calendar;
    private String birthday;
    private String _dateNow;

    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog progressDialog;
    private Firebase mFirebase;
    private Uri mImgUri = null;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Firebase.setAndroidContext(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        //        Initialize the progressDialog
        progressDialog = new ProgressDialog(this);

        _etxtName = (EditText) findViewById(R.id.editTextInputName);
        _etxtUserName = (EditText) findViewById(R.id.editTextInputUserName);
        _etxtPassword = (EditText) findViewById(R.id.editTextInputPassword);
        _etxtBirthDay = (EditText) findViewById(R.id.editTextInputBirthDay);
        _etxtEmail = (EditText) findViewById(R.id.editTextInputEmail);
        _etxtAddress = (EditText) findViewById(R.id.editTextInputAddress);
        _etxtDescription = (EditText) findViewById(R.id.editTextInputDescription);

        _btnDoAdd = (Button) findViewById(R.id.buttonDoAdd);

        _rdoGrGender = (RadioGroup) findViewById(R.id.radioGroupGender);
        _rdoBtnMale = (RadioButton) findViewById(R.id.radioButtonMale);
        _rdoBtnFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        _rdoBtnOthers = (RadioButton) findViewById(R.id.radioButtonOthers);

        _imgVGetImage = (ImageView) findViewById(R.id.imageViewGetImage);

        _imgVGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Check for runtime permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Call for Permission", Toast.LENGTH_SHORT).show();

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                }else{
                    callgallery();
                }
            }
        });

        _etxtBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

//        Initialize Firebase Database paths for database and storage
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
//        Push will create new child every time we upload
        mFirebase = new Firebase("https://uploadimagetofirebase-3e420.firebaseio.com/").push();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://uploadimagetofirebase-3e420.appspot.com");

        _rdoGrGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                _genderReturn = String.valueOf(radioButton.getText().toString());
            }
        });

        _btnDoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make GET request - View information
//                new GetDataTask().execute("http://192.168.104.2:3000/api/users");

//                Toast.makeText(getApplicationContext(),birthday,Toast.LENGTH_SHORT).show();

                saveAllThings();

            }
        });
    } //End of onCreate

    //    Check for runtime permissions for storage access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callgallery();
                }
                return;
        }
        Toast.makeText(getApplicationContext(),"...",Toast.LENGTH_SHORT).show();
    }

    //    If access granted gallery open
    private void callgallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }
//    After selecting image from gallery image will directly uploaded to firebase db
//    and image will show in image view

    @SuppressWarnings("VisibleForTests")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            mImgUri = data.getData();
            _imgVGetImage.setImageURI(mImgUri);
            StorageReference filePath = mStorageRef.child("User_Images").child(mImgUri.getLastPathSegment());

            progressDialog.setMessage("Uploading image...");
            progressDialog.show();

            filePath.putFile(mImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl(); //Ignore this error

                    _imgUrlReturn = downloadUri.toString();

                    mFirebase.child("Image_Url").setValue(downloadUri.toString());
                    Glide.with(getApplicationContext())
                            .load(downloadUri)
                            .crossFade()
                            .placeholder(R.drawable.load_icon)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(_imgVGetImage);
                    Toast.makeText(getApplicationContext(),"Updated. ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void showDatePickerDialog(){
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year,month,dayOfMonth);
                birthday = String.valueOf(dateFormat.format(date.getTime()));
                _etxtBirthDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
//    Save all things function
    public void saveAllThings(){

//        Save image to firebase
        final String imgName = _etxtName.getText().toString();
        if(imgName.isEmpty()){
            Toast.makeText(getApplicationContext(),"Fill all Field", Toast.LENGTH_SHORT).show();
            return;
        }
        Firebase childRef_name = mFirebase.child("Image_Title");
        childRef_name.setValue(imgName);

        //Make POST request - Add new information
        new PostDataRequest().execute("http://192.168.104.2:3000/api/users");

        Toast.makeText(getApplicationContext(),"Upload successful", Toast.LENGTH_LONG).show();

    }

    //Send POST request (Add)
    class PostDataRequest extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddActivity.this);
            progressDialog.setMessage("Inserting data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return postData(params[0]);
            } catch (IOException e) {
                return "Network Error!";
            } catch (JSONException e) {
                return "Data Invalid!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @NonNull
        private String postData(String urlPath) throws IOException, JSONException {
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            _dateNow = dateFormat.format(Calendar.getInstance().getTime());

            try {
                //Create data to send to server
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("name", _etxtName.getText());
                dataToSend.put("birthday", birthday);
                dataToSend.put("email", _etxtEmail.getText());
                dataToSend.put("userName", _etxtUserName.getText());
                dataToSend.put("passWord", _etxtPassword.getText());
                dataToSend.put("address", _etxtAddress.getText());
                dataToSend.put("description", _etxtDescription.getText());
                dataToSend.put("avatar", _imgUrlReturn);
                dataToSend.put("gender", _genderReturn);
                dataToSend.put("createDay", _dateNow);
                dataToSend.put("countOfPosts", 0);
                dataToSend.put("countOfFriends", 0);
                dataToSend.put("countOfBookmarks", 0);
                dataToSend.put("idFriendsList", null);
                dataToSend.put("idBookmarksList", null);

                //Initialize and config request, then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000/*milliseconds*/);
                urlConnection.setConnectTimeout(10000/*milliseconds*/);
                urlConnection.setRequestMethod("POST"); //Set method is POST - Add new
                urlConnection.setDoOutput(true); //Enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");//set hander
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }
            return result.toString();
        }
    };    // End PostDataTask
}
