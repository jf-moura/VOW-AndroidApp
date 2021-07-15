package pt.vow.ui.graphics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import pt.vow.R;
import pt.vow.databinding.FragmentGraphicsBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.GetRatingResult;
import pt.vow.ui.activityInfo.GetRatingViewModel;
import pt.vow.ui.activityInfo.GetRatingViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;

public class GraphicFragment extends Fragment {
    private FragmentGraphicsBinding binding;
    private GetRatingViewModel getRatingViewModel;
    private LoggedInUserView user;

    private GraphView graphView;

    private List<Double> xRate, yRate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentGraphicsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        graphView = root.findViewById(R.id.idGraphView);
        xRate = new ArrayList<>();
        yRate = new ArrayList<>();

        // TODO: verificar com o getRating da ActivityInfoActivity
        /*getRatingViewModel = new ViewModelProvider(this, new GetRatingViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(GetRatingViewModel.class);
        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        getRatingViewModel.getRating(user.getUsername(), user.getTokenID(), "entity", "5168126949851136");
        getRatingViewModel.getRating(user.getUsername(), user.getTokenID(), "entity", "5731076903272448");

        getRatingViewModel.getRatingResult().observeForever(new Observer<GetRatingResult>() {
            @Override
            public void onChanged(@Nullable GetRatingResult getRatingResult) {
                if (getRatingResult == null) {
                    xRate.add(0.0);
                    yRate.add(0.0);
                    return;
                }
                if (getRatingResult.getError() != null) {
                    return;
                }
                if (getRatingResult.getSuccess() != null) {
                    String rate = getRatingResult.getSuccess().getRating();

                    xRate.add(Double.parseDouble(getRatingResult.getSuccess().getActivityRatingSum()));
                    yRate.add(Double.parseDouble(getRatingResult.getSuccess().getActivityRatingCounter()));
                }
            }
        });

        DataPoint[] dataPoints = new DataPoint[xRate.size()];
        for (int i = 0; i < xRate.size(); i++)
            dataPoints[i] = new DataPoint(xRate.get(i), yRate.get(i));

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);

        graphView.setTitle(String.valueOf(R.string.rating));
        graphView.setTitleColor(R.color.logo_darker_blue);
        graphView.setTitleTextSize(18);
        graphView.addSeries(series);*/

        return root;
    }
}

