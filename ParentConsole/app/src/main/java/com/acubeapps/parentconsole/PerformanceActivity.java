package com.acubeapps.parentconsole;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.acubeapps.parentconsole.model.ChildCourseResult;
import com.acubeapps.parentconsole.model.QuestionDetails;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PerformanceActivity extends AppCompatActivity {

    ChildCourseResult result;

    @BindView(R.id.txtQ1)
    TextView txtQ1;

    @BindView(R.id.txtQ2)
    TextView txtQ2;

    @BindView(R.id.txtSoln1)
    TextView txtSoln1;

    @BindView(R.id.txtSoln2)
    ImageView imgSoln2;

    @BindView(R.id.txt_percentile)
    TextView txtPerc;

    @Inject
    SharedPreferences sharedPreferences;

    String childName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        Injectors.appComponent().injectPerformaceActivity(this);
        ButterKnife.bind(this);
        result = (ChildCourseResult) getIntent().getSerializableExtra(Constants.CHILD_RESULT);
        childName = sharedPreferences.getString(result.childId, null);
        setTitle(childName + "'s Performance");
        bindData();
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            int nh = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
            imgSoln2.setImageBitmap(scaled);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    private void bindData(){
        List<QuestionDetails> questionDetailsList = result.questionList;
        QuestionDetails q1 = questionDetailsList.get(0);
        txtQ1.setText(q1.questionText + " ?");
        txtSoln1.setText("Your kid answered " + q1.options.get(Integer.valueOf(q1.getSolution()) - 1).getOptionText());

        QuestionDetails q2 = questionDetailsList.get(1);
        txtQ2.setText(q2.questionText);

        Picasso.with(this).load(q2.getSolution()).into(target);

        txtPerc.setText(childName + " scored: " + result.percentile + " percentile.");
    }
}
