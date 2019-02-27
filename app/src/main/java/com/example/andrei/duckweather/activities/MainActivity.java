package com.example.andrei.duckweather.activities;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.fragments.CurrentWeatherFragment;
import com.example.andrei.duckweather.fragments.ViewPagerAdapter;
import com.example.andrei.duckweather.model.AppDatabase;
import com.example.andrei.duckweather.model.Constraints;
import com.example.andrei.duckweather.model.CurrentWeather;
import com.example.andrei.duckweather.model.DailyWeather;
import com.example.andrei.duckweather.model.DatabaseInterface;
import com.example.andrei.duckweather.model.Forecast;
import com.example.andrei.duckweather.model.HourlyWeather;
import com.example.andrei.duckweather.model.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements CurrentWeatherFragment.MainFragmentCallback {

    public static final int REQUEST_CODE = 1;
    private Forecast forecast;
    private String latitude;
    private String longitude;
    private String locationName;
    private DrawerLayout drawerLayout;
    private String requestURL;
    private SharedPreferences sharedPreferences;
    private DatabaseInterface databaseInterface;
    private boolean isAppFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocalSettings();
        initializeDatabase();
        initializeUI();


        if (Utilities.isNetworkAvailable(this)) {
            pushRequest();
        } else {
            alertUserAboutInternetConnection();
        }
    }

    private void initializeUI() {
        setUpNavigationView();
        configureToolbar();
    }

    private void showViewPagerFragment() {
        ViewPager viewPager = findViewById(R.id.view_pager_main);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), forecast);
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void initializeDatabase() {
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "room_database")
                .build();
        databaseInterface = appDatabase.dao();
    }

    private void getStoredDataFromDatabase() {
        Thread backgroundThread = new Thread(() -> {
            forecast = new Forecast();
            forecast.setCurrentWeather(databaseInterface.getCurrentWeather());
            forecast.setCurrentWeekWeather(databaseInterface.getCurrentWeekWeather());
            forecast.setHourlyWeather(databaseInterface.getTwoDaysWeather());
        });
        backgroundThread.start();
    }

    private void getLocalSettings() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        latitude = sharedPreferences.getString(Constraints.KEY_LATITUDE_SHARED_PREFERENCES, "53.4808");
        longitude = sharedPreferences.getString(Constraints.KEY_LONGITUDE_SHARED_PREFERENCES, "2.2426");

        buildRequestURL(latitude, longitude);

        locationName = sharedPreferences.getString(Constraints.KEY_LOCATION_SHARED_PREFERENCES, "Manchester");
        isAppFullscreen = sharedPreferences.getBoolean(Constraints.KEY_APP_FULLSCREEN, false);

        if (isAppFullscreen) {
            makeAppFullscreen();
        }

    }

    private void buildRequestURL(String latitude, String longitude) {
        requestURL = String.format("https://api.darksky.net/forecast/%s/%s,%s", getResources().getString(R.string.apiKey), latitude,
                longitude);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constraints.KEY_LONGITUDE_SHARED_PREFERENCES, longitude);
        editor.putString(Constraints.KEY_LATITUDE_SHARED_PREFERENCES, latitude);
        editor.putString(Constraints.KEY_LOCATION_SHARED_PREFERENCES, locationName);
        editor.putBoolean(Constraints.KEY_APP_FULLSCREEN, isAppFullscreen);
        editor.apply();
        Toast.makeText(this, "Location change successfully", Toast.LENGTH_SHORT).show();
    }

    private void alertUserAboutInternetConnection() {
        new AlertDialog.Builder(this)
                .setMessage("There is no internet connection.The app will switch to offline mode using stored data")
                .setTitle("INTERNET CONNECTION")
                .setPositiveButton("SWITCH", (view, which) -> getStoredDataFromDatabase())
                .setNegativeButton("EXIT", (view, which) -> finish())
                .show();

    }


    private void setUpNavigationView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.drawer_menu_fullscreen:
                    changeFullscreenMode();
                    drawerLayout.closeDrawers();
                    menuItem.setChecked(false);
                    return true;
                case R.id.drawer_menu_change_location:
                    startChangeLocationActivity();
                    drawerLayout.closeDrawers();
                    menuItem.setChecked(false);
                    return true;


            }
            return true;
        });
    }

    private void displaySaveSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("CHANGE LOCATION")
                .setMessage("Do you want to change you current location to " + "\"" + locationName + "\"? " +
                        "\nChanges will take place after you restart the app.")
                .setPositiveButton("Yes,please", (dialog, which) -> saveSettings())
                .setNegativeButton("NO", null)
                .show();
    }


    private void startChangeLocationActivity() {
        Intent startLocationActivityIntent = new Intent(this, ChangeLocationActivity.class);
        startActivityForResult(startLocationActivityIntent, REQUEST_CODE);
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setTitle("");
    }


    /**
     * Using the Volley library we push a get request to the
     * DarkSky servers to get the currentForecast
     * <p>
     * We initiate a request queue and then we push our request
     * <p>
     * Finally,we need to update the UI with the fetched data
     * <p>
     * <------    Updating the views has to be done in the main Thread !  </------>
     */
    private void pushRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, response -> {
            getForecast(response);
            runOnUiThread(() -> showViewPagerFragment());


        }, error -> error.printStackTrace());

        requestQueue.add(stringRequest);
    }

    /**
     * The method is called to replace the existing
     * data in the room database
     * It erases all data from the current database
     * and then repopulates it with fresh data
     * This operation must be done in the background
     */
    private void updateLocalDatabase() {
        Thread backgroundThread = new Thread(() -> {
            //delete all data if exists
            databaseInterface.deleteAllDataFromCurrentWeather();
            databaseInterface.deleteAllDataFromDailyWeather();
            databaseInterface.deleteAllDataFromHourlyWeather();
            //insert new data
            databaseInterface.insertCurrentWeather(forecast.getCurrentWeather());
            databaseInterface.insertTwoDaysWeather(forecast.getHourlyWeather());
            databaseInterface.insertCurrentWeekWeather(forecast.getCurrentWeekWeather());
        });
        backgroundThread.start();


    }

    private void getForecast(String jsonData) {
        try {
            forecast = new Forecast();
            forecast.setCurrentWeather(getCurrentWeather(jsonData));
            forecast.setCurrentWeekWeather(getWeeklyWeather(jsonData));
            forecast.setHourlyWeather(getHourlyWeather(jsonData));
            updateLocalDatabase();
        } catch (JSONException jsonE) {
            jsonE.printStackTrace();
        }
    }

    /**
     * @param jsonData the jason data from dark sky servers
     *                 we need to process
     * @return an array of 8 DailyWeather objects containing
     * the weather for the each day with specific details
     * @throws JSONException the exception is thrown in case there is
     *                       an error with parsing the json data
     */
    private DailyWeather[] getWeeklyWeather(String jsonData) throws JSONException {
        DailyWeather[] weekWeather = new DailyWeather[8];
        JSONObject jsonObject = new JSONObject(jsonData);
        //get the daily object
        JSONObject daily = jsonObject.getJSONObject("daily");
        //get the weather for each day
        JSONArray dailyJsonArray = daily.getJSONArray("data");
        for (int i = 0; i < dailyJsonArray.length(); i++) {
            String timezone = jsonObject.getString("timezone");

            DailyWeather dailyWeather = new DailyWeather();
            JSONObject day = dailyJsonArray.getJSONObject(i);

            dailyWeather.setIcon(day.getString("icon"));

            dailyWeather.setWeekSummary(convertWeekSummary(daily.getString("summary")));

            dailyWeather.setDay(Utilities.formatTime(
                    day.getLong("time"), timezone, "EEEE"));

            dailyWeather.setSunriseTime(Utilities.formatTime(
                    day.getLong("sunriseTime"), timezone, "H:mm"
            ));

            dailyWeather.setSunsetTime(Utilities.formatTime(
                    day.getLong("sunsetTime"), timezone, "H:mm"
            ));

            dailyWeather.setTemperatureMax(Utilities.convertToCelsius
                    (day.getDouble("temperatureMax")));

            dailyWeather.setTemperatureMin(Utilities.convertToCelsius(
                    day.getDouble("temperatureMin")));

            dailyWeather.setHumidity(day.getDouble("humidity"));

            dailyWeather.setTemperatureMinTime(Utilities.formatTime(
                    day.getLong("temperatureMinTime"), timezone, "H:mm"));

            dailyWeather.setTemperatureMaxTime(Utilities.formatTime(
                    day.getLong("temperatureMaxTime"), timezone, "H:mm"
            ));

            weekWeather[i] = dailyWeather;
        }
        return weekWeather;
    }

    /**
     * @param jsonData - the json data from the dark sky servers
     * @return - an array of 48 HourlyWeather objects that contains the
     * weather for the next 48 hours
     * @throws JSONException
     */
    private HourlyWeather[] getHourlyWeather(String jsonData) throws JSONException {
        HourlyWeather[] twoDaysWeather = new HourlyWeather[49];
        JSONObject fullObject = new JSONObject(jsonData);
        JSONObject hourlyObject = fullObject.getJSONObject("hourly");
        JSONArray twoDaysWeatherJsonArray = hourlyObject.getJSONArray("data");
        //process every hour from the jsonArray
        for (int i = 0; i < twoDaysWeatherJsonArray.length(); i++) {
            JSONObject currentHourJson = twoDaysWeatherJsonArray.getJSONObject(i);
            twoDaysWeather[i] = new HourlyWeather(
                    fullObject.getString("timezone"),
                    currentHourJson.getString("summary"),
                    currentHourJson.getDouble("humidity"),
                    Utilities.convertToCelsius(currentHourJson.getDouble("temperature")),
                    currentHourJson.getLong("time"),
                    currentHourJson.getString("icon")
            );

        }
        return twoDaysWeather;
    }


    /**
     * @param jsonData the data received from the dark sky servers
     * @return a CurrentWeather object made from the jsonData
     * @throws JSONException in case we are unable to parse the data
     *                       This method uses the JSONObject class in order to parse
     *                       the data from the parameter jsonData
     */
    private CurrentWeather getCurrentWeather(String jsonData) throws JSONException {
        CurrentWeather currentWeather = new CurrentWeather();
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject currently = jsonObject.getJSONObject("currently");
        currentWeather.setTimezone(jsonObject.getString("timezone"));
        currentWeather.setIconID(Utilities.getImageId(currently.getString("icon")));
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setPrecipitationProbability(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTemperature((Utilities.convertToCelsius(currently.getDouble("temperature"))));
        currentWeather.setLocationName(locationName);
        return currentWeather;
    }


    /**
     * Some summaries may have the temperature in fahrenheit
     * We need to change that to Celsius
     */
    private String convertWeekSummary(String summary) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] strings = summary.split(" ");
        for (String word : strings) {
            if (!word.matches("[0-9]+.F")) {
                stringBuilder.append(word).append(" ");
            } else {
                int temperatureFahrenheit = Integer.valueOf(word.substring(0, word.length() - 2));
                stringBuilder.append(Utilities.convertToCelsius(temperatureFahrenheit) + " C ");
            }
        }
        return stringBuilder.toString();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
    private void changeFullscreenMode() {
        if (isAppFullscreen) {
            exitFullscreen();
            isAppFullscreen = false;
        } else {
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
    public void refresh() {
        pushRequest();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            latitude = data.getStringExtra(Constraints.KEY_LATITUDE_REQUEST_LOCATION) + "";
            longitude = data.getStringExtra(Constraints.KEY_LONGITUDE_REQUEST_LOCATION) + "";
            locationName = data.getStringExtra(Constraints.KEY_LOCATION_REQUEST_LOCATION);
            displaySaveSettingsDialog();

        }
    }


}