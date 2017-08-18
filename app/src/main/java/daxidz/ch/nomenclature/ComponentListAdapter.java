package daxidz.ch.nomenclature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daxidz.ch.nomenclature.database.ChemicalCardDAO;

/**
 * Created by dtruan on 14-Aug-17.
 */

public class ComponentListAdapter extends ArrayAdapter<ChemicalCard> {

    private ArrayList<ChemicalCard> dataSet;
    Context mContext;
    ChemicalCardDAO chemicalCardDAO;

    private static class ViewHolder {
        TextView name;
        TextView formula;
        TextView tag;
        LinearLayout textArea;
        View separator;
    }

    public ComponentListAdapter(ArrayList<ChemicalCard> data, Context context, ChemicalCardDAO chemicalCardDAO) {
        super(context, R.layout.row_component_list, data);
        this.dataSet = data;
        this.mContext = context;
        this.chemicalCardDAO = chemicalCardDAO;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ChemicalCard dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_component_list, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.formula = (TextView) convertView.findViewById(R.id.formula);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.tag);
            viewHolder.textArea = (LinearLayout) convertView.findViewById(R.id.textArea);
            viewHolder.separator = (View) convertView.findViewById(R.id.separator);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataModel.setTag(ChemicalCard.Tag.values()[(dataModel.getTag().ordinal() + 1) % ChemicalCard.Tag.values().length]);
                notifyDataSetChanged();
                chemicalCardDAO.open();
                chemicalCardDAO.updateTag(dataModel);
                chemicalCardDAO.close();
            }
        });

        viewHolder.textArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataModel.setShouldLearn(!dataModel.isShouldLearn());
                chemicalCardDAO.open();
                chemicalCardDAO.updateShouldLearn(dataModel);
                chemicalCardDAO.close();
                notifyDataSetChanged();
            }
        });

        viewHolder.name.setText(dataModel.getName());
        viewHolder.formula.setText(dataModel.getNomenclature());
        viewHolder.tag.setText(dataModel.getTag().getName());
        if (dataModel.isShouldLearn()) {
            viewHolder.textArea.setBackgroundResource(R.color.should_learn);
        } else {
            viewHolder.textArea.setBackgroundResource(R.color.should_not_learn);
        }
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
