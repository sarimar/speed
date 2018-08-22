package speedmeter.fly.speed;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 13/05/2018.
 */

public class BackGroundWorker extends AsyncTask<String,String,String> {

    //Volley
    public AsyncResponse delegate = null;
    //Context context;//constructor for backgroundworker
    AlertDialog alertDialog;//alert dialog to see the response of the login
    public String ror = "";

    //BackGroundWorker(Context ctx)
    ////////////////
    public BackGroundWorker(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    public String doInBackground(String... params) {
        String operationtype = params[0];
        String login_url = "http://192.168.43.171/myserver/login.php";
        String register_url = "http://192.168.43.171/myserver/register.php";
        if (operationtype.equals("login"))//post some data by defining url
        {
            try {

                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // its like post and get client
                conn.setRequestMethod("POST");//login need POST
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream outputStream = conn.getOutputStream();//the outputStream comes from the httpurlconnction
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //the utf is the type of the outputstream
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" +
                        URLEncoder.encode(user_name, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");
                //username and password will be the key in post method and the & to join the url
                //the data url that we want to post
                //URLENCODER.CODE()--> converting a String to the application
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                //after posting the data to server we get responses
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
                //ISO IS THE TYPE OF THE INPUT STREAM
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;//saving the respons from the post method

                }
                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                return result;


            } catch (MalformedURLException e) {//for url
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();//for open connction
            }


        } else if (operationtype.equals("register")) {
            try {
                String name = params[1];
                String surname = params[2];
                String age = params[3];
                String user_name = params[4];
                String password = params[5];
                URL url = new URL(register_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // its like post and get client
                conn.setRequestMethod("POST");//login need POST
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream outputStream = conn.getOutputStream();//the outputStream comes from the httpurlconnction
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //the utf is the type of the outputstream
                String post_data =
                        URLEncoder.encode("name", "UTF-8") + "=" +
                                URLEncoder.encode(name, "UTF-8") + "&" +
                                URLEncoder.encode("surename", "UTF-8") + "=" +
                                URLEncoder.encode(surname, "UTF-8") + "&" +
                                URLEncoder.encode("age", "UTF-8") + "=" +
                                URLEncoder.encode(age, "UTF-8") + "&" +
                                URLEncoder.encode("user_name", "UTF-8") + "=" +
                                URLEncoder.encode(user_name, "UTF-8") + "&" +
                                URLEncoder.encode("password", "UTF-8") + "=" +
                                URLEncoder.encode(password, "UTF-8");
                //username and password will be the key in post method and the & to join the url
                //the data url that we want to post
                //URLENCODER.CODE()--> converting a String to the application
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                //after posting the data to server we get responses
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
                //ISO IS THE TYPE OF THE INPUT STREAM
                String line = "";
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;//saving the respons from the post method

                }
                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    public void onPostExecute(String result) {//TO PRINT THE RESPONSE OF THE SERVER
        //try to make new intent to see the result in the main activity page

        delegate.processFinish(result);


    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }


}