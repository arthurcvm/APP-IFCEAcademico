package br.com.arthurcvm.ifceacademico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getBaseContext().getSharedPreferences("br.com.arthurcvm.ifceacademico.preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (prefs.getInt("prev_error", 0) != 1 && prefs.getBoolean("auto_login", false)) {
            if (prefs.getString("matricula", "").length() > 0 && prefs.getString("senha", "").length() > 0) {
                editor.putBoolean("auth", true);
                editor.putInt("prev_error", 0);
                editor.commit();
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        } else if (prefs.getInt("prev_error", 0) == 1) {
            TextView erro = (TextView) findViewById(R.id.error);
            erro.setText("Matrícula ou senha inválidos");
            editor.putBoolean("auto_login", false);
            editor.putInt("prev_error", 0);
            editor.commit();

        } else if (prefs.getInt("prev_error", 0) == 2) {
            TextView erro = (TextView) findViewById(R.id.error);
            erro.setText("Acesso expirou");
            editor.putInt("prev_error", 0);
            editor.commit();
        }
    }


    public void login(View v){
        SharedPreferences prefs = getBaseContext().getSharedPreferences("br.com.arthurcvm.ifceacademico.preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        TextView matricula = (TextView)findViewById(R.id.matricula);
        TextView senha = (TextView)findViewById(R.id.senha);
        CheckBox auto_login = (CheckBox) findViewById(R.id.auto_login);
        boolean erro = false;
        if(matricula.getText().length()<4){
            matricula.setError("Matrícula inválida");
            erro = true;
        }
        if(senha.getText().length()<3){
            senha.setError("Senha inválida");
            erro = true;
        }

        if(!erro) {
            editor.putString("matricula", matricula.getText().toString());
            editor.putString("senha", senha.getText().toString());
            editor.putBoolean("auto_login", auto_login.isChecked());
            editor.putBoolean("auth", true);
            editor.putInt("prev_error", 0);
            editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
