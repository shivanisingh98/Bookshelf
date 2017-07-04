package com.example.shivani.bookshelf;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class BookAdapter extends ArrayAdapter<Book>{
    public BookAdapter(Activity context, ArrayList<Book> books)
    {
        super(context,0,books);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(

                    R.layout.list_item, parent, false);

        }
        Book currentBook=getItem(position);
        TextView mtitle=(TextView) listItemView.findViewById(R.id.title);
        String t=currentBook.getTitle();
        mtitle.setText(t);
        TextView msubtitle=(TextView) listItemView.findViewById(R.id.subtitle);
        String authors[]=currentBook.getSubtitle();
        String authorsToDisplay="";
        for(int i=0;i<authors.length;i++)
        {
            if(i==0)
            {
                authorsToDisplay+=authors[i];
            }
            else
            {
                authorsToDisplay+=" ,"+authors[i];
            }
        }
        Log.v("authors",authorsToDisplay);
        msubtitle.setText(authorsToDisplay);
        TextView thumbnail=(TextView) listItemView.findViewById(R.id.circle);
        thumbnail.setText(Character.toUpperCase(t.charAt(0))+"");
        msubtitle.setText(authorsToDisplay);
                return listItemView;
    }
}
