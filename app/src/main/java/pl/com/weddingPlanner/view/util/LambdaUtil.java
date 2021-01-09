package pl.com.weddingPlanner.view.util;

import android.text.Editable;
import android.text.TextWatcher;

import org.jetbrains.annotations.NotNull;

public class LambdaUtil {

    @NotNull
    public static TextWatcher getOnTextChangedTextWatcher(TextWatcher4ArgsLambda command) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                command.execute(charSequence, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    @FunctionalInterface
    public interface TextWatcher4ArgsLambda {
        void execute(CharSequence charSequence, int i, int i1, int i2);
    }
}
