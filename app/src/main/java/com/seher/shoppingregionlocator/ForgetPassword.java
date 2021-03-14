package com.seher.shoppingregionlocator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seher.shoppingregionlocator.helperClasses.User;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgetPassword extends AppCompatActivity {

    private Button sendBtn,backBtn;
    private TextInputLayout email;

    ProgressBar login_progressBar;
    String type;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email=findViewById(R.id.forgot_screen_email);

        sendBtn=findViewById(R.id.forget_screen_send_btn);
        backBtn=findViewById(R.id.forget_screen_back_login_btn1);

        login_progressBar=findViewById(R.id.login_progressBar);


        login_progressBar.setVisibility(View.INVISIBLE);



        email.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && email.getEditText().getText().toString().isEmpty())
                {
                    email.setError("Field cannot be empty");
                }
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

    public void send(View view) {
        if(
            //!validateRegiteredFace() |
                !validateEmail()){
            Toast.makeText(getApplicationContext(),"Enter email first",Toast.LENGTH_LONG).show();

            return;
        }

        login_progressBar.setVisibility(View.VISIBLE);

        final String userEnteredUsername=email.getEditText().getText().toString();


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

            count=0;
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        User u = postSnapshot.getValue(User.class);
                        if(u.getEmail().equals(userEnteredUsername))
                        {
                            count++;
                            login_progressBar.setVisibility(View.INVISIBLE);
                            email.setErrorEnabled(false);

                            setMail(u.getEmail(),u.getPassword(),u.getFullname());


                            Intent intent=new Intent(getApplicationContext(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(),"Check your email. Password is sent through mail",Toast.LENGTH_LONG).show();


                            break;
                        }
                    }
                    if(count==0){
                        login_progressBar.setVisibility(View.INVISIBLE);
                        email.setError("User with this email does not exist. Register yourself.");
                        email.requestFocus();
                        
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }


    public void backToLogin(View view) {

        Intent intent=new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void setMail(String mail, String pass, String name) {
        String emailID="";
        String emailPass="";

        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session= Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailID,emailPass);
            }
        });

        try {
            String recipients=mail;
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(emailID));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));
            message.setSubject("Reset Password");
            String msg="Hi "+name+",<br> You are requested for password and your password is "+"'"+pass+"'";
            message.setText(String.valueOf(Html.fromHtml(msg)));

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }




        private class SendMail extends AsyncTask<Message,String,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  progressDialog=ProgressDialog.show(getContext(),"Please Wait","Sending Mail...",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.dismiss();
//            if(s.equals("Success"))
//            {
////                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
////                builder.setCancelable(false);
////                builder.setTitle("Success");
////                builder.setMessage("Mail sent successfully");
////                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.dismiss();
////                    }
////                });
////                builder.show();
//                Toast.makeText(getActivity(),"Mail sent",Toast.LENGTH_SHORT).show();
//
//            }
//            else {
//                Toast.makeText(getActivity(),"Something went wrong... in sending mail",Toast.LENGTH_SHORT).show();
//            }
        }
    }

}