package speedmeter.fly.speed;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    private static Button login_btn;
    private static Button register_btn;
    private static EditText usernameET;
    private static EditText passwordET;
    private static TextView attempts;
    int attmps_count = 5;
    private static CountDownTimer countDownTimer;
    private static TextView texttimer;
    String reso = "0";
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*ActionBar ab = getSupportActionBar();
        ab.setLogo(R.mipmap.ic_launcher);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);*/
        login_btn = (Button) findViewById(R.id.Runbtn);
        login_btn.getBackground().setAlpha(150);//set opacity for button
        register_btn = (Button) findViewById(R.id.btn_register);
        register_btn.getBackground().setAlpha(150);
        usernameET = (EditText) findViewById(R.id.Eduser);
        passwordET = (EditText) findViewById(R.id.Edpass);
        attempts = (TextView) findViewById(R.id.txtresult);
        attempts.setText(Integer.toString(attmps_count));
        texttimer = (TextView) findViewById(R.id.txttimer);

    }



    public void onLogin(View view) {
        String re = "1";
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String type = "login";//TYPE OF THE OPERATION
        BackGroundWorker backGroundWorker = new BackGroundWorker(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Toast.makeText(Login.this, output, Toast.LENGTH_SHORT).show();
                Log.d("massage",output);


                if (output.equals("1")) {

                    startActivity(new Intent(Login.this, Home.class));
                } else {
                    attmps_count--;
                    attempts.setText(Integer.toString(attmps_count));
                    if (attmps_count == 0) {
                        login_btn.setEnabled(false);
                        countDownTimer:
                        new CountDownTimer(15 * 1000, 1000) {
                            @Override
                            public void onTick(long milliUnitFinished) {
                                texttimer.setText("" + milliUnitFinished / 1000);


                            }

                            @Override
                            public void onFinish() {
                                texttimer.setText("");
                                login_btn.setEnabled(true);
                                attmps_count = 5;
                                attempts.setText(Integer.toString(attmps_count));
                            }
                        }.start();//attempt timer
                    }

                }

            }


        });
        backGroundWorker.execute(type, username, password);//execute the background worker with the username,pass and the type of the operation like login


    }
    public void onregis(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }
}
        /*if (re.equals("login not success"))
        {
           // Toast.makeText(MainActivity.this, "Username and password isn't correct", Toast.LENGTH_SHORT).show();
            attmps_count--;
            attempts.setText(Integer.toString(attmps_count));
            if (attmps_count == 0) {
                login_btn.setEnabled(false);
                countDownTimer:
                new CountDownTimer(15 * 1000, 1000) {
                    @Override
                    public void onTick(long milliUnitFinished) {
                        texttimer.setText("" + milliUnitFinished / 1000);


                    }

                    @Override
                    public void onFinish() {
                        texttimer.setText("");
                        login_btn.setEnabled(true);
                        attmps_count = 5;
                        attempts.setText(Integer.toString(attmps_count));
                    }
                }.start();//attempt timer
            }
        }
        else
        {
            startActivity(new Intent(this,Register.class));


        }*/


        /*if ((username.getText().toString().equals("user")) && (password.getText().toString().equals("pass"))) {
            Toast.makeText(MainActivity.this, "Username and password is correct", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("phoneprograming.b_fit");
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Username and password isn't correct", Toast.LENGTH_SHORT).show();
            attmps_count--;
            attempts.setText(Integer.toString(attmps_count));
            if (attmps_count == 0) {
                login_btn.setEnabled(false);
                countDownTimer:
                new CountDownTimer(15 * 1000, 1000) {
                    @Override
                    public void onTick(long milliUnitFinished) {
                        texttimer.setText("" + milliUnitFinished / 1000);


                    }

                    @Override
                    public void onFinish() {
                        texttimer.setText("");
                        login_btn.setEnabled(true);
                        attmps_count = 5;
                        attempts.setText(Integer.toString(attmps_count));
                    }
                }.start();//attempt timer
            }
        }*/





//login method, with attempts,time, two labels for username and pass