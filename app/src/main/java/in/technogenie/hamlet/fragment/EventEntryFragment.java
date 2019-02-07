package in.technogenie.hamlet.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import in.technogenie.hamlet.MainActivity;
import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.Event;
import in.technogenie.hamlet.utils.CommunicationsUtils;
import in.technogenie.hamlet.utils.Constants;

import static android.app.Activity.RESULT_OK;

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
    String selectedLocation=null;

    TextView eventName, description,eventDate, startTime, endTime, contactName, userPhone;
    PlaceAutocompleteFragment location;
    ImageButton eventBanner;
    Button submit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private OnFragmentInteractionListener mListener;

    private final int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;
    private String uriPath;

    //Firebase
    StorageReference storageReference;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;

    //Google Places SDK Implementation
    //GeoDataClient geoDataClient;
   // PlaceDetectionClient placeDetectionClient;
    final int PLACE_PICKER_REQUEST = 1;
    final int AUTOCOMPLETE_REQUEST = 2;


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
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DB_EVENT_PATH);
        storageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_EVENT_PATH);

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

        eventBanner = view.findViewById(R.id.event_banner);
        eventName = view.findViewById(R.id.event_name);
        description = view.findViewById(R.id.description);
        location = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.location);
        contactName = view.findViewById(R.id.contact_name);
        eventDate = view.findViewById(R.id.event_date);
        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        //TextView status;
        //eventBanner = (ImageView) view.findViewById(R.id.event_banner);
        userPhone = view.findViewById(R.id.user_phone);

        submit = view.findViewById(R.id.submit);
       // geoDataClient = Places.getGeoDataClient(this, null);
       // placeDetectionClient = Places.getPlaceDetectionClient(this, null);

        eventBanner.setOnClickListener(this);

        submit.setOnClickListener(this);
        //eventDate.setOnClickListener(this);
        eventDate.setOnFocusChangeListener(this);
        //startTime.setOnClickListener(this);
        startTime.setOnFocusChangeListener(this);
        //endTime.setOnClickListener(this);
        endTime.setOnFocusChangeListener(this);

        //Set Filter to locations in india only
        /*AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        location.setFilter(filter);*/

        location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Place Selected :"+ place.getName().toString());
                selectedLocation =place.getName().toString();

            }
            @Override
            public void onError(Status status) {
                Log.w(TAG, "Error in Place Selection :"+ status.toString());
                selectedLocation = status.toString();

            }
        });



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

        if (v == eventBanner) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        } else if (v == submit) {

            //Event upload into Firebase Storage & Database
            uploadEvent(getContext());
            return;
        } else if (v == eventDate) {
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
        } else if (v == startTime) {
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
        } else if (v == endTime) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                eventBanner.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadEvent(final Context context) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        if(filePath != null)
        {
            final StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uriPath = uri.toString();
                                    Log.d(TAG, "onSuccess Download URL :" + uriPath);

                                    Event eventVO = new Event();
                                    eventVO.setEventName(eventName.getText().toString());
                                    eventVO.setLocation(selectedLocation );
                                    eventVO.setDescription(description.getText().toString());
                                    eventVO.setContactName(contactName.getText().toString());
                                    eventVO.setImageURL(uriPath);
                                    try {
                                        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
                                        String strEventDate = eventDate.getText().toString();
                                        Date dtEventDate = sfd.parse(strEventDate);

                                        eventVO.setEventDate(dtEventDate);

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
                                    final String key = mDatabase.push().getKey();
                                    eventVO.setEventId(key);

                                    Log.d(TAG, "Event Data :" + eventVO.toString());

                                    mDatabase.child(key).setValue(eventVO);
                                    progressDialog.dismiss();

                                    ((MainActivity) getActivity()).replaceFragment(new EventsFragment());

                                }
                            });
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
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
