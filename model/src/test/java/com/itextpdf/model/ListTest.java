package com.itextpdf.model;

import com.itextpdf.basics.PdfException;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.testutils.CompareTool;
import com.itextpdf.model.element.AreaBreak;
import com.itextpdf.model.element.List;
import com.itextpdf.model.element.ListItem;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ListTest {

    static final public String sourceFolder = "./src/test/resources/com/itextpdf/model/ListTest/";
    static final public String destinationFolder = "./target/test/com/itextpdf/model/ListTest/";

    @BeforeClass
    static public void beforeClass() {
        new File(destinationFolder).mkdirs();
    }

    @Test
    public void nestedListTest01() throws IOException, PdfException, InterruptedException {
        String outFileName = destinationFolder + "nestedListTest01.pdf";
        String cmpFileName = sourceFolder + "cmp_nestedListTest01.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new FileOutputStream(outFileName)));

        Document document = new Document(pdfDocument);

        List romanList2 = new List(Property.ListNumberingType.ROMAN_LOWER).setSymbolIndent(20).
                setMarginLeft(25).
                add("One").add("Two").add("Three");

        List romanList = new List(Property.ListNumberingType.ROMAN_LOWER).setSymbolIndent(20).
                setMarginLeft(25).
                add("One").add("Two").add((ListItem) new ListItem("Three").add(romanList2));

        List list = new List(Property.ListNumberingType.DECIMAL).setSymbolIndent(20).
                add("One").add("Two").add("Three").add("Four").add((ListItem) new ListItem("Roman List").add(romanList)).
                add("Five").add("Six").add((ListItem) new ListItem().add(romanList2));
        document.add(list);

        document.close();

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

    @Test
    public void listNumberingTest01() throws IOException, PdfException, InterruptedException {
        String outFileName = destinationFolder + "listNumberingTest01.pdf";
        String cmpFileName = sourceFolder + "cmp_listNumberingTest01.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new FileOutputStream(outFileName)));

        Document document = new Document(pdfDocument);

        java.util.List<List> lists = new ArrayList<>();
        lists.add(new List(Property.ListNumberingType.DECIMAL));
        lists.add(new List(Property.ListNumberingType.ROMAN_LOWER));
        lists.add(new List(Property.ListNumberingType.ROMAN_UPPER));
        lists.add(new List(Property.ListNumberingType.ENGLISH_LOWER));
        lists.add(new List(Property.ListNumberingType.ENGLISH_UPPER));
        lists.add(new List(Property.ListNumberingType.GREEK_LOWER));
        lists.add(new List(Property.ListNumberingType.GREEK_UPPER));

        for (int i = 1; i <= 30; i++) {
            for (List list : lists) {
                list.add("Item #" + i);
            }
        }

        for (List list : lists) {
            document.add(list).add(new AreaBreak());
        }

        document.close();

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

}