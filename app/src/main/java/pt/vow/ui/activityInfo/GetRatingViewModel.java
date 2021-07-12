package pt.vow.ui.activityInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.rating.GetRatingRepository;
import pt.vow.data.rating.RatingRepository;

public class GetRatingViewModel extends ViewModel {
    private MutableLiveData<GetRatingResult> getRatingResult = new MutableLiveData<>();
    private GetRatingRepository getRatingRepository;
    private final Executor executor;

    public GetRatingViewModel(GetRatingRepository getRatingRepository, Executor executor) {
        this.getRatingRepository = getRatingRepository;
        this.executor = executor;
    }

    LiveData<GetRatingResult> getRatingResult() {
        return getRatingResult;
    }


    public void getRating(String username, String tokenID, String owner, String activityid) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<GetRatingView> result = getRatingRepository.getRating(username, tokenID, owner, activityid);
                if (result instanceof Result.Success) {
                    GetRatingView data = ((Result.Success<GetRatingView>) result).getData();
                    getRatingResult.postValue(new GetRatingResult(new GetRatingView(data.getRating(), data.getActivityRatingSum(), data.getActivityRatingCounter())));
                } else {
                    getRatingResult.postValue(new GetRatingResult(R.string.get_rating_failed));
                }
            }
        });
    }
}
