package com.hulkdx.moneymanagerv2;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.hulkdx.moneymanagerv2.injections.TestComponentRule;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.ui.main.MainActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import io.reactivex.Flowable;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static com.hulkdx.moneymanagerv2.common.TestDataFactory.makeListTransactions;
import static org.mockito.Mockito.when;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/09/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());

    public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<MainActivity>(MainActivity.class, false, false) {
                @Override
                protected Intent getActivityIntent() {
                    return new Intent(InstrumentationRegistry.getTargetContext(),
                            MainActivity.class);
                }
            };

    // This Rule make sure dagger applies before running the test.
    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(main);

    @Test
    public void transactionsShows() {
        List<Transaction> testData = makeListTransactions(10);

        when(component.getMockDataManager().getTransactions())
                .thenReturn(Flowable.just(testData));

        when(component.getMockDataManager().getCategories())
                .thenReturn(Flowable.just(new ArrayList<>()));

        when(component.getMockDataManager().getPreferencesHelper())
                .thenReturn(component.getMockPreferencesHelper());

        main.launchActivity(null);

        int position = 0;
        for (Transaction transaction : testData) {
            // It shows month and day.
            String date = transaction.getDate();
            String month = new DateFormatSymbols()
                    .getShortMonths()[ Integer.valueOf(date.split("-")[1]) - 1 ];
            String day = date.split("-")[2];
            // it shows amount
            String amount = String.format(Locale.ENGLISH, "+ %.2f", transaction.getAmount());

            onView(withId(R.id.transaction_recycler_view))
                    .check(matches(atPosition(position, hasDescendant(
                            withText(month)
                    ))));

            onView(withId(R.id.transaction_recycler_view))
                    .check(matches(atPosition(position, hasDescendant(
                            withText(day)
                    ))));

            onView(withId(R.id.transaction_recycler_view))
                    .check(matches(atPosition(position, hasDescendant(
                            withText(amount)
                    ))));

            position++;
        }
    }

    public static Matcher<View> atPosition(final int position,
                                           @NonNull final Matcher<View> itemMatcher) {

        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder =
                        view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }


}
