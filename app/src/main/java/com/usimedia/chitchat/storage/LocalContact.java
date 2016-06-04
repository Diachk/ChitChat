package com.usimedia.chitchat.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.usimedia.chitchat.model.ChatContact;
import com.usimedia.chitchat.model.Column;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Cheick on 04/06/16.
 */
public class LocalContact extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chitchat";
    private static final String TABLE_NAME = "contacts";
    private static final int VERSION = 1;
    private static final Column ID;
    private static final Column EMAIL;
    private static final Column NAME;
    private static final Column STATUS_MESSAGE;
    private static final Column LAST_SEEN;
    private static final String SPACE = " ";
    private static final SimpleDateFormat SERVICE_RESPONSE_DATE_FORMAT;

    static {
        ID = new Column("_id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        EMAIL = new Column("email" , "TEXT");
        NAME = new Column("name" ,  "TEXT");
        STATUS_MESSAGE = new Column("status_message", "TEXT");
        LAST_SEEN = new Column("last_seen", "TEXT");
        SERVICE_RESPONSE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
    }


    public LocalContact(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private void createTable(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS"
                + SPACE + TABLE_NAME + SPACE
                + "("
                + SPACE + ID.getName() + SPACE + ID.getType() + SPACE + ","
                + SPACE + EMAIL.getName() + SPACE + EMAIL.getType() + SPACE + ","
                + SPACE + NAME.getName() + SPACE + NAME.getType() + SPACE + ","
                + SPACE + STATUS_MESSAGE.getName() + SPACE + STATUS_MESSAGE.getType() + SPACE + ","
                + SPACE + LAST_SEEN.getName() + SPACE + LAST_SEEN.getType() + SPACE
                + ");");
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IS EXISTS" + SPACE + TABLE_NAME + ";");
    }

    private void insertChatContact(ChatContact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EMAIL.getName(), contact.getEmail());
        cv.put(NAME.getName(), contact.getName());
        cv.put(STATUS_MESSAGE.getName(), contact.getStatusMessage());
        cv.put(LAST_SEEN.getName(), SERVICE_RESPONSE_DATE_FORMAT.format(contact.getLastSeen()));

        db.insert(TABLE_NAME, null, cv);
    }

    private Cursor getAllContacts() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT" + SPACE + "*" + SPACE + "FROM" + SPACE + TABLE_NAME + ";";

        return db.rawQuery(query, null);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        createTable(db);
    }
}
