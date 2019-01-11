package in.technogenie.hamlet.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.adapter.MembersTabsAdapter;
import in.technogenie.hamlet.beans.CustomerVO;
import in.technogenie.hamlet.parser.JSONParser;
import in.technogenie.hamlet.utils.InternetConnection;
import in.technogenie.hamlet.utils.Utility;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberMasterFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    List<CustomerVO> customers = null;
    List categoryList = null;
    List<String> businessCategories = null;
    ProgressBar progressBar;
    private final static String TAG = MemberMasterFragment.class.getSimpleName();

    public MemberMasterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_master, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        tabLayout.addTab(tabLayout.newTab().setText("Business"));
        // tabLayout.addTab(tabLayout.newTab().setText("Chapter"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Downloader().execute(" ");
        } else {
            //Snackbar.make(this, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available.",
                    Toast.LENGTH_LONG).show();
        }

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);


        return view;
    }

    class Downloader extends AsyncTask<String, Void, String> {
        String myString = null;
        //MembersFragment membersFragment;

        Downloader() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected String doInBackground(String... arg0) {
            customers = new ArrayList<CustomerVO>();
            try {


                JSONArray array = JSONParser.getMemberDataArrayFromWeb();

                int lenArray = array.length();
                if (lenArray > 0) {
                    for (int jIndex = 0; jIndex < lenArray; jIndex++) {


                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        CustomerVO customerVO = new CustomerVO();
                        JSONObject innerObject = array.getJSONObject(jIndex);
                        if (innerObject.getString("Membership_ID") != null) {
                            customerVO.setJciID(innerObject.getString("Membership_ID"));
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
                            customerVO.setOccupation(innerObject.getString("Organization_Name"));
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
                }


                /**
                 * Sort the Customer List in ascending order
                 */
                Collections.sort(customers, new Comparator<CustomerVO>() {
                    @Override
                    public int compare(CustomerVO lhs, CustomerVO rhs) {
                        return (lhs.getName().trim()).compareTo(rhs.getName().trim());
                    }
                });


                myString = (new Gson()).toJson(customers);


            } catch (JSONException e) {
                Log.e("MembersFragment", "JSON Exception :" + e);
            }
            return myString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            MembersTabsAdapter tabsAdapter = new MembersTabsAdapter(getActivity().getSupportFragmentManager(),
                    tabLayout.getTabCount(), customers, getCategoryList(customers));
            viewPager.setAdapter(tabsAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Log.d(TAG, "Tab Selected :" + tab.getPosition() + ":" + tab.getText());
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            progressBar.setVisibility(View.GONE);
        }
    }

    private List getCategoryList(List<CustomerVO> customers) {

        List categoryList = new ArrayList();
        Set set = new HashSet();
        HashMap map = new HashMap();

        if (customers != null && customers.size() > 0) {

            for (CustomerVO customer : customers) {
                String category = customer.getCategory();
                category = Utility.toTitleCase(category);
                set.add(category);
            }
            Iterator itr = set.iterator();

            while (itr.hasNext()) {
                categoryList.add(itr.next());
            }

            Collections.sort(categoryList);

            Log.d(TAG, "Customer Categories :" + map.keySet());
        }

        return categoryList;
    }


}
