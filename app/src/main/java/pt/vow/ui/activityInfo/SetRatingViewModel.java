package pt.vow.ui.activityInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.rating.RatingRepository;

public class SetRatingViewModel extends ViewModel {

    private MutableLiveData<SetRatingResult> ratingResult = new MutableLiveData<>();
    private RatingRepository ratingRepository;
    private final Executor executor;

    public SetRatingViewModel(RatingRepository ratingRepository, Executor executor) {
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
                Result<SetRatingView> result = ratingRepository.setRating(username, tokenID, owner, activityid, rating);
                if (result instanceof Result.Success) {
                    SetRatingView data = ((Result.Success<SetRatingView>) result).getData();
                    ratingResult.postValue(new SetRatingResult(new SetRatingView(data.getUsername(), data.getActivityID(), data.getRating())));
                } else {
                    ratingResult.postValue(new SetRatingResult(R.string.set_rating_failed));
                }
            }
        });
    }

}
