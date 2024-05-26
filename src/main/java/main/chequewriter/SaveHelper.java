package main.chequewriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class SaveHelper {

    public static void saveAsPDF(double width, double height, ArrayList<Map<String, Object>> data) {

        PDDocument document = new PDDocument();

        PDPage page = new PDPage(new PDRectangle((float) width, (float) height));
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            for (Map<String, Object> map : data) {
                write(contentStream, (String) map.get("name"), (int) map.get("x"),(int) map.get("y"));
            }

            contentStream.close();
            document.save("payee-date.pdf");
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
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset((float) x, (float) y);
        contentStream.showText(text);
        contentStream.endText();
    }
}
