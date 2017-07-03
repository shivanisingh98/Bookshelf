package com.example.shivani.bookshelf;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {
    URL url;
    BookAdapter mAdapter;
    TextView prob = (TextView) findViewById(R.id.empty);
    View prog = findViewById(R.id.progress);

    private String QUERY_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView bookListView = (ListView) findViewById(R.id.list);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);
        ConnectivityManager conmng = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conmng.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else {
            prog.setVisibility(View.GONE);
            prob.setText("No Internet Connection");
        }
    }

    public void makeUrl(View view) {
        EditText mkeyword = (EditText) findViewById(R.id.keyword);
        String kw = mkeyword.getText().toString();
        QUERY_URL += kw;
        if (kw == null) {
            Log.e("makeUrl", "Empty Query!");
            return;
        }
        try {
            url = new URL(QUERY_URL + kw);
        } catch (MalformedURLException e) {
            Log.e("makeUrl", "Malformed URL", e);
        }
    }

    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        return (new BookLoader(this, QUERY_URL));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        mAdapter.clear();
        prog.setVisibility(View.GONE);
        prob.setText("No Data to Display");
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        mAdapter.clear();
    }

    private class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

        private String murl;
        public BookLoader(Context context, String url)
        {
            super(context);
            murl=url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public ArrayList<Book> loadInBackground() {
            if(murl==null) {
                return null;
            }
            ArrayList<Book> answer =QueryUtils.fetchBookData(url);
            return answer;
        }
    }

}
