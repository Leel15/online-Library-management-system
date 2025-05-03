import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import java.awt.Desktop;
import javax.swing.*;
import java.io.File;

public class ReturndReceipt {

    private String fileName;
    private int c ;
    public ReturndReceipt(int c ){
        this.fileName = "ReturnInvoice";
        this.c = c ;
        ChooseFileToModify();
    }

    // Method to open file chooser and allow the user to select the invoice to modify
    public void ChooseFileToModify() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Invoice to Modify");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("User chose: " + selectedFile.getAbsolutePath());
            ModifyInvoice(selectedFile);
        } else {
            JOptionPane.showMessageDialog(null, "No file selected", "Error", JOptionPane.ERROR_MESSAGE);
            if(result == JFileChooser.CANCEL_OPTION)
                c = 0 ;
            else 
                c = 5 ;
        }
    }
    
    public int getc () {
        return c ;
    }

    // Method to modify the selected invoice
    public void ModifyInvoice(File selectedFile) {

        try {
            // Ensure the file name ends with ".pdf"
            String outputFileName = selectedFile.getParent() + File.separator + "Updated_" + selectedFile.getName();

            // Read the existing PDF file
            PdfReader reader = new PdfReader(selectedFile.getAbsolutePath());  
            PdfWriter writer = new PdfWriter(outputFileName); 
            PdfDocument pdfDoc = new PdfDocument(reader, writer);
            Document document = new Document(pdfDoc);

            // Add an image (logo)
            String imagePath = "C:/Users/helah/Downloads/library_logo (1).png";
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                ImageData imageData = ImageDataFactory.create(imagePath);
                Image logo = new Image(imageData);
                float Width = 100;
                logo.setWidth(200);
                logo.setHeight(200);
                float x = 400 - Width - 80;
                float y = 9;
                logo.setFixedPosition(x, y);
                logo.setFixedPosition(x, y);
                document.add(logo);
                c = 1 ;
            } else {
                JOptionPane.showMessageDialog(null, "Photo not found", "Photo Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the document
            document.close();

            // Show a success message
            JOptionPane.showMessageDialog(null, "Return Receipt created successfully: " + "Updated_" + selectedFile.getName(), "Return Invoice", JOptionPane.INFORMATION_MESSAGE);

            // Open the generated PDF file (if Desktop is supported)
            if (Desktop.isDesktopSupported()) {
                File pdfFile = new File(outputFileName);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile); // Open the PDF file
                } else {
                    JOptionPane.showMessageDialog(null, "Return Invoice not found", "Invoice Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            // Handle exceptions
            JOptionPane.showMessageDialog(null, "Error in modifying Return Invoice", "Invoice Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    
}
