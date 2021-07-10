package pt.vow.ui.route;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import pt.vow.R;
import pt.vow.databinding.FragmentChooseTypeBinding;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.newActivity.NewActivityFragment;

public class ChooseType extends Fragment {

    private BottomNavigationView topNavigation;
    private ChooseType mActivity;
    private FragmentChooseTypeBinding binding;
    private LoggedInUserView user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentChooseTypeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        mActivity = this;
        topNavigation = root.findViewById(R.id.topNavigation);

        Fragment fragment = new NewActivityFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chooseType, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        topNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_new_activity:
                        Fragment fragment = new NewActivityFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.chooseType, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.navigation_new_route:
                        /* afragment = new NewRouteFragment();
                        FragmentManager afragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction afragmentTransaction = afragmentManager.beginTransaction();
                        afragmentTransaction.replace(R.id.chooseType, afragment);
                        afragmentTransaction.addToBackStack(null);
                        afragmentTransaction.commit();*/
                        /*Intent intent = new Intent(getActivity(), CreateRouteActivity.class);
                        intent.putExtra("UserLogged", user);
                        startActivity(intent);*/
                        Fragment afragment = new CreateRouteFragment();
                        FragmentManager afragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction afragmentTransaction = afragmentManager.beginTransaction();
                        afragmentTransaction.replace(R.id.chooseType, afragment);
                        afragmentTransaction.addToBackStack(null);
                        afragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });
        return root;
    }

}
