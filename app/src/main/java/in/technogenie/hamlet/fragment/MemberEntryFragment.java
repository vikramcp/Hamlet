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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.technogenie.hamlet.MainActivity;
import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.CustomerVO;
import in.technogenie.hamlet.beans.Event;
import in.technogenie.hamlet.utils.CommunicationsUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MemberEntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemberEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberEntryFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, AdapterView.OnItemSelectedListener {


    public final static String MEMBER_ENTRY = "memberEntry";
    private final static String TAG = "MemberEntryFragment";
    CustomerVO customerVO;

    TextView memberName, mobileName, emailID, residenceAddress, dateOfBirth, marriageAnniversary,
            bloodGroup, membershipID, lomName, lomDesignation, products, orgName, orgDesignation, orgAddress, website;
    Spinner category;
    String strCategory;
    //ImageView eventBanner;
    Button submit;
    private OnFragmentInteractionListener mListener;
    private int mYear, mMonth, mDay;
    DatabaseReference mDatabase;


    public MemberEntryFragment() {
        // Required empty public constructor
    }

    /**
     * newInstance Method
     *
     * @param customerVO
     * @return
     */
    public static MemberEntryFragment newInstance(CustomerVO customerVO) {
        MemberEntryFragment fragment = new MemberEntryFragment();
        Bundle args = new Bundle();
        args.putSerializable(MEMBER_ENTRY, customerVO);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //Getting Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            customerVO = (CustomerVO) getArguments().getSerializable(MEMBER_ENTRY);
            Log.d(TAG, customerVO.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_entry, container, false);

        memberName = view.findViewById(R.id.member_name);
        mobileName = view.findViewById(R.id.mobile_number);
        emailID= view.findViewById(R.id.email_id);
        residenceAddress= view.findViewById(R.id.residence_address);
        dateOfBirth= view.findViewById(R.id.date_of_birth);
        marriageAnniversary= view.findViewById(R.id.marriage_anniversary);
        bloodGroup= view.findViewById(R.id.blood_group);
        membershipID= view.findViewById(R.id.membership_id);
        lomName= view.findViewById(R.id.lom_name);
        lomDesignation= view.findViewById(R.id.lom_designation);
        category= view.findViewById(R.id.business_category);
        products= view.findViewById(R.id.products);
        orgName= view.findViewById(R.id.org_name);
        orgDesignation= view.findViewById(R.id.org_designation);
        orgAddress= view.findViewById(R.id.org_address);
        website= view.findViewById(R.id.website);

        submit = view.findViewById(R.id.submit);

        submit.setOnClickListener(this);

        dateOfBirth.setOnFocusChangeListener(this);
        marriageAnniversary.setOnFocusChangeListener(this);
        category.setOnItemSelectedListener(this);

        ArrayAdapter<String> myAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.business_categories));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        category.setAdapter(myAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

            CustomerVO customerVO = new CustomerVO();
            customerVO.setAddress(residenceAddress.getText().toString());
            customerVO.setBloodGroup(bloodGroup.getText().toString());
            customerVO.setCategory(strCategory);
            customerVO.setCompanyDesignation(orgDesignation.getText().toString());
            customerVO.setCompanyName(orgName.getText().toString());
            customerVO.setCurrentRole(lomDesignation.getText().toString());
            customerVO.setEmailID(emailID.getText().toString());
            customerVO.setLomName(lomName.getText().toString());
            customerVO.setMembershipID(membershipID.getText().toString());
            customerVO.setMobile(mobileName.getText().toString());
            customerVO.setName(memberName.getText().toString());
            customerVO.setOfficeAddress(orgAddress.getText().toString());
            customerVO.setProducts(products.getText().toString());

            try {
                SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
                customerVO.setAnniversary(sfd.parse(marriageAnniversary.getText().toString()));
                customerVO.setDateOfBirth(sfd.parse(dateOfBirth.getText().toString()));

            } catch (ParseException pe) {
                Log.e(TAG, "ParseException:" + pe);
            }

            /**
             * Setting event value into database
             */
            final String key = FirebaseDatabase.getInstance().getReference().child("member").push().getKey();
            customerVO.setCustomerId(key);

            Log.d(TAG, "Member Data :" + customerVO.toString());
            mDatabase.child("member").child(key).setValue(customerVO);
            Snackbar.make(v, "Member successfully submitted.", 2000);

            ((MainActivity) getActivity()).replaceFragment(new MemberMasterFragment());
            return;
        }

        if (v == marriageAnniversary) {
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
                            marriageAnniversary.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }


        if (v == dateOfBirth) {
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
                            dateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        Log.d(TAG, "Exiting implementActions Method.");
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        strCategory = String.valueOf(parent.getItemAtPosition(position));
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
