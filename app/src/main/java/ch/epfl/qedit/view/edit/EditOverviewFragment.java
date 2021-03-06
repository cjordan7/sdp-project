package ch.epfl.qedit.view.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.qedit.R;
import ch.epfl.qedit.model.MatrixFormat;
import ch.epfl.qedit.model.Question;
import ch.epfl.qedit.view.util.ListEditView;
import java.util.LinkedList;
import java.util.List;

/** This fragment is used to view and edit the list of questions of a quiz. */
public class EditOverviewFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Question> questions;
    private int numQuestions;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_overview, container, false);

        // Retrieve and configure the recycler view
        ListEditView listEditView = view.findViewById(R.id.question_list);
        final ListEditView.ListEditAdapter adapter = createAdapter();
        listEditView.setAdapter(adapter);

        // Configure the add button
        view.findViewById(R.id.add_question_button)
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                numQuestions++;
                                adapter.addItem(
                                        new Question(
                                                "Q" + numQuestions,
                                                "is it " + numQuestions + " ?",
                                                new MatrixFormat(1, 1)));
                            }
                        });

        return view;
    }

    private ListEditView.ListEditAdapter createAdapter() {
        // For now, we just add dummy questions to the quiz
        questions = new LinkedList<>();
        for (numQuestions = 0; numQuestions < 5; numQuestions++)
            questions.add(
                    new Question(
                            "Q" + (numQuestions + 1),
                            "is it " + (numQuestions + 1) + " ?",
                            new MatrixFormat(1, 1)));

        // Create an adapter for the question list
        return new ListEditView.ListEditAdapter<>(
                questions,
                new ListEditView.GetItemText<Question>() {
                    @Override
                    public String getText(Question item) {
                        return item.getTitle();
                    }
                });
    }
}
