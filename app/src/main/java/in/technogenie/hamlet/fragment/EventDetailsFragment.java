package in.technogenie.hamlet.fragment;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.Event;
import in.technogenie.hamlet.utils.CommunicationsUtils;
import in.technogenie.hamlet.utils.Constants;
import in.technogenie.hamlet.utils.ScreenUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {


    private final static String ARG_PARAM1 = "event";
    Event eventVO;

    TextView eventName;
    TextView description;
    TextView location;
    TextView eventDate;
    TextView startTime;
    TextView endTime;
    TextView status;
    ImageView eventBanner;
    ImageButton userPhone;

    //Firebase
    StorageReference storageReference;
    private DatabaseReference mDatabase;

    private OnFragmentInteractionListener mListener;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * newInstance Method
     *
     * @param eventVO
     * @return
     */
    public static EventDetailsFragment newInstance(Event eventVO) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, eventVO);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {

            eventVO = (Event) getArguments().getSerializable(ARG_PARAM1);
            Log.d("EventDetailsFragment", eventVO.toString());
        }

        //Instantiate Firebase Storage & Database
        storageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_EVENT_PATH);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DB_EVENT_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        eventName = view.findViewById(R.id.event_name);
        description = view.findViewById(R.id.description);
        location = view.findViewById(R.id.location);
        eventDate = view.findViewById(R.id.event_date);
        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        //TextView status;
        eventBanner = view.findViewById(R.id.event_banner);
        userPhone = view.findViewById(R.id.user_phone);


        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

        eventName.setText(eventVO.getEventName());
        description.setText(eventVO.getDescription());
        location.setText(eventVO.getLocation());
        if (eventVO.getEventDate() != null)
            eventDate.setText(dateFormat.format(eventVO.getEventDate()));

        SimpleDateFormat sf = new SimpleDateFormat("h:mm a");
        if (eventVO.getStartTime() != null) {
            startTime.setText(sf.format(eventVO.getStartTime()));
        }
        if (eventVO.getEndTime() != null) {
            endTime.setText(sf.format(eventVO.getEndTime()));
        }

        //Load with Picasso
        Picasso.get()
                .load(eventVO.getImageURL())
                .centerCrop()
                .resize(ScreenUtils.getScreenWidth(getActivity()) / 2, ScreenUtils.getScreenHeight(getActivity()) / 3)//Resize image to width half of screen and height 1/3 of screen height
                .into(eventBanner);

        userPhone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Method to make a phone call to members
                CommunicationsUtils.onCall(getActivity(), eventVO.getPhone());

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
                    Log.d("TAG", "Call Permission Not Granted");
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
