package com.seher.shoppingregionlocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.seher.shoppingregionlocator.helperClasses.User;

public class Login extends AppCompatActivity {
    Button signup,login;

    TextView login_welcome,signin_text;
    ImageView image;
    TextInputLayout username,password;

    ProgressBar login_progressBar;
    int count;

    private User userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup=findViewById(R.id.signup_button);
        login=findViewById(R.id.login_button);
        login_welcome=findViewById(R.id.login_welcome_text);
        signin_text=findViewById(R.id.signin_continue_text);
        image=findViewById(R.id.login_logo);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        login_progressBar=findViewById(R.id.login_progressBar);


        login_progressBar.setVisibility(View.INVISIBLE);

        String text="New User? <font color=#ECB028>Sign Up</font>";
        signup.setText(Html.fromHtml(text));

        username.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && username.getEditText().getText().toString().isEmpty())
                {
                    username.setError("Field cannot be empty");
                }
            }

        });

        password.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && password.getEditText().getText().toString().isEmpty())
                {
                    password.setError("Field cannot be empty");
                }
            }

        });


        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0){
                    username.setErrorEnabled(false);
                }
                else
                    username.setError("Field cannot be empty");

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
                if (s.length()>0){
                    password.setErrorEnabled(false);
                }
                else
                    password.setError("Field cannot be empty");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    private boolean validateUsername(){
        String val=username.getEditText().getText().toString();

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            username.requestFocus();
            return false;
        }
        username.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword(){
        String val=password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            password.requestFocus();
            return false;
        }

        password.setErrorEnabled(false);
        return true;

    }

    public void loginUser(View view) {
//        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
////                            intent.putExtra("userType","Voter");
////                            intent.putExtra("user",userEnteredUsername);
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        if(!validatePassword() | !validateUsername()){
            Toast.makeText(getApplicationContext(),"Fill both Fields",Toast.LENGTH_LONG).show();
            return;
        }
        login_progressBar.setVisibility(View.VISIBLE);
        final String userEnteredUsername=username.getEditText().getText().toString();
        final String userEnteredPassword=password.getEditText().getText().toString();
//
//        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        System.out.println("asdddddddddddddddd");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        System.out.println(reference);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    if(u.getEmail().equals(userEnteredUsername))
                    {
                        count++;
                        login_progressBar.setVisibility(View.INVISIBLE);
                        username.setErrorEnabled(false);
                        if(u.getPassword().equals(userEnteredPassword)){

                            password.setErrorEnabled(false);

                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtra("username",userEnteredUsername);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            password.setError("Wrong Password...");
                            password.requestFocus();
                        }
                        break;
                    }
                }
                if(count==0){
                    login_progressBar.setVisibility(View.INVISIBLE);
                    username.setError("Username does not exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void forgetPassword(View view) {
        Intent intent=new Intent(getApplicationContext(),ForgetPassword.class);
        startActivity(intent);
    }

    public void signup(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        Pair[] pair=new Pair[7];

        pair[0]=new Pair<View,String>(signup,"t_signup_button");
        pair[1]=new Pair<View,String>(login,"t_login_button");
        pair[2]=new Pair<View,String>(login_welcome,"s_text");
        pair[4]=new Pair<View,String>(signin_text,"t_signin_text");
        pair[3]=new Pair<View,String>(image,"s_logo");
        pair[5]=new Pair<View,String>(username,"s_logo");
        pair[6]=new Pair<View,String>(password,"t_password");
        ActivityOptions op=ActivityOptions.makeSceneTransitionAnimation(this,pair);
        startActivity(intent,op.toBundle());
    }
}