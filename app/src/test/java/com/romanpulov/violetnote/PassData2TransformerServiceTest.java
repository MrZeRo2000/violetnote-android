package com.romanpulov.violetnote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassNoteA;
import com.romanpulov.violetnote.service.PassData2TransformerService;
import com.romanpulov.violetnote.service.PassData2TransformerService.CategoryNoteRecord;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassDataAttribute;
import com.romanpulov.violetnotecore.Model.PassNote2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * system, user and password are mandatory: always present, in this order, even when null or empty.
 * url, custom attributes and info are optional: only non-empty (non-null, non-zero-length) ones are present.
 * Order: system, user, password, url, custom attributes, info.
 */
public class PassData2TransformerServiceTest {

    private static PassNote2 note(String system, String user, String password, String url, String info) {
        return new PassNote2(system, user, password, url, info, null, null);
    }

    private static PassData2 data(PassCategory2... categories) {
        return new PassData2(new ArrayList<>(Arrays.asList(categories)));
    }

    private static PassNoteA transformSingle(PassNote2 n) {
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));
        assertEquals(1, r.notes().size());
        return r.notes().get(0);
    }

    private static List<String> names(PassNoteA note) {
        List<String> result = new ArrayList<>();
        for (PassNoteA.AttrItem a : note.getNoteAttrs()) {
            result.add(a.mName);
        }
        return result;
    }

    private static List<String> values(PassNoteA note) {
        List<String> result = new ArrayList<>();
        for (PassNoteA.AttrItem a : note.getNoteAttrs()) {
            result.add(a.mValue);
        }
        return result;
    }

    // ---------- structure ----------

    @Test
    public void transform_noCategories() {
        CategoryNoteRecord r = PassData2TransformerService.transform(data());

        assertTrue(r.categories().isEmpty());
        assertTrue(r.notes().isEmpty());
    }

    @Test
    public void transform_emptyCategory() {
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Empty", new ArrayList<>())));

        assertEquals(1, r.categories().size());
        assertEquals("Empty", r.categories().get(0).getCategoryName());
        assertEquals(0, r.categories().get(0).getNotesCount());
        assertTrue(r.notes().isEmpty());
    }

    @Test
    public void transform_notesCountAndNoteCategoryLinks() {
        CategoryNoteRecord r = PassData2TransformerService.transform(data(
                PassCategory2.createWithNotes("A", Arrays.asList(
                        note("s1", "u1", "p1", null, null),
                        note("s2", "u2", "p2", null, null))),
                PassCategory2.createWithNotes("B", List.of(note("s3", "u3", "p3", null, null)))));

        PassCategoryA a = r.categories().get(0);
        PassCategoryA b = r.categories().get(1);
        assertEquals("A", a.getCategoryName());
        assertEquals(2, a.getNotesCount());
        assertEquals("B", b.getCategoryName());
        assertEquals(1, b.getNotesCount());

        assertEquals(3, r.notes().size());
        assertSame(a, r.notes().get(0).getCategory());
        assertSame(a, r.notes().get(1).getCategory());
        assertSame(b, r.notes().get(2).getCategory());
        assertEquals("A", r.notes().get(0).getCategoryName());
        assertEquals("B", r.notes().get(2).getCategoryName());
    }

    @Test
    public void transform_preservesCategoryAndNoteOrder() {
        CategoryNoteRecord r = PassData2TransformerService.transform(data(
                PassCategory2.createWithNotes("Z", List.of(note("z1", "u", "p", null, null))),
                PassCategory2.createWithNotes("Empty", new ArrayList<>()),
                PassCategory2.createWithNotes("A", Arrays.asList(
                        note("a1", "u", "p", null, null),
                        note("a2", "u", "p", null, null)))));

        assertEquals("Z", r.categories().get(0).getCategoryName());
        assertEquals("Empty", r.categories().get(1).getCategoryName());
        assertEquals(0, r.categories().get(1).getNotesCount());
        assertEquals("A", r.categories().get(2).getCategoryName());
        assertEquals("z1", r.notes().get(0).getNoteAttrs().get(0).mValue);
        assertEquals("a1", r.notes().get(1).getNoteAttrs().get(0).mValue);
        assertEquals("a2", r.notes().get(2).getNoteAttrs().get(0).mValue);
    }

    // ---------- all attributes non-empty ----------

    @Test
    public void transform_allStandardAttributesNonEmpty_fixedOrderInfoLast() {
        PassNoteA a = transformSingle(note("sys", "usr", "pwd", "http://url", "info text"));

        assertEquals(Arrays.asList("system", "user", "password", "url", "info"), names(a));
        assertEquals(Arrays.asList("sys", "usr", "pwd", "http://url", "info text"), values(a));
    }

    @Test
    public void transform_customAttributesPlacedBetweenUrlAndInfo() {
        PassNote2 n = note("s", "u", "p", "l", "i");
        n.setAttributes(Arrays.asList(
                new PassDataAttribute("first", "1"),
                new PassDataAttribute("second", "2")));

        PassNoteA a = transformSingle(n);

        assertEquals(Arrays.asList("system", "user", "password", "url", "first", "second", "info"), names(a));
        assertEquals(Arrays.asList("s", "u", "p", "l", "1", "2", "i"), values(a));
    }

    @Test
    public void transform_systemAndUserAreFirstTwoWhenNonEmpty() {
        PassNote2 n = note("sys", "usr", "pwd", "url", "info");
        n.setAttributes(List.of(new PassDataAttribute("extra", "x")));

        List<PassNoteA.AttrItem> attrs = transformSingle(n).getNoteAttrs();

        assertEquals(PassNote2.ATTR_SYSTEM, attrs.get(0).mName);
        assertEquals("sys", attrs.get(0).mValue);
        assertEquals(PassNote2.ATTR_USER, attrs.get(1).mName);
        assertEquals("usr", attrs.get(1).mValue);
    }

    // ---------- empty attributes are skipped ----------

    @Test
    public void transform_emptyUrlAndInfoAreSkipped() {
        PassNoteA a = transformSingle(note("s", "u", "p", "", ""));

        assertEquals(Arrays.asList("system", "user", "password"), names(a));
        assertEquals(Arrays.asList("s", "u", "p"), values(a));
    }

    @Test
    public void transform_nullUrlAndInfoAreSkipped() {
        PassNoteA a = transformSingle(note("s", "u", "p", null, null));

        assertEquals(Arrays.asList("system", "user", "password"), names(a));
    }

    @Test
    public void transform_emptyUrl_infoStillAdded() {
        PassNoteA a = transformSingle(note("s", "u", "p", "", "info text"));

        assertEquals(Arrays.asList("system", "user", "password", "info"), names(a));
        assertEquals("info text", a.getNoteAttrs().get(3).mValue);
    }

    @Test
    public void transform_emptyInfo_urlStillAdded() {
        PassNoteA a = transformSingle(note("s", "u", "p", "http://url", null));

        assertEquals(Arrays.asList("system", "user", "password", "url"), names(a));
        assertEquals("http://url", a.getNoteAttrs().get(3).mValue);
    }

    @Test
    public void transform_onlyOptionalInfoNonEmpty() {
        PassNoteA a = transformSingle(note("s", "u", "p", null, "just info"));

        assertEquals(Arrays.asList("system", "user", "password", "info"), names(a));
        assertEquals(Arrays.asList("s", "u", "p", "just info"), values(a));
    }

    @Test
    public void transform_whitespaceOnlyOptionalValueIsNotEmpty() {
        PassNoteA a = transformSingle(note("s", "u", "p", " ", null));

        assertEquals(Arrays.asList("system", "user", "password", "url"), names(a));
        assertEquals(" ", a.getNoteAttrs().get(3).mValue);
    }

    // ---------- mandatory attributes are always present ----------

    @Test
    public void transform_mandatoryAttributesKeptEvenWhenEmptyOrNull() {
        PassNoteA a = transformSingle(note(null, "", null, null, null));

        assertEquals(Arrays.asList("system", "user", "password"), names(a));
        assertEquals(Arrays.asList(null, "", null), values(a));
    }

    @Test
    public void transform_emptySystem_staysFirstAndUserStaysSecond() {
        PassNoteA a = transformSingle(note("", "usr", "pwd", "url", "info"));

        assertEquals(Arrays.asList("system", "user", "password", "url", "info"), names(a));
        assertEquals(Arrays.asList("", "usr", "pwd", "url", "info"), values(a));
    }

    @Test
    public void transform_emptyUser_passwordStaysThird() {
        PassNoteA a = transformSingle(note("sys", null, "pwd", null, null));

        assertEquals(PassNote2.ATTR_SYSTEM, a.getNoteAttrs().get(0).mName);
        assertEquals(PassNote2.ATTR_USER, a.getNoteAttrs().get(1).mName);
        assertNull(a.getNoteAttrs().get(1).mValue);
        assertEquals(PassNote2.ATTR_PASSWORD, a.getNoteAttrs().get(2).mName);
        assertEquals("pwd", a.getNoteAttrs().get(2).mValue);
    }

    @Test
    public void transform_allAttributesEmpty_onlyMandatoryRemain() {
        PassNoteA a = transformSingle(note(null, "", null, "", ""));

        assertEquals(Arrays.asList("system", "user", "password"), names(a));
        assertEquals(3, a.getNoteAttrs().size());
    }

    @Test
    public void transform_allAttributesEmpty_noteStillCountedInCategory() {
        CategoryNoteRecord r = PassData2TransformerService.transform(data(
                PassCategory2.createWithNotes("Cat", Arrays.asList(
                        note(null, null, null, null, null),
                        note("s", "u", "p", null, null)))));

        assertEquals(2, r.categories().get(0).getNotesCount());
        assertEquals(2, r.notes().size());
        assertEquals(3, r.notes().get(0).getNoteAttrs().size());
    }

    // ---------- custom attributes ----------

    @Test
    public void transform_customAttributesWithNullOrEmptyValueAreSkipped() {
        PassNote2 n = note("s", "u", "p", "l", "i");
        n.setAttributes(Arrays.asList(
                new PassDataAttribute("kept", "1"),
                new PassDataAttribute("emptyValue", ""),
                new PassDataAttribute("nullValue", null),
                new PassDataAttribute("alsoKept", "2")));

        PassNoteA a = transformSingle(n);

        assertEquals(Arrays.asList("system", "user", "password", "url", "kept", "alsoKept", "info"), names(a));
        assertEquals(Arrays.asList("s", "u", "p", "l", "1", "2", "i"), values(a));
    }

    @Test
    public void transform_nullCustomAttributes() {
        PassNote2 n = note("s", "u", "p", null, "i");
        n.setAttributes(null);

        assertEquals(Arrays.asList("system", "user", "password", "info"), names(transformSingle(n)));
    }

    @Test
    public void transform_emptyCustomAttributes() {
        PassNote2 n = note("s", "u", "p", null, "i");
        n.setAttributes(new ArrayList<>());

        assertEquals(Arrays.asList("system", "user", "password", "info"), names(transformSingle(n)));
    }

    @Test
    public void transform_onlyCustomAttributesNonEmpty() {
        PassNote2 n = note("s", "u", "p", null, null);
        n.setAttributes(Arrays.asList(
                new PassDataAttribute("a", "1"),
                new PassDataAttribute("b", "")));

        PassNoteA a = transformSingle(n);

        assertEquals(Arrays.asList("system", "user", "password", "a"), names(a));
        assertEquals(Arrays.asList("s", "u", "p", "1"), values(a));
    }

    @Test
    public void transform_customAttributesKeepOrderWhenSomeSkipped() {
        PassNote2 n = note("s", "u", "p", "", "");
        n.setAttributes(Arrays.asList(
                new PassDataAttribute("a", ""),
                new PassDataAttribute("b", "2"),
                new PassDataAttribute("c", null),
                new PassDataAttribute("d", "4")));

        assertEquals(Arrays.asList("system", "user", "password", "b", "d"), names(transformSingle(n)));
    }

    @Test
    public void transform_customAttributeNamedLikeStandardIsNotMerged() {
        PassNote2 n = note("s", "u", "p", "l", "i");
        n.setAttributes(List.of(new PassDataAttribute("system", "dup")));

        PassNoteA a = transformSingle(n);

        assertEquals(Arrays.asList("system", "user", "password", "url", "system", "info"), names(a));
        assertEquals(Arrays.asList("s", "u", "p", "l", "dup", "i"), values(a));
    }
}
