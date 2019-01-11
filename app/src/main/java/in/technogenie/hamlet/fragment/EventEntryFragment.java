package in.technogenie.hamlet.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.technogenie.hamlet.MainActivity;
import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.Event;
import in.technogenie.hamlet.utils.CommunicationsUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventEntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventEntryFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {


    private final static String ARG_PARAM1 = "eventEntry";
    private final static String TAG = "EventEntryFragment";
    Event eventVO;

    TextView eventName, description, location, eventDate, startTime, endTime, contactName, userPhone;
    //ImageView eventBanner;
    Button submit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private OnFragmentInteractionListener mListener;

    DatabaseReference mDatabase;


    public EventEntryFragment() {
        // Required empty public constructor
    }

    /**
     * newInstance Method
     *
     * @param eventVO
     * @return
     */
    public static EventEntryFragment newInstance(Event eventVO) {
        EventEntryFragment fragment = new EventEntryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, eventVO);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //Getting Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            eventVO = (Event) getArguments().getSerializable(ARG_PARAM1);
            Log.d(TAG, eventVO.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_entry, container, false);

        eventName = (TextView) view.findViewById(R.id.event_name);
        description = (TextView) view.findViewById(R.id.description);
        location = (TextView) view.findViewById(R.id.location);
        contactName = (TextView) view.findViewById(R.id.contact_name);
        eventDate = (TextView) view.findViewById(R.id.event_date);
        startTime = (TextView) view.findViewById(R.id.start_time);
        endTime = (TextView) view.findViewById(R.id.end_time);
        //TextView status;
        //eventBanner = (ImageView) view.findViewById(R.id.event_banner);
        userPhone = (TextView) view.findViewById(R.id.user_phone);

        submit = (Button) view.findViewById(R.id.submit);

        submit.setOnClickListener(this);
        //eventDate.setOnClickListener(this);
        eventDate.setOnFocusChangeListener(this);
        //startTime.setOnClickListener(this);
        startTime.setOnFocusChangeListener(this);
        //endTime.setOnClickListener(this);
        endTime.setOnFocusChangeListener(this);

/*        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    CommunicationsUtils.onCall(getActivity(), eventVO.getPhone());
                } else {
                    Snackbar.make(getView(), "Permission Not Granted.", 2000);
                    Log.d(TAG, "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Entering onClick Method..");
        implementActions(v);
        Log.d(TAG, "Exiting onClick Method..");

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "Entering onFocusChange Method.." + v);
        if (hasFocus) {
            implementActions(v);
        }
        Log.d(TAG, "Exiting onFocusChange Method..");

    }


    private void implementActions(View v) {
        Log.d(TAG, "Entering implementActions Method. View=" + v);


        // Action to be performed on click of Submit button
        if (v == submit) {
            Event eventVO = new Event();
            eventVO.setEventName(eventName.getText().toString());
            eventVO.setLocation(location.getText().toString());
            eventVO.setDescription(description.getText().toString());
            eventVO.setContactName(contactName.getText().toString());

            try {
                SimpleDateFormat sfd = new SimpleDateFormat("dd/mm/yyyy");
                eventVO.setEventDate(sfd.parse(eventDate.getText().toString()));

                SimpleDateFormat sf = new SimpleDateFormat("H:mm");
                eventVO.setStartTime(sf.parse(startTime.getText().toString()));
                eventVO.setEndTime(sf.parse(endTime.getText().toString()));
            } catch (ParseException pe) {
                Log.e(TAG, "ParseException:" + pe);
            }
            eventVO.setPhone(userPhone.getText().toString());

            /**
             * Setting event value into database
             */
            final String key = FirebaseDatabase.getInstance().getReference().child("Event").push().getKey();
            eventVO.setEventId(key);

            Log.d(TAG, "Event Data :" + eventVO.toString());

            mDatabase.child("event").child(key).setValue(eventVO);

            Snackbar.make(v, "Event successfully submitted.", 2000);

            ((MainActivity) getActivity()).replaceFragment(new EventsFragment());
            return;
        }

        if (v == eventDate) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            eventDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == startTime) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            //c.get(Calendar.AM_PM);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            startTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if (v == endTime) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            endTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        Log.d(TAG, "Exiting implementActions Method.");
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
