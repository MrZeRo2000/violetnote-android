package com.romanpulov.violetnote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnote.model.PassNoteA;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

/**
 * Characterization tests for {@link PassDataA#newSearchInstance} and {@link PassDataA#getSearchValues}.
 * Attribute index 0 is the "system" attribute, index 1 is the "user" attribute, others are never searched.
 */
public class PassDataASearchTest {

    private PassCategoryA catA;
    private PassCategoryA catB;

    @Before
    public void setUp() {
        catA = new PassCategoryA("A");
        catB = new PassCategoryA("B");
    }

    private static PassNoteA note(PassCategoryA category, String... values) {
        List<PassNoteA.AttrItem> attrs = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            attrs.add(new PassNoteA.AttrItem("attr" + i, values[i]));
        }
        return new PassNoteA(category, attrs);
    }

    /** Builds a PassDataA via reflection: its constructor is private and PassData2 is heavyweight to set up. */
    private static PassDataA build(String password, PassNoteA... notes) {
        try {
            java.lang.reflect.Constructor<PassDataA> c = PassDataA.class.getDeclaredConstructor(String.class);
            c.setAccessible(true);
            PassDataA d = c.newInstance(password);
            List<PassCategoryA> categories = new ArrayList<>();
            List<PassNoteA> noteList = new ArrayList<>(Arrays.asList(notes));
            for (PassNoteA n : notes) {
                if (!categories.contains(n.getCategory())) categories.add(n.getCategory());
            }
            set(d, "mPassCategoryDataA", categories);
            set(d, "mPassNoteDataA", noteList);
            return d;
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void set(PassDataA d, String field, Object value) throws ReflectiveOperationException {
        java.lang.reflect.Field f = PassDataA.class.getDeclaredField(field);
        f.setAccessible(true);
        f.set(d, value);
    }

    private static Set<String> asSet(Collection<String> c) {
        return new HashSet<>(c);
    }

    // ---------- newSearchInstance ----------

    @Test
    public void search_matchesSystemAttributeOnly() {
        PassNoteA n1 = note(catA, "alpha", "beta", "gamma");
        PassNoteA n2 = note(catA, "xxx", "alpha", "yyy");
        PassDataA source = build("pwd", n1, n2);

        PassDataA result = PassDataA.newSearchInstance(source, "alpha", true, false);

        assertEquals(1, result.getPassNoteData().size());
        assertSame(n1, result.getPassNoteData().get(0));
    }

    @Test
    public void search_matchesUserAttributeOnly() {
        PassNoteA n1 = note(catA, "alpha", "beta", "gamma");
        PassNoteA n2 = note(catA, "xxx", "alpha", "yyy");
        PassDataA source = build("pwd", n1, n2);

        PassDataA result = PassDataA.newSearchInstance(source, "alpha", false, true);

        assertEquals(1, result.getPassNoteData().size());
        assertSame(n2, result.getPassNoteData().get(0));
    }

    @Test
    public void search_matchesBothAttributes_preservesSourceOrder() {
        PassNoteA n1 = note(catA, "alpha", "beta");
        PassNoteA n2 = note(catA, "xxx", "alpha");
        PassNoteA n3 = note(catA, "nope", "nope");
        PassDataA source = build("pwd", n1, n2, n3);

        PassDataA result = PassDataA.newSearchInstance(source, "alpha", true, true);

        assertEquals(Arrays.asList(n1, n2), result.getPassNoteData());
    }

    @Test
    public void search_noteMatchingInBothAttributesAddedOnce() {
        PassNoteA n1 = note(catA, "alpha", "alpha");
        PassDataA source = build("pwd", n1);

        PassDataA result = PassDataA.newSearchInstance(source, "alpha", true, true);

        assertEquals(1, result.getPassNoteData().size());
        assertEquals(1, result.getPassCategoryData().size());
    }

    @Test
    public void search_neitherFlag_returnsEmpty() {
        PassDataA source = build("pwd", note(catA, "alpha", "alpha"));

        PassDataA result = PassDataA.newSearchInstance(source, "alpha", false, false);

        assertTrue(result.getPassNoteData().isEmpty());
        assertTrue(result.getPassCategoryData().isEmpty());
    }

    @Test
    public void search_thirdAttributeIsNeverSearched() {
        PassDataA source = build("pwd", note(catA, "xxx", "yyy", "alpha"));

        assertTrue(PassDataA.newSearchInstance(source, "alpha", true, true).getPassNoteData().isEmpty());
    }

    @Test
    public void search_isCaseInsensitiveAndSubstring() {
        PassNoteA n1 = note(catA, "My AlphaBet", "u");
        PassDataA source = build("pwd", n1);

        assertEquals(1, PassDataA.newSearchInstance(source, "alpha", true, false).getPassNoteData().size());
        assertEquals(1, PassDataA.newSearchInstance(source, "ALPHAB", true, false).getPassNoteData().size());
        assertEquals(1, PassDataA.newSearchInstance(source, "bet", true, false).getPassNoteData().size());
        assertEquals(0, PassDataA.newSearchInstance(source, "gamma", true, false).getPassNoteData().size());
    }

    @Test
    public void search_searchStringIsTreatedAsRegex() {
        PassDataA source = build("pwd", note(catA, "abc", "u"), note(catA, "a.c", "u"), note(catA, "xyz", "u"));

        PassDataA result = PassDataA.newSearchInstance(source, "a.c", true, false);

        assertEquals(2, result.getPassNoteData().size());
    }

    @Test
    public void search_invalidRegexThrows() {
        PassDataA source = build("pwd", note(catA, "abc", "u"));

        assertThrows(PatternSyntaxException.class,
                () -> PassDataA.newSearchInstance(source, "[", true, false));
    }

    @Test
    public void search_multilineValueDoesNotMatch() {
        // '.' does not match line terminators and matches() requires the whole value to match
        PassDataA source = build("pwd", note(catA, "line1\nalpha", "u"));

        assertTrue(PassDataA.newSearchInstance(source, "alpha", true, false).getPassNoteData().isEmpty());
    }

    @Test
    public void search_emptyStringMatchesAllNonNullValues() {
        PassNoteA n1 = note(catA, "a", "b");
        PassNoteA n2 = note(catA, null, null);
        PassNoteA n3 = note(catA, "", "");
        PassDataA source = build("pwd", n1, n2, n3);

        PassDataA result = PassDataA.newSearchInstance(source, "", true, true);

        assertEquals(Arrays.asList(n1, n3), result.getPassNoteData());
    }

    @Test
    public void search_nullValueSkippedButLaterAttributeStillChecked() {
        PassNoteA n1 = note(catA, null, "alpha");
        PassDataA source = build("pwd", n1);

        assertEquals(1, PassDataA.newSearchInstance(source, "alpha", true, true).getPassNoteData().size());
        assertTrue(PassDataA.newSearchInstance(source, "alpha", true, false).getPassNoteData().isEmpty());
    }

    @Test
    public void search_noteWithNoAttrsOrOneAttr() {
        PassNoteA empty = note(catA);
        PassNoteA single = note(catA, "alpha");
        PassDataA source = build("pwd", empty, single);

        assertEquals(List.of(single),
                PassDataA.newSearchInstance(source, "alpha", true, true).getPassNoteData());
        assertTrue(PassDataA.newSearchInstance(source, "alpha", false, true).getPassNoteData().isEmpty());
    }

    @Test
    public void search_categoriesAreOnePerMatchedNote_notDeduplicated() {
        PassNoteA n1 = note(catA, "alpha", "u");
        PassNoteA n2 = note(catB, "alpha", "u");
        PassNoteA n3 = note(catA, "alpha", "u");
        PassDataA source = build("pwd", n1, n2, n3);

        PassDataA result = PassDataA.newSearchInstance(source, "alpha", true, false);

        assertEquals(2, result.getPassCategoryData().size());
        assertTrue(result.getPassCategoryData().contains(catA));
        assertTrue(result.getPassCategoryData().contains(catB));
    }

    @Test
    public void search_keepsPasswordAndDoesNotModifySource() {
        PassNoteA n1 = note(catA, "alpha", "u");
        PassNoteA n2 = note(catA, "zzz", "u");
        PassDataA source = build("secret", n1, n2);

        PassDataA result = PassDataA.newSearchInstance(source, "alpha", true, true);

        assertEquals("secret", result.getPassword());
        assertEquals(2, source.getPassNoteData().size());
        assertEquals(1, source.getPassCategoryData().size());
    }

    @Test
    public void search_emptySource() {
        PassDataA result = PassDataA.newSearchInstance(build("pwd"), "x", true, true);

        assertTrue(result.getPassNoteData().isEmpty());
        assertTrue(result.getPassCategoryData().isEmpty());
    }

    // ---------- getSearchValues ----------

    @Test
    public void values_systemOnly() {
        PassDataA source = build("pwd", note(catA, "s1", "u1", "x1"), note(catA, "s2", "u2", "x2"));

        assertEquals(new HashSet<>(Arrays.asList("s1", "s2")), asSet(source.getSearchValues(true, false)));
    }

    @Test
    public void values_userOnly() {
        PassDataA source = build("pwd", note(catA, "s1", "u1", "x1"), note(catA, "s2", "u2", "x2"));

        assertEquals(new HashSet<>(Arrays.asList("u1", "u2")), asSet(source.getSearchValues(false, true)));
    }

    @Test
    public void values_both_thirdAttributeExcluded() {
        PassDataA source = build("pwd", note(catA, "s1", "u1", "x1"));

        assertEquals(new HashSet<>(Arrays.asList("s1", "u1")), asSet(source.getSearchValues(true, true)));
    }

    @Test
    public void values_neitherFlag_returnsEmpty() {
        PassDataA source = build("pwd", note(catA, "s1", "u1"));

        assertTrue(source.getSearchValues(false, false).isEmpty());
    }

    @Test
    public void values_areDeduplicatedAcrossNotesAndAttributes() {
        PassDataA source = build("pwd", note(catA, "same", "same"), note(catB, "same", "other"));

        Collection<String> values = source.getSearchValues(true, true);

        assertEquals(2, values.size());
        assertEquals(new HashSet<>(Arrays.asList("same", "other")), asSet(values));
    }

    @Test
    public void values_nullAndEmptyValuesAreIncluded() {
        PassDataA source = build("pwd", note(catA, null, ""));

        Collection<String> values = source.getSearchValues(true, true);

        assertEquals(2, values.size());
        assertTrue(values.contains(null));
        assertTrue(values.contains(""));
    }

    @Test
    public void values_shortNotes() {
        PassDataA source = build("pwd", note(catA), note(catA, "only"));

        assertEquals(Set.of("only"), asSet(source.getSearchValues(true, true)));
        assertTrue(source.getSearchValues(false, true).isEmpty());
    }

    @Test
    public void values_emptyData() {
        assertTrue(build("pwd").getSearchValues(true, true).isEmpty());
    }

    @Test
    public void values_onSearchResultOnlyReflectsMatchedNotes() {
        PassDataA source = build("pwd", note(catA, "alpha", "u1"), note(catA, "zzz", "u2"));
        PassDataA result = PassDataA.newSearchInstance(source, "alpha", true, false);

        assertEquals(new HashSet<>(Arrays.asList("alpha", "u1")), asSet(result.getSearchValues(true, true)));
    }
}
