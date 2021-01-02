package pl.com.weddingPlanner.view.dialog;

import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogQuestionBinding;
import pl.com.weddingPlanner.view.BaseActivity;

public class QuestionDialog extends CustomAlertDialog {

    private DialogQuestionBinding binding;

    public QuestionDialog(BaseActivity activity, String question) {
        super(activity, R.layout.dialog_question);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_question, null, false);

        setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
            activity.executeQuestionDialog();
        });
        setNegativeButton(R.string.dialog_no, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
        setText(question);
    }

    private void setText(String question) {
        binding.question.setText(question);
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
