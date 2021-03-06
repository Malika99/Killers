package com.example.killers.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.killers.ApplicationActivity;
import com.example.killers.MainActivity;
import com.example.killers.MapsActivity;
import com.example.killers.R;
import com.example.killers.SessionManager;
import com.google.firebase.auth.FirebaseAuth;


public class CommentFragment extends Fragment {

private Button button;
    Button buttonContact;
    Button btnNum;
    Dialog dialog;
    Button btnApp;
    Button btnLogout;
    SessionManager sessionManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commentfragment, container, false);

        btnNum = view.findViewById(R.id.btnNum);
        buttonContact = view.findViewById(R.id.btnContact);
        btnApp = view.findViewById(R.id.btnApplication);
        btnLogout = view.findViewById(R.id.btnLogout);
        sessionManager = new SessionManager(getContext().getApplicationContext());


        btnApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationActivity();
            }
        });

        btnNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:102"));
                startActivity(intent);
            }
        });

        button = view.findViewById(R.id.btnMap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity();
            }
        });




        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("ВЫЙТИ");
                builder.setMessage("Вы точно хотите выйти?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        sessionManager.setLogin(false);
                        sessionManager.setEmail("");
                        startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //вызов диалогового окна
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //скрываем заголовок
                dialog.setContentView(R.layout.previewdialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //прозрачный фон диалогового окна
                dialog.setCancelable(false); //окно нельзя закрыть кнопкой назад, только на крестик
                //для закрытия диалогового окна
                TextView btnclose = (TextView)dialog.findViewById(R.id.btnclose);
                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(getActivity(), CommentFragment.class);
                            startActivity(intent);
                        }catch (Exception e) {
                            //Hier кода не будет
                        }
                        dialog.dismiss();//закрыть диалоговое окно
                    }
                });
                dialog.show();
            }
        });

        return view;
    }


    public void MapsActivity() {
        Intent intent = new Intent(getContext(), MapsActivity.class);
        startActivity(intent);
    }

    public void ApplicationActivity() {
        Intent intent = new Intent(getContext(), ApplicationActivity.class);
        startActivity(intent);
    }
}
