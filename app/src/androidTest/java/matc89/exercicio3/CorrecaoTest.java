package matc89.exercicio3;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CorrecaoTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule = new ActivityScenarioRule<>(
            MainActivity.class);

    public void checaToast(String msg) {
        mActivityRule.getScenario().onActivity(activity -> {
            onView(withText(msg)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(isDisplayed()));
        });
    }

    public void checaTamanhoDaLista(int size) {
        onView(withId(R.id.listView)).check(matches(ListBuilder.withListSize(size)));
    }

    public void insereTarefa(String descricao, String prioridade) {
        onView(withId(R.id.editDescricao))
                .perform(typeText(descricao));
        onView(withId(R.id.editPrioridade))
                .perform(typeText(prioridade));
        onView(withId(R.id.buttonAdicionar))
                .perform(click());
        onView(withId(R.id.editDescricao))
                .perform(clearText());
        onView(withId(R.id.editPrioridade))
                .perform(clearText());

    }

    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void camposDevemComecarVazios() {
        onView(withId(R.id.editDescricao))
                .check(matches(withText("")));
        onView(withId(R.id.editPrioridade))
                .check(matches(withText("")));
        onView(withId(R.id.buttonRemover))
                .check(matches(not(isEnabled())));
    }

    @Test
    public void prioridadeInvalida() {
        insereTarefa("abc", "11");
        checaToast("A prioridade deve estar entre 1 e 10.");
        checaTamanhoDaLista(0);

        insereTarefa("abc", "0");
        checaToast("A prioridade deve estar entre 1 e 10.");
        checaTamanhoDaLista(0);
    }

    @Test
    public void tarefaCorreta() {
        insereTarefa("abc", "5");

        new ListBuilder()
                .withItem("abc", "Prioridade: 5")
                .check();
    }

    @Test
    public void aoInserirAtivaBotaoRemover() {
        insereTarefa("abc", "5");

        onView(withId(R.id.buttonRemover))
                .check(matches(isEnabled()));
    }

    @Test
    public void aoEsvaziarDesativaBotaoRemover() {
        insereTarefa("abc", "5");
        onView(withId(R.id.buttonRemover))
                .perform(click());

        checaTamanhoDaLista(0);
        onView(withId(R.id.buttonRemover))
                .check(matches(not(isEnabled())));
    }

    @Test
    public void insereEmOrdemCrescente() {
        insereTarefa("a", "1");
        insereTarefa("b", "6");
        insereTarefa("c", "9");

        new ListBuilder()
                .withItem("a", "Prioridade: 1")
                .withItem("b", "Prioridade: 6")
                .withItem("c", "Prioridade: 9")
                .check();
    }

    @Test
    public void insereEmOrdemInversa() {
        insereTarefa("c", "9");
        insereTarefa("b", "6");
        insereTarefa("a", "1");

        new ListBuilder()
                .withItem("a", "Prioridade: 1")
                .withItem("b", "Prioridade: 6")
                .withItem("c", "Prioridade: 9")
                .check();
    }

    @Test
    public void insereComPrioridadeIgual() {
        insereTarefa("c", "5");
        insereTarefa("b", "5");

        new ListBuilder()
                .withItem("c", "Prioridade: 5")
                .withItem("b", "Prioridade: 5")
                .check();
    }

    @Test
    public void insereComPrioridadeIgual2() {
        insereTarefa("c", "5");
        insereTarefa("a", "6");
        insereTarefa("b", "5");

        new ListBuilder()
                .withItem("c", "Prioridade: 5")
                .withItem("b", "Prioridade: 5")
                .withItem("a", "Prioridade: 6")
                .check();
    }

    @Test
    public void insereDuplicata() {
        insereTarefa("programar", "9");
        insereTarefa("programar", "6");

        checaToast("Tarefa já cadastrada.");
        new ListBuilder()
                .withItem("programar", "Prioridade: 9")
                .check();
    }

    @Test
    public void removeDoMeio() {
        insereTarefa("c", "9");
        insereTarefa("b", "6");
        insereTarefa("a", "1");
        insereTarefa("z", "2");

        onData(allOf(is(instanceOf(Tarefa.class))))
                .inAdapterView(withId(R.id.listView))
                .atPosition(1)
                .perform(click());

        new ListBuilder()
                .withItem("a", "Prioridade: 1")
                .withItem("b", "Prioridade: 6")
                .withItem("c", "Prioridade: 9")
                .check();
    }

    private static Matcher<Object> withValues(final String descricao, final int prioridade) {
        return new TypeSafeMatcher<Object>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with values: ");
            }

            @Override
            protected boolean matchesSafely(Object item) {
                if (!(item instanceof Tarefa)) {
                    return false;
                }

                Tarefa tarefa = (Tarefa)item;
                return tarefa.getDescricao().equals(descricao) && tarefa.getPrioridade() == prioridade;
            }
        };
    }
    private static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }

                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}

class ListBuilder {
    class Item {
        public String text1;
        public String text2;

        public Item(String text1, String text2) {
            this.text1 = text1;
            this.text2 = text2;
        }
    }
    List<Item> list = new ArrayList<>();

    public ListBuilder withItem(String x, String y) {
        list.add(new Item(x, y));
        return this;
    }

    public void check() {
        onView(withId(R.id.listView)).check(matches(withListSize(list.size())));
        int i = 0;
        for (Item item : list) {
            onData(allOf(is(instanceOf(Tarefa.class))))
                .inAdapterView(withId(R.id.listView))
                .atPosition(i)
                .onChildView(withId(android.R.id.text1))
                .check(matches(withText(item.text1)));
            onData(allOf(is(instanceOf(Tarefa.class))))
                    .inAdapterView(withId(R.id.listView))
                    .atPosition(i)
                    .onChildView(withId(android.R.id.text2))
                    .check(matches(withText(item.text2)));

            i++;
        }
    }

    public static Matcher<View> withListSize(final int size) {
        return new TypeSafeMatcher<View> () {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView)view).getCount() == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }
}

// Checa se de fato não inseriu
//        onView(withId(R.id.listView))
//                .check(matches(not(withAdaptedData(withValues("abc", 11)))));
