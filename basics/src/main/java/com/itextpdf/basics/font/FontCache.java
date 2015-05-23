package com.itextpdf.basics.font;

import com.itextpdf.basics.IntHashtable;
import com.itextpdf.basics.PdfException;
import com.itextpdf.basics.PdfRuntimeException;
import com.itextpdf.basics.Utilities;
import com.itextpdf.basics.font.cmap.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class FontCache {

    /**
     * The path to the font resources.
     */
    public static final String RESOURCE_PATH_CMAP = FontConstants.RESOURCE_PATH + "cmap/";

    private static final HashMap<String, HashMap<String, Object>> allFonts = new HashMap<String, HashMap<String, Object>>();
    private static final HashMap<String, Set<String>> registryNames = new HashMap<String, Set<String>>();

    static {
        try {
            loadRegistry();
            for (String font : registryNames.get("fonts")) {
                allFonts.put(font, readFontProperties(font));
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Find and constructs a {@code FontProgram}-object.
     *
     * @param embedded true if the font is to be embedded in the PDF
     * @return the Font constructed based on the parameters
     * @param fontname the name of the font
     * @param encoding the encoding of the font
     * @param style the style of this font
     */

    public static FontProgram getFont(String fontname, String encoding, boolean embedded, int style) {
        //TODO FontProgram has no embedded property, but this parameter is useful to avoid Font,
        //TODO which can't be embedded due to license.
        return null;
    }


    /**
     * Checks if its one of the predefined CID fonts.
     *
     * @param fontName the font name.
     * @param enc      the encoding.
     * @return {@code true} if it is CJKFont.
     */
    public static boolean isCidFont(String fontName, String enc) {
        if (!registryNames.containsKey("fonts")) {
            return false;
        } else if (!registryNames.get("fonts").contains(fontName)) {
            return false;
        } else if (enc.equals(PdfEncodings.IDENTITY_H) || enc.equals(PdfEncodings.IDENTITY_V)) {
            return true;
        }
        String registry = (String) allFonts.get(fontName).get("Registry");
        Set<String> encodings = registryNames.get(registry);
        return encodings != null && encodings.contains(enc);
    }

    public static String getCompatibleCidFont(String cmap) {
        for (Map.Entry<String, Set<String>> e : registryNames.entrySet()) {
            if (e.getValue().contains(cmap)) {
                String registry = e.getKey();
                for (Map.Entry<String, HashMap<String, Object>> e1 : allFonts.entrySet()) {
                    if (registry.equals(e1.getValue().get("Registry")))
                        return e1.getKey();
                }
            }
        }
        return null;
    }

    public static HashMap<String, HashMap<String, Object>> getAllFonts() {
        return allFonts;
    }

    public static HashMap<String, Set<String>> getRegistryNames() {
        return registryNames;
    }

    public static CMapCidUni getCid2UniCmap(String uniMap) {
        CMapCidUni cidUni = new CMapCidUni();
        return parseCmap(uniMap, cidUni);
    }

    public static CMapUniCid getUni2CidCmap(String uniMap) {
        CMapUniCid uniCid = new CMapUniCid();
        return parseCmap(uniMap, uniCid);
    }

    public static CMapByteCid getByte2CidCmap(String cmap) {
        CMapByteCid uniCid = new CMapByteCid();
        return parseCmap(cmap, uniCid);
    }

    public static CMapCidByte getCid2Byte(String cmap) {
        CMapCidByte cidByte = new CMapCidByte();
        return parseCmap(cmap, cidByte);
    }

    public static FontProgram getFont(String fontName) {
        return null;
    }

    private static void loadRegistry() throws IOException {
        InputStream is = Utilities.getResourceStream(RESOURCE_PATH_CMAP + "cjk_registry.properties");
        Properties p = new Properties();
        p.load(is);
        is.close();
        for (Object key : p.keySet()) {
            String value = p.getProperty((String) key);
            String[] sp = value.split(" ");
            Set<String> hs = new HashSet<String>();
            for (String s : sp) {
                if (s.length() > 0) {
                    hs.add(s);
                }
            }
            registryNames.put((String) key, hs);
        }
    }

    private static HashMap<String, Object> readFontProperties(String name) throws IOException {
        name += ".properties";
        InputStream is = Utilities.getResourceStream(RESOURCE_PATH_CMAP + name);
        Properties p = new Properties();
        p.load(is);
        is.close();
        IntHashtable W = createMetric(p.getProperty("W"));
        p.remove("W");
        IntHashtable W2 = createMetric(p.getProperty("W2"));
        p.remove("W2");
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Enumeration<Object> e = p.keys(); e.hasMoreElements(); ) {
            Object obj = e.nextElement();
            map.put((String) obj, p.getProperty((String) obj));
        }
        map.put("W", W);
        map.put("W2", W2);
        return map;
    }

    private static IntHashtable createMetric(String s) {
        IntHashtable h = new IntHashtable();
        StringTokenizer tk = new StringTokenizer(s);
        while (tk.hasMoreTokens()) {
            int n1 = Integer.parseInt(tk.nextToken());
            h.put(n1, Integer.parseInt(tk.nextToken()));
        }
        return h;
    }

    private static <T extends AbstractCMap> T parseCmap(String name, T cmap) {
        try {
            CMapParser.parseCid(name, cmap, new CMapLocationResource());
        } catch (IOException e) {
            throw new PdfRuntimeException(PdfException.IoException, e);
        }
        return cmap;
    }
}
