package in.technogenie.hamlet.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.CustomerVO;
import in.technogenie.hamlet.utils.InternetConnection;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MembersBusinessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MembersBusinessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MembersBusinessFragment extends Fragment implements SearchView.OnQueryTextListener {

    private final static String TAG = MembersBusinessFragment.class.getSimpleName();
    List<CustomerVO> customers = null;
    Map customerBusinessMap = null;
    List categoryList = null;
    ListView listView;
    ProgressBar progressBar;
    String searchText;


    private OnFragmentInteractionListener mListener;

    public MembersBusinessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param customers
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MembersBusinessFragment newInstance(List<CustomerVO> customers, List categoryList) {
        MembersBusinessFragment fragment = new MembersBusinessFragment();
        Bundle args = new Bundle();
        args.putSerializable("customers", (ArrayList) customers);
        args.putSerializable("categoryList", (ArrayList) categoryList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // listen to backstack changes
        /*getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);
        ((MainActivity)getActivity()).hideUpButton();*/

        customers = (ArrayList<CustomerVO>) getArguments().getSerializable("customers");
        categoryList = (ArrayList<CustomerVO>) getArguments().getSerializable("categoryList");

        Log.d(TAG, "Customers Size :" + customers.size());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_members_business, container, false);
        listView = (ListView) view.findViewById(R.id.listMemberView);
        listView.setTextFilterEnabled(true);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

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

                String category = categoryList.get(position).toString();
                ArrayList filterCustomers = new ArrayList();

                //Filter customers based on category selected
                for (CustomerVO customerVO : customers) {
                    if (customerVO.getCategory().trim().equalsIgnoreCase(category.trim())) {
                        filterCustomers.add(customerVO);
                    }
                }

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                MembersFragment fragment2 = MembersFragment.newInstance(filterCustomers);
                Log.d(TAG, "Filtered Customer List :" + filterCustomers);

                ft.replace(R.id.frame, fragment2);
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
        if(getActivity().getSupportFragmentManager().getBackStackEntryCount() < 1) {
            ((MainActivity)getActivity()).hideUpButton();
        }
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

    private Map groupCustomersbyCategory(List<CustomerVO> customers) {
        HashMap map = new HashMap();

        if (customers != null && customers.size() > 0) {

            for (CustomerVO customer : customers) {
                String category = customer.getCategory();
                List subList = null;
                if (map.get(category) != null) {
                    subList = (ArrayList<CustomerVO>) map.get(category);
                } else {
                    subList = new ArrayList();
                }

                subList.add(customer);
                map.put(category, subList);
            }
            Log.d(TAG, "Customer Categories :" + map.keySet());
        }
        return map;
    }


    class MembersBusinessAdapter extends BaseAdapter {

        private Context mContext;
        List<CustomerVO> customerList;
        Map customerBusinessMap;

        public MembersBusinessAdapter(Context c, Map customerBusinessMap) {
            mContext = c;
            this.customerBusinessMap = customerBusinessMap;
        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            int count = 0;
            if (customerBusinessMap != null) {
                count = customerBusinessMap.size();
            }
            return count;
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return null;
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                grid = new View(mContext);
            } else {
                grid = (View) convertView;
            }

            grid = inflater.inflate(R.layout.member_biz_grid_single, null);

            String name = null;
            if (categoryList != null && categoryList.size() > 0) {
                //Set buzSet = customerBusinessMap.keySet();
                name = categoryList.get(position).toString();
                //Log.d(TAG, "Categories :"+ name);
            }
            TextView bizCategory = (TextView) grid.findViewById(R.id.business_category);
            if (name != null)
                bizCategory.setText(name);

            return grid;
        }
    }

    class Downloader extends AsyncTask<String, Void, String> {

        MembersBusinessFragment membersBizFragment;

        Downloader(MembersBusinessFragment membersBizFragment) {
            this.membersBizFragment = membersBizFragment;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected String doInBackground(String... arg0) {

            Log.d(TAG, "Inside Asynk Task. Customer Size :" + customers.size());

            customerBusinessMap = groupCustomersbyCategory(customers);

            Log.d(TAG, "Inside Asynk Task. Customer Map Size :" + customerBusinessMap.size());

            return "success";
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            Log.d(TAG, "result :" + result);

            listView.setAdapter(new MembersBusinessAdapter(getContext(), customerBusinessMap));

            progressBar.setVisibility(View.GONE);
        }
    }
}