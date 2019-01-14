package com.example.andrei.duckweather.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.Constraints;
import com.example.andrei.duckweather.model.CurrentWeather;
import com.example.andrei.duckweather.model.DailyWeather;
import com.example.andrei.duckweather.model.Forecast;
import com.example.andrei.duckweather.model.HourlyWeather;
import com.example.andrei.duckweather.model.Useful;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.Arrays;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//TODO
//What happens if the user does not have access to the internet ?
//TODO
//Maybe we should add some transitions when switching from one
//activity to another
//TODO
//persist data
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String KEY_PARCELABLE_DAILY_ARRAY = "DAILY_ARRAY";
    public static final String KEY_PARCELABLE_HOURLY_ARRAY = "HOURLY_ARRAY";
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 1;
    private TextView temperatureTextView;
    private TextView timeTextView;
    private TextView summaryTextView;
    private TextView humidityTextView;
    private TextView precipitationTextView;
    private TextView locationTextView;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Forecast forecast;
    private String latitude;
    private String longitude;
    private String locationName;
    private SharedPreferences sharedPreferences;

    /**
     * The user may press the daily forecast button
     * or the hourly forecast button
     * but the data has not been downloaded yet
     * This can happen when the user uses a slow
     * internet connection
     */
    private boolean dataFetched;
    private boolean isAppFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        getSettings();


        isAppFullscreen = false;

        dataFetched = false;

        initializeViews();

        if(isNetworkAvailable()) {
            pushRequestToServer();
        }else{
            alertUserAboutInternetConnection();
        }
    }

    private void getSettings() {
         latitude = sharedPreferences.getString(Constraints.KEY_LATITUDE_SHARED_PREFERENCES,"53.4808");
         longitude = sharedPreferences.getString(Constraints.KEY_LONGITUDE_SHARED_PREFERENCES,"2.2426");
         locationName = (sharedPreferences.getString(Constraints.KEY_LOCATION_SHARED_PREFERENCES,"Manchester"));

    }

    private void alertUserAboutInternetConnection() {
        new AlertDialog.Builder(this)
                .setMessage("There is no internet connection.Connect and come back.")
                .setTitle("INTERNET CONNECTION")
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();

    }

    private void initializeViews() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_main);
        locationTextView = findViewById(R.id.location_text_view_main);
        temperatureTextView=findViewById(R.id.temperatureTextView);
        timeTextView=findViewById(R.id.timeTextView);
        summaryTextView=findViewById(R.id.summaryTextView);
        humidityTextView=findViewById(R.id.humidityValueTextView);
        precipitationTextView=findViewById(R.id.precipitationValueTextView);
        drawerLayout = findViewById(R.id.drawer_layout);
        Button dailyActivityButton = findViewById(R.id.dailyActivityButton);
        dailyActivityButton.setOnClickListener(listener -> startDailyActivity());
        Button hourlyActivityButton = findViewById(R.id.hourly_forecast_button);
        hourlyActivityButton.setOnClickListener(view -> startHourlyActivity());
        setUpNavigationView();
        configureToolbar();
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    private void setUpNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.drawer_menu_fullscreen:
                    enterFullScreen();
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.drawer_menu_change_location :
                    startChangeLocationActivity();
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    return  true;
                case R.id.drawer_save_settings:
                    menuItem.setChecked(true);
                    displaySaveSettingsDialog();
                    drawerLayout.closeDrawers();
                    return true;
            }
            return true;
        });
    }

    private void displaySaveSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("CHANGE LOCATION")
                .setMessage("Do you want to change you current location to " + "\""+locationName +"\"")
                .setPositiveButton("Yes,please", (dialog, which) -> saveSettings())
                .setNegativeButton("NO",null)
                .show();
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constraints.KEY_LONGITUDE_SHARED_PREFERENCES,longitude);
        editor.putString(Constraints.KEY_LATITUDE_SHARED_PREFERENCES,latitude);
        editor.putString(Constraints.KEY_LOCATION_SHARED_PREFERENCES,locationName);
        editor.apply();
    }

    private void startChangeLocationActivity() {
        Intent startLocationActivityIntent = new Intent(this,ChangeLocationActivity.class);
        startActivityForResult(startLocationActivityIntent,REQUEST_CODE);
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setTitle("");
    }


    private void startHourlyActivity() {
         if(dataFetched){
              Intent hourlyActivityIntent = new Intent(this,HourlyActivity.class);
              hourlyActivityIntent.putExtra(KEY_PARCELABLE_HOURLY_ARRAY,forecast.getTwoDaysWeather());
              startActivity(hourlyActivityIntent);
         }else {
             Toast.makeText(this, "Please wait,fetching data...", Toast.LENGTH_SHORT).show();
         }
    }

    /**
     * The method creates an intend in order to
     * start DailyActivity
     * We add data to the intent using an ArrayList<DailyWeather>
     * !!!-------We are not able to use a simple List --------!!!
     * In order to access that array in the DailyActivity
     * we need a TAG
     * If the data is not yet fetched ,we display a simple Toast
     */
    private void startDailyActivity() {
        if(dataFetched ) {
            Intent dailyActivityIntent = new Intent(this, DailyActivity.class);
            ArrayList<DailyWeather> dailyWeathers = new ArrayList<>(Arrays.asList(forecast.getCurrentWeekWeather()));
            dailyActivityIntent.putParcelableArrayListExtra(KEY_PARCELABLE_DAILY_ARRAY, dailyWeathers);
            startActivity(dailyActivityIntent);
        }else {
            Toast.makeText(this, "Please wait,fetching data...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The method builds an OkHttpRequest in order
     * to obtain data from the dark sky servers
     * For the sake of simplicity we have a default
     * location,Manchester with its latitude and
     * longitude
     * The request is then passed to a call
     * which is executed in the background in order not to
     * block the main Thread
     * The callback method onResponse() is called if we managed to connect to
     * the dark sky servers .If the response is successful then
     * we try to parse the json data and update the views in our main
     * activity
     *
     * <------    Updating the views has to be done in the main Thread !  </------>
     *
     * The callback method onFailure() is called if we did not manage
     * to connect to the dark sky servers and an alert dialog
     * is shown
     */
    private void pushRequestToServer()  {
        String url=String.format("https://api.darksky.net/forecast/%s/%s,%s",getResources().getString(R.string.secretKey), latitude,
                longitude);
        Log.d(TAG,url);
        OkHttpClient client =new OkHttpClient();
        Request request= new  Request.Builder()
                .url(url)
                .build();
        Call call =client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this,"Error trying to get data,please try again",Toast.LENGTH_LONG);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            dataFetched = true;
                            forecast= new Forecast();
                            getForecast(jsonData);
                            updateUI();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void getForecast(String jsonData) throws JSONException {
        forecast.setCurrentWeather(getCurrentWeather(jsonData));
        forecast.setCurrentWeekWeather(getWeeklyWeather(jsonData));
        forecast.setTwoDaysWeather(getHourlyWeather(jsonData));

    }

    /**
     *
     * @param jsonData the jason data from dark sky servers
     *                 we need to process
     * @return an array of 8 DailyWeather objects containing
     * the weather for the each day with specific details
     * @throws JSONException the exception is thrown in case there is
     * an error with parsing the json data
     */
    private DailyWeather[] getWeeklyWeather(String jsonData) throws JSONException {
        DailyWeather [] weekWeather = new DailyWeather [8];
        JSONObject jsonObject = new JSONObject(jsonData);
        //get the daily object
        JSONObject daily = jsonObject.getJSONObject("daily");
        //get the weather for each day
        JSONArray dailyJsonArray = daily.getJSONArray("data");
        for(int i =0; i < dailyJsonArray.length();i++){
            DailyWeather dailyWeather=new DailyWeather();
            JSONObject day = dailyJsonArray.getJSONObject(i);
            dailyWeather.setTimezone(jsonObject.getString("timezone"));
            dailyWeather.setWeekSummary(convertWeekSummary(daily.getString("summary")));
             dailyWeather.setTime(day.getLong("time"));
            dailyWeather.setSummary(day.getString("summary"));
            dailyWeather.setSunriseTime(day.getLong("sunriseTime"));
            dailyWeather.setSunsetTime(day.getLong("sunsetTime"));
            dailyWeather.setTemperatureMax(day.getDouble("temperatureMax"));
            dailyWeather.setTemperatureMin(day.getDouble("temperatureMin"));
            dailyWeather.setHumidity(day.getDouble("humidity"));
            dailyWeather.setHumidity(day.getDouble("humidity"));
            dailyWeather.setTemperatureMaxTime(day.getLong("temperatureMaxTime"));
            dailyWeather.setTemperatureMinTime(day.getLong("temperatureMinTime"));
            weekWeather[i] = dailyWeather;
        }
        return weekWeather;
    }

    /**
     *
     * @param jsonData - the json data from the dark sky servers
     * @return - an array of 48 HourlyWeather objects that contains the
     * weather for the next 48 hours
     * @throws JSONException
     */
    private HourlyWeather[] getHourlyWeather(String jsonData) throws JSONException{
        HourlyWeather[] twoDaysWeather = new HourlyWeather[49];
        JSONObject fullObject = new JSONObject(jsonData);
        JSONObject hourlyObject = fullObject.getJSONObject("hourly");
        JSONArray twoDaysWeatherJsonArray = hourlyObject.getJSONArray("data");
         //process every hour from the jsonArray
        for(int i =0;i< twoDaysWeatherJsonArray.length();i++){
            JSONObject currentHourJson = twoDaysWeatherJsonArray.getJSONObject(i);
            twoDaysWeather[i] = new HourlyWeather(
                                fullObject.getString("timezone"),
                                currentHourJson.getString("summary"),
                                currentHourJson.getDouble("humidity"),
                                currentHourJson.getDouble("temperature"),
                                currentHourJson.getLong("time")
            );

        }
        return twoDaysWeather;
    }


    /**
     *
     * This method needs to run on the main UI thread
     * otherwise it will cause the app to crash
     *
     */
    private void updateUI() {
        CurrentWeather currentWeather = forecast.getCurrentWeather();
        timeTextView.setText(currentWeather.getTimeAsString());
        temperatureTextView.setText("" + currentWeather.getTemperature());
        summaryTextView.setText(currentWeather.getSummary());
        humidityTextView.setText(currentWeather.getHumidity()+"");

        if(currentWeather.getPrecipitationProbability()==0.0){
            precipitationTextView.setText("NONE");
        }else {
            precipitationTextView.setText(currentWeather.getPrecipitationProbability() + "");
        }
        }

    /**
     *
     * @param jsonData the data received from the dark sky servers
     * @return a CurrentWeather object made from the jsonData
     * @throws JSONException in case we are unable to parse the data
     * This method uses the JSONObject class in order to parse
     * the data from the parameter jsonData
     */
    private CurrentWeather getCurrentWeather(String jsonData)throws JSONException {
        CurrentWeather currentWeather=new CurrentWeather();
        JSONObject jsonObject=new JSONObject(jsonData);
        currentWeather.setTimezone(jsonObject.getString("timezone"));
        JSONObject currently = jsonObject.getJSONObject("currently");
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setPrecipitationProbability(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTemperature((int)currently.getDouble("temperature"));
        return currentWeather;
    }


    /**
     * This method uses a ConnectivityManager object along with
     * a NetworkInfo object to check if there is active internet
     * connection
     * @return- true if the NetworkInfo object is not null and there the
     * method isConnected() from the NetworkInfo class returns true
     */
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  networkInfo !=null && networkInfo.isConnected();
    }

    /**
     * Some summaries may have the temperature in fahrenheit
     * We need to change that to Celsius
     * */
    private String convertWeekSummary(String summary) {

        String newSummary= "";
        String [] strings = summary.split(" ");
        for(String word:strings){
            if(!word.matches("[0-9]+.F")){
                newSummary = newSummary + word + " ";
            }else{
                int temperatureFahrenheit = Integer.valueOf(word.substring(0,word.length()-2));
                newSummary = newSummary+ Useful.convertToCelsius(temperatureFahrenheit)+ " C ";
            }
        }
        return  newSummary;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method is used to check if
     * the app should enter full screen
     * or exit fullscreen
     */
    private void enterFullScreen() {
        if(isAppFullscreen){
            exitFullscreen();
            isAppFullscreen = false;
        }else {
             makeAppFullscreen();
             isAppFullscreen = true;
        }
    }

    private void exitFullscreen() {
       getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    private void makeAppFullscreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
           latitude = data.getStringExtra(Constraints.KEY_LATITUDE_REQUEST_LOCATION)+"";
           longitude = data.getStringExtra(Constraints.KEY_LONGITUDE_REQUEST_LOCATION)+"";
           locationName=(data.getStringExtra(Constraints.KEY_LOCATION_REQUEST_LOCATION));
           locationTextView.setText(locationName);
           pushRequestToServer();


        }
    }

    @Override
    public void onRefresh() {
        pushRequestToServer();
        swipeRefreshLayout.setRefreshing(false);
    }
}