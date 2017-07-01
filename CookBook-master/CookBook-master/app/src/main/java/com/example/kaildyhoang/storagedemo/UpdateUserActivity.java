package com.example.kaildyhoang.storagedemo;

import android.*;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.firebase.client.Firebase;
import com.firebase.client.core.Tag;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateUserActivity extends AppCompatActivity {
    String ip ="192.168.1.19";
    private EditText _etxtName, _etxtUserName, _etxtPassword, _etxtBirthDay, _etxtEmail, _etxtAddress, _etxtDescription;
    private TextView _txtvDelete;
    private Button _btnDoUpdate;
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
    private String _idValueBundle = "";
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private List<ShowUserDataItemsActivity> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        _idValueBundle = getIntent().getExtras().getString("_IdBundle");

        //Make POST request - Add new information
        new GetDataTask().execute("http://"+ip+":3000/api/users");

        Firebase.setAndroidContext(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        //        Initialize the progressDialog
        progressDialog = new ProgressDialog(this);

        _etxtName = (EditText) findViewById(R.id.editTextInputNameUP);
        _etxtUserName = (EditText) findViewById(R.id.editTextInputUserNameUP);
        _etxtPassword = (EditText) findViewById(R.id.editTextInputPasswordUP);
        _etxtBirthDay = (EditText) findViewById(R.id.editTextInputBirthDayUP);
        _etxtEmail = (EditText) findViewById(R.id.editTextInputEmailUP);
        _etxtAddress = (EditText) findViewById(R.id.editTextInputAddressUP);
        _etxtDescription = (EditText) findViewById(R.id.editTextInputDescriptionUP);

        _txtvDelete = (TextView) findViewById(R.id.textViewDeleteUP);

        _btnDoUpdate = (Button) findViewById(R.id.buttonDoUpdateUP);

        _rdoGrGender = (RadioGroup) findViewById(R.id.radioGroupGenderUP);
        _rdoBtnMale = (RadioButton) findViewById(R.id.radioButtonMaleUP);
        _rdoBtnFemale = (RadioButton) findViewById(R.id.radioButtonFemaleUP);
        _rdoBtnOthers = (RadioButton) findViewById(R.id.radioButtonOthersUP);

        _imgVGetImage = (ImageView) findViewById(R.id.imageViewGetImageUP);

        _imgVGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Check for runtime permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                } else {
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

        _btnDoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAllThings(_idValueBundle);
            }
        });

        _txtvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(_idValueBundle);
            }
        });
    }

    //    Check for runtime permissions for storage access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callgallery();
                }
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }

    //    If access granted gallery open
    private void callgallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    private void delete(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserActivity.this);
                        builder.setMessage("Do you want to delete this data?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Make DELETE request - Delete object by id
                                        new DeleteDataTask().execute("http://"+ip+":3000/api/users/"+ id);
                                        startActivity(new Intent(getApplicationContext(),ShowUserDataActivity.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Warming!");
                        dialog.show();
    }

    @SuppressWarnings("VisibleForTests")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

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
                    Toast.makeText(getApplicationContext(), "Updated. ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, month, dayOfMonth);
                birthday = String.valueOf(dateFormat.format(date.getTime()));
                _etxtBirthDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void saveAllThings(String id) {

//        Save image to firebase
        final String imgName = _etxtName.getText().toString();
        if (imgName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill all Field", Toast.LENGTH_SHORT).show();
            return;
        }
        Firebase childRef_name = mFirebase.child("Image_Title");
        childRef_name.setValue(imgName);

        //Make PUT request - UPDATE information
        new PutDataRequest().execute("http://"+ip+":3000/api/users/"+id);

        Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();

    }

    //Send PUT request (Update)
    class PutDataRequest extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpdateUserActivity.this);
            progressDialog.setMessage("Editing data...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return putData(params[0]);
            } catch (IOException e) {
                return "Network error!";
            } catch (JSONException e) {
                return "Data invalid!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            mResult.setText(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String putData(String urlPath) throws IOException, JSONException {
            String result = null;
            BufferedWriter bufferedWriter = null;
            try {

                //Create data update
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


                //Initialize and config request, then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000/*milliseconds*/);
                urlConnection.setConnectTimeout(10000/*milliseconds*/);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true); //Enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");//set hander
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //Check update successful or not
                if (urlConnection.getResponseCode() == 200) {
                    return "Update successful!";
                } else {
                    return "Update failed !";
                }
            } finally {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }
        }

        ; //End PutDataRequest
    }
    //Send GET request by ID (View)
    class GetDataTask extends AsyncTask<String , Void, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(UpdateUserActivity.this);
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

                    if(jsonObject.has("_id") && jsonObject.getString("_id").equalsIgnoreCase(_idValueBundle)){

//                        user.set_idUser(jsonObject.getString("_id"));
                        _etxtName.setText(jsonObject.getString("name"));
                        _etxtBirthDay.setText(jsonObject.getString("birthday"));
                        _etxtEmail.setText(jsonObject.getString("email"));
                        _etxtUserName.setText(jsonObject.getString("userName"));
                        _etxtPassword.setText(jsonObject.getString("passWord"));
                        _etxtAddress.setText(jsonObject.getString("address"));
                        _etxtDescription.setText(jsonObject.getString("description"));
                        if(jsonObject.getString("gender").equalsIgnoreCase("male")){
                            _rdoGrGender.check(R.id.radioButtonMaleUP);
                        }else if(jsonObject.getString("gender").equalsIgnoreCase("female")){
                            _rdoGrGender.check(R.id.radioButtonFemaleUP);
                        }else{
                            _rdoGrGender.check(R.id.radioButtonOthersUP);
                        }
                        Glide.with(getApplicationContext())
                                .load(jsonObject.getString("avatar"))
                                .crossFade()
                                .placeholder(R.drawable.load_icon)
                                .thumbnail(0.1f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(_imgVGetImage);

                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
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
    //send Delete request
    class DeleteDataTask extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpdateUserActivity.this);
            progressDialog.setMessage("Deleting data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return deleteData(params[0]);
            }catch (IOException e){
                return "Network Error!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(progressDialog != null){
                progressDialog.dismiss();
            }
        }

        private String deleteData (String urlPath) throws IOException{
            String result = null;
            //Initialize and config request, then connect to server
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/*milliseconds*/);
            urlConnection.setConnectTimeout(10000/*milliseconds*/);
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type","application/json");//set hander
            urlConnection.connect();

            //Check delete successful or not
            if(urlConnection.getResponseCode() == 204){
                result = "Delete successfully!";
            }else{
                result = "Delete failed!";
            }

            return result;
        }
    }; //End DeleteDataTask
}
