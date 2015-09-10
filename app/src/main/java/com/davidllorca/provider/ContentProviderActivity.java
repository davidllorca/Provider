package com.davidllorca.provider;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;


public class ContentProviderActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        /*
            All contacts
         */
        //Uri allContacts = Uri.parse("content://contacts/people");
        // or
        Uri allContacts = ContactsContract.Contacts.CONTENT_URI;

        /*
            Retrieve first contact

            Uri firsContact = Uri.parse("content://contacts/people/1");
             or
            firsContact = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, 1)
         */

        // Parameters to return in cursor. 3rd parameter in CursorLoader. "null" for return all paramenters.
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER};

        Cursor cursor;
        /*
            4th/5th parameter: Selection(WHERE)
            E.g. ..., ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%Smith'", null, null);
            E.g. ..., ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?", new String[]{"%Smith}, null);
            6th parameter: sortOrder(ORDER BY)
            E.g.  CursorLoader cursorLoader = new CursorLoader(
                this,
                allContacts,
                projection,
                ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?",
                new String[]{"%Smith},
                ContactsContract.Contacts.DISPLAY_NAME + "ASC");
         */
        CursorLoader cursorLoader = new CursorLoader(
                this,
                allContacts,
                projection,
                null,
                null,
                null);
        cursor = cursorLoader.loadInBackground(); // Query in new Thread

        String[] columns = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID};

        int[] views = new int[]{
                R.id.contact_name,
                R.id.contact_id};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.activity_provider, cursor, columns, views,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        this.setListAdapter(adapter);

        // Show results by Cursor
        printContacts(cursor);
    }

    private void printContacts(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                // Get id
                String contactId = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID));
                // Get contact name
                String contactDisplayName = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                Log.v("Content provider", contactId + ", " + contactDisplayName);
                //Get phone number
                int contactPhone = 0;
                // Check phone parameter
                int hasPhone = cursor.getInt(cursor.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER));
                // "1" if there is at least one phone number, "0" otherwise.
                if (hasPhone == 1) {
                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null, null);
                    while (phoneCursor.moveToNext()) {
                        contactPhone = phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER);
                        Log.v("Content provider", contactPhone + "");
                    }
                    phoneCursor.close();
                }
            } while (cursor.moveToNext());
        }
    }
}
