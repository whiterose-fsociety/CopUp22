package com.example.copup2.Post;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.copup2.FirebaseModels.Post;
import com.example.copup2.Models.PostModels.Product;
import com.example.copup2.Models.PostModels.ProductType.Head;
import com.example.copup2.Models.PostModels.ProductType.LowerBody;
import com.example.copup2.Models.PostModels.ProductType.Outfit;
import com.example.copup2.Models.PostModels.ProductType.ProductType;
import com.example.copup2.Models.PostModels.ProductType.Shoes;
import com.example.copup2.Models.PostModels.ProductType.Torso;
import com.example.copup2.R;
import com.example.copup2.Utilities.BottomSheetHelper;
import com.example.copup2.Utilities.StringManipulation;
import com.example.copup2.Utilities.ViewManipulation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.api.LogDescriptor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectDetailsFragment extends Fragment {
    private static final String TAG = SelectDetailsFragment.class.getName();

    private OnBackListener listener;

    public interface OnBackListener {
        public void navigateBack();
    }


    public SelectDetailsFragment() {
        super();
        setArguments(new Bundle());
    }

    @BindView(R.id.bottom_sheet_text)
    TextView Message;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBackListener) {
            listener = (OnBackListener) context;
        } else {
            throw new ClassCastException(context.toString() + " call OnBackListener.navigateBack");
        }
    }

    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.type_spinner)
    Spinner typeSpinner;
    @BindView(R.id.condition_spinner)
    Spinner conditionSpinner;
    @BindView(R.id.transaction_spinner)
    Spinner transactionSpinner;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.size)
    EditText mSize;
    @BindView(R.id.price)
    EditText mPrice;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.post)
    TextView mPost;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_select_details, container, false);
        ButterKnife.bind(this, mView);
        initSpinner();
        init();
        return mView;
    }

    /**
     * #TODO SEND BACK THE PRODUCT TO AVOID USERS FROM STARTING OVER
     */
    @OnClick(R.id.back)
    public void navigateBack() {
        listener.navigateBack();
    }

    private void init() {
        if (getProductFromBundle() != null) {
            Log.d(TAG, "init: CAUGHT PRODUCT");
            Product product = getProductFromBundle();
            Log.d(TAG, "init: PRODUCT " + product.getTitle());
        } else {
            Log.d(TAG, "init: CANNOT FIND PRODUCT");

        }
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(0);
        radioButton.setChecked(true);
        Post.setBottomSheet(mBottomSheetBehavior, Message);
        Post.setContext(getContext());
        Post.setProgressBar(progressBar);
        Post.setFragment(this);
    }

    private Product getProductFromBundle() {
        Log.d(TAG, "getProductFromBundle: ATTEMPTING TO FETCH PRODUCT");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.d(TAG, "getProductFromBundle: BUNDLE IS FULL");
            return bundle.getParcelable(getString(R.string.product_parcelable));
        } else {
            Log.d(TAG, "getProductFromBundle: BUNDLE IS NULL");
            return null;
        }
    }

    /**
     * Initialse the spinners
     */
    private void initSpinner() {
        ViewManipulation viewManipulation = new ViewManipulation(getContext());
        String[] productTypeArray = getResources().getStringArray(R.array.product_type);
        String[] conditionArray = getResources().getStringArray(R.array.condition_entries);
        String[] genderArray = getResources().getStringArray(R.array.gender_entries);
        String[] transactionArray = getResources().getStringArray(R.array.transaction_type);
        viewManipulation.initSpinner(genderArray, genderSpinner);
        viewManipulation.initSpinner(productTypeArray, typeSpinner);
        viewManipulation.initSpinner(conditionArray, conditionSpinner);
        viewManipulation.initSpinner(transactionArray, transactionSpinner);
        listenerMethod();
    }

    private void listenerMethod() {
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (typeSpinner.getItemAtPosition(i).equals("Shoes")) {
                    Log.d(TAG, "onItemSelected: SHOES");
                    radioGroup.setVisibility(View.GONE);
                    mSize.setVisibility(View.VISIBLE);
                    mSize.setHint(getString(R.string.hint_size_shoes));
                } else {
                    mSize.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @OnClick(R.id.post)
    public void post() {
        Log.d(TAG, "post: ATTEMPTING TO POST IMAGE ");
        progressBar.setVisibility(View.VISIBLE);
        if (getProductFromBundle() != null) {
            Product product = getProductFromBundle();
            int selectedID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = radioGroup.findViewById(selectedID);
            int idx = radioGroup.indexOfChild(radioButton);
            RadioButton radioButton1 = (RadioButton) radioGroup.getChildAt(idx);
            String selectedText = radioButton1.getText().toString();
            String price = mPrice.getText().toString().trim();
            String gender = genderSpinner.getSelectedItem().toString().trim();
            String type = typeSpinner.getSelectedItem().toString().trim();
            String condition = conditionSpinner.getSelectedItem().toString().trim();
            String transaction = transactionSpinner.getSelectedItem().toString().trim();
            if (radioGroup.getVisibility() == View.VISIBLE) {
                Log.d(TAG, "post: SELECTED RADIO BUTTON " + selectedText);
                String size = selectedText;
                if (!price.equals("")) {
                    if (StringManipulation.isValidProductPrice(price)) {
                        Log.d(TAG, "post: CORRECT ");
                        Log.d(TAG, "post: GENDER " + gender);
                        Log.d(TAG, "post: TYPE " + type);
                        Log.d(TAG, "post: CONDDITION " + condition);
                        Log.d(TAG, "post: SIZE " + size);
                        ProductType productType;
                        switch (type) {
                            case "Torso":
                                productType = new Torso(size);
                                break;
                            case "Head":
                                productType = new Head(size);
                                break;
                            case "Outfit":
                                productType = new Outfit(size);
                                break;
                            case "Lower Body":
                                productType = new LowerBody(size);
                                break;
                            default:
                                productType = null;
                                break;
                        }
                        if (productType != null) {
                            product.setProductType(productType);
                            product.setGender(gender);
                            product.setCondition(condition);
                            product.setPrice(Double.parseDouble(price));
                            product.setTransactionType(transaction);
                            Log.d(TAG, "post: FULL PRODUCT IS " + product.toString());
                            Post.uploadImageFromGallery(product);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            BottomSheetHelper.setMessage(getString(R.string.register_error_message));
                            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                        }
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        BottomSheetHelper.setMessage(getString(R.string.input_error));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    BottomSheetHelper.setMessage(getString(R.string.input_error));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                }
            } else if (mSize.getVisibility() == View.VISIBLE) {
                Log.d(TAG, "post: SELECTED SIZE " + mSize.getText().toString().trim());
                String size = mSize.getText().toString().trim();
                if (!price.equals("") && !size.equals("")) {
                    if (StringManipulation.isValidProductPrice(size) && StringManipulation.isValidProductPrice(price)) {
                        Log.d(TAG, "post: CORRECT ");
                        Log.d(TAG, "post: GENDER " + gender);
                        Log.d(TAG, "post: TYPE " + type);
                        Log.d(TAG, "post: CONDDITION " + condition);
                        Log.d(TAG, "post: SIZE " + size);
                        Double dSize = Double.parseDouble(size);
                        ProductType productType = new Shoes(dSize);
                        if (productType != null) {
                            product.setProductType(productType);
                            product.setGender(gender);
                            product.setCondition(condition);
                            product.setPrice(Double.parseDouble(price));
                            product.setTransactionType(transaction);
                            Log.d(TAG, "post: FULL PRODUCT IS " + product.toString());
                            Post.uploadImageFromGallery(product);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            BottomSheetHelper.setMessage(getString(R.string.register_error_message));
                            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                        }


                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        BottomSheetHelper.setMessage(getString(R.string.input_error));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    BottomSheetHelper.setMessage(getString(R.string.input_error));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                }
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            BottomSheetHelper.setMessage(getString(R.string.register_error_message));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
        }


    }


}

