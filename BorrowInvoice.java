import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import java.awt.Desktop;
import java.text.SimpleDateFormat;

import javax.swing.*;
import java.io.File;

public class BorrowInvoice {
    private String BorrowNumber;
    private String bookName;
    private String MemeberName;
    private String priceText;
    private String BorrowDate;
    private String ReturendDate ;
    private String fileName;

    public BorrowInvoice(String BorrowNumber, String BookName, String MemberName , String BorrowDate , String ReturendDate) {
        this.BorrowNumber = BorrowNumber;
        this.bookName = BookName;
        this.MemeberName = MemberName;
        this.priceText = "50";
        this.BorrowDate = BorrowDate ;
        this.ReturendDate = ReturendDate;
        this.fileName = "Invoice";
        GenerateInvoice();
    }

   // Method to generate the PDF invoice
    public void GenerateInvoice() {
        double tax = 0; // Tax amount (currently set to 0)

        try {
            // Check if any input is missing
            if (BorrowNumber.isEmpty() || bookName.isEmpty() || MemeberName.isEmpty() || priceText.isEmpty() || BorrowDate.isEmpty() || ReturendDate.isEmpty() || fileName.isEmpty()) {
                System.out.println("Logical error: Missing required fields.");
                return;
            }

            // Ensure the file name ends with ".pdf"
            if (!fileName.endsWith(".pdf")) {
                fileName += (BorrowNumber + ".pdf");
            }

            // Set the PDF page size to a custom small size
            PageSize smallPageSize = new PageSize(400, 600);
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(smallPageSize); // Apply custom page size
            Document document = new Document(pdfDoc);

            // Format dates (convert from the original format to the desired format)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String borrowTimeFormatted = outputFormat.format(inputFormat.parse(BorrowDate));
            String returnTimeFormatted = outputFormat.format(inputFormat.parse(ReturendDate));

            // Load a default font (Times Roman)
            PdfFont font = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.TIMES_ROMAN);

            // Add a logo image to the PDF , Remove the commant and turn it to a valid statement if you download library_logo.png
            /*String imagePath = "C:/Users/helah/Downloads/library_logo.png"; 
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                ImageData imageData = ImageDataFactory.create(imagePath);
                Image logo = new Image(imageData);
                logo.setWidth(150); 
                logo.setHeight(150); 
                logo.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER); 
                document.add(logo); 
            } else {
                JOptionPane.showMessageDialog(null, "Photo not found", "Photo Error", JOptionPane.ERROR_MESSAGE);
            }*/

            // Parse the price to a double
            double price = Double.parseDouble(priceText);

            // Add text details to the PDF
            document.add(new Paragraph("Borrow Book Invoice").setFont(font).setBold().setFontSize(16).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            document.add(new Paragraph("____________________________________").setBold().setFontSize(16).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            document.add(new Paragraph("Borrow Number: " + BorrowNumber).setFont(font).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            document.add(new Paragraph("Book Name: " + bookName).setFont(font).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            document.add(new Paragraph("Member Name: " + MemeberName).setFont(font).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            document.add(new Paragraph("Borrow Date: " + borrowTimeFormatted).setFont(font).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            document.add(new Paragraph("Return Date: " + returnTimeFormatted).setFont(font).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            document.add(new Paragraph("")); // Add an empty line for spacing
            document.add(new Paragraph("____________________________________").setBold().setFontSize(16).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            document.add(new Paragraph("Price:     " + price + " SAR").setFont(font).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            document.add(new Paragraph("Tax:       " + tax + " SAR").setFont(font).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            document.add(new Paragraph("Total:     " + (price + tax) + " SAR").setFont(font).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));
            document.add(new Paragraph("\n\nSee you again! ").setBold().setFontSize(9).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            // Close the document
            document.close();

            // Show a success message
            JOptionPane.showMessageDialog(null, "Invoice created successfully: " + fileName, "Invoice", JOptionPane.INFORMATION_MESSAGE);

            // Open the generated PDF file (if Desktop is supported)
            if (Desktop.isDesktopSupported()) {
                File pdfFile = new File(fileName);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile); // Open the PDF file
                } else {
                    JOptionPane.showMessageDialog(null, "Invoice not found", "Invoice Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            // Handle exceptions
            JOptionPane.showMessageDialog(null, "Error in Invoice creation", "Invoice Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
