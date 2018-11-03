package com.chaung.service.contract.service.file;

import com.chaung.service.contract.domain.Contract;
import com.chaung.service.contract.service.dto.BasketDTO;
import com.chaung.service.contract.service.dto.ContractProductDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class ServiceContractGenerator {

    public static final String PDF = "application/pdf";

    public static byte[] generate(BasketDTO basketDTO, String fileFormat) {
        if (PDF.equals(fileFormat)) {
            return generatePdf(basketDTO);
        }
        return null;
    }

    private static  byte[] generatePdf(BasketDTO basketDTO) {

        if (basketDTO == null) {
            return null;
        }

        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 1, 2, 2, 2, 2});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            setTableHeader(table,"Product", headFont);
            setTableHeader(table,"Service Id", headFont);
            setTableHeader(table,"Commitment", headFont);
            setTableHeader(table,"Service name", headFont);
            setTableHeader(table,"Device", headFont);
            setTableHeader(table,"Price per month", headFont);



            Set<ContractProductDTO> products = basketDTO.getProducts();
            if (products.size() > 0) {
                products.stream().forEach(contractProductDTO -> {
                    setTableRowCell(table, contractProductDTO.getProductType());
                    setTableRowCell(table, contractProductDTO.getServiceId());
                    setTableRowCell(table, "" + contractProductDTO.getCommitment() + " Months");
                    setTableRowCell(table, contractProductDTO.getName());
                    setTableRowCell(table, contractProductDTO.getDevice());
                    setTableRowCell(table, "$" +contractProductDTO.getPrice());
                });

            } else {
                setTableRowCell(table, null);
                setTableRowCell(table, null);
                setTableRowCell(table, null);
                setTableRowCell(table, null);
                setTableRowCell(table, null);
                setTableRowCell(table, null);
            }

            Font serviceContractFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Font.BOLD);
            Font customerInfFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, Font.NORMAL);

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(Chunk.TABBING);
            document.add(new Phrase("Service Agreement", serviceContractFont));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(new Phrase("Created Date: " + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")), customerInfFont));
            document.add(Chunk.NEWLINE);
            document.add((new Phrase("Customer name: " + basketDTO.getCustomerInfo().getName(), customerInfFont)));
            document.add(Chunk.NEWLINE);
            document.add(new Phrase("Customer Document ID: " + basketDTO.getCustomerInfo().getDocId(), customerInfFont));
            document.add(Chunk.NEWLINE);
            document.add(new Phrase("Product Details" , customerInfFont));
            document.add(table);


            document.close();

        } catch (DocumentException ex) {

            ex.printStackTrace();
            //Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out.toByteArray();
    }

    public static void setTableHeader(PdfPTable table, String header, Font headFont) {
        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase(header, headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);
    }

    public static void setTableRowCell(PdfPTable table, String data) {
        PdfPCell cell;
        data = StringUtils.isEmpty(data) ? "-" : data;
        cell = new PdfPCell(new Phrase(data));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
