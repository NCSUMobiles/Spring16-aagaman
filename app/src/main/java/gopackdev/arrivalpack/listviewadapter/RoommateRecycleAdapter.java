package gopackdev.arrivalpack.listviewadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gopackdev.arrivalpack.R;
import gopackdev.arrivalpack.bluemixbean.StudentBean;

/**
 * Created by Chi-Han on 4/23/2016.
 */
public class RoommateRecycleAdapter extends RecyclerView.Adapter<RoommateRecycleAdapter.ViewHolder> {
    private List<StudentBean> studentBeanList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView studentName;
        public TextView studentGender;
        public TextView studentEmail;
        public TextView studentNation;
        public ViewHolder(View v) {
            super(v);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            studentGender = (TextView) itemView.findViewById(R.id.studentGender);
            studentEmail = (TextView) itemView.findViewById(R.id.studentEmail);
            studentNation = (TextView) itemView.findViewById(R.id.studentCountry);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RoommateRecycleAdapter(List<StudentBean> myDataset) {
        studentBeanList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RoommateRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        v.setPadding(2,2,2,2);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        StudentBean tmp_b = studentBeanList.get(position);
        holder.studentName.setText(tmp_b.getFirstName()+" "+tmp_b.getLastName());
        holder.studentEmail.setText(tmp_b.getSchoolEmail());
        holder.studentGender.setText(tmp_b.getGender());
        holder.studentNation.setText(tmp_b.getNationality());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return studentBeanList.size();
    }
}
