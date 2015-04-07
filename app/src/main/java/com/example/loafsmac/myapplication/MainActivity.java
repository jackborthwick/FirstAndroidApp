package com.example.loafsmac.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.RunnableScheduledFuture;


public  class MainActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    TextView mainTextView;
    Button mainButton;
    EditText mainEditText;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    ShareActionProvider mShareActionProvider;
    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTextView = (TextView) findViewById(R.id.main_text);
        mainTextView.setText("Set in Java!");
        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);
        mainEditText = (EditText) findViewById(R.id.main_edittext);
        mainListView = (ListView) findViewById(R.id.main_listview);
        mArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_expandable_list_item_1,
                mNameList);
        mainListView.setAdapter(mArrayAdapter);
        mainListView.setOnItemClickListener(this);
        displayWelcome();

    }

    private void displayWelcome() {
        //Access the devices key-value storage
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        //read the user's name
        //or empty string is nothing found
        String name = mSharedPreferences.getString(PREF_NAME, "");
        if (name.length() > 0) {
            //if name valid display Toast welcoming them
            Toast.makeText(this,"Welcome back brohan " + name, Toast.LENGTH_LONG).show();
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("hiyathereFREND");
            alert.setMessage("what's ya nombre");

            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String inputName = input.getText().toString();
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.commit();
                    Toast.makeText(getApplicationContext(), "Welcome " + inputName, Toast.LENGTH_LONG).show();

                }
            });
            alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);//access the share item defined in xml\
        if ( shareItem != null) {//access object responsible for putting together sharing submenu
            mShareActionProvider = (ShareActionProvider)shareItem.getActionProvider();
        }
        setShareIntent();
        return true;
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            //create intent with contents of textview
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT,mainTextView.getText());
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onClick(View v) {
        mainTextView.setText(mainEditText.getText().toString()+" is rad!!!");
        mNameList.add((mainEditText.getText().toString()));
        mArrayAdapter.notifyDataSetChanged();
        setShareIntent();
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // Log the item's position and contents
        // to the console in Debug
        Log.d("MyApplication", position + ": " + mNameList.get(position));
        //mNameList.remove(0);
    }

}
