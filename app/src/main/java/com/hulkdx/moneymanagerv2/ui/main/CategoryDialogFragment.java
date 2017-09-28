/*
 * Created by Mohammad Jafarzadeh Rezvan on 22/7/2017.
 * Category Dialog Fragment Class.
 * Note: Use android color picker for custom colors: https://github.com/yukuku/ambilwarna
 */
package com.hulkdx.moneymanagerv2.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hulkdx.moneymanagerv2.R;
import com.hulkdx.moneymanagerv2.data.model.Category;

public class CategoryDialogFragment extends DialogFragment
        implements DialogInterface.OnClickListener {

    private EditText mNameEditText;
    private LinearLayout mColorsLinearLayout;
    private CategoryFragmentListener mListener;
    private static int catColorsSelectedImageViews = -1;
    private int[] colorsArray = null;

    public CategoryDialogFragment() { }


    public static CategoryDialogFragment newInstance() {
        return new CategoryDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The root of a dialog shall be null.
        // @link: https://stackoverflow.com/a/26596481/3996989`
        @SuppressLint("InflateParams")
        View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_category, null);

        mNameEditText = mView.findViewById(R.id.et_name);
        mColorsLinearLayout = mView.findViewById(R.id.colors_linearLayout);
        createColorImageView();
        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setNeutralButton(R.string.dialog_action_ok, this)
                .setView(mView)
                .create();
    }

    // Create colors view for each string array
    private void createColorImageView() {
        int[] colorsArray = getActivity().getResources().getIntArray(R.array.category_colors);
        // ImageView is defined as final so we can use them in OnClick method to remove
        // the ImageResource.
        // Note: is there a better way for doing it?!
        final ImageView[] catColorsImageViews = new ImageView[colorsArray.length];
        for (int i = 0, count = colorsArray.length; i < count; i++) {
            catColorsImageViews[i] = new ImageView(getActivity());
            // Convert 50 dx to 50 dp
            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
            catColorsImageViews[i].setLayoutParams(new LinearLayout.LayoutParams(
                    (int) (50 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)),
                    (int) (50 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))));

            catColorsImageViews[i].setBackgroundColor(colorsArray[i]);
            // Set id to i so we can retrieve it from OnClick method.
            catColorsImageViews[i].setId(i);
            catColorsImageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Remove the image Resource from previous selected ImageViews
                    if (catColorsSelectedImageViews != -1) {
                        catColorsImageViews[catColorsSelectedImageViews].setImageResource(0);
                    }
                    ((ImageView) view).setImageResource(R.drawable.ic_category_selected);
                    catColorsSelectedImageViews = view.getId();
                }
            });
            mColorsLinearLayout.addView(catColorsImageViews[i]);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CategoryFragmentListener) {
            mListener = (CategoryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        colorsArray = getActivity().getResources().getIntArray(R.array.category_colors);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // On Clicking Ok button in dialog
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (mNameEditText.getText().toString().isEmpty() || catColorsSelectedImageViews == -1) {
            return;
        }
        // Set int hexColor to String #000000
        String hexColor = String.format("#%06X",
                (0xFFFFFF & colorsArray[catColorsSelectedImageViews]));

        mListener.onClickOkCategory(new Category(mNameEditText.getText().toString(), hexColor));
    }

    /**
     * This interface must be implemented by MainActivity to allow an
     * interaction in this fragment to be communicated to the activity.
     */
    public interface CategoryFragmentListener {
        void onClickOkCategory(Category category);
    }
}
