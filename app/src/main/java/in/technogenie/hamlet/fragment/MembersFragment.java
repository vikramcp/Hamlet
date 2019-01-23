package in.technogenie.hamlet.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.adapter.ImageViewAdapter;
import in.technogenie.hamlet.beans.CustomerVO;
import in.technogenie.hamlet.utils.InternetConnection;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MembersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MembersFragment extends Fragment implements SearchView.OnQueryTextListener {

    List<CustomerVO> customers =  null;
    ListView listView;
    ProgressBar progressBar;
    private FloatingActionButton newMembers;
    String searchText;
    private final static String TAG = MembersFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public MembersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MembersFragment newInstance(List<CustomerVO> customers) {
        MembersFragment fragment = new MembersFragment();
        Bundle args = new Bundle();
        args.putSerializable("customers", (ArrayList) customers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*// listen to backstack changes
        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);
        ((MainActivity)getActivity()).hideUpButton();*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_members, container, false);
        listView = (ListView) view.findViewById(R.id.listMemberView);
        listView.setTextFilterEnabled(true);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        newMembers = (FloatingActionButton) view.findViewById(R.id.newMembers);

        // hide the UP button
        //((MainActivity) getActivity()).hideUpButton();


        customers = (ArrayList<CustomerVO>) getArguments().getSerializable("customers");

        Log.d(TAG, "Customers Size :" + customers.size());

        //Listner for New Members Floating Action Button
        newMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                MemberEntryFragment fragment3 = new MemberEntryFragment();

                Log.d(TAG, "Opening new Member Registration Form :");

                ft.add(R.id.frame, fragment3);
                ft.addToBackStack(null);
                ft.commit();
            }
        });



        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Downloader(this).execute(" ");
        } else {
            //Snackbar.make(this, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available.",
                    Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                 /*Toast.makeText(getContext(), "" + position + ":User :"+ customers.get(position).getName(),
                         Toast.LENGTH_SHORT).show();
*/

                Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy")
                        .create();

                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                MemberDetailsFragment fragment2 = new MemberDetailsFragment();

                Bundle bundle = new Bundle();
                CustomerVO obj = customers.get(position);
                Log.d("MembersFragment","Customer Object Selected :"+ obj.toString() );

                bundle.putSerializable("customer", obj);
                //bundle.putParcelable("customer", obj);
                fragment2.setArguments(bundle);

                ft.add(R.id.frame, fragment2);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override
    public void onBackStackChanged() {
        // enable Up button only  if there are entries on the backstack
        *//*if(getActivity().getSupportFragmentManager().getBackStackEntryCount() < 1) {
            ((MainActivity)getActivity()).hideUpButton();
        }*//*
    }*/

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


class Downloader extends AsyncTask<String, Void, String> {
    String myString = null;
    MembersFragment membersFragment;

    Downloader(MembersFragment membersFragment) {
        this.membersFragment = membersFragment;
    }

    //ProgressDialog dialog;
    int jIndex;
    int x;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

/*        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            *//**
     * Progress Dialog for User Interaction
     *//*
            x=customers.size();

            if(x==0)
                jIndex=0;
            else
                jIndex=x;

            dialog = new ProgressDialog(MembersFragment.this);
            dialog.setTitle("Hey Wait Please..."+x);
            dialog.setMessage("Getting User List..");
            dialog.show();
        }*/

