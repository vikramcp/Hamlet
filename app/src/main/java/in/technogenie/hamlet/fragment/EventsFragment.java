package in.technogenie.hamlet.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.adapter.EventViewAdapter;
import in.technogenie.hamlet.beans.Event;
import in.technogenie.hamlet.utils.InternetConnection;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment implements SearchView.OnQueryTextListener {

    List<Event> events = null;
    ListView listView;
    ProgressBar progressBar;
    private FloatingActionButton newEvents;
    String searchText;

    private DatabaseReference mDatabase;
    //private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;

/*
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
*/

    private OnFragmentInteractionListener mListener;



    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
/*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        listView = (ListView) view.findViewById(R.id.listEventsView);
        //listView.setTextFilterEnabled(true);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        newEvents = (FloatingActionButton) view.findViewById(R.id.newEvents);

        // Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference("event");

        events = new ArrayList<Event>();


        //Listner for New Events Floating Action Button
        newEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                EventEntryFragment fragment3 = new EventEntryFragment();

                Log.d("EventsFragment", "Event Object Selected :");

                ft.replace(R.id.frame, fragment3);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            // new Downloader(this).execute(" ");

        } else {
            //Snackbar.make(this, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available.",
                    Toast.LENGTH_LONG).show();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                EventDetailsFragment fragment2 = new EventDetailsFragment();

                Bundle bundle = new Bundle();
                Event obj = events.get(position);
                Log.d("EventsFragment", "Event Object Selected :" + obj.toString());

                bundle.putSerializable("event", obj);
                //bundle.putParcelable("customer", obj);
                fragment2.setArguments(bundle);

                ft.replace(R.id.frame, fragment2);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("EventsFragment", "Entering onStart() :");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                events.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Event event = postSnapshot.getValue(Event.class);
                    //adding artist to the list
                    events.add(event);
                }
                //creating adapter
                EventViewAdapter eventAdapter = new EventViewAdapter(getContext(), events, "Events");
                //attaching adapter to the listview
                listView.setAdapter(eventAdapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("EventsFragment", "Exiting onStart() :");
    }

    @Override
    public void onStop() {
        super.onStop();
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
        void onFragmentInteraction(Uri uri);
    }


   /* class Downloader extends AsyncTask<String, Void, String> {
        String myString = null;
        EventsFragment eventsFragment;

        Downloader(EventsFragment eventsFragment) {
            this.eventsFragment = eventsFragment;
        }

        //ProgressDialog dialog;
        int jIndex;
        int x;

*//*        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            *//**//**
     * Progress Dialog for User Interaction
     *//**//*
            x=customers.size();

            if(x==0)
                jIndex=0;
            else
                jIndex=x;

            dialog = new ProgressDialog(MembersFragment.this);
            dialog.setTitle("Hey Wait Please..."+x);
            dialog.setMessage("Getting User List..");
            dialog.show();
        }*//*

        @SuppressWarnings("unchecked")
        @Override
        protected String doInBackground(String... arg0) {
            events = new ArrayList<Event>();
            try {

                *//**
     * Getting JSON Object from Web Using okHttp
     *//*
                // JSONObject jsonObject = JSONParser.getDataFromWeb();

                // Log.d("MembersFragment", "JSON Object :"+ jsonObject);


                //JSONArray array = jsonObject.getJSONArray(Keys.KEY_CONTACTS);
                JSONArray array = JSONParser.getEventsDataArrayFromWeb();

                *//**
     * Check Length of Array...
     *//*

                //Log.d("MembersFragment", "JSON Array :"+ array);
                int lenArray = array.length();
                if(lenArray > 0) {
                    for (; jIndex < lenArray; jIndex++) {


                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        //SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

                        Event eventVO = new Event();
                        JSONObject innerObject = array.getJSONObject(jIndex);
                        if (innerObject.getString("Event_ID") != null) {
                            eventVO.setEventId(innerObject.getString("Event_ID"));
                        }
                        if (innerObject.getString("Event_Name") != null) {
                            eventVO.setEventName(innerObject.getString("Event_Name"));
                        }
                        if (innerObject.getString("Description") != null) {
                            eventVO.setDescription(innerObject.getString("Description"));
                        }
                        if (innerObject.getString("Location") != null) {
                            eventVO.setLocation(innerObject.getString("Location"));
                        }

                        String dateString = innerObject.getString("Event_Date");
                        Date date = null;
                        Date time = null;
                        try {
                            if (dateString != null) {

                                date = dateFormat.parse(dateString);
                                eventVO.setEventDate(date);
                            }
                        } catch (ParseException pe) {
                            Log.e("EventsFragment", "Event Date Parse Exception :" + pe);
                        }

                        String stString = innerObject.getString("Start_Time");
                        try {
                            if (stString != null) {
                                time = (dateFormat.parse(stString));
                                Log.d("EventsFragment", "Start Time :" + stString);
                                eventVO.setStartTime(time);
                            }
                        } catch (ParseException pe) {
                            Log.e("EventsFragment", "Start Time Parse Exception :" + pe);
                        }

                        String edString = innerObject.getString("End_Time");
                        try {
                            if (edString != null) {
                                time = dateFormat.parse(edString);
                                Log.d("EventsFragment", "End Time :" + time);
                                eventVO.setEndTime(time);
                            }
                        } catch (ParseException pe) {
                            Log.e("EventsFragment", "End Time Parse Exception :" + pe);
                        }

                        events.add(eventVO);
                    }
                }


                *//**
     * Sort the Customer List in ascending order
     *//*
                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event lhs, Event rhs) {
                        return (lhs.getEventName().trim()).compareTo(rhs.getEventName().trim());
                    }
                });

                //Log.d("MemberActivity", "Member List: " + customers);
                myString = (new Gson()).toJson(events);


            } catch (JSONException e) {
                Log.e("MembersFragment", "JSON Exception :" + e);
            }
            return myString;
        }

*//*        @SuppressWarnings("unchecked")
        @Override
        protected String doInBackground(String... arg0) {
            try {
                Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy")
                        .create();

                InputStream is = MembersFragment.getResources().openRawResource(
                        R.raw.biz_member_list);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(is));
                String gsonStr;
                StringBuilder text = new StringBuilder();
                while ((gsonStr = in.readLine()) != null) {
                    text.append(gsonStr);
                }
                Type type = new TypeToken<List<CustomerVO>>() {
                }.getType();
                customers = (List<CustomerVO>) gson.fromJson(text.toString(),
                        type);

                *//**//**
     * Sort the Customer List in ascending order
     *//**//*
                Collections.sort(customers, new Comparator<CustomerVO>() {
                    @Override
                    public int compare(CustomerVO lhs, CustomerVO rhs) {
                        return (lhs.getName().trim()).compareTo(rhs.getName().trim());
                    }
                });

                *//**//**
     * Search Functionality Implementation
     *//**//*
                if (! TextUtils.isEmpty(searchText)) {
                    customers = getCustomerFilterList(customers);
                }

                //Log.d("MemberActivity", "Member List: " + customers);
                myString = (new Gson()).toJson(customers);
                in.close();

            } catch (IOException e) {
                Log.e("MemberActivity", "IO Exception :" + e);
            }
            return myString;
        }*//*



        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            //dialog.dismiss();

            //Log.d("MemberActivity", "result :" + result);
            // Log.d("MemberActivity", "customers :" + customers);
            //listView.setAdapter(new EventViewAdapter(getContext(), events,"EventsFragment"));
			*//*listView.setAdapter(new ArrayAdapter< String >
			(getApplicationContext(),android.R.layout.simple_list_item_1,customers));*//*


            progressBar.setVisibility(View.GONE);
        }
    }*/

}
