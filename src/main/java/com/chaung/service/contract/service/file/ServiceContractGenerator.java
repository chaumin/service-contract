package com.chaung.service.contract.service.file;

import com.chaung.service.contract.domain.Contract;
import com.chaung.service.contract.service.dto.BasketDTO;
import com.chaung.service.contract.service.dto.ContractProductDTO;
import com.chaung.service.contract.service.dto.ContractVaseDTO;
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

        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable productDetailsTable = new PdfPTable(6);
            productDetailsTable.setWidthPercentage(100);
            productDetailsTable.setWidths(new int[]{1, 1, 2, 2, 2, 2});


            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            setTableHeader(productDetailsTable,"Product", headFont);
            setTableHeader(productDetailsTable,"Service Id", headFont);
            setTableHeader(productDetailsTable,"Commitment", headFont);
            setTableHeader(productDetailsTable,"Service name", headFont);
            setTableHeader(productDetailsTable,"Device", headFont);
            setTableHeader(productDetailsTable,"Price per month", headFont);

            PdfPTable addedServiceTable = new PdfPTable(2);
            addedServiceTable.setWidthPercentage(50);
            addedServiceTable.setWidths(new int[]{3, 7});
            setTableHeader(addedServiceTable,"Service ID", headFont);
            setTableHeader(addedServiceTable,"VAS name", headFont);
            Boolean hasVasService = Boolean.FALSE;



            Set<ContractProductDTO> products = basketDTO.getProducts();
            if (products.size() > 0) {
                products.stream().forEach(contractProductDTO -> {
                    setTableRowCell(productDetailsTable, contractProductDTO.getProductType());
                    setTableRowCell(productDetailsTable, contractProductDTO.getServiceId());
                    setTableRowCell(productDetailsTable, "" + contractProductDTO.getCommitment() + " Months");
                    setTableRowCell(productDetailsTable, contractProductDTO.getName());
                    setTableRowCell(productDetailsTable, contractProductDTO.getDevice());
                    setTableRowCell(productDetailsTable, "$" +contractProductDTO.getPrice());

                    Set<ContractVaseDTO> contractVaseDTOs = contractProductDTO.getVases();
                    if (contractVaseDTOs.size() > 0) {
                        contractVaseDTOs.stream().forEach(vaseDTO -> {
                            setTableRowCell(addedServiceTable, contractProductDTO.getServiceId());
                            setTableRowCell(addedServiceTable, vaseDTO.getVas());
                        });

                    }
                });

            } else {
                setTableRowCell(productDetailsTable, null);
                setTableRowCell(productDetailsTable, null);
                setTableRowCell(productDetailsTable, null);
                setTableRowCell(productDetailsTable, null);
                setTableRowCell(productDetailsTable, null);
                setTableRowCell(productDetailsTable, null);
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
            document.add(productDetailsTable);

            document.add(Chunk.NEWLINE);
            document.add(new Phrase("Value Added Services" , customerInfFont));
            document.add(addedServiceTable);


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
        hcell.setVerticalAlignment(Element.ALIGN_CENTER);
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
