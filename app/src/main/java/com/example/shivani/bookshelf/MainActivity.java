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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {
    static URL url;
    BookAdapter mAdapter;
    EditText mkeyword;
    ListView bookListView;
    TextView prob;
    View prog;
    String kw;
    private String QUERY_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    int cn=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);
        prog = findViewById(R.id.progress);
        prog.setVisibility(View.GONE);
        prob = (TextView) findViewById(R.id.empty);
        prob.setVisibility(View.GONE);
        mkeyword = (EditText) findViewById(R.id.keyword);

    }

    public void makeUrl(View view) {

        kw = mkeyword.getText().toString();
        kw = QUERY_URL + kw;
        if (kw == null) {
            Log.e("makeUrl", "Empty Query!");
            return;
        }
        try {
            url = new URL(kw);
            Log.v("makeUrl", "url created");
            ConnectivityManager conmng = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conmng.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {

                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(cn++, null, this);
            } else {


                prob.setText("No Internet Connection");
                prob.setVisibility(View.VISIBLE);

            }
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            prog.setVisibility(View.VISIBLE);
        } catch (MalformedURLException e) {
            Log.e("makeUrl", "Malformed URL", e);
        }
    }

    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        return (new BookLoader(this, kw));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        mAdapter.clear();


        prog.setVisibility(View.GONE);
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);

        }
        loader.reset();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        mAdapter.clear();
    }

    private static class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

        private String murl;

        public BookLoader(Context context, String url) {
            super(context);
            murl = url;
            onStartLoading();
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public ArrayList<Book> loadInBackground() {
            ArrayList<Book> answer = null;
            if (murl == null) {
                return null;
            }
            try {
                answer = QueryUtils.fetchBookData(new URL(murl));

            } catch (MalformedURLException e) {
                Log.e("loadInBackground", "malformed URL", e);
            }
            return answer;
        }
    }

}
