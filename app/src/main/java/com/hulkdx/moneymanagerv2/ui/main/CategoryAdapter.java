/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/20/2017.
 */
package com.hulkdx.moneymanagerv2.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hulkdx.moneymanagerv2.R;
import com.hulkdx.moneymanagerv2.data.model.Category;
import com.hulkdx.moneymanagerv2.injection.ApplicationContext;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private List<Category> mCategories;
    private final Context mContext;
    private Callback mCallback;
    private ImageView mCurrentSelectedImage = null;

    @Inject
    public CategoryAdapter(@ApplicationContext Context context) {
        mCategories = new ArrayList<>();
        mContext = context;
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_category, parent, false);
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        // The add button
        if (position == mCategories.size()) {
            holder.nameTV.setText(mContext.getResources().getString(R.string.add_new_category));
            holder.hexColorImageView.setVisibility(View.GONE);
            holder.setAddBtn(true);
            return;
        }
        holder.setAddBtn(false);
        holder.setCategory(mCategories.get(position));
        holder.nameTV.setText(mCategories.get(position).getName());
        holder.hexColorImageView.setVisibility(View.VISIBLE);
        
        if (mCategories.get(position).getHexColor() != null) {
            holder.hexColorImageView.setBackgroundColor(
                    Color.parseColor(mCategories.get(position).getHexColor()));
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size() + 1;
    }

    public void resetSelectedCategories() {
        if (mCurrentSelectedImage != null) mCurrentSelectedImage.setImageResource(0);
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_textview) TextView nameTV;
        @BindView(R.id.view_hex_color) ImageView hexColorImageView;

        private Category mCategory;
        private boolean mIsAddBtn = false;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        void onItemClicked() {
            if (mCallback != null) {
                // Add button clicked
                if (mIsAddBtn) {
                    mCallback.showAddCategoryDialogFragment();
                    return;
                }

                mCallback.onCategoryClicked(mCategory.getId());
                hexColorImageView.setImageResource(R.drawable.ic_category_selected);
                // Remove the previous selected ImageView drawable.
                if (mCurrentSelectedImage != null) {
                    mCurrentSelectedImage.setImageResource(0);
                }
                if (mCurrentSelectedImage == hexColorImageView) {
                    mCurrentSelectedImage = null;
                } else {
                    mCurrentSelectedImage = hexColorImageView;
                }
            }
        }

        public void setCategory(Category category) {
            mCategory = category;
        }

        public void setAddBtn(boolean addBtn) {
            mIsAddBtn = addBtn;
        }
    }

    interface Callback {
        void onCategoryClicked(long categoryId);
        void showAddCategoryDialogFragment();
    }
}
