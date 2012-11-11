/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android;

import android.app.Activity;
import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import bbct.common.data.BaseballCard;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardListWithDataTest extends ActivityInstrumentationTestCase2<BaseballCardList> {

    public BaseballCardListWithDataTest() {
        super(BaseballCardList.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();

        InputStream cardInputStream = this.inst.getContext().getAssets().open(DATA_ASSET);
        this.cardInput = new BaseballCardCsvFileReader(cardInputStream, true);
        this.allCards = this.cardInput.getAllBaseballCards();

        // Create the database and populate table with test data
        BaseballCardSQLHelper sqlHelper = new BaseballCardSQLHelper(this.inst.getTargetContext());
        this.dbUtil = new DatabaseUtil();
        this.dbUtil.populateTable(allCards);

        this.activity = this.getActivity();
        this.list = (ListView) this.activity.findViewById(android.R.id.list);
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();
        this.dbUtil.deleteDatabase();
        this.cardInput.close();

        super.tearDown();
    }

    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.list);

        // Check that database was created with the correct version and table
        SQLiteDatabase db = this.dbUtil.getDatabase();
        Assert.assertNotNull(db);
        Assert.assertEquals(BaseballCardSQLHelper.SCHEMA_VERSION, db.getVersion());
        Assert.assertEquals(BaseballCardSQLHelper.TABLE_NAME, SQLiteDatabase.findEditTable(BaseballCardSQLHelper.TABLE_NAME));
    }

    public void testStateDestroy() {
        Assert.fail("Implement me!");
    }

    public void testStatePause() {
        Assert.fail("Implement me!");
    }

    public void testMenuLayoutNoFilter() {
        this.sendKeys(KeyEvent.KEYCODE_MENU);
        Assert.fail("Now how do I check the contents of the menu?");
    }

    public void testMenuLayoutWithFilter() {
        Assert.fail("Implement me!");
    }

    /**
     * Test of {@link BaseballCardList#onListItemClick}.
     */
    public void testOnListItemClick() {
        System.out.println("onListItemClick");
        ListView l = null;
        View v = null;
        int position = 0;
        long id = 0L;
        BaseballCardList instance = new BaseballCardList();
        instance.onListItemClick(l, v, position, id);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    public void testAddDuplicateCard() {
        Assert.fail("Implement me!");
    }

    public void testAddCardToPopulatedDatabase() {
        Assert.fail("Implement me!");
    }

    public void testAddCardMatchingCurrentFilter() {
        Assert.fail("Implement me!");
    }

    public void testAddCardNotMatchingCurrentFilter() {
        Assert.fail("Implement me!");
    }

    public void testAddCardAfterClearFilter() {
        Assert.fail("Implement me!");
    }

    public void testYearFilter() {
        final int year = 1993;
        FilterInput yearInput = new FilterInput() {

            @Override
            public void doInput() {
                AndroidTestUtil.sendKeysFromInt(BaseballCardListWithDataTest.this, year);
            }
        };
        
        Predicate<BaseballCard> yearPred = new Predicate<BaseballCard>() {
            @Override
            public boolean doTest(BaseballCard obj) {
                return obj.getYear() == year;
            }
        };
        
        this.testFilter(YearFilter.class, R.id.year_filter_radio_button, yearInput, yearPred);
    }

    public void testNumberFilter() {
        Assert.fail("Implement me!");
    }

    public void testYearAndNumberFilter() {
        Assert.fail("Implement me!");
    }

    public void testPlayerNameFilter() {
        Assert.fail("Implement me!");
    }

    private void testFilter(Class<?> filterClass, int radioButtonId, FilterInput filterInput, Predicate<BaseballCard> filterPred) {
        BBCTTestUtil.assertListViewContainsItems(this.allCards, this.list);

        Instrumentation.ActivityMonitor filterMonitor = new Instrumentation.ActivityMonitor(filterClass.getName(), null, false);
        this.inst.addMonitor(filterMonitor);

        Activity filterOptions = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.filter_menu, FilterOptions.class);
        final Button optionsOkButton = (Button) filterOptions.findViewById(R.id.ok_button);
        final RadioButton filterRadioButton = (RadioButton) filterOptions.findViewById(radioButtonId);

        filterOptions.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertFalse(filterRadioButton.performClick());
                Assert.assertTrue(optionsOkButton.performClick());
            }
        });

        Activity filter = this.inst.waitForMonitorWithTimeout(filterMonitor, TIME_OUT);
        Assert.assertNotNull(filter);
        final Button filterOkButton = (Button) filter.findViewById(R.id.ok_button);

        filterInput.doInput();

        filter.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(filterOkButton.performClick());
            }
        });

        this.inst.waitForIdleSync();
        Assert.assertTrue(filter.isFinishing());
        Assert.assertTrue(filterOptions.isFinishing());

        List<BaseballCard> matches = this.filterList(this.allCards, filterPred);
        BBCTTestUtil.assertListViewContainsItems(matches, this.list);
    }

    private List<BaseballCard> filterList(List<BaseballCard> list, Predicate<BaseballCard> pred) {
        List<BaseballCard> filteredList = new ArrayList<BaseballCard>();

        for (BaseballCard obj : list) {
            if (pred.doTest(obj)) {
                filteredList.add(obj);
            }
        }

        return filteredList;
    }

    private interface FilterInput {

        public void doInput();
    }
    private List<BaseballCard> allCards;
    private Instrumentation inst = null;
    private Activity activity = null;
    private BaseballCardCsvFileReader cardInput = null;
    private DatabaseUtil dbUtil = null;
    private ListView list = null;
    private static final String DATA_ASSET = "cards.csv";
    private static final int TIME_OUT = 5 * 1000; // 5 seconds
}
