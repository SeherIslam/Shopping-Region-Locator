package com.seher.shoppingregionlocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seher.shoppingregionlocator.helperClasses.User;

import java.util.Properties;
import java.util.Random;

public class SignUp extends AppCompatActivity {

    TextInputLayout fullname,phone,email,dob,pass;
    Button login,signup;

    DatabaseReference reference;
    int count;
    User newuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login=findViewById(R.id.signup_login_button);
        String text="Already have an account? <font color=#48C9B0>Login</font>";
        login.setText(Html.fromHtml(text));

        fullname=findViewById(R.id.full_name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        dob=findViewById(R.id.doj);
        pass=findViewById(R.id.password);


        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        final MaterialDatePicker mdp = builder.build();
        dob.getEditText().setKeyListener(null);
        mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dob.getEditText().setText(mdp.getHeaderText());
            }
        });
        dob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdp.show(getSupportFragmentManager(),"DOB_PICKER");
            }
        });


        // error message for first time

        fullname.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && fullname.getEditText().getText().toString().isEmpty())
                {
                    fullname.setError("Field cannot be empty");
                }
            }

        });


        email.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && email.getEditText().getText().toString().isEmpty())
                {
                    email.setError("Field cannot be empty");
                }
            }

        });

        pass.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && pass.getEditText().getText().toString().isEmpty())
                {
                    pass.setError("Field cannot be empty");
                }
            }

        });

        phone.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && phone.getEditText().getText().toString().isEmpty())
                {
                    phone.setError("Field cannot be empty");
                }
            }

        });


        //after first letter errors
        fullname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                final String email_checker="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

                if(s.length()==0) {
                    email.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(email_checker)))
                {
                    email.setError("Invalid Email");
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

        pass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0) {
                    pass.setError("Field cannot be empty");
                }
                else if (s.length()<7){
                    phone.setError("Password lenght must be greater than 7");
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

        phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0) {
                    phone.setError("Field cannot be empty");
                }
                else if (s.length()<7){
                    phone.setError("Invalid Phone number");
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
        dob.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0) {
                    dob.setError("Field cannot be empty");
                }
                else if (s.equals("DD/MM/YYYY")){
                    dob.setError("Select date");
                }
                else
                {
                    dob.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



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

    private boolean validatePassword(){
        String val=pass.getEditText().getText().toString();
        if(val.isEmpty()){
            pass.setError("Field cannot be empty");

            return false;
        }
        else if(val.length()<7)
        {
            phone.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePhone(){
        String val=phone.getEditText().getText().toString();
        if(val.isEmpty()){
            phone.setError("Field cannot be empty");
            phone.requestFocus();
            return false;
        }
        else if(val.length()<7)
        {
            phone.requestFocus();
            return false;
        }
        return true;
    }


    private boolean validateDOB(){
        String val=dob.getEditText().getText().toString();
        if(val.equals("DD/MM/YYYY")){
            dob.setError("Select date");
            dob.requestFocus();
            return false;
        }
        return true;
    }



    public void registerUser(View view) {
        if(
                !validateDOB() | !validatePhone()  | !validatePassword() | !validateEmail() | !validateFullname()){

            Toast.makeText(getApplicationContext(),"Fill all Fields with no errors",Toast.LENGTH_LONG).show();

            return;
        }

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users").child("Admins");


        String name=fullname.getEditText().getText().toString();
        String mail=email.getEditText().getText().toString();
        String number=phone.getEditText().getText().toString();
        String DOB=dob.getEditText().getText().toString();
        String password=pass.getEditText().getText().toString();
        String id = reference.push().getKey();
        User newuser=new User(name,mail,number,DOB,id,password);


        count=0;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    if(u.getEmail().equals(mail))
                    {
                        count++;
                        email.setError("Account already exists. If you don't know your password go for forget password");
                        email.setErrorEnabled(true);
                        Toast.makeText(getApplicationContext(),"Account already exists. If you don't know your password go for forget password",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if(count==0){
                    register(newuser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void register(User user)
    {

        reference.child(user.getId()).setValue(user);

        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userID",user.getId());
        startActivity(intent);


    }

    public void back_to_login(View view) {
        Intent intent=new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

}