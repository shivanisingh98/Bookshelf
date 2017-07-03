package com.example.shivani.bookshelf;

import android.app.Activity;
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
        mtitle.setText(currentBook.getTitle());
        TextView msubtitle=(TextView) listItemView.findViewById(R.id.subtitle);
        msubtitle.setText(currentBook.getSubtitle());
                return listItemView;
    }
}
