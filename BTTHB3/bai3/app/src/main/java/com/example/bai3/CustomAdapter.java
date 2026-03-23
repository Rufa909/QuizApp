package com.example.bai3;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private ArrayList<Contact> arrContact;

    public CustomAdapter(Context context, int resource, ArrayList<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrContact = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvAvatar = convertView.findViewById(R.id.tvAvatar);
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvMaSinhVien = convertView.findViewById(R.id.tvMaSinhVien);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = arrContact.get(position);
        viewHolder.tvAvatar.setText(String.valueOf(position + 1));
        viewHolder.tvAvatar.setBackgroundColor(contact.getColor());
        viewHolder.tvName.setText(contact.getName());
        viewHolder.tvMaSinhVien.setText(contact.getIdStudent());

        return convertView;
    }

    public class ViewHolder {
        TextView tvAvatar, tvName, tvMaSinhVien;
    }
}