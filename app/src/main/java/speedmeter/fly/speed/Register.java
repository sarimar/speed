package speedmeter.fly.speed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText nameEt, surnameEt, ageEt, usernameEt, passwordEt;
    Button btnregis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEt = (EditText) findViewById(R.id.edname);
        surnameEt = (EditText) findViewById(R.id.edsurname);
        ageEt = (EditText) findViewById(R.id.edage);
        usernameEt = (EditText) findViewById(R.id.edusername);
        passwordEt = (EditText) findViewById(R.id.edpassword);
        btnregis = (Button) findViewById(R.id.edreg);
    }

    public void onRegister(View view) {
        String name = nameEt.getText().toString();
        String surname = surnameEt.getText().toString();
        String age = ageEt.getText().toString();
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String type = "register";
        BackGroundWorker backGroundWorker = new BackGroundWorker(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Toast.makeText(Register.this, output, Toast.LENGTH_SHORT).show();
                if (output.equalsIgnoreCase("1")) {
                    startActivity(new Intent(Register.this, MainActivity.class));
                } else {
                    Toast.makeText(Register.this, "lehek", Toast.LENGTH_SHORT).show();

                }

            }

        });
        backGroundWorker.execute(type, name, surname, age, username, password);//execute the background worker with the username,pass and the type of the operation like login

    }


}

