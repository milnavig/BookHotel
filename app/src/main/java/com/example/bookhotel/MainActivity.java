package com.example.bookhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar; // заменил для работы тулбара
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //private Button add_word;
    //private Button login;
    Button btnForm;
    TextView inputDate;
    TextView inputTime;
    TextView inputNights;
    TextView inputGuests;
    Spinner spinner;
    DBHelper dbHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.custom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent("com.example.bookhotel.ReservationList");
                startActivity(intent);
                return true;
            case R.id.item2:
                return true;
            case R.id.item3:
                return true;
            case R.id.item4:
                dbHelper = new DBHelper(this);
                //SQLiteDatabase database = dbHelper.getWritableDatabase();
                dbHelper.deleteAuth();
                Intent intent2 = new Intent("com.example.bookhotel.Login");
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Book Hotel");
        toolbar.setLogo(R.drawable.logo_white_25px);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.menu_25));

        addListenerOnButton();
    }

    private ListView listviewitems;
    private MainActivity.ItemAdapter adapteritem;
    private ArrayList<News> arrListItems;

    public void addListenerOnButton() {
        //add_word = (Button)findViewById(R.id.but);
        //login = (Button)findViewById(R.id.login);

        isLogined();

        getListItemData();

        renderForm();
    }

    private Boolean renderForm(){
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Одеса", "Київ"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        getListItemData();

        btnForm = (Button) findViewById(R.id.buttonForm);
        inputDate = (TextView) findViewById(R.id.inputDate);
        inputTime = (TextView) findViewById(R.id.inputTime);
        inputNights = (TextView) findViewById(R.id.inputNights);
        inputGuests = (TextView) findViewById(R.id.inputGuests);
        spinner = (Spinner) findViewById(R.id.spinner);
        dbHelper = new DBHelper(this);

        btnForm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        final String date = inputDate.getText().toString();
                        final String time = inputTime.getText().toString();
                        final String nights = inputNights.getText().toString();
                        final String guests = inputGuests.getText().toString();
                        final String city = spinner.getSelectedItem().toString();

                        String user = dbHelper.getAuth();

                        OkHttpClient client = new OkHttpClient();

                        String url = "https://for-thesis.space/make-reservation";

                        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
                        httpBuilder.addQueryParameter("city", city).addQueryParameter("arrivalDate", date)
                                .addQueryParameter("arrivalTime", time).addQueryParameter("nights", nights)
                                .addQueryParameter("guests", guests).addQueryParameter("user", user);

                        Request request = new Request.Builder()
                                .url(httpBuilder.build())
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                final String my_response = response.body().string();

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent("com.example.bookhotel.ReservationList");
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
            }
        );

        return Boolean.TRUE;
    }


    private Boolean isLogined(){
        dbHelper = new DBHelper(this);
        Log.d("EMAIL", dbHelper.getAuth());
        if (dbHelper.getAuth() == "no") {
            Intent intent = new Intent("com.example.bookhotel.Login");
            startActivity(intent);
        } else {

        }
        return true;
    }

    ArrayList<News> listViewNews;

    private ArrayList<News> getListItemData(){
        listViewNews = new ArrayList<News>();
        listviewitems = (ListView) findViewById(R.id.list_news);

        //arrListItems = getListItemData();

        //dbHelper = new DBHelper(this);
        //SQLiteDatabase dbdata = dbHelper.getWritableDatabase();
        //получаем данные из бд в виде курсора
        //userCursor =  dbdata.rawQuery("select * from "+ dbHelper.TABLE_WORDS, null);
        //Integer num = userCursor.getCount();
        //userCursor.moveToFirst();
        OkHttpClient client = new OkHttpClient();
        String url = "https://for-thesis.space/events.json";

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                        final String my_response = response.body().string();
                        //code = my_response;
                        /*
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                test.setText(my_response);
                            }
                        });*/

                        //Log.d("HELLO", my_response);

                        //JSONObject jObj = new JSONObject(my_response); // не нужно
                        //String name = jObj.getString("name");
                        List<HashMap<String,String>> res = new ArrayList<>();
                        JSONArray jsonarray = null;
                        try {
                            jsonarray = new JSONArray(my_response);
                            for(int i=0;i<jsonarray.length();i++) {
                                JSONObject jsonObject = jsonarray.getJSONObject(i);
                                Iterator<?> iterator = jsonObject.keys();
                                HashMap<String,String> map = new HashMap<>();
                                while (iterator.hasNext()) {
                                    Object key = iterator.next();
                                    Object value = jsonObject.get(key.toString());
                                    map.put(key.toString(),value.toString());

                                }
                                res.add(map);

                                listViewNews.add(new News(map.get("title"), map.get("description"), map.get("img"), map.get("date")));
                            }

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(listViewNews.size()>0)
                                    {
                                        adapteritem = new ItemAdapter(MainActivity.this, listViewNews);
                                        listviewitems.setAdapter(adapteritem);
                                    }
                                    else
                                    {

                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
            }
        });

        return listViewNews;
    }

    public class ItemAdapter extends BaseAdapter {
        private LayoutInflater lyt_Inflater = null;

        private ArrayList<News> arrlstitems;
        private Context context;

        public ItemAdapter(Context cnt,ArrayList<News> items)
        {
            this.context = cnt;
            this.arrlstitems = items;
        }


        @Override
        public int getCount()
        {
            return arrlstitems.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View view_lyt = convertView;
            try
            {

                if(arrlstitems.size()>0)
                {
                    final News item = arrlstitems.get(position);

                    String title = item.getTitle();
                    String description = item.getDescription();
                    String img = item.getImg();
                    String date = item.getDate();

                    //URL newurl = new URL("https://for-thesis.space/"+img);
                    //Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());



                    lyt_Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view_lyt = lyt_Inflater.inflate(R.layout.item_news, null);

                    TextView txt_title=(TextView) view_lyt.findViewById(R.id.title);
                    TextView txt_body=(TextView) view_lyt.findViewById(R.id.body);
                    //ImageView newsIcon = (ImageView) view_lyt.findViewById(R.id.imageNews);
                    //newsIcon.setImageBitmap(mIcon_val);
                    txt_title.setText(title);
                    txt_body.setText(description);


                    view_lyt.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {


                        }
                    });

                }

            }
            catch (Exception e)
            {
                Log.i("Exception==", e.toString());
            }

            return view_lyt;
        }
    }
}
