package daxidz.ch.nomenclature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dtruan on 14-Aug-17.
 */

public class ComponentListAdapter extends ArrayAdapter<ChemicalCard> {

    private ArrayList<ChemicalCard> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView name;
        TextView formula;
        TextView tag;
    }

    public ComponentListAdapter(ArrayList<ChemicalCard> data, Context context) {
        super(context, R.layout.row_component_list, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChemicalCard dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_component_list, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.formula = (TextView) convertView.findViewById(R.id.formula);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.tag);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(dataModel.getName());
        viewHolder.formula.setText(dataModel.getNomenclature());
        viewHolder.tag.setText(dataModel.getTag().getName());
        switch (dataModel.getTag()) {
            case HARD:
                viewHolder.tag.setBackgroundResource(R.color.hard_tag);
                break;
            case NONE:
                viewHolder.tag.setBackgroundResource(R.color.none_tag);
                break;
            case KNOWN:
                viewHolder.tag.setBackgroundResource(R.color.known_tag);
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
