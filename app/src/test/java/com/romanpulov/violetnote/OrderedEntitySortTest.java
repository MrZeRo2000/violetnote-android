package com.romanpulov.violetnote;

import android.support.annotation.NonNull;

import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
}
