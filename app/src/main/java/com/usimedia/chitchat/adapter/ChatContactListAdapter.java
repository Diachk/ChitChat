package com.usimedia.chitchat.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.usimedia.chitchat.R;
import com.usimedia.chitchat.model.ChatContact;

import java.text.SimpleDateFormat;

/**
 * Created by Cheick on 03/06/16.
 */
public class ChatContactListAdapter extends ArrayAdapter<ChatContact> {

    private static final SimpleDateFormat UI_DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
    private static final int CHAT_CONTACT_LAYOUT_ID = R.layout.chat_contact_list_item;
    private ChatContact[] contacts;
    private Activity context;

    public ChatContactListAdapter(Activity activity, ChatContact[] objects) {
        super(activity, CHAT_CONTACT_LAYOUT_ID, objects);
        contacts = objects;
        context = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View chatContactView;

        if(convertView == null) {
            chatContactView = context.getLayoutInflater().inflate(CHAT_CONTACT_LAYOUT_ID, parent, false);
        }
        else {
            chatContactView = convertView;
        }

        ChatContact currentContact = contacts[position];

        TextView nameTextView = (TextView) chatContactView
                .findViewById(R.id.list_item_chat_contact_name);

        nameTextView.setText(currentContact.getName());

        TextView statusMessageTextView = (TextView) chatContactView
                .findViewById(R.id.list_item_chat_contact_status_message);

        statusMessageTextView.setText(currentContact.getStatusMessage());

        TextView lastSeenTextView = (TextView) chatContactView
                .findViewById(R.id.list_item_chat_contact_last_seen);

        lastSeenTextView.setText(UI_DATE_FORMAT.format(currentContact.getLastSeen()));


        return chatContactView;
    }
}
