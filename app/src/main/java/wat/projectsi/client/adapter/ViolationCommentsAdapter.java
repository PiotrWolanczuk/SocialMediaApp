package wat.projectsi.client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.model.violation.ViolationComment;
import wat.projectsi.client.model.violation.ViolationPost;

public class ViolationCommentsAdapter extends RecyclerView.Adapter<ViolationCommentsAdapter.ViolationViewHolder>{
    private Context context;
    private List<ViolationComment> violationList;

    public ViolationCommentsAdapter(List<ViolationComment> violationList, Context context) {
        this.context = context;
        this.violationList = violationList;
    }

    @NonNull
    @Override
    public ViolationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViolationViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.violation_comment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViolationViewHolder violationViewHolder, int i) {
        ViolationComment violation = violationList.get(i);

        violationViewHolder.content.setText(violation.getContent());
        violationViewHolder.description.setText(violation.getDescription());
    }

    @Override
    public int getItemCount() {
        return violationList.size();
    }

    public class ViolationViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView description;

        public ViolationViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.violation_item);
            description = itemView.findViewById(R.id.violation_description);
        }
    }
}
