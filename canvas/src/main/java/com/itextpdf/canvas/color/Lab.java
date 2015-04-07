package com.itextpdf.canvas.color;

import com.itextpdf.basics.PdfException;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.colorspace.PdfCieBasedCs;

public class Lab extends Color {

    public Lab(PdfCieBasedCs.Lab cs, float[] value) {
        super(cs, value);
    }

    public Lab(PdfDocument document, float[] whitePoint, float[] value) throws PdfException {
        super(new PdfCieBasedCs.Lab(document, whitePoint), value);
    }

    public Lab(PdfDocument document, float[] whitePoint, float[] blackPoint, float[] range, float[] value) throws PdfException {
        this(new PdfCieBasedCs.Lab(document, whitePoint, blackPoint, range), value);
    }

}