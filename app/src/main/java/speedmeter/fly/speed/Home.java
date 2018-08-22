package speedmeter.fly.speed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }
    public void onClickrun(View view){
        startActivity(new Intent(Home.this, MainActivity.class));

    }
    public void onClickReport(View view){
        startActivity(new Intent(Home.this, Reports.class));
    }


}
