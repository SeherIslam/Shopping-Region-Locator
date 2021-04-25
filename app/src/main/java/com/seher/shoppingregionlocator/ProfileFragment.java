package com.seher.shoppingregionlocator;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.seher.shoppingregionlocator.helperClasses.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {


    private ValueEventListener eventListener;

    private DatabaseReference databaseReference;

    private StorageReference storageReference;

    private ProgressDialog progressDialog;

    private User user;
    private int countChanges;

    private ImageView imageView;
    private FloatingActionButton fab;
    private TextView name,username;
    private TextInputLayout fullname,email,phone,password;


    private Button update;

    private Bitmap photo;
    private Uri imageUri;



    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_PICTURE = 1999;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_GALLERY_PERMISSION_CODE=200;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };




    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setTitle("Profile");

        findElements(view);
        countChanges=0;
        progressDialog=new ProgressDialog(getActivity());
        update_Btn_NotClickable();

        dataUpdateChecking();

        final String username=getArguments().getString("username");

        //Toast.makeText(getContext(),"You will get an otp at "+username,Toast.LENGTH_LONG).show();


        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u= postSnapshot.getValue(User.class);

                    if(username.equals(u.getEmail())) {
                        setData(u);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });



        return view;


    }


    private void findElements(View view)
    {

        imageView=view.findViewById(R.id.profile_image);
        fab=view.findViewById(R.id.fab);
        name=view.findViewById(R.id.name);
        username=view.findViewById(R.id.username);

        fullname=view.findViewById(R.id.full_name);
        email=view.findViewById(R.id.email);
        phone=view.findViewById(R.id.phone);
        password=view.findViewById(R.id.password);
        update=view.findViewById(R.id.update);
    }

    private void update_Btn_Clickable()
    {
        update.setEnabled(true);
        countChanges--;
        update.setBackgroundColor(Color.BLACK);
    }
    private void update_Btn_NotClickable()
    {
        update.setEnabled(false);
        countChanges++;
        update.setBackgroundColor(0xFFF2F4F4);
    }

    private void dataUpdateChecking()
    {

        //for image update btn enable is in on activity result function as image loads in image view after that update btn will be enable

        fullname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(countChanges==0)
                {
                    update_Btn_NotClickable();
                }
                else if(fullname.getEditText().getText().toString().equals(user.getFullname()))
                {
                    update_Btn_NotClickable();
                }
                else
                {
                    update_Btn_Clickable();
                }

                final String full_name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

                if(s.length()==0) {
                    fullname.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(full_name_checker)))
                {
                    fullname.setError("Invalid Name. Special Characters and numbers are not acceptable");
                }
                else if(s.length()>0 && s.length()<30) {
                    fullname.setErrorEnabled(false);
                }
                else if (s.length()>=30){
                    fullname.setError("Maximum length reached");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(countChanges==0)
                {
                    update_Btn_NotClickable();
                }
                else if(email.getEditText().getText().toString().equals(user.getEmail()))
                {
                    update_Btn_NotClickable();
                }
                else
                {
                    update_Btn_Clickable();
                }


                final String email_checker="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

                if(s.length()==0) {
                    email.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(email_checker)))
                {
                    email.setError("Invalid Email ...");
                }
                else
                {
                    email.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(countChanges==0)
                {
                    update_Btn_NotClickable();
                }
                else if(phone.getEditText().getText().toString().equals(user.getPhoneNo()))
                {
                    update_Btn_NotClickable();
                }
                else
                {
                    update_Btn_Clickable();
                }

                final String phone_check="^[0-9]{7,13}$";


                if(s.length()==0) {
                    phone.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(phone_check))){
                    phone.setError("Invalid Phone ...");
                }
                else
                {
                    phone.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(countChanges==0)
                {
                    update_Btn_NotClickable();
                }
                else if(password.getEditText().getText().toString().equals(user.getPassword()))
                {
                    update_Btn_NotClickable();
                }
                else
                {
                    update_Btn_Clickable();
                }

                if(s.length()==0) {
                    password.setError("Field cannot be empty");
                }
                else if (s.length()>=21){
                    password.setError("Maximum length reached");
                }
                else if (s.length()<7)
                {
                    password.setError("Minimum length seven");
                }
                else
                {
                    password.setErrorEnabled(false);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void setData(User u)
    {
        try {
            user=new User();
            user=u;
            if(u.getProfileImage()!=null)
            {
                Glide.with(getContext())
                        .load(u.getProfileImage())
                        .into(imageView);
            }

            name.setText(u.getFullname());
            username.setText("@"+u.getFullname());
            fullname.getEditText().setText(u.getFullname());
            email.getEditText().setText(u.getEmail());




            phone.getEditText().setText(u.getPhoneNo());

            password.getEditText().setText(u.getPassword());

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    private void update()
    {
        if( !validatePassword() | !validatePhone() | !validateEmail()   | !validateFullname()){
            Toast.makeText(getContext(),"Fill all Fields with no errors", Toast.LENGTH_LONG).show();

            return;
        }
        if(imageUri!=null)
        {
            try {

                progressDialog.setMessage("Updating Info ...");
                progressDialog.show();
                final String id = user.getId();

                storageReference= FirebaseStorage.getInstance().getReference("UserProfileImages");



                storageReference.child(id).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                user.setProfileImage(uri.toString());
                                user.setFullname(fullname.getEditText().getText().toString());
                                user.setEmail(email.getEditText().getText().toString());
                                user.setPhoneNo(phone.getEditText().getText().toString());
                                user.setPassword(password.getEditText().getText().toString());


                                databaseReference.child(id).setValue(user);
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"Data Updated Successfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });


            } catch (Exception e) {

                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        else {

            progressDialog.setMessage("Updating Info ...");
            progressDialog.show();

            final String id = user.getId();

            user.setFullname(fullname.getEditText().getText().toString());
            user.setEmail(email.getEditText().getText().toString());
            user.setPhoneNo(phone.getEditText().getText().toString());
            user.setPassword(password.getEditText().getText().toString());


            databaseReference.child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Data Updated Successfully", Toast.LENGTH_SHORT).show();

                }
            });
        }


    }



    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getContext());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to upload your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(
//                                    getActivity(),
//                                    PERMISSIONS_STORAGE,
//                                    MY_GALLERY_PERMISSION_CODE
//                            );
//
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_GALLERY_PERMISSION_CODE);

                        } else {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_PICTURE);


                        }

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                        }

                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera Permission Granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else {
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_LONG).show();
            }

        } else if(requestCode==MY_GALLERY_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Gallery read permission granted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_PICTURE);
            } else {
                Toast.makeText(getContext(), "Gallery read permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Uri getImageUri(Activity inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            //imageUri = data.getData();
            photo = (Bitmap) data.getExtras().get("data");

            imageUri=getImageUri(getActivity(),photo);
            imageView.setImageBitmap(photo);
            update_Btn_Clickable();

        } else if (requestCode == GALLERY_PICTURE && resultCode == Activity.RESULT_OK) {


            // The following code snipet is used when Intent for single image selection is set

            imageUri = data.getData();
            //Log.d(TAG,"file uri: "+imageUri.toString());
            try {
                photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(photo);
            update_Btn_Clickable();


        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Log.d(TAG, "PHOTO is null");
            getActivity().finish();
        }
    }


    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }






    private boolean validateFullname(){
        String val=fullname.getEditText().getText().toString();
        final String full_name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

        if(val.isEmpty()){
            fullname.setError("Field cannot be empty");
            fullname.requestFocus();
            return false;
        }
        else if(!val.matches(full_name_checker))
        {
            fullname.requestFocus();
            return false;
        }
        return true;
    }


    private boolean validateEmail(){
        String val=email.getEditText().getText().toString();
        final String emailchecker="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            email.requestFocus();
            return false;
        }
        else if(!val.matches(emailchecker))
        {

            email.requestFocus();
            return false;
        }
        return true;

    }

    private boolean validatePhone(){
        String val=phone.getEditText().getText().toString();
        final String username_check="^[0-9]{7,13}$";

        if(val.isEmpty()){
            phone.setError("Field cannot be empty");
            phone.requestFocus();
            return false;
        }
        else if(!val.matches(username_check))
        {
            phone.setError("Invalid Phone ...");
            phone.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePassword(){
        String val=password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            password.requestFocus();
            return false;
        }

        else if(val.length()<7)
        {
            password.requestFocus();
            return false;
        }
        return true;

    }








    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

}