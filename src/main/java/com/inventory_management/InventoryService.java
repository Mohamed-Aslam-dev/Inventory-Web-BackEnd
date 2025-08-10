package com.inventory_management;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;



@Service
public class InventoryService {
	
	private String BASE_FOLDER;

    public InventoryService() {
    	String os = System.getProperty("os.name").toLowerCase();
        String javaVendor = System.getProperty("java.vendor").toLowerCase();

        if (os.contains("linux") && javaVendor.contains("android")) {
            // Android
            BASE_FOLDER = "/storage/emulated/0/InventoryApp/";
        } 
        else if (os.contains("windows")) {
            // Windows direct run
            BASE_FOLDER = "C:\\Users\\Aslam\\Documents\\inventory_app_files\\";
        } 
        else if (os.contains("linux")) {
            // Docker (Linux inside container) → mapped to Windows
            BASE_FOLDER = "/storage/emulated/0/InventoryApp/";
        }

        try {
            Files.createDirectories(Paths.get(BASE_FOLDER));
            System.out.println("Base folder created at: " + BASE_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException("Could not create base folder: " + BASE_FOLDER, e);
        }
    }

    public String getBaseFolder() {
        return BASE_FOLDER;
    }
    
	public String createCsvFile(String csvFileName) throws IOException {
		
		String newFileName = BASE_FOLDER+csvFileName+".csv";
	
        Path path = Paths.get(newFileName);

        if (!Files.exists(path)) {
            Files.write(path, Collections.singletonList("ProductID,ProductName"));
            return "CSV file created: " + newFileName;
        } else {
            return "CSV file already exists: " + newFileName;
        }	
		
	}
	
	public List<String> viewListOfCsvFile() {
		
		File listOfCsvFileFolder = new File(BASE_FOLDER);
		
		File[] files = listOfCsvFileFolder.listFiles((dir,name)->name.endsWith(".csv"));
		
		List<String> fileNames = new ArrayList<>();
		
		if(files != null) {
			for(File file:files) {
				String nameWithoutExtension = file.getName().replaceFirst("[.][^.]+$", "");
	            fileNames.add(nameWithoutExtension);
			}
		}
		
		return fileNames;
	}
	
	public String addRowToCsv(String fileName, String productName) throws IOException {
	    Path path = Paths.get(BASE_FOLDER + fileName + ".csv");
	    int nextId = 1;

	    if (Files.exists(path)) {
	        List<String> lines = Files.readAllLines(path);
	        if (!lines.isEmpty()) {
	            String lastLine = lines.get(lines.size() - 1);
	            String[] parts = lastLine.split(",");
	            if (parts.length >= 1) {
	                try {
	                    nextId = Integer.parseInt(parts[0].trim()) + 1;
	                } catch (NumberFormatException e) {
	                    nextId = 1;
	                }
	            }
	        }
	    }

	    String newRow = nextId + "," + productName;
	    Files.write(path, Collections.singletonList(newRow), StandardOpenOption.APPEND);
	    return "Row added to " + fileName + ".csv with ID: " + nextId;
	}
	
	public List<String> readProductNamesFromCSV(String filePath) {
	    List<String> productNames = new ArrayList<>();
	    try (BufferedReader br = new BufferedReader(new FileReader(BASE_FOLDER+filePath+".csv"))) {
	        String line;
	        br.readLine(); // Skip header
	        while ((line = br.readLine()) != null) {
	            String[] data = line.split(",");
	            if (data.length > 0 && !data[1].isEmpty()) {
	                productNames.add(data[1].trim()); // Only add the product name
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return productNames;
	}
	
	
	public byte[] createPDF(List<ProductsDTO> products, String companyName) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	        Document document = new Document();
	        PdfWriter.getInstance(document, out);
	        document.open();

	        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
	        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
	        String formattedDate = LocalDate.now().format(formatter);
	  
	        document.add(new Paragraph(companyName+" Products", headerFont));
	        document.add(new Paragraph(formattedDate, headerFont));
	        document.add(Chunk.NEWLINE);

	        PdfPTable table = new PdfPTable(2); // 2 columns: Name and Quantity
	        table.setWidthPercentage(100);

	        // Table headers
	        Stream.of("Name", "Quantity").forEach(header -> {
	            PdfPCell cell = new PdfPCell();
	            cell.setPhrase(new Phrase(header, headerFont));
	            table.addCell(cell);
	        });

	        // Table rows
	        for (ProductsDTO product : products) {
	            table.addCell(new Phrase(product.getProductName(), contentFont));
	            table.addCell(new Phrase(String.valueOf(product.getProductQuantity()), contentFont));
	        }

	        document.add(table);
	        document.close();

	        return out.toByteArray();
	    } catch (Exception e) {
	        throw new RuntimeException("PDF creation failed", e);
	    }

	}
}
