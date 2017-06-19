package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText keywordText = (EditText) findViewById(R.id.keywords);
        Button searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = keywordText.getText().toString();
                launchSearch();
            }
        });

    }

    public void launchSearch(){
        //Check Internet access
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            if (checkQuery()) {
                BookListingAsyncTask asyncTask = new BookListingAsyncTask();
                asyncTask.execute();
                updateDialog(getResources().getString(R.string.searching));
            }
        } else {updateDialog(getResources().getString(R.string.no_connection));}
    }

    public Boolean checkQuery(){
        if(query.isEmpty()){
            updateDialog(getResources().getString(R.string.empty_query));
            return false;
        }
        if(Pattern.matches(".*\\p{P}.*",query)){
            updateDialog(getResources().getString(R.string.query_has_punct));
            return false;
        }
        return true;
    }

    public void updateDialog(String dialogText){
        TextView dialogView = (TextView) findViewById(R.id.dialog);
        dialogView.setText(dialogText);
    }

    public void updateUI(ArrayList<Book> books){
        BookAdapter adapter = new BookAdapter(this,books);
        ListView listView = (ListView) findViewById(R.id.results_list);
        listView.setAdapter(adapter);
    }

    public String MakeUrlString(String q){
        String noSpace = q.toLowerCase().replace(" ","+");
        return "https://www.googleapis.com/books/v1/volumes?q=" + noSpace + "&maxResults=20";
    }

    private class BookListingAsyncTask extends AsyncTask<String,Void,ArrayList<Book>>{

        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            return Utils.fetchListingData(MakeUrlString(query));
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            super.onPostExecute(books);
            if(books.isEmpty()){updateDialog(getResources().getString(R.string.no_results));}
            else{
                updateUI(books);
                updateDialog("");
            }
        }
    }
}
