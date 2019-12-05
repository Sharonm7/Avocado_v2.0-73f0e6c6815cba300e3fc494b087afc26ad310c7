package com.example.avocado1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventAttendee;
import com.example.avocado1.R;
import com.example.avocado1.CalendarActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import  com.example.avocado1.Movie;
import com.google.gson.Gson;

import static android.content.Intent.getIntent;


public class EventDialog extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private TimePicker startTime;
    private DatePicker startDate;
    private TimePicker endTime;
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    private Button createEvent;
    private Button cancelEvent;
    private TextView eventTitle;
    private TextView eventDes;
    private TextView eventLocation;
    private EditText eventAttendee;
    private EventAttendee eventAttendeeEmail[];
    private String eventDate;
    private String movieTitle;
    private String userEmail;
    public EventDialog ev;

    public EventDialog() {
    }

    @SuppressLint("ValidFragment")
    public EventDialog(String eventDate, String movieTitle, String userEmail) {

        this.eventDate = eventDate;
        this.movieTitle = movieTitle;
        this.userEmail = userEmail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_event_layout, container, false);

        getDate(eventDate);


        eventTitle = (TextView)view.findViewById(R.id.eventTitle);
        eventDes = (TextView)view.findViewById(R.id.eventDes);
        eventAttendee = (EditText)view.findViewById(R.id.eventAttendee);

        //startDate = (DatePicker)view.findViewById(R.id.startDate);

        startTime = (TimePicker) view.findViewById(R.id.startTime);
        endTime = (TimePicker)view.findViewById(R.id.endTime);

        createEvent = (Button)view.findViewById(R.id.createEvent);
        cancelEvent = (Button)view.findViewById(R.id.cancelEvent);

        createEvent.setOnClickListener(this);
        cancelEvent.setOnClickListener(this);


        eventTitle.setText(movieTitle);
        eventDes.setText(eventDate);
        eventAttendee.setText(userEmail);


        return view;
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }


    public Date getDate(String eventDate){
try {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = sdf.parse(eventDate);

    return date;
}
catch (ParseException e) {
    //handle exception
}
return null;
    }

    public int getDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return(calendar.get(Calendar.DAY_OF_MONTH));

    }

    public int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return(calendar.get(Calendar.MONTH));

    }
    public int getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return(calendar.get(Calendar.YEAR));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancelEvent:
                dismiss();
                break;
            case R.id.createEvent:
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                startCalendar.set(Calendar.DAY_OF_MONTH,getDay(getDate(eventDate)));
                startCalendar.set(Calendar.MONTH,getMonth(getDate(eventDate)));
                startCalendar.set(Calendar.YEAR, getYear(getDate(eventDate)));
                startCalendar.set(Calendar.HOUR_OF_DAY, startTime.getCurrentMinute()-18);
                startCalendar.set(Calendar.MINUTE, startTime.getCurrentMinute());
                Date startDate = getDate(eventDate);

                DateTime start = new DateTime(startDate);


                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));

                endCalendar.set(Calendar.HOUR_OF_DAY, endTime.getCurrentMinute());
                endCalendar.set(Calendar.MINUTE, endTime.getCurrentMinute());
                DateTime end = new DateTime(startDate);

                if(!eventAttendee.getText().toString().equalsIgnoreCase("")) {
                    eventAttendeeEmail = new EventAttendee[3];
                    String email[] = eventAttendee.getText().toString().trim().split(",");
                    int i = 0;
                    for (String s : email) {
                        EventAttendee eventAttendee = new EventAttendee();
                        eventAttendee.setEmail(s);
                        eventAttendeeEmail[i] = eventAttendee;
                    }
                }

                StringBuffer buffer = new StringBuffer(eventTitle.getText().toString()+"\n");
                buffer.append("\n");
                buffer.append(eventDes.getText().toString());
                ((CalendarActivity)getActivity()).createEventAsync(eventTitle.getText().toString(), buffer.toString(), start, end, eventAttendeeEmail );
                dismiss();
                break;
        }
    }

    private String startDateString;
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

//        switch (datePicker.getId()){
//            case R.id.startDate:
//                startDateString  = i+" , "+i1+" , "+i2;
//                System.out.println(startDateString);
//                Log.d("start Date string", startDateString);
//                break;
//
//
//        }
    }

    private String startTimeString, endTimeString;
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        switch (timePicker.getId()){
            case R.id.startTime:
                startTimeString = i+", "+i1;
                System.out.println(startTimeString);
                break;
            case R.id.endTime:
                endTimeString = i+", "+i1;
                System.out.println(startTimeString);
                break;
        }
    }
}
