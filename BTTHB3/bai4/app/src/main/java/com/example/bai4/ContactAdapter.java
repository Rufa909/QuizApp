package com.example.bai4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private List<Contact> objects;

    public ContactAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Contact contact = objects.get(position);
        ImageView imgGender = convertView.findViewById(R.id.imgGender);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtPhone = convertView.findViewById(R.id.txtPhone);

        txtName.setText(contact.getName());
        txtPhone.setText(contact.getPhone());

        if (contact.isMale()) {
            imgGender.setBackgroundColor(android.graphics.Color.CYAN);
        } else {
            imgGender.setBackgroundColor(android.graphics.Color.MAGENTA);
        }

        return convertView;
    }
}