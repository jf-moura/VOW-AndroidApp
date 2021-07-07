package pt.vow.ui.activityInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.rating.RatingRepository;

public class RatingViewModel  extends ViewModel {

    private MutableLiveData<SetRatingResult> ratingResult = new MutableLiveData<>();
    private RatingRepository ratingRepository;
    private final Executor executor;

    public RatingViewModel(RatingRepository ratingRepository, Executor executor) {
        this.ratingRepository = ratingRepository;
        this.executor = executor;
    }

    LiveData<SetRatingResult> getRatingResult() {
        return ratingResult;
    }


    public void setRating(String username, String tokenID, String owner, String activityid, long rating) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RatingView> result = ratingRepository.setRating(username, tokenID, owner, activityid, rating);
                if (result instanceof Result.Success) {
                    RatingView data = ((Result.Success<RatingView>) result).getData();
                    ratingResult.postValue(new SetRatingResult(new RatingView(data.getUsername(), data.getActivityID(), data.getRating())));
                } else {
                    ratingResult.postValue(new SetRatingResult(R.string.set_rating_failed));
                }
            }
        });
    }

}
