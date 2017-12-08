package com.zspirytus.dmdemo.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zspirytus.dmdemo.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_aboutfragment,container,false);
        LoadPane(view);
        return view;
    }

    private void LoadPane(View view){
        TextView centerTextView = view.findViewById(R.id.aboutfragment_text);
        TextView webTextView = view.findViewById(R.id.aboutfragment_webservice_git);
        TextView appTextView = view.findViewById(R.id.aboutfragment_app_git);
        final String app = getString(R.string.app_git);
        final String web = getString(R.string.webservice_git);
        String color = "#0000ff";
        String appText = "App: <font color='"+color+"'>"+app+"</font>";
        String webText = "WebService: <font color='"+color+"'>"+web+"</font>";
        appTextView.setText(Html.fromHtml(appText));
        webTextView.setText(Html.fromHtml(webText));
        appTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(app));
                startActivity(intent);
            }
        });
        webTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(web));
                startActivity(intent);
            }
        });
    }
}
