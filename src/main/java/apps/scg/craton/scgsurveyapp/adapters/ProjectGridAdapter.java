package apps.scg.craton.scgsurveyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.scg.craton.scgsurveyapp.R;
import apps.scg.craton.scgsurveyapp.bll.Project;
import apps.scg.craton.scgsurveyapp.bll.Village;

/**
 * Created by CodeThunder on 2017/02/28.
 */

public class ProjectGridAdapter extends BaseAdapter {

    Context context;
    ArrayList<Project> projects;

    public ProjectGridAdapter(Context context, ArrayList<Project> projects)
    {
        this.context = context;
        this.projects = projects;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Object getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {
            grid = new View(context);
            grid = inflater.inflate(R.layout.item_grid,null);
            ImageView img = (ImageView)grid.findViewById(R.id.img_check);
            TextView txt = (TextView)grid.findViewById(R.id.grid_text);
            txt.setText(projects.get(position).getName());
            img.setImageResource(R.drawable.checklist);
        }
        else
        {
            grid = convertView;
        }
        return grid;
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

}
