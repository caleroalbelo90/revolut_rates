package com.revolut.revolutrates.ui.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.revolut.revolutrates.R;
import com.revolut.revolutrates.model.RevolutCurrencyItem;
import com.revolut.revolutrates.ui.main.adapter.CurrencyViewAdapter;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

public class PlaceholderFragment extends Fragment implements CurrencyViewAdapter.ItemClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    LayoutInflater inflater;

    RecyclerView currencyRateList;

    ImageView baseCurrencyImage;

    CurrencyViewAdapter adapter;

    TextView baseCurrencyInitials;
    TextView baseCurrencyName;

    EditText baseCurrencyRate;

    View baseInfoContainer;

    boolean isAnimating = false;

    RevolutCurrencyItem lastRevolutCurrencyItem;

    private RecyclerView.ItemAnimator.ItemAnimatorFinishedListener animationFinishedListener =
            new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                @Override
                public void onAnimationsFinished() {
                    // The current animation have finished and there is currently no animation running,
                    // but there might still be more items that will be animated after this method returns.
                    // Post a message to the message queue for checking if there are any more
                    // animations running.
                    new Handler().post(waitForAnimationsToFinishRunnable);
                }
            };

    private Runnable waitForAnimationsToFinishRunnable = new Runnable() {
        @Override
        public void run() {
            waitForAnimationsToFinish();
        }
    };

    private void waitForAnimationsToFinish() {
        if (currencyRateList.isAnimating()) {
            // The recycler view is still animating, try again when the animation has finished.
            currencyRateList.getItemAnimator().isRunning(animationFinishedListener);
            return;
        }

        // The recycler view have animated all it's views
        onRecyclerViewAnimationsFinished();
    }

    private void onRecyclerViewAnimationsFinished() {
        currencyRateList.smoothScrollToPosition(0);
        isAnimating = false;
        pageViewModel.start();
    }

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        int index = 1;

        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        this.inflater = inflater;

        pageViewModel.getCurrencyMutableLiveData().observe(getViewLifecycleOwner(), revolutCurrencyItemListPair -> {
            if (isAnimating) return;

            adapter.updateData(revolutCurrencyItemListPair.second);

            if (revolutCurrencyItemListPair.first == null) {
                populateBase(revolutCurrencyItemListPair.second.get(0));
            } else {
                populateBase(revolutCurrencyItemListPair.first);
            }
        });

        pageViewModel.start();

        baseCurrencyImage = root.findViewById(R.id.baseCurrencyImage);
        baseCurrencyInitials = root.findViewById(R.id.baseCurrencyInitials);
        baseCurrencyName = root.findViewById(R.id.baseCurrencyName);
        baseCurrencyRate = root.findViewById(R.id.baseCurrencyRate);
        baseInfoContainer = root.findViewById(R.id.baseInfoContainer);

        currencyRateList = root.findViewById(R.id.currencyRateList);

        currencyRateList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CurrencyViewAdapter(getContext(), new LinkedList<>());
        adapter.setClickListener(this);

        currencyRateList.setAdapter(adapter);

        currencyRateList.getItemAnimator().setMoveDuration(1000);

        baseCurrencyRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    adapter.updateBaseRate(1);
                } else {
                    adapter.updateBaseRate(Float.parseFloat(s.toString()));
                }
            }
        });

        return root;
    }

    @Override
    public void onItemClick(View view, String currency) {

        ((EditText) currencyRateList.getChildAt(0).findViewById(R.id.currencyRate)).setText("0.0");
        currencyRateList.getChildAt(0).findViewById(R.id.infoContainer).setVisibility(View.VISIBLE);

        baseInfoContainer.setVisibility(View.INVISIBLE);

        int position = ((LinearLayoutManager) currencyRateList.getLayoutManager()).findFirstVisibleItemPosition();

        int delayInMillis = 0;

        if (position == 0) {
            currencyRateList.scrollToPosition(0);
            delayInMillis = 900;
        } else {
            currencyRateList.smoothScrollToPosition(0);
            delayInMillis = 1400;
        }

        isAnimating = true;
        pageViewModel.setCurrencyAtTop(currency);
        new Handler().postDelayed(() -> {
            isAnimating = false;
            pageViewModel.start();
        }, delayInMillis);
    }

    public void populateBase(RevolutCurrencyItem revolutCurrencyItem) {

        if (revolutCurrencyItem == null) return;

        if (lastRevolutCurrencyItem == null) {
            lastRevolutCurrencyItem = revolutCurrencyItem;
        }

        baseInfoContainer.setVisibility(View.VISIBLE);

        baseCurrencyInitials.setText(revolutCurrencyItem.getCurrency());
        baseCurrencyName.setText(revolutCurrencyItem.getCurrencyCompleteName());

        final int flag = World.getFlagOf(revolutCurrencyItem.getCountryCode());
        baseCurrencyImage.setImageResource(flag);
    }

}