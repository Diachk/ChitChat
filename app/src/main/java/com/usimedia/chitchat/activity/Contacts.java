package com.usimedia.chitchat.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.usimedia.chitchat.R;
import com.usimedia.chitchat.adapter.ChatContactListAdapter;
import com.usimedia.chitchat.model.ChatContact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Contacts extends AppCompatActivity {

    private static final SimpleDateFormat SERVICE_RESPONSE_DATE_FORMAT;
    private static final String CONTACTS_SERVICE_URL;
    private static final OkHttpClient HTTP_CLIENT;
    private static final MediaType JSON;

    static {
        SERVICE_RESPONSE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        CONTACTS_SERVICE_URL = "http:192.168.1.5:8000/api/contacts";
        HTTP_CLIENT = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
    }



    private int numberOfContact;
    private ListView contactListView;


    private class contactResolverTask extends AsyncTask<String, Void, List<ChatContact>> {
        @Override
        protected List<ChatContact> doInBackground(String... params) {
            List<String> phoneNumbers = Arrays.asList(params);

            try {
                return getContacts(phoneNumbers);
            } catch (JSONException e) {
                Log.d("Contacts" , "Json parser exception");
                e.printStackTrace();
                return Collections.emptyList();
            } catch (IOException e) {
                Log.d("Contacts" , "Network Exception");
                e.printStackTrace();
                return Collections.emptyList();
            }

        }

        @Override
        protected void onPostExecute(List<ChatContact> names) {
            super.onPostExecute(names);
            setValuesToUiListView(names);
        }
    }

    private List<ChatContact> getContacts(List<String> phoneNumbers) throws JSONException, IOException {

        JSONArray jsonNumbers = new JSONArray(phoneNumbers);
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("numbers", jsonNumbers);

        String rawResult = post(CONTACTS_SERVICE_URL, jsonRequest.toString());

        JSONObject jsonResult = new JSONObject(rawResult);

        JSONArray jsonContacts = jsonResult.getJSONArray("contacts");

        Log.d("Contacts" , "Number of contacts received from backend = " + jsonContacts.length());

        final List<ChatContact> contactList = new ArrayList<>();

        ChatContact currentContact;

        for (int i=0; i<jsonContacts.length(); i++){
            currentContact = new ChatContact();
            currentContact.setName(jsonContacts.getJSONObject(i)
                    .getString("name"));

            currentContact.setStatusMessage(jsonContacts.getJSONObject(i)
                    .getString("status_message"));

            try {
                currentContact.setLastSeen(SERVICE_RESPONSE_DATE_FORMAT
                        .parse(jsonContacts.getJSONObject(i).getString("last_seen")));
            } catch (ParseException e) {
                e.printStackTrace();
                currentContact.setLastSeen(null);
            }

            contactList.add(currentContact);
            Log.d("Contacts", "Current contact name being parsed = " + currentContact.getName());
        }

        return contactList;
    }

    private String post(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();

        return response.body().string();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactListView = (ListView) findViewById(R.id.contact_ativity_contact_list);

        ContentResolver contentResolver = getContentResolver();

        final Uri providerUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        final String[] elementsRequired = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = contentResolver.query(
                providerUri,
                elementsRequired,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

        numberOfContact = cursor.getCount();


        Set<String> contactNumbers = new HashSet<>();

        String number;

        while (cursor.moveToNext())
        {
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if(null != number) {
                contactNumbers.add(number.replaceAll("\\s+", ""));
            }

        }

        final List<String> distinctNumbers = new ArrayList<>(contactNumbers);

        String[] numbersBuffer = new String[distinctNumbers.size()];

        new contactResolverTask().execute(distinctNumbers.toArray(numbersBuffer));
    }

    private void setValuesToUiListView(final List<ChatContact> elements) {

        ChatContact[] elementsBuffer = new ChatContact[elements.size()];

        final ArrayAdapter<ChatContact> contactListAdapter = new ChatContactListAdapter(
                Contacts.this,
                elements.toArray(elementsBuffer));

        contactListView.setAdapter(contactListAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toContactProfileActivity = new Intent(Contacts.this, ContactProfile.class);
                toContactProfileActivity.putExtra("chat_contact", elements.get(position));
                startActivity(toContactProfileActivity);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Contact", "Number of contacts = "+numberOfContact);
        Toast.makeText(Contacts.this, "Number of contacts = " + numberOfContact, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
