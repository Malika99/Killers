package com.example.killers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ApplicationActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editName;
    private EditText editPhone;
    private EditText editText;
    Button btnSend;
    String sEmail, sPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        editEmail = findViewById(R.id.editEmail);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.edit_phone);
        editText = findViewById(R.id.edit_text);
        btnSend = findViewById(R.id.btn_send);

        sEmail = "malika.mukanova99@gmail.com";
        sPassword = "malika777";

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sEmail, sPassword);
                    }
                });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(sEmail));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(editEmail.getText().toString().trim()));
                    message.setSubject(editName.getText().toString().trim());
                    message.setText(editText.getText().toString().trim());

                    new SendMail().execute(message);

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private class SendMail  extends AsyncTask<Message, String, String > {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ApplicationActivity.this
            , "Пожалуйста подождите", "Идет отправка...", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Отправлено";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Ошибка";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s.equals("Отправлено")){


                AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Отправлено</font>"));
                builder.setMessage("Письмо доставлено.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        btnSend.setText("");
                        editName.setText("");
                        editPhone.setText("");
                        editText.setText("");
                    }
                });
                builder.show();
            }else {
                Toast.makeText(getApplicationContext()
                , "Что-то пошло не так?", Toast.LENGTH_SHORT).show();
            }
        }
    }

}