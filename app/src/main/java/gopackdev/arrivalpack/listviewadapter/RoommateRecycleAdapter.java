package gopackdev.arrivalpack.listviewadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import gopackdev.arrivalpack.R;
import gopackdev.arrivalpack.Roommate;
import gopackdev.arrivalpack.StudentDetailsActivity;
import gopackdev.arrivalpack.bluemixbean.StudentBean;

/**
 * Created by Chi-Han on 4/23/2016.
 */
public class RoommateRecycleAdapter extends RecyclerView.Adapter<RoommateRecycleAdapter.ViewHolder> {
    private List<StudentBean> studentBeanList;
    private Activity context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView studentName;
        public TextView studentGender;
        public TextView studentEmail;
        public TextView studentNation;
        public Button detailsBtn;
        public Button chatBtn;

        public ViewHolder(View v) {
            super(v);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            studentGender = (TextView) itemView.findViewById(R.id.studentGender);
            studentEmail = (TextView) itemView.findViewById(R.id.studentEmail);
            studentNation = (TextView) itemView.findViewById(R.id.studentCountry);
            detailsBtn = (Button) itemView.findViewById(R.id.detailsBtn);
            chatBtn = (Button) itemView.findViewById(R.id.chatBtn);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RoommateRecycleAdapter(List<StudentBean> myDataset, Activity ctx) {
        studentBeanList = myDataset;
        context = ctx;
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
        v.setPadding(2, 2, 2, 2);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        StudentBean tmp_b = studentBeanList.get(position);
        holder.studentName.setText(tmp_b.getFirstName() + " " + tmp_b.getLastName());
        holder.studentEmail.setText(tmp_b.getSchoolEmail());
        holder.studentGender.setText(tmp_b.getGender());
        holder.studentNation.setText(tmp_b.getNationality());
        holder.chatBtn.setTag(position);
        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                emailIntent.setAction(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{studentBeanList.get((Integer) v.getTag()).getSchoolEmail()});
                emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");
                emailIntent.putExtra(android.content.Intent.EXTRA_BCC, "");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Hello!");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(Detail));
                emailIntent.setType("text/html");
// FOLLOWING STATEMENT CHECKS WHETHER THERE IS ANY APP THAT CAN HANDLE OUR EMAIL INTENT
                context.startActivity(Intent.createChooser(emailIntent,
                        "Send Email Using: "));
            }
        });
        holder.detailsBtn.setTag(position);
        holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentBean tmp_b = studentBeanList.get((Integer)v.getTag());
                String JSONData = tmp_b.JSONFormat();
                Intent detailIntent = new Intent(context, StudentDetailsActivity.class);
                detailIntent.putExtra("student",JSONData);
                context.startActivity(detailIntent);
            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return studentBeanList.size();
    }
}
