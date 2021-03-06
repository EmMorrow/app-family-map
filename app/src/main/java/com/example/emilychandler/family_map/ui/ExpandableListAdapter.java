package com.example.emilychandler.family_map.ui;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.emilychandler.family_map.R;
import com.example.emilychandler.family_map.data.Event;
import com.example.emilychandler.family_map.data.Model;
import com.example.emilychandler.family_map.data.Person;
import com.joanzapata.iconify.widget.IconTextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<Object>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Object>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Person currPerson = Model.getInstance().getCurrPerson();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.person_list_item, null);
        }

        TextView firstLine = (TextView) convertView.findViewById(R.id.first_line);
        TextView secondLine = (TextView) convertView.findViewById(R.id.second_line);
        IconTextView image = (IconTextView) convertView.findViewById(R.id.icon);
        image.setText("{fa-map-marker}");
        image.setTextColor(Color.GRAY);

        if (_listDataHeader.get(groupPosition).equals("Life Events")) {
            final Event event = (Event) getChild(groupPosition,childPosition);
            firstLine.setText(event.getEventType() + ": " + event.getCity() + ", " +
                    event.getCountry() + " (" + event.getYear() + ")");
            secondLine.setText(currPerson.getFirstName() + " " + currPerson.getLastName());

        }

        else if (_listDataHeader.get(groupPosition).equals("Family")) {
            final Person person = (Person) getChild(groupPosition,childPosition);
            firstLine.setText(person.getFirstName() + " " + person.getLastName());

            if (person.getGender().equals("f")) {
                image.setText("{fa-female}");
                image.setTextColor(Color.MAGENTA);
            }
            else {
                image.setText("{fa-male}");
                image.setTextColor(Color.BLUE);
            }

            if (currPerson.getFather().equals(person.getPersonId())) secondLine.setText("Father");
            else if (currPerson.getMother().equals(person.getPersonId())) secondLine.setText("Mother");
            else if (currPerson.getSpouse().equals(person.getPersonId())) secondLine.setText("Spouse");
            else secondLine.setText("Child");
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.person_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView; //yeah
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}