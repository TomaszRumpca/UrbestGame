package pl.gda.pg.tomrumpc.urbestgame.task.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pl.gda.pg.tomrumpc.urbestgame.R;
import pl.gda.pg.tomrumpc.urbestgame.data.DbFacade;

public class QATask extends AppCompatActivity {

    EditText answerField;
    Button saveButton;
    String taskName;
    DbFacade db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qatask);

        Bundle bundle = getIntent().getExtras();
        taskName = bundle.getString(getResources().getString(R.string.task_title));
        ((TextView) findViewById(R.id.headerTitle)).setText(taskName);
        String points = bundle.getString(getResources().getString(R.string.task_points));
        ((TextView) findViewById(R.id.headerPoints)).setText(points);
        String description = bundle.getString(getResources().getString(R.string.task_description));
        ((TextView) findViewById(R.id.headerDescription)).setText(description);
        String abbr = bundle.getString(getResources().getString(R.string.task_abbr));
        ((TextView) findViewById(R.id.headerAbbr)).setText(abbr);

        answerField = (EditText) findViewById(R.id.answerField);
        saveButton = (Button) findViewById(R.id.saveAnswer);

        db = new DbFacade(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean isActive = db.isTaskActive(taskName);
        answerField.setClickable(isActive);
        saveButton.setClickable(isActive);

        String currentAnswer = db.getAnswer(taskName);
        if (currentAnswer != null) {
            answerField.setText(currentAnswer);
        }
    }

    @Override
    protected void onPause() {
        db.saveCurrentAnswer(taskName, answerField.getText().toString());
        super.onPause();
    }

    public void onSaveClick(View view) {
        String answer = answerField.getText().toString();
        answerField.setClickable(false);
        saveButton.setClickable(false);
        db.submitAnswer(taskName, answer);
        onBackPressed();
    }

}
