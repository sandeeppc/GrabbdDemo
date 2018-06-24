package com.grabbddemoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grabbddemoapp.R;


/**
 * Created by Sandy
 */

public class TypeListAdapter extends RecyclerView.Adapter<TypeListAdapter.ViewHolder> {

    private TypeListListener typeListListener;
    public interface TypeListListener{

        public void itemSelected(String section);
    }

    public TypeListAdapter(final TypeListListener typeListListener){
        this.typeListListener = typeListListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_section, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final int adapterPos = holder.getAdapterPosition();
        switch (adapterPos) {
            case 0 :
                holder.tvSection.setText(R.string.title_food);
                break;
                case 1 :
                    holder.tvSection.setText(R.string.title_drinks);
                break;
                case 2 :
                    holder.tvSection.setText(R.string.title_coffee);
                break;
                case 3 :
                    holder.tvSection.setText(R.string.title_shop);
                break;
                case 4 :
                    holder.tvSection.setText(R.string.title_arts);
                break;
                case 5 :
                    holder.tvSection.setText(R.string.title_outdoors);
                break;
                case 6 :
                    holder.tvSection.setText(R.string.title_sights);
                break;
                case 7 :
                    holder.tvSection.setText(R.string.title_trending);
                break;
        }

    }


    @Override
    public int getItemCount() {
        return 8;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSection;

        ViewHolder(View itemView) {
            super(itemView);
            tvSection = itemView.findViewById(R.id.tvSection);

            itemView.setOnClickListener(view -> typeListListener.itemSelected(tvSection.getText().toString()));
        }
    }

}
