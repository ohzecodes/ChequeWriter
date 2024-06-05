package main.chequewriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.Map;

public class PDFHelper {

    private static String SaveLocation=".";
    private static String FontName;
    private static String FontSize;

    public static void setFontName(String fontName) {
        FontName = fontName;
    }

    public static void setFontSize(String fontSize) {
        FontSize = fontSize;
    }

    public static void setSaveLocation(String saveLocation) {
        SaveLocation = saveLocation;
    }

    public static PDType1Font getFontByName(String fontName) {
        if (fontName == null) {
          return PDType1Font.HELVETICA;
        }

        return switch (fontName.toUpperCase()) {
            case "HELVETICA" -> PDType1Font.HELVETICA;
            case "HELVETICA_BOLD" -> PDType1Font.HELVETICA_BOLD;
            case "HELVETICA_OBLIQUE" -> PDType1Font.HELVETICA_OBLIQUE;
            case "HELVETICA_BOLD_OBLIQUE" -> PDType1Font.HELVETICA_BOLD_OBLIQUE;
            case "TIMES_ROMAN" -> PDType1Font.TIMES_ROMAN;
            case "TIMES_BOLD" -> PDType1Font.TIMES_BOLD;
            case "TIMES_ITALIC" -> PDType1Font.TIMES_ITALIC;
            case "TIMES_BOLD_ITALIC" -> PDType1Font.TIMES_BOLD_ITALIC;
            case "COURIER" -> PDType1Font.COURIER;
            case "COURIER_BOLD" -> PDType1Font.COURIER_BOLD;
            case "COURIER_OBLIQUE" -> PDType1Font.COURIER_OBLIQUE;
            case "COURIER_BOLD_OBLIQUE" -> PDType1Font.COURIER_BOLD_OBLIQUE;
            case "SYMBOL" -> PDType1Font.SYMBOL;
            case "ZAPF_DINGBATS" -> PDType1Font.ZAPF_DINGBATS;
            default -> throw new IllegalArgumentException("Unknown font name: " + fontName);
        };
    }

    public static void saveAsPDF(double width, double height, Map<String, Object> coordinates, Map<String, Object> chequeData, String bankName) {

        PDDocument document = new PDDocument();

        PDPage page = new PDPage(new PDRectangle((float) width, (float) height));
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            String  payee= (String) chequeData.get("payee");
            String date=chequeData.get("date").toString();
            write(contentStream, date, (double) ((Map<String, Object>) coordinates.get("date")).get("x"), (double)((Map<String, Object>) coordinates.get("date")).get("y"));
            write(contentStream, payee, (double) ((Map<String, Object>) coordinates.get("payee")).get("x"), (double)((Map<String, Object>) coordinates.get("payee")).get("y"));
            write(contentStream, (String) chequeData.get("amountDigits"), (double)((Map<String, Object>) coordinates.get("digits")).get("x"), (double) ((Map<String, Object>) coordinates.get("digits")).get("y"));
            write(contentStream, (String) chequeData.get("amountWords"), (double) ((Map<String, Object>) coordinates.get("words")).get("x"), (double)((Map<String, Object>) coordinates.get("words")).get("y"));

            contentStream.close();
            document.save(SaveLocation+String.format("/%s-%s-%s-cheque.pdf",date, bankName, payee));
//            System.out.println(SaveLocation+"/bkNAME-PAYEE-DATE-cheque.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void write(PDPageContentStream contentStream, String text, double x, double y) throws IOException {
        System.out.println( text+ x+y);

        float fs = (FontSize != null) ? Float.parseFloat(FontSize) : 8f;
        contentStream.setFont(getFontByName(FontName), fs);
        contentStream.beginText();
        contentStream.newLineAtOffset(0, 0);
        contentStream.newLineAtOffset((float) x, (float) y);
        contentStream.showText(text);
        contentStream.endText();

    }


}
