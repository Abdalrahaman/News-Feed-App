package com.example.pro_abdo.news_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<New> {


    public NewsAdapter(Context context, List<New> news) {
        super(context, 0, news);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.new_list_item, parent, false);
        }

        New currentNew = getItem(position);


        TextView sectionName = (TextView) listItemView.findViewById(R.id.section_name);
        sectionName.setText(currentNew.getmSectionName().toString());

        //=========================================================

        TextView newTitle = (TextView) listItemView.findViewById(R.id.new_title);
        newTitle.setText(currentNew.getmTitle().toString());

        //=========================================================

        TextView autherName = (TextView) listItemView.findViewById(R.id.author_name);
        autherName.setText(isAuthorAvaliable(currentNew.getmAuthorName()));

        //=========================================================

        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.US);

        try {

            Date dateObject = jsonFormatter.parse(currentNew.getmDate().toString());

            // Find the TextView with view ID date
            TextView dateView = (TextView) listItemView.findViewById(R.id.date);
            // Format the date string (i.e. "Mar 3, 1984")
            String formattedDate = formatDate(dateObject);
            // Display the date of the current new in that TextView
            dateView.setText(formattedDate);

            // Find the TextView with view ID time
            TextView timeView = (TextView) listItemView.findViewById(R.id.time);
            // Format the time string (i.e. "4:30PM")
            String formattedTime = formatTime(dateObject);
            // Display the time of the current new in that TextView
            timeView.setText(formattedTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //=========================================================

        return listItemView;
    }


    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }


    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }


    private String isAuthorAvaliable(String[] author) {

        String author_Name = "";

        if (author != null) {

            for (int i = 0; i < author.length; i++) {

                // check if found more author name to put "," and finished not put anything
                if(i != author.length-1)
                    author_Name += " " + author[i] + " ,";
                else
                    author_Name += " " + author[i] ;

            }
        } else
            author_Name = "   No author";

        return author_Name;
    }

}
