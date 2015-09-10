package com.davidllorca.provider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class OwnContentProviderActivity extends Activity {

    // Views
    EditText ISBNEditText;
    EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_content_provider);

        // References views
        ISBNEditText = (EditText) findViewById(R.id.ISBN_txt);
        titleEditText = (EditText) findViewById(R.id.title_txt);
    }

    /**
     * Add book into provider.
     *
     * @param view
     */
    public void onClickAddTitle(View view) {
        // Prepare data
        ContentValues values = new ContentValues();
        values.put(BooksProvider.TITLE, titleEditText.getText().toString());
        values.put(BooksProvider.ISBN, ISBNEditText.getText().toString());
        // Inset book
        Uri uri = getContentResolver().insert(BooksProvider.CONTENT_URI, values);
        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Retrieve all books of provider.
     *
     * @param view
     */
    public void onClickRetrieveTitles(View view) {
        Uri allTitlesUri = Uri.parse("content://com.davidllorca.provider.Books/books");
        Cursor cursor;
        CursorLoader cursorLoader = new CursorLoader(
                this,
                allTitlesUri,
                null,
                null,
                null,
                "title desc");
        cursor = cursorLoader.loadInBackground();
        // Show titles
        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(getBaseContext(),
                        cursor.getString(cursor.getColumnIndex(
                                BooksProvider._ID)) + ", " +
                                cursor.getString(cursor.getColumnIndex(
                                        BooksProvider.TITLE)) + ", " +
                                cursor.getString(cursor.getColumnIndex(
                                        BooksProvider.ISBN)), Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
        }
    }

    /*
        SNIPPETS
     */

    /*
        UPDATE BOOK

        ContentValues editedValues = new ContentValues();
        editedValues.put(BooksProvider.TITLE, "Title to change");
        getContentResolver().update(
            Uri.parse(
            "content://com.davidllorca.provider.Books/books/"+ id,
            editedValues,
            null,
            null);
    */

    /*
        DELETE BOOK

        getContentResolver().delete(
            Uri.parse("content://com.davidllorca.provider.Books/books/" + id,
            null,
            null);
     */

    /*
        DELETE ALL BOOKS

        getContentResolver().delete(
            Uri.parse("content://com.davidllorca.provider.Books/books,
            null,
            null);
     */

}
