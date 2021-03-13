package com.example.killers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.killers.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    RelativeLayout root;

    SessionManager sessionManager;

    private TextView textBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSignIn = findViewById(R.id.btnSigIn);
        btnRegister = findViewById(R.id.btnRegister);

        root = findViewById(R.id.root_element);

        textBottom = findViewById(R.id.text_bottom);

        auth = FirebaseAuth.getInstance(); //с помощью этой функции запускаем авторизацию в бд
        db = FirebaseDatabase.getInstance(); //подключаемся к бд
        users = db.getReference("Users"); //указываем таблицу с которой будем работать



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRegisterWindow();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSignInWindow();

            }
        });

        //Initialize SessionManager
        sessionManager = new SessionManager(getApplicationContext());


    }


    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this); //создан объект на основе класса LayoutInflater
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null); //помещен шаблон в этот объект
        dialog.setView(sign_in_window);

        final MaterialEditText email = sign_in_window.findViewById(R.id.emailField);
        final MaterialEditText pass = sign_in_window.findViewById(R.id.passField);

        dialog.setNegativeButton("Выйти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); //для закрытия диалогового окна
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();//ошибка если пользователь не введет почту
                    return;
                }

                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введенный пароль должен быть более 5 символов", Snackbar.LENGTH_SHORT).show();//ошибка если пользователь не введет пароль
                    return;
                }


                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();

    }


    private void showRegisterWindow() { //для всплывающего окна
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this); //создан объект на основе класса LayoutInflater
        View register = inflater.inflate(R.layout.registor, null); //помещен шаблон в этот объект
        dialog.setView(register);

        final MaterialEditText email = register.findViewById(R.id.emailField);
        final MaterialEditText pass = register.findViewById(R.id.passField);
        final MaterialEditText name = register.findViewById(R.id.nameField);
        final MaterialEditText phone = register.findViewById(R.id.phoneField);


        dialog.setNegativeButton("Выйти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); //для закрытия диалогового окна
            }
        });

        dialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT). show();//ошибка если пользователь не введет почту
                    return;
                }

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT). show();//ошибка если пользователь не введет имя
                    return;
                }

                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT). show();//ошибка если пользователь не введет телефон
                    return;
                }

                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введенный пароль должен быть более 5 символов", Snackbar.LENGTH_SHORT). show();//ошибка если пользователь не введет пароль
                    return;
                }

                //Регистрация пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setPhone(phone.getText().toString());


                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //ключ для пользователя это его ID
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root, "Пользователь успешно добавлен.", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Ошибка регистрации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = auth.getCurrentUser();
        if (cUser != null)
        {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            showSigned();
        }
        else
        {
            notSigned();
        }
    }


    private void  showSigned(){
        btnSignIn.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
        textBottom.setVisibility(View.GONE);
    }

    private void notSigned(){
        btnSignIn.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        textBottom.setVisibility(View.VISIBLE);
    }

}