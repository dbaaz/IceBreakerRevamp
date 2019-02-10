package com.arbiter.droid.icebreakerprot1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arbiter.droid.icebreakerprot1.Common.removeItem;
import static com.arbiter.droid.icebreakerprot1.Common.setItem;


public class RecyclerViewMenuAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {


    private List<AbstractMenuModel> modelList;

    private OnItemClickListener mItemClickListener;

    private Context context;
    public RecyclerViewMenuAdapter(Context context, List<AbstractMenuModel> modelList) {
        this.modelList = modelList;
        this.context = context;

    }

    public void updateList(ArrayList<AbstractMenuModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public int getSectionCount() {
        return modelList.size();
    }

    @Override
    public int getItemCount(int section) {

        return modelList.get(section).getSingleItemArrayList().size();

    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        String sectionName = modelList.get(section).getTitle();
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.txtHeader.setText(sectionName);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        ArrayList<AbstractMenuModel> itemsInSection = modelList.get(section).getSingleItemArrayList();

        String itemTitle = itemsInSection.get(relativePosition).getTitle();
        String itemMessage = itemsInSection.get(relativePosition).getMessage();

        ViewHolder itemViewHolder = (ViewHolder) holder;

        itemViewHolder.itemTxtTitle.setText(itemTitle);
        itemViewHolder.itemTxtMessage.setText(itemMessage);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, boolean header) {
        if (header)

        {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_section, parent, false);
            return new SectionViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_list_menu, parent, false);
            ButterKnife.bind(this,view);
            return new ViewHolder(view);
        }

    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, AbstractMenuModel model);
    }

    // SectionViewHolder Class for Sections
    public static class SectionViewHolder extends RecyclerView.ViewHolder {


        final TextView txtHeader;

        public SectionViewHolder(View itemView) {
            super(itemView);

            txtHeader = (TextView) itemView.findViewById(R.id.txt_header);


        }
    }

    // ItemViewHolder Class for Items in each Section
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView itemTxtTitle;
        final Button decButton;
        final Button incButton;
        final TextView counterText;
        final ImageView imgUser;
        public int counter=0;
        final TextView itemTxtMessage;


        public ViewHolder(View itemView) {
            super(itemView);
            itemTxtTitle = (TextView) itemView.findViewById(R.id.item_txt_title);
            itemTxtMessage = (TextView) itemView.findViewById(R.id.item_txt_message);
            decButton = itemView.findViewById(R.id.button13);
            incButton = itemView.findViewById(R.id.button14);
            imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            counterText = itemView.findViewById(R.id.textView13);
            decButton.setOnClickListener(v -> {
                if(counter>0) {
                    counterText.setText(--counter + "");
                    if (counter == 0)
                        removeItem(itemTxtTitle.getText().toString());
                    else
                        setItem(itemTxtTitle.getText().toString(), counter);
                }
            });
            incButton.setOnClickListener(v -> {
                counterText.setText(++counter+"");
                setItem(itemTxtTitle.getText().toString(),counter);
            });
        }
    }


}


