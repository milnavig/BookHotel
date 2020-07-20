package com.example.bookhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReservationList extends AppCompatActivity {
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
    TextView BatteryData;
    int percentage;
    SimpleDateFormat formatter;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

        /* Toolbar */
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Book Hotel");
        toolbar.setLogo(R.drawable.logo_white_25px);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.menu_25));

        addListenerOnButton();
        /*
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    date = new Date();
                    Log.i("BatteryStatus", " " +  percentage + " " + formatter.format(date));

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            BatteryData = (TextView) findViewById(R.id.BatteryData);;
                            String newData = BatteryData.getText().toString() + " " +  percentage + " " + formatter.format(date);
                            BatteryData.setText(newData);
                        }
                    });
                }
            }
        },0,15000);*/

        Context context = this;
        context.deleteDatabase("Auth");
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_USER, "alex4@gmail.com");
        contentValues.put(DBHelper.BOOK_CITY, "Одеса");
        contentValues.put(DBHelper.BOOK_ARRIVAL_DATE, "5-го листопада 2022");
        contentValues.put(DBHelper.BOOK_ARRIVAL_TIME, "11:00");
        contentValues.put(DBHelper.BOOK_NIGHTS, "3");
        contentValues.put(DBHelper.BOOK_GUESTS, "2");
        contentValues.put(DBHelper.BOOK_STATUS, "Підтверджено");
        contentValues.put(DBHelper.BOOK_ON, "2020-04-05T23:35:04+03:00");
        contentValues.put(DBHelper.BOOK_PRICE, "603");
        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms");
        date = new Date();
        Log.i("StartWriting", " " + formatter.format(date));
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        for (int i = 0; i < 50; i++) {
            database.insert(DBHelper.TABLE_BOOKINGS, null, contentValues);
        }
        date = new Date();
        Log.i("EndWriting", " " + formatter.format(date));


        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms");
        date = new Date();
        Log.i("StartReading", " " + formatter.format(date));
        dbHelper = new DBHelper(this);
        dbHelper.getBookings();
        Date date2 = new Date();
        Log.i("EndReading", " " + formatter.format(date2));
    }

    private ListView listviewitems;
    private ItemAdapter adapteritem;
    private ArrayList<Reservation> arrListItems;

    public void addListenerOnButton() {
        listviewitems = (ListView) findViewById(R.id.list);
        //arrListItems = getListItemData();

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
                                Log.d("HELLO3", "ERRRRRREE");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                final String my_response = response.body().string();

                                ReservationList.this.runOnUiThread(new Runnable() {
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
    }

    ArrayList<Reservation> listViewReservations;
    //DBHelper dbHelper;

    private ArrayList<Reservation> getListItemData(){
        ArrayList<Reservation> listViewItems = new ArrayList<Reservation>();
        listViewReservations = new ArrayList<Reservation>();
        listviewitems = (ListView) findViewById(R.id.list);

        //dbHelper = new DBHelper(this);
        //SQLiteDatabase dbdata = dbHelper.getWritableDatabase();
        //получаем данные из бд в виде курсора
        //userCursor =  dbdata.rawQuery("select * from "+ dbHelper.TABLE_WORDS, null);
        //Integer num = userCursor.getCount();
        //userCursor.moveToFirst();
        /*
        for (Integer i =0 ; i < 2; i++) {
            listViewItems.add(new Reservation(1, "alex@gmail.com", "Одеса","5-го листопада 2022","11:00","3","2","Підтверджено","2020-04-05T23:35:04+03:00",603));
            //userCursor.moveToNext();
        }*/
        OkHttpClient client = new OkHttpClient();

        String url = "https://for-thesis.space/reservations.json";

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        dbHelper = new DBHelper(this);
        String user_email = dbHelper.getAuth();

        httpBuilder.addQueryParameter("user",user_email);
        Log.d("EMAIL", user_email);


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

                            listViewReservations.add(new Reservation(map.get("id"), map.get("user"), map.get("city"), map.get("arrivalDate"), map.get("arrivalTime"), map.get("nights"), map.get("guests"), map.get("status"), map.get("bookedOn"), map.get("price")));
                        }

                        ReservationList.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(listViewReservations.size()>0)
                                {
                                    adapteritem = new ReservationList.ItemAdapter(ReservationList.this, listViewReservations);
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
        });


        return listViewItems;
    }


