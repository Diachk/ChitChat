package com.usimedia.chitchat;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Contacts extends AppCompatActivity {

    int numberOfContact;
    Set<String> contactNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ListView contactListView = (ListView) findViewById(R.id.contact_ativity_contact_list);






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


        contactNumbers = new HashSet<>();

        String number = null;

        while (cursor.moveToNext())
        {
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if(null != number) {
                contactNumbers.add(number.replaceAll("\\s+", ""));
            }

        }

        final List<String> distinctNumbers = new ArrayList<>(contactNumbers);

        final ArrayAdapter<String> contactListAdapter = new ArrayAdapter<>(
                Contacts.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                distinctNumbers
        );



        contactListView.setAdapter(contactListAdapter);
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
