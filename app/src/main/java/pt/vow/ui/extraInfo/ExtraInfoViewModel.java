package pt.vow.ui.extraInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;

import pt.vow.data.extraInfo.ExtraInfoRepository;

public class ExtraInfoViewModel {
    private MutableLiveData<ExtraInfoFormState> extraInfoFormState = new MutableLiveData<>();
    private MutableLiveData<ExtraInfoResult> extraInfoResult = new MutableLiveData<>();
    private pt.vow.data.extraInfo.ExtraInfoRepository extraInfoRepository;

    private final Executor executor;

    ExtraInfoViewModel(ExtraInfoRepository extraInfoRepository, Executor executor) {
        this.extraInfoRepository = extraInfoRepository;
        this.executor = executor;
    }

    LiveData<ExtraInfoFormState> getExtraInfoFormState() {
        return extraInfoFormState;
    }

    LiveData<ExtraInfoResult> getExtraInfoResult() {
        return extraInfoResult;
    }

  /*  public void ExtraInfoEntity(String name, String username, String email, String password, String phoneNumber, String website) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<AddedExtraInfo> result = extraInfoRepository.addExtraInfo(name, username);
                if (result instanceof Result.Success) {
                    AddedExtraInfo data = ((Result.Success<AddedExtraInfo>) result).getData();
                    extraInfoResult.postValue(new ExtraInfoResult(new ExtraInfoView(data.getDisplayName())));
                } else {
                    extraInfoResult.postValue(new ExtraInfoResult(R.string.register_failed));
                }
            }
        });*/
    }