public class ItemAdapter extends BaseAdapter {
        private LayoutInflater lyt_Inflater = null;

        private ArrayList<Reservation> arrlstitems;
        private Context context;

        public ItemAdapter(Context cnt,ArrayList<Reservation> items)
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
                    final Reservation item = arrlstitems.get(position);
                    String id = item.getId();
                    String user = item.getUser();
                    String city = item.getCity();
                    String arrivalDate = item.getArrivalDate();
                    String arrivalTime = item.getArrivalTime();
                    String nights = item.getNights();
                    String guests = item.getGuests();
                    String status = item.getStatus();
                    String bookedOn = item.getBookedOn();
                    String price = item.getPrice();


                    lyt_Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view_lyt = lyt_Inflater.inflate(R.layout.item_reservation, null);

                    TextView txt_id=(TextView) view_lyt.findViewById(R.id.id);
                    TextView txt_arrivalTime=(TextView) view_lyt.findViewById(R.id.arrivalTime);
                    TextView txt_info=(TextView) view_lyt.findViewById(R.id.info);
                    TextView txt_status=(TextView) view_lyt.findViewById(R.id.status);
                    TextView txt_bookedOn=(TextView) view_lyt.findViewById(R.id.bookedOn);
                    TextView txt_price=(TextView) view_lyt.findViewById(R.id.price);
                    //ImageButton imgbtnDelete=(ImageButton) view_lyt.findViewById(R.id.imgbtnDelete);
                    //ImageButton imgbtnEdit=(ImageButton) view_lyt.findViewById(R.id.imgbtnEdit);
                    txt_id.setText("Номер бронювання: " + id);
                    txt_arrivalTime.setText(arrivalDate + " " + arrivalTime);
                    txt_info.setText(nights + " ночей " + guests + " гостей");
                    txt_status.setText(status);
                    txt_bookedOn.setText("Дата замовлення: " + bookedOn);
                    txt_price.setText(price + ".99 ₴");

                /*
                if(isEditMode)
                {
                    imgbtnDelete.setVisibility(View.VISIBLE);
                }
                else
                {
                    imgbtnDelete.setVisibility(View.GONE);
                }*/

                    view_lyt.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {


                        }
                    });
                    /*
                    imgbtnDelete.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            if(arrlstitems.size()>0)
                                removeItemFromList(position);
                        }
                    });

                    imgbtnEdit.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            if(arrlstitems.size()>0)
                                editItemFromList(position);
                        }
                    });*/

                }

            }
            catch (Exception e)
            {
                Log.i("Exception==", e.toString());
            }

            return view_lyt;
        }
    }
    /*
    // Method for remove Single item from list
    protected void removeItemFromList(int position)
    {
        final int deletePosition = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(WordList.this);
        builder.setTitle("Alert!");
        builder.setMessage("Do you want delete this item?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //System.out.println(deletePosition);
                        Word el = arrListItems.get(deletePosition);
                        Integer el_id = el.getId();
                        dbHelper.deleteWord(el);

                        arrListItems.remove(deletePosition);
                        adapteritem.notifyDataSetChanged();
                        adapteritem.notifyDataSetInvalidated();

                        if(arrListItems.size()==0)
                        {

                        }
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        dialog.cancel();
                    }
                });
        AlertDialog  alertDialog = builder.create();
        alertDialog.show();
    }
    protected void editItemFromList(int position)
    {
        final int editPosition = position;
        Word el = arrListItems.get(editPosition);
        int el_id = el.getId();

        Intent intent = new Intent("com.example.learnwords.EditWord");
        intent.putExtra("id", el_id);
        startActivity(intent);
    }*/
}
