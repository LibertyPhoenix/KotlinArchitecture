package com.smartstudy.uskid.library.share.util;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.uskid.library.R;


public class BottomSheetDialogView extends BaseSharePlatformSelector {


    private BottomSheetDialog mBottomSheetDialog;

    public BottomSheetDialogView(FragmentActivity context, OnShareSelectorDismissListener dismissListener,
                                 SimpleAdapter.OnItemClickListener onItemClickListener) {
        super(context, dismissListener);
        mBottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_recycler_view, null);
        RecyclerView recyclerView = view.findViewById(R.id.bottom_sheet_recycler_view);
        TextView cancelTextView = view.findViewById(R.id.cancel_action);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        SimpleAdapter adapter = new SimpleAdapter();
        adapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(adapter);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
    }


    @NonNull public static BottomSheetDialogView show(FragmentActivity context, OnShareSelectorDismissListener dismissListener,
                                                      SimpleAdapter.OnItemClickListener onItemClickListener) {
        return new BottomSheetDialogView(context, dismissListener, onItemClickListener);
    }

    @Override
    public void show() {

    }

    @Override
    public void dismiss() {
        mBottomSheetDialog.dismiss();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_name);
            mImageView = itemView.findViewById(R.id.iv_icon);
        }
    }

    public static class SimpleAdapter extends RecyclerView.Adapter<ViewHolder> {

        public interface OnItemClickListener {
            void onItemClick(int position, ShareTarget target);
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_share, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mTextView.setText(getShareTargets()[position].titleId);
            holder.mImageView.setImageResource(getShareTargets()[position].iconId);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position, getShareTarget(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return getShareTargets().length;
        }

    }
}