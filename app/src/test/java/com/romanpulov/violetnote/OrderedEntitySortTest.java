package com.romanpulov.violetnote;

import androidx.annotation.NonNull;

import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanpulov on 26.06.2017.
 */

public class OrderedEntitySortTest {
    @Test
    public void test1() {
        List<BasicOrderedEntityNoteA> list = new ArrayList<>();

        BasicOrderedEntityNoteA o = new BasicOrderedEntityNoteA();
        o.setId(1);
        o.setOrderId(6);
        list.add(o);

        o = new BasicOrderedEntityNoteA();
        o.setId(2);
        o.setOrderId(2);
        list.add(o);

        o = new BasicOrderedEntityNoteA();
        o.setId(3);
        o.setOrderId(4);
        list.add(o);

        BasicOrderedEntityNoteA.sortAsc(list);
        BasicOrderedEntityNoteA so = list.get(0);
        System.out.println(so.getId());
        Assert.assertEquals(so.getId(), 2L);
        so = list.get(1);
        System.out.println(so.getId());
        Assert.assertEquals(so.getId(), 3L);
        so = list.get(2);
        System.out.println(so.getId());
        Assert.assertEquals(so.getId(), 1L);

        BasicOrderedEntityNoteA.sortDesc(list);
        so = list.get(0);
        System.out.println(so.getId());
        Assert.assertEquals(so.getId(), 1L);
        so = list.get(1);
        System.out.println(so.getId());
        Assert.assertEquals(so.getId(), 3L);
        so = list.get(2);
        System.out.println(so.getId());
        Assert.assertEquals(so.getId(), 2L);
    }

    @Test
    public void testPriority() {
        List<BasicOrderedEntityNoteA> list = new ArrayList<>();

        BasicOrderedEntityNoteA o = new BasicOrderedEntityNoteA();
        o.setId(1);
        o.setOrderId(6);
        o.setPriority(1);
        list.add(o);

        o = new BasicOrderedEntityNoteA();
        o.setId(2);
        o.setOrderId(2);
        list.add(o);

        o = new BasicOrderedEntityNoteA();
        o.setId(3);
        o.setOrderId(4);
        list.add(o);

        System.out.println("testPriority");

        // higher priority handling
        BasicOrderedEntityNoteA.sortAsc(list);
        BasicOrderedEntityNoteA so = list.get(0);
        System.out.println(so.getId());
        Assert.assertEquals(so.getId(), 1L);

        // lower priority handling
        so.setPriority(0);
        so = list.get(1);
        long middleId = so.getId();
        so.setPriority(-1);
        BasicOrderedEntityNoteA.sortAsc(list);
        so = list.get(2);
        Assert.assertEquals(so.getId(), middleId);
    }
}