    @SuppressWarnings("unchecked")
    @Override
    protected String doInBackground(String... arg0) {
        //customers = new ArrayList<CustomerVO>();
        try {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            // JSONObject jsonObject = JSONParser.getDataFromWeb();
            // Log.d("MembersFragment", "JSON Object :"+ jsonObject);
            //JSONArray array = jsonObject.getJSONArray(Keys.KEY_CONTACTS);

            // JSONArray array = JSONParser.getMemberDataArrayFromWeb();

            /**
             * Check Length of Array...
             */

            //Log.d("MembersFragment", "JSON Array :"+ array);
           /* int lenArray = array.length();
            if(lenArray > 0) {
                for (; jIndex < lenArray; jIndex++) {


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                    CustomerVO customerVO = new CustomerVO();
                    JSONObject innerObject = array.getJSONObject(jIndex);
                    if (innerObject.getString("Membership_ID") != null) {
                        customerVO.setMembershipID(innerObject.getString("Membership_ID"));
                    }
                    if (innerObject.getString("Full_Name") != null) {
                        customerVO.setName(innerObject.getString("Full_Name"));
                    }
                    if (innerObject.getString("LOM_Name") != null) {
                        customerVO.setLomName(innerObject.getString("LOM_Name"));
                    }
                    if (innerObject.getString("Designation") != null) {
                        customerVO.setCurrentRole(innerObject.getString("Designation"));
                    }
                    if (innerObject.getString("Mobile_Number") != null) {
                        customerVO.setMobile(innerObject.getString("Mobile_Number"));
                    }
                    if (innerObject.getString("Email_ID") != null) {
                        customerVO.setEmailID(innerObject.getString("Email_ID"));
                    }
                    if (innerObject.getString("Residence_Address") != null) {
                        customerVO.setAddress(innerObject.getString("Residence_Address"));
                    }
                    if (innerObject.getString("Organization_Name") != null) {
                        customerVO.setCompanyName(innerObject.getString("Organization_Name"));
                    }
                    if (innerObject.getString("Category") != null) {
                        customerVO.setCategory(innerObject.getString("Category"));
                    }
                    if (innerObject.getString("Products") != null) {
                        customerVO.setProducts(innerObject.getString("Products"));
                    }
                    if (innerObject.getString("Organization_Address") != null) {
                        customerVO.setOfficeAddress(innerObject.getString("Organization_Address"));
                    }
                    if (innerObject.getString("Website") != null) {
                        customerVO.setWebsite(innerObject.getString("Website"));
                    }
                    if (innerObject.getString("Blood_Group") != null) {
                        customerVO.setBloodGroup(innerObject.getString("Blood_Group"));
                    }

                    String dateString = innerObject.getString("Date_of_Birth");
                    Date date = null;
                    try {
                        if (dateString != null) {

                            date = dateFormat.parse(dateString);
                            customerVO.setDateOfBirth(new java.sql.Date(date.getTime()));
                        }
                    } catch (ParseException pe) {
                        Log.e("MemberActivity", "DOB Parse Exception :" + pe);
                    }

                    String dateMAString = innerObject.getString("Marriage_Anniversary");
                    try {
                        if (dateMAString != null) {
                            date = dateFormat.parse(dateMAString);
                            customerVO.setAnniversary(new java.sql.Date(date.getTime()));
                        }
                    } catch (ParseException pe) {
                        Log.e("MemberActivity", "MA Parse Exception :" + pe);
                    }

                    customers.add(customerVO);

                }
            }*/


            Collections.sort(customers, new Comparator<CustomerVO>() {
                @Override
                public int compare(CustomerVO lhs, CustomerVO rhs) {
                    return (lhs.getName().trim()).compareTo(rhs.getName().trim());
                }
            });

            /**
             * Search Functionality Implementation
             */
            if (! TextUtils.isEmpty(searchText)) {
                customers = getCustomerFilterList(customers);
            }

            //Log.d("MemberActivity", "Member List: " + customers);
            myString = (new Gson()).toJson(customers);


        } catch (Exception e) {
            Log.e("MembersFragment", "JSON Exception :" + e);
        }
        return myString;
    }

    private List<CustomerVO> getCustomerFilterList(List<CustomerVO> customers) {

        Log.d("MembersFragment", "Search Text in getCustomerFilterList :" + searchText);
        List<CustomerVO> returnList = new ArrayList<CustomerVO>();

        for (int i=0;i < customers.size(); i++ ) {
            CustomerVO vo = customers.get(i);
            if ((vo.getName() != null && vo.getName().toUpperCase().contains(searchText.toUpperCase())) ||
                    (vo.getCurrentRole()!= null && vo.getCurrentRole().toUpperCase().contains(searchText.toUpperCase())) ||
                    (vo.getCompanyName() != null && vo.getCompanyName().toUpperCase().contains(searchText.toUpperCase())) ||
                    (vo.getCategory()!= null && vo.getCategory().toUpperCase().contains(searchText.toUpperCase())) ||
                    (vo.getProducts()!= null && vo.getProducts().toUpperCase().contains(searchText.toUpperCase())) ||
                    (vo.getLomName()!= null && vo.getLomName().toUpperCase().contains(searchText.toUpperCase())) ||
                    (vo.getBloodGroup()!= null && vo.getBloodGroup().toUpperCase().contains(searchText.toUpperCase()))) {
                Log.d("MembersFragment", "Search Result:" + vo.getName());
                returnList.add(vo);
            }
        }
        return returnList;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        //dialog.dismiss();

        //Log.d("MemberActivity", "result :" + result);
        // Log.d("MemberActivity", "customers :" + customers);
        listView.setAdapter(new ImageViewAdapter(getContext(), customers,"MembersFragment"));
			/*listView.setAdapter(new ArrayAdapter< String >
			(getApplicationContext(),android.R.layout.simple_list_item_1,customers));*/


        progressBar.setVisibility(View.GONE);
    }
}
}