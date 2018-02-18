package com.marat.smarthouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import Controller.DataGetter;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private AutoCompleteTextView mLoginView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerButton = (Button)findViewById(R.id.register_button);
        mEmailView = (AutoCompleteTextView)findViewById(R.id.email);
        mLoginView = findViewById(R.id.login);
        mPasswordView = (EditText)findViewById(R.id.password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attemptRegister()){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean attemptRegister(){
        String email = mEmailView.getText().toString();
        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (isValidLogin(login) && isValidEmail(email) && isValidPassword(password)){
            try {
                DataGetter.registrateUser(email, login, password);
                return true;
            }
            catch (IllegalArgumentException e){
                mLoginView.setError("Пользователь уже существует");
                mLoginView.requestFocus();
                return false;
            }
            catch (IOException e){
                Snackbar.make(findViewById(R.id.main_layout), "Ошибка подключения", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }
        if (!isValidPassword(password)){
            if (password.length() == 0)
                mPasswordView.setError("Это необходимое поле");
            else mPasswordView.setError("Пароль должен содержать 8 символов");
        }
        if (!isValidEmail(email)){
            if (email.length() == 0)
                mEmailView.setError("Это необходимое поле");
            else mEmailView.setError("Email должен содержать знак @");
        }
        if (!isValidLogin(login)){
            if (login.length() == 0)
                mLoginView.setError("Это необходимое поле");
            else mLoginView.setError("Логин должен содержать 6 символов");
        }
        return false;
    }

    private boolean isValidPassword(String password){
        if (password.length() >= 8)
            return true;
        return false;
    }

    private boolean isValidEmail(String email){
        if (email.contains("@"))
            return true;
        return false;
    }

    private boolean isValidLogin(String login){
        if (login.length() > 6)
            return true;
        return false;
    }

}
