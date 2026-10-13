package com.romanpulov.violetnote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassNoteA;
import com.romanpulov.violetnote.model.service.PassData2TransformerService;
import com.romanpulov.violetnote.model.service.PassData2TransformerService.CategoryNoteRecord;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassDataAttribute;
import com.romanpulov.violetnotecore.Model.PassNote2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PassData2TransformerServiceTest {

    private static PassNote2 note(String system, String user, String password, String url, String info) {
        return new PassNote2(system, user, password, url, info, null, null);
    }

    private static PassData2 data(PassCategory2... categories) {
        return new PassData2(new ArrayList<>(Arrays.asList(categories)));
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
    public void transform_fullNote_standardAttributesFirstInFixedOrder() {
        PassNote2 n = note("sys", "usr", "pwd", "http://url", "info text");
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));

        PassNoteA a = r.notes().get(0);
        assertEquals(Arrays.asList("system", "user", "password", "url", "info"), names(a));
        assertEquals(Arrays.asList("sys", "usr", "pwd", "http://url", "info text"), values(a));
    }

    @Test
    public void transform_systemAndUserAreFirstTwoAttributes() {
        PassNote2 n = note("sys", "usr", "pwd", "url", "info");
        n.setAttributes(List.of(new PassDataAttribute("extra", "x")));
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));

        List<PassNoteA.AttrItem> attrs = r.notes().get(0).getNoteAttrs();
        assertEquals(PassNote2.ATTR_SYSTEM, attrs.get(0).mName);
        assertEquals("sys", attrs.get(0).mValue);
        assertEquals(PassNote2.ATTR_USER, attrs.get(1).mName);
        assertEquals("usr", attrs.get(1).mValue);
    }

    @Test
    public void transform_emptyAndNullAttributesAreKeptInPlace() {
        PassNote2 n = note(null, "", null, "", null);
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));

        PassNoteA a = r.notes().get(0);
        assertEquals(5, a.getNoteAttrs().size());
        assertEquals(Arrays.asList("system", "user", "password", "url", "info"), names(a));
        assertEquals(Arrays.asList(null, "", null, "", null), values(a));
    }

    @Test
    public void transform_allAttributesEmpty_stillFiveStandardAttributes() {
        PassNote2 n = note(null, null, null, null, null);
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));

        PassNoteA a = r.notes().get(0);
        assertEquals(5, a.getNoteAttrs().size());
        assertEquals(Arrays.asList(null, null, null, null, null), values(a));
    }

    @Test
    public void transform_nullCustomAttributes_onlyStandardAttributes() {
        PassNote2 n = note("s", "u", "p", "l", "i");
        n.setAttributes(null);
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));

        assertEquals(5, r.notes().get(0).getNoteAttrs().size());
    }

    @Test
    public void transform_emptyCustomAttributes_onlyStandardAttributes() {
        PassNote2 n = note("s", "u", "p", "l", "i");
        n.setAttributes(new ArrayList<>());
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));

        assertEquals(5, r.notes().get(0).getNoteAttrs().size());
    }

    @Test
    public void transform_customAttributesAppendedAfterStandardInOrder() {
        PassNote2 n = note("s", "u", "p", "l", "i");
        n.setAttributes(Arrays.asList(
                new PassDataAttribute("first", "1"),
                new PassDataAttribute("second", ""),
                new PassDataAttribute("third", null)));
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));

        PassNoteA a = r.notes().get(0);
        assertEquals(Arrays.asList("system", "user", "password", "url", "info", "first", "second", "third"), names(a));
        assertEquals(Arrays.asList("s", "u", "p", "l", "i", "1", "", null), values(a));
    }

    @Test
    public void transform_customAttributeNamedLikeStandardIsNotMerged() {
        PassNote2 n = note("s", "u", "p", "l", "i");
        n.setAttributes(List.of(new PassDataAttribute("system", "dup")));
        CategoryNoteRecord r = PassData2TransformerService.transform(
                data(PassCategory2.createWithNotes("Cat", List.of(n))));

        PassNoteA a = r.notes().get(0);
        assertEquals(6, a.getNoteAttrs().size());
        assertEquals("s", a.getNoteAttrs().get(0).mValue);
        assertEquals("dup", a.getNoteAttrs().get(5).mValue);
    }

    @Test
    public void transform_notesCountAndNoteCategoryLinks() {
        PassNote2 n1 = note("s1", "u1", "p1", null, null);
        PassNote2 n2 = note("s2", "u2", "p2", null, null);
        PassNote2 n3 = note("s3", "u3", "p3", null, null);
        CategoryNoteRecord r = PassData2TransformerService.transform(data(
                PassCategory2.createWithNotes("A", Arrays.asList(n1, n2)),
                PassCategory2.createWithNotes("B", List.of(n3))));

        assertEquals(2, r.categories().size());
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

        assertEquals(Arrays.asList("Z", "Empty", "A"),
                Arrays.asList(r.categories().get(0).getCategoryName(),
                        r.categories().get(1).getCategoryName(),
                        r.categories().get(2).getCategoryName()));
        assertEquals("z1", r.notes().get(0).getNoteAttrs().get(0).mValue);
        assertEquals("a1", r.notes().get(1).getNoteAttrs().get(0).mValue);
        assertEquals("a2", r.notes().get(2).getNoteAttrs().get(0).mValue);
        assertEquals(0, r.categories().get(1).getNotesCount());
    }
}
